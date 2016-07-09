package com.obdobion.argument;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;
import com.obdobion.argument.input.NamespaceParser;
import com.obdobion.argument.input.XmlParser;

/**
 * @author Chris DeGreef
 *
 */
public class CmdLine implements ICmdLine, Cloneable
{
    public static final String INCLUDE_FILE_PREFIX = "@";
    public static final String MaxHelpCommandName  = "help";
    public static final char   MinHelpCommandName  = '?';
    public static final char   NegateCommandName   = '!';

    static private void checkForUnusedInput (final Token[] tokens) throws ParseException
    {
        if (tokenCount(tokens) > 0)
        {
            final StringBuilder extraInput = new StringBuilder();
            for (final Token token : tokens)
            {
                if (token.isUsed())
                    continue;
                extraInput.append(token.getValue());
            }
            throw new ParseException("extraneous input is not valid: " + extraInput.toString(), 0);
        }
    }

    static public ICmdLine create (final List<String> definition) throws ParseException, IOException
    {
        return new CmdLine().compile(definition);
    }

    static public ICmdLine create (final String... definition) throws ParseException, IOException
    {
        return new CmdLine().compile(definition);
    }

    static public int matchingArgs (
        final List<ICmdLineArg<?>> bestArgs,
        final List<ICmdLineArg<?>> possibleArgs,
        final Token token,
        final boolean includeAlreadyParsed)
    {
        int maxTokenLengthUsed = -1;
        final Iterator<ICmdLineArg<?>> aIter = possibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg.isParsed() && !includeAlreadyParsed)
                continue;

            final int sal = arg.salience(token);
            if (sal == maxTokenLengthUsed)
            {
                bestArgs.add(arg);
            }
            if (sal > 0 && sal > maxTokenLengthUsed)
            {
                maxTokenLengthUsed = sal;
                bestArgs.clear();
                bestArgs.add(arg);
            }
        }
        /*
         * It is a special case if the entered arg is exactly the same length as
         * one of the best args. Typically, more than one best arg is considered
         * an error. But this will remove all but the one that has the exact
         * same length. So no error will occur. Args like "archive" and
         * "archiveAll" will conflict when "arc" is entered but "archive" will
         * return "archive" as the only best arg.
         */

        if (bestArgs.size() > 1)
        {
            for (final ICmdLineArg<?> barg : bestArgs)
            {
                if (barg.getKeyword().equalsIgnoreCase(token.getWordCommand()))
                {
                    bestArgs.clear();
                    bestArgs.add(barg);
                    break;
                }
            }
        }

        return maxTokenLengthUsed;
    }

    static private boolean mostSalient (
        final List<ICmdLineArg<?>> possibleArgs,
        final Token[] tokens,
        final int tokenIdx,
        final List<ICmdLineArg<?>> args)
            throws ParseException
    {
        if (!tokens[tokenIdx].isCommand())
            return false;

        final List<ICmdLineArg<?>> bestArgs = new ArrayList<>();
        matchingArgs(bestArgs, possibleArgs, tokens[tokenIdx], true);

        if (bestArgs.size() == 0)
            return false;
        if (bestArgs.size() == 1)
        {
            final ICmdLineArg<?> arg = bestArgs.get(0);
            if (arg != null)
            {
                args.add(arg);
                arg.setParsed(true);
                if (tokens[tokenIdx].isWordCommand())
                {
                    tokens[tokenIdx].setUsed(true);
                    return true;
                }
                // max must be 1 at this point since verbose must match
                // entire tokens
                tokens[tokenIdx].removeCharCommand();
                return true;
            }
            return false;
        }

        final Iterator<ICmdLineArg<?>> bIter = bestArgs.iterator();
        final StringBuilder bldr = new StringBuilder();
        bldr.append("ambiguous token ");
        bldr.append(tokens[tokenIdx].getValue());
        bldr.append(" matches ");
        while (bIter.hasNext())
        {
            bldr.append(bIter.next().getKeyword());
            bldr.append(' ');
        }
        throw new ParseException(bldr.toString(), -1);
    }

    static private int parseGroup (
        final CmdLineCLA group,
        final Token[] tokens,
        final int _tokenIndex,
        final Object target)
            throws ParseException, IOException
    {
        StringBuilder str = null;

        int tlex = 0;
        int tokenIndex = _tokenIndex;

        for (tokenIndex++; tokenIndex < tokens.length; tokenIndex++)
        {
            if (tokens[tokenIndex].isUsed())
                continue;
            if (tokens[tokenIndex].isGroupStart())
            {
                tlex++;
                if (tlex == 1)
                {
                    tokens[tokenIndex].setUsed(true);
                    str = new StringBuilder();
                    continue;
                }
            }
            if (tokens[tokenIndex].isGroupEnd())
            {
                tlex--;
                if (tlex == 0)
                {
                    tokens[tokenIndex].setUsed(true);
                    group.setValue(group.convert(str.toString(), false, target));
                    continue;
                }
            }
            if (tlex == 0)
            {
                tokenIndex--; // reuse last token later
                break;
            }

            if (tokens[tokenIndex].isLiteral())
            {
                /*
                 * Always quote the value in case it was quoted. It doesn't hurt
                 * to unnecessarily quote. But it would hurt not to quote at
                 * all.
                 */
                String value = tokens[tokenIndex].getValue();

                value = replaceEscapes(value);
                final boolean singlequote = value.contains("'");
                final boolean doublequote = value.contains("\"");
                char delim;
                if (singlequote)
                    if (doublequote)
                    {
                        delim = '"';
                        value = value.replace("\"", "\\\"");
                    } else
                        delim = '"';
                else
                    delim = '\'';
                str.append(delim);
                str.append(value);
                str.append(delim);
                str.append(" ");
            } else
            {
                str.append(tokens[tokenIndex].getValue());
                str.append(" ");
            }
            tokens[tokenIndex].setUsed(true);
        }
        if (tlex != 0)
            throw new ParseException("Missing " + tlex + " right bracket(s)", 0);
        validateMultipleEntries(group);
        return tokenIndex;
    }

    static private void parseOrphaned (final Token[] tokens) throws ParseException
    {
        final StringBuilder bldr = new StringBuilder();
        for (int t = 0; t < tokens.length; t++)
        {
            if (!tokens[t].isUsed())
            {
                bldr.append(tokens[t].getValue());
                bldr.append(' ');
            }
        }
        if (bldr.length() != 0)
            throw new ParseException("unexpected input: " + bldr.toString(), -1);
    }

    @SuppressWarnings(
    {
        "rawtypes",
        "unchecked"
    })
    static private int parseValues (
        final ICmdLineArg arg,
        final Token[] tokens,
        final int t)
            throws ParseException, IOException
    {
        int tokenIndex = t;
        /*
         * take remainder of the token if any as parm 1
         */
        boolean aValueWasFound = false;
        if (!tokens[tokenIndex].isUsed())
        {
            // skip the dash
            arg.setValue(arg.convert(tokens[tokenIndex]
                    .remainderValue(), (tokens[tokenIndex].isLiteral() || arg.isCaseSensitive()), null));
            tokens[tokenIndex].setUsed(true);
            aValueWasFound = true;
        }

        /*
         * take any following non-dash parms
         */

        if (!aValueWasFound || arg.isMultiple())
            for (tokenIndex++; tokenIndex < tokens.length; tokenIndex++)
            {
                if (arg.isMultiple() && arg.size() == arg.getMultipleMax())
                {
                    tokenIndex--; // make sure to allow reuse of - token
                    break;
                }
                if (tokens[tokenIndex].isUsed())
                    continue;
                if (!tokens[tokenIndex].isCommand())
                {
                    arg.setValue(arg.convert(tokens[tokenIndex]
                            .getValue(), (tokens[tokenIndex].isLiteral() || arg.isCaseSensitive()), null));
                    tokens[tokenIndex].setUsed(true);
                    if (!arg.isMultiple())
                        break;
                } else
                {
                    tokenIndex--; // make sure to allow reuse of - token
                    break;
                }
            }

        validateMultipleEntries(arg);

        if (arg.hasValue() && arg.getCriteria() != null)
        {
            for (int v = 0; v < arg.size(); v++)
            {
                /*
                 * The user may have entered in a partial value. If the value
                 * can be normalized to something in the criteria then we will
                 * use the normalized value. This pretty much only applies to
                 * lists even though it is implemented on all criteria.
                 */
                arg.setValue(v, arg.getCriteria().normalizeValue(arg.getValue(v), arg.isCaseSensitive()));
                if (!arg.getCriteria().isSelected((Comparable) arg.getValue(v), arg.isCaseSensitive()))
                {
                    throw new ParseException(arg.getValue(v) + " is not valid for " + arg, -1);
                }
            }
        }
        return tokenIndex;
    }

    static private String replaceEscapes (final String value)
    {
        final boolean backslash = value.contains("\\");
        if (backslash)
            return value.replace("\\", "\\\\");
        return value;
    }

    static private int tokenCount (final Token[] tokens)
    {
        int cnt = 0;
        for (int t = 0; t < tokens.length; t++)
            if (!tokens[t].isUsed())
                cnt++;
        return cnt;
    }

    /**
     * Verify the multiple requirement if any. Use group->values().size().
     */
    static private void validateMultipleEntries (final ICmdLineArg<?> arg) throws ParseException
    {
        if (arg.isRequiredValue() && !arg.hasValue())
            throw new ParseException("missing a required value for " + arg, -1);
        if (arg.hasValue() && arg.size() > 1 && !arg.isMultiple())
            throw new ParseException("multiple values not allowed for " + arg, -1);

        if (arg.hasValue() && arg.isMultiple())
        {
            if (arg.size() < arg.getMultipleMin())
                throw new ParseException("insufficient required values for " + arg, -1);
            if (arg.size() > arg.getMultipleMax())
                throw new ParseException("excessive required values for " + arg, -1);
        }
    }

    String               name;
    String               help;
    final char           commandPrefix;
    final char           notPrefix;
    final List<File>     defaultIncludeDirectories = new ArrayList<>();
    IParserInput         originalInput;
    List<ICmdLineArg<?>> allPossibleArgs           = new ArrayList<>();
    List<ICmdLineArg<?>> _namedBooleans            = null;
    List<ICmdLineArg<?>> _namedValueArgs           = null;
    List<ICmdLineArg<?>> _namedGroups              = null;
    List<ICmdLineArg<?>> _positional               = null;
    int                  depth;

    public CmdLine()
    {
        this("[argument]", null);
    }

    public CmdLine(final String _name)
    {
        this(_name, null, '-', NegateCommandName);
    }

    public CmdLine(final String _name, final char _commandPrefix, final char _notPrefix)
    {
        this(_name, null, _commandPrefix, _notPrefix);
    }

    public CmdLine(final String _name, final String _help)
    {
        this(_name, _help, '-', NegateCommandName);
    }

    public CmdLine(final String _name, final String _help, final char _commandPrefix, final char _notPrefix)
    {
        super();

        this.depth = 0;

        this.commandPrefix = _commandPrefix;
        this.notPrefix = _notPrefix;
        setName(_name);

        if (_help != null)
            setHelp(_help + "\n");

        // final String defaultHelp = INCLUDE_FILE_PREFIX
        // +
        // "<filename> will import a specification from filename. You can return
        // an argument to its default value with "
        // + commandPrefix
        // + notPrefix
        // + " <argument>; eg: "
        // + commandPrefix
        // + "debug and "
        // + commandPrefix
        // + "help can be turned off with "
        // + commandPrefix
        // + notPrefix
        // + "debug,help";
        //
        // if (help == null)
        // setHelp(defaultHelp + "\n");
        // else
        // setHelp(help + "\n\n" + defaultHelp + "\n");
    }

    @Override
    public void add (final ICmdLineArg<?> arg)
    {
        allPossibleArgs.add(arg);
    }

    @Override
    public void add (final int index, final ICmdLineArg<?> arg)
    {
        allPossibleArgs.add(index, arg);
    }

    @Override
    public void addDefaultIncludeDirectory (
        final File defaultIncludeDirectory)
    {
        this.defaultIncludeDirectories.add(defaultIncludeDirectory);
    }

    @Override
    public List<ICmdLineArg<?>> allArgs ()
    {
        return allPossibleArgs;
    }

    @Override
    public void applyDefaults ()
    {
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg instanceof DefaultCLA)
                ((DefaultCLA) arg).applyDefaults(commandPrefix, allPossibleArgs);
            else
                arg.applyDefaults();
        }
    }

    @Override
    public ICmdLineArg<?> arg (final String commandToken)
    {
        if (commandToken == null)
            return null;

        final List<ICmdLineArg<?>> bestArgs = new ArrayList<>();
        matchingArgs(bestArgs, allPossibleArgs, new Token(commandPrefix, commandToken), true);

        if (bestArgs.size() == 0)
            // throw new ParseException(commandToken + " is unknown", -1);
            return null;
        if (bestArgs.size() > 1)
            // throw new ParseException(commandToken + " is ambiguous", -1);
            return null;
        return bestArgs.get(0);
    }

    @Override
    public void asDefinedType (final StringBuilder sb)
    {
        // should not be called.
    }

    @Override
    public Object asEnum (final String _name, final Object[] _possibleConstants) throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in an Enum", 0);
    }

    @Override
    public void assignVariables (final Object target) throws ParseException
    {
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg instanceof CmdLineCLA)
            {
                final CmdLineCLA cmdArg = (CmdLineCLA) arg;
                for (final ICmdLine cl : cmdArg.getValues())
                {
                    Object newtarget = VariableAssigner.getInstance()
                            .newGroupVariable(cmdArg, target, cl.arg(cmdArg.factoryArgName));
                    if (newtarget == null)
                        newtarget = target;
                    cl.assignVariables(newtarget);
                }
            } else if (arg.getVariable() != null && arg.hasValue())
            {
                if (target instanceof Object[])
                {
                    final Object[] targetArray = (Object[]) target;
                    VariableAssigner.getInstance().assign(arg, targetArray[targetArray.length - 1]);
                } else if (target instanceof List)
                {
                    VariableAssigner.getInstance().assign(arg, ((List<?>) target).get(((List<?>) target).size() - 1));
                } else
                    VariableAssigner.getInstance().assign(arg, target);
            }
        }
    }

    private void checkRequired () throws ParseException
    {
        final StringBuilder bldr = new StringBuilder();
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg.isRequired() && !arg.isParsed())
            {
                bldr.append(arg.toString());
                bldr.append(' ');
            }
        }
        if (bldr.length() != 0)
            throw new ParseException("missing required parameters: " + bldr.toString(), -1);
    }

    @Override
    public ICmdLine clone () throws CloneNotSupportedException
    {
        final CmdLine clone = (CmdLine) super.clone();
        clone.allPossibleArgs = new ArrayList<>();
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
            clone.allPossibleArgs.add(aIter.next().clone());
        clone.setDepth(getDepth() + 1);
        return clone;
    }

    @Override
    public int compareTo (final ICmdLine o)
    {
        return 0;
    }

    public ICmdLine compile (final List<String> definition) throws ParseException, IOException
    {
        final String[] args = definition.toArray(new String[definition.size()]);
        return compile(args);
    }

    @Override
    public ICmdLine compile (final String... definition) throws ParseException, IOException
    {
        final CLAFactory factory = new CLAFactory();

        final ICmdLine cmdline = this;
        final List<String> groupdef = new ArrayList<>();

        CmdLineCLA group = null;

        for (final String element : definition)
        {
            if (group != null)
            {
                if (factory.atEnd(commandPrefix, group, element))
                {
                    group.templateCmdLine = new CmdLine(group.keyword == null
                            ? ("" + group.keychar)
                            : ("" + group.keychar + "," + group.keyword), commandPrefix, notPrefix);
                    group.templateCmdLine.compile(groupdef.toArray(new String[groupdef.size()]));
                    groupdef.clear();
                    group = null;
                } else
                {
                    groupdef.add(element);
                }
            } else
            {
                final ICmdLineArg<?> arg = (factory.instanceFor(commandPrefix, element));
                cmdline.add(arg);
                if (arg instanceof CmdLineCLA)
                {
                    group = (CmdLineCLA) arg;
                }
            }
        }
        if (group != null)
            throw new ParseException("unended group: " + group.toString(), -1);

        createSystemGeneratedArguments(factory, cmdline);

        postCompileAnalysis();
        return this;
    }

    /**
     * Allows for handling multiple parser definitions that need to be
     * concatinated together.
     *
     * @throws IOException
     */
    public ICmdLine compile (final String[][] definition) throws ParseException, IOException
    {
        int maxSize = 0;
        for (final String[] x : definition)
            maxSize += x.length;
        final String[] newDef = new String[maxSize];
        int n = 0;
        for (final String[] x : definition)
            for (final String y : x)
                newDef[n++] = y;
        return compile(newDef);
    }

    @Override
    public ICmdLine convert (final String valueStr) throws ParseException, IOException
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    @Override
    public ICmdLine convert (
        final String valueStr,
        final boolean caseSensitive,
        final Object target)
            throws ParseException, IOException
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    void createSystemGeneratedArguments (final CLAFactory factory, final ICmdLine cmdline) throws ParseException,
        IOException
    {
        ICmdLineArg<?> sysgen;

        sysgen = factory.instanceFor(commandPrefix, "-t"
            + CLAFactory.TYPE_DEFAULT
            + " -k'"
            + notPrefix
            + "' -m1 -h'Return one or more arguments to their initial states.'");
        sysgen.setSystemGenerated(true);
        cmdline.add(sysgen);

        sysgen = factory.instanceFor(commandPrefix, "-t"
            + CLAFactory.TYPE_BOOLEAN
            + " -k'"
            + MinHelpCommandName
            + "' -h'Show an abbreviated help message.'");
        sysgen.setSystemGenerated(true);
        cmdline.add(sysgen);

        sysgen = factory.instanceFor(commandPrefix, "-t"
            + CLAFactory.TYPE_BOOLEAN
            + " -k "
            + MaxHelpCommandName
            + " -h'Show complete help.'");
        sysgen.setSystemGenerated(true);
        cmdline.add(sysgen);
    }

    /**
     * @throws ParseException
     */
    private void crossCheck () throws ParseException
    {
        /*
         * not yet, but probably needs a new criteria class
         */
    }

    @Override
    public String defaultInstanceClass ()
    {
        return null;
    }

    @Override
    public void dontAllowCamelCaps ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
    }

    @Override
    public boolean equals (final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CmdLine other = (CmdLine) obj;
        if (name == null)
        {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public void exportCommandLine (final File file) throws IOException
    {
        final FileWriter fis = new FileWriter(file);
        final BufferedWriter buf = new BufferedWriter(fis);
        final StringBuilder str = new StringBuilder();
        exportCommandLine(str);
        buf.write(str.toString().trim());
        buf.close();
    }

    @Override
    public void exportCommandLine (final StringBuilder str)
    {
        CommandLineParser.unparseTokens(allPossibleArgs, str);
    }

    @Override
    public void exportNamespace (final File file) throws IOException
    {
        final FileWriter fis = new FileWriter(file);
        final BufferedWriter buf = new BufferedWriter(fis);
        final StringBuilder str = new StringBuilder();
        exportNamespace("", str);
        buf.write(str.toString());
        buf.close();
    }

    @Override
    public void exportNamespace (final String prefix, final StringBuilder out)
    {
        NamespaceParser.unparseTokens(prefix, allPossibleArgs, out);
    }

    @Override
    public void exportXml (final String tag, final File file) throws IOException
    {
        final FileWriter fis = new FileWriter(file);
        final BufferedWriter buf = new BufferedWriter(fis);
        final StringBuilder str = new StringBuilder();
        str.append("<").append(tag).append(">");
        exportXml(str);
        str.append("</").append(tag).append(">");
        buf.write(str.toString());
        buf.close();
    }

    @Override
    public void exportXml (final StringBuilder out)
    {
        XmlParser.unparseTokens(allPossibleArgs, out);
    }

    private void extractArgumentsFromTokens (
        final Token[] tokens,
        final Object target,
        final List<ICmdLineArg<?>> args)
            throws ParseException, IOException
    {
        if (tokenCount(tokens) > 0)
            parseDirectives(args, tokens, target);

        if (tokenCount(tokens) > 0)
            parseIncludeFiles(args, tokens, target);

        if (tokenCount(tokens) > 0)
            parseNamedBoolean(args, tokens);

        if (isUsageRun())
            return;

        if (tokenCount(tokens) > 0)
            parseNamedGroups(args, tokens, target);
        if (tokenCount(tokens) > 0)
            parseNamedValueArgs(args, tokens);
        if (tokenCount(tokens) > 0)
            parsePositional(args, tokens);
        if (tokenCount(tokens) > 0)
            parseOrphaned(tokens);
    }

    @Override
    public String getCamelCaps ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    @Override
    public char getCommandPrefix ()
    {
        return commandPrefix;
    }

    @Override
    public ICmdLineArgCriteria<?> getCriteria ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    @Override
    public List<ICmdLine> getDefaultValues ()
    {
        return null;
    }

    public Object getDelegateOrValue ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    public Object getDelegateOrValue (final int occurrence)
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    int getDepth ()
    {
        return depth;
    }

    @Override
    public String getEnumClassName ()
    {
        // should not be called.
        return null;
    }

    @Override
    public String getFactoryArgName ()
    {
        return null;
    }

    @Override
    public String getFactoryMethodName ()
    {
        return null;
    }

    @Override
    public String getFormat ()
    {
        return null;
    }

    @Override
    public String getHelp ()
    {
        return help;
    }

    @Override
    public String getInstanceClass ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    @Override
    public Character getKeychar ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return ' ';
    }

    @Override
    public String getKeyword ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    @Override
    public String getMetaphone ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    @Override
    public int getMultipleMax ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return 0;
    }

    @Override
    public int getMultipleMin ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return 0;
    }

    @Override
    public String getName ()
    {
        return name;
    }

    @Override
    public ICmdLine getValue ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    @Override
    public ICmdLine getValue (
        final int index)
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    @Override
    public byte[] getValueAsbyteArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a byte[]", 0);
    }

    @Override
    public Byte[] getValueAsByteArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a Byte[]", 0);
    }

    @Override
    public Date[] getValueAsDateArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a Date[]", 0);
    }

    @Override
    public Equ getValueAsEquation () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in an Equ", 0);
    }

    @Override
    public Equ[] getValueAsEquationArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a Equ[]", 0);
    }

    @Override
    public File[] getValueAsFileArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a File[]", 0);
    }

    @Override
    public float[] getValueAsfloatArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a float[]", 0);
    }

    @Override
    public Float[] getValueAsFloatArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a Float[]", 0);
    }

    @Override
    public int[] getValueAsintArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a int[]", 0);
    }

    @Override
    public Integer[] getValueAsIntegerArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a Integer[]", 0);
    }

    @Override
    public Long[] getValueAsLongArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a Long[]", 0);
    }

    @Override
    public Pattern getValueAsPattern () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a Pattern", 0);
    }

    @Override
    public Pattern[] getValueAsPatternArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a Pattern[]", 0);
    }

    @Override
    public String[] getValueAsStringArray () throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in a String[]", 0);
    }

    @Override
    public String getVariable ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    private Token handleDirective (
        final Token[] tokens,
        final int directiveIdx,
        final int parmStart,
        final int parmEnd)
            throws ParseException, IOException
    {
        final int originalInputStart = tokens[parmStart].getInputStartX();
        final int originalInputEnd = tokens[parmEnd].getInputEndX();

        final String data = originalInput.substring(originalInputStart, originalInputEnd + 1);

        final String directiveName = tokens[directiveIdx].getValue().toLowerCase();
        if ("_date".equals(directiveName))
            return new DateDirective(data).replaceToken(tokens, parmStart, parmEnd);
        if ("_datetime".equals(directiveName))
            return new DateTimeDirective(data).replaceToken(tokens, parmStart, parmEnd);
        if ("_time".equals(directiveName))
            return new TimeDirective(data).replaceToken(tokens, parmStart, parmEnd);
        throw new ParseException("Unknown Directive: " + tokens[directiveIdx], 0);
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null)
                ? 0
                : name.hashCode());
        return result;
    }

    @Override
    public boolean hasValue ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    @Override
    public int indexOf (final ICmdLineArg<?> arg)
    {
        return allPossibleArgs.indexOf(arg);
    }

    @Override
    public boolean isCamelCapsAllowed ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    @Override
    public boolean isCaseSensitive ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    @Override
    public boolean isMetaphoneAllowed ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    @Override
    public boolean isMultiple ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    @Override
    public boolean isParsed ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    @Override
    public boolean isPositional ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    @Override
    public boolean isRequired ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    @Override
    public boolean isRequiredValue ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    @Override
    public boolean isSystemGenerated ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    public boolean isUsageRun ()
    {
        ICmdLineArg<?> arg = arg("" + commandPrefix + MinHelpCommandName);
        boolean usageRun = arg != null && arg.isParsed();
        if (!usageRun)
        {
            arg = arg("" + commandPrefix + commandPrefix + MaxHelpCommandName);
            usageRun = arg != null && arg.isParsed();
        }
        return usageRun;
    }

    /**
     * Attempt to find the specification file in one of several places. We start
     * with the exact name that is given. Then we remove the path and replace
     * the path part with known places where specification files may be found.
     * We only report an error if it could not be found at all.
     *
     * @param includeToken
     * @return
     */
    private Token[] loadCommandLineParserIncludeFile (final String filename) throws ParseException
    {
        File specFile = new File(filename);
        /*
         * Find the file.
         */
        final String nameOnly = specFile.getName();
        if (!specFile.exists())
            for (final File dir : defaultIncludeDirectories)
            {
                specFile = new File(dir, nameOnly);
                if (specFile.exists())
                    break;
            }

        IParserInput clp;
        try
        {
            clp = CommandLineParser.getInstance(commandPrefix, specFile);
            return clp.parseTokens();
        } catch (final IOException e)
        {
            throw new ParseException(INCLUDE_FILE_PREFIX + nameOnly + " could not be found", 0);
        }
    }

    private List<ICmdLineArg<?>> namedBooleans ()
    {
        if (_namedBooleans != null)
            return _namedBooleans;

        _namedBooleans = new ArrayList<>();
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (!arg.isPositional() && !arg.isRequiredValue())
                _namedBooleans.add(arg);
        }
        return _namedBooleans;
    }

    private List<ICmdLineArg<?>> namedGroups ()
    {
        if (_namedGroups != null)
            return _namedGroups;

        _namedGroups = new ArrayList<>();
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (!arg.isPositional() && arg instanceof CmdLineCLA)
                _namedGroups.add(arg);
        }
        return _namedGroups;
    }

    private List<ICmdLineArg<?>> namedValueArgs ()
    {
        if (_namedValueArgs != null)
            return _namedValueArgs;

        _namedValueArgs = new ArrayList<>();
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (!arg.isPositional() && arg.isRequiredValue())
                _namedValueArgs.add(arg);
        }
        return _namedValueArgs;
    }

    @Override
    public Object parse (final IParserInput data) throws IOException, ParseException
    {
        parseTokens(data, null);
        return null;
    }

    @Override
    public Object parse (final IParserInput data, final Object target) throws IOException, ParseException
    {
        parseTokens(data, target);
        return target;
    }

    @Override
    public Object parse (final Object target, final String... args) throws IOException, ParseException
    {
        final IParserInput data = CommandLineParser.getInstance(getCommandPrefix(), args);
        parseTokens(data, target);
        return target;
    }

    @Override
    public Object parse (final String... args) throws IOException, ParseException
    {
        final IParserInput data = CommandLineParser.getInstance(getCommandPrefix(), args);
        parseTokens(data, null);
        return null;
    }

    /**
     * @param args null is ok
     * @param tokens
     * @param target null is ok
     * @throws ParseException
     * @throws IOException
     */
    private void parseDirectives (
        final List<ICmdLineArg<?>> args,
        final Token[] tokens,
        final Object target)
            throws ParseException, IOException
    {
        for (int t = 0; t < tokens.length; t++)
        {
            if (!tokens[t].isUsed())
            {
                if (tokens[t].isParserDirective())
                {
                    /*
                     * A Parser directive always starts with an underscore. It
                     * must be followed by ().
                     */
                    tokens[t].setUsed(true);
                    /*
                     * token + 1 = (
                     *
                     * find corresponding ) token #
                     */
                    final int parmStart = t + 1;
                    int parmEnd = -1;
                    int lex = 0;
                    for (int e = parmStart; e < tokens.length; e++)
                    {
                        tokens[e].setUsed(true);
                        if (tokens[e].isGroupStart())
                        {
                            lex++;
                            continue;
                        }
                        if (tokens[e].isGroupEnd())
                        {
                            lex--;
                            if (lex == 0)
                            {
                                parmEnd = e;
                                break;
                            }
                        }
                    }
                    if (parmEnd == -1)
                        throw new ParseException("unended directive: " + tokens[t], 0);

                    /*
                     * Start and end tokens by requirement are () and not really
                     * part of the directive, so skip over them.
                     */
                    tokens[t] = handleDirective(tokens, t, parmStart + 1, parmEnd - 1);

                    continue;
                }
            }
        }

    }

    private void parseIncludeFiles (
        final List<ICmdLineArg<?>> args,
        final Token[] tokens,
        final Object target)
            throws ParseException, IOException
    {
        for (int t = 0; t < tokens.length; t++)
        {
            if (!tokens[t].isUsed())
            {
                if (tokens[t].isIncludeFile())
                {
                    /*
                     * An include file always starts with @ and is immediately
                     * followed by the filename. However, the file name might be
                     * the next token if it was quoted or there was some
                     * whitespace after the @. And it would otherwise be the
                     * remainder of the @ token.
                     */

                    tokens[t].setUsed(true);
                    Token[] newTokens = null;

                    if (tokens[t].getValue().length() > 1)
                    {
                        newTokens = loadCommandLineParserIncludeFile(tokens[t].getValue().substring(1));
                    } else
                    {
                        final int filenameT = t + 1;

                        if (filenameT >= tokens.length)
                            throw new ParseException("end of input found instead of include directive file name", 0);

                        if (!tokens[filenameT].isLiteral())
                            throw new ParseException("missing include directive file name, found \""
                                + tokens[filenameT].toString()
                                + "\"", 0);

                        tokens[filenameT].setUsed(true);
                        newTokens = loadCommandLineParserIncludeFile(tokens[filenameT].getValue());
                    }
                    extractArgumentsFromTokens(newTokens, target, args);

                    if (isUsageRun())
                    {
                        args.clear();
                        return;
                    }

                    continue;
                }
            }
        }

    }

    private void parseNamedBoolean (final List<ICmdLineArg<?>> args, final Token[] tokens) throws ParseException
    {
        final List<ICmdLineArg<?>> possibleArgs = namedBooleans();
        int tlex = 0;
        for (int t = 0; t < tokens.length; t++)
        {
            if (!tokens[t].isUsed())
            {
                if (tokens[t].isGroupStart())
                {
                    tlex++;
                    continue;
                }
                if (tokens[t].isGroupEnd())
                {
                    tlex--;
                    continue;
                }
                if (tlex == 0)
                {
                    final int holdArgCnt = args.size();
                    while (mostSalient(possibleArgs, tokens, t, args))
                    {
                        if (holdArgCnt < args.size())
                        { // arg was found
                            final BooleanCLA arg = (BooleanCLA) args.get(args.size() - 1);
                            if (arg.keychar != null && arg.keychar == MinHelpCommandName)
                            {
                                System.out.println(UsageBuilder.getWriter(this, false).toString());
                                return;
                            }
                            if (arg.keyword != null && arg.keyword.equalsIgnoreCase(MaxHelpCommandName))
                            {
                                System.out.println(UsageBuilder.getWriter(this, true).toString());
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (tlex != 0)
            throw new ParseException("Unmatched bracket", 0);
    }

    private void parseNamedGroups (
        final List<ICmdLineArg<?>> args,
        final Token[] tokens,
        final Object target)
            throws ParseException, IOException
    {
        final List<ICmdLineArg<?>> possibleArgs = namedGroups();
        for (int t = 0; t < tokens.length; t++)
        {
            if (!tokens[t].isUsed())
            {
                final int holdArgCnt = args.size();
                mostSalient(possibleArgs, tokens, t, args);
                if (holdArgCnt < args.size())
                { // arg was found
                    t = parseGroup((CmdLineCLA) args.get(args.size() - 1), tokens, t, target);
                }
            }
        }
    }

    private void parseNamedValueArgs (final List<ICmdLineArg<?>> args, final Token[] tokens)
        throws ParseException, IOException
    {
        final List<ICmdLineArg<?>> possibleArgs = namedValueArgs();
        int tlex = 0;
        for (int t = 0; t < tokens.length; t++)
        {
            if (!tokens[t].isUsed())
            {
                if (tokens[t].isGroupStart())
                {
                    tlex++;
                    continue;
                }
                if (tokens[t].isGroupEnd())
                {
                    tlex--;
                    continue;
                }
                if (tlex == 0)
                {
                    final int holdArgCnt = args.size();
                    mostSalient(possibleArgs, tokens, t, args);
                    if (holdArgCnt < args.size())
                    { // arg was found
                        t = parseValues(args.get(args.size() - 1), tokens, t);
                    }
                }
            }
        }
        if (tlex != 0)
            throw new ParseException("Unmatched bracket", 0);
    }

    private void parsePositional (final List<ICmdLineArg<?>> args, final Token[] tokens)
        throws ParseException, IOException
    {
        final List<ICmdLineArg<?>> possibleArgs = positional();
        final Iterator<ICmdLineArg<?>> pIter = possibleArgs.iterator();
        int t = 0;
        while (pIter.hasNext())
        {
            final ICmdLineArg<?> arg = pIter.next();

            for (; t < tokens.length; t++)
            {
                if (!tokens[t].isUsed())
                {
                    /*
                     * - and -- commands are mutually exclusive from
                     * positionals.
                     */
                    if (tokens[t].isCommand())
                        continue;

                    if (arg instanceof CmdLineCLA)
                        t = parseGroup((CmdLineCLA) arg, tokens, --t, null);
                    else
                        t = parseValues(arg, tokens, t);
                    args.add(arg);
                    break;
                }
            }
        }
    }

    private List<ICmdLineArg<?>> parseTokens (final IParserInput data, final Object target)
        throws ParseException, IOException
    {
        originalInput = data;

        final Token[] tokens = data.parseTokens();

        resetArgs();

        final List<ICmdLineArg<?>> args = new ArrayList<>();
        extractArgumentsFromTokens(tokens, target, args);

        if (isUsageRun())
        {
            args.clear();
            return args;
        }

        checkForUnusedInput(tokens);
        applyDefaults();
        checkRequired();
        crossCheck();

        /*
         * Only start this process when the top level cmdline is exiting. That
         * means that everything is parsed into the value holders and now we can
         * start making instances.
         */
        if (getDepth() == 0)
            assignVariables(target);

        return args;
    }

    private List<ICmdLineArg<?>> positional ()
    {
        if (_positional != null)
            return _positional;

        _positional = new ArrayList<>();
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg.isPositional())
                _positional.add(arg);
        }
        return _positional;
    }

    void postCompileAnalysis ()
    {
        /*
         * Do not allow camelCaps for a keyword if that is exactly the same as
         * another keyword.
         */
        final List<String> allKnownKeywords = new ArrayList<>();
        for (final ICmdLineArg<?> arg : allPossibleArgs)
        {
            if (arg.getKeyword() != null)
                allKnownKeywords.add(arg.getKeyword().toLowerCase());
        }
        for (final ICmdLineArg<?> arg : allPossibleArgs)
        {
            if (arg.getCamelCaps() != null)
                if (allKnownKeywords.contains(arg.getCamelCaps().toLowerCase()))
                    arg.dontAllowCamelCaps();
        }

    }

    @Override
    public void remove (final ICmdLineArg<?> arg)
    {
        allPossibleArgs.remove(arg);
    }

    @Override
    public void remove (final int argIndex)
    {
        allPossibleArgs.remove(argIndex);
    }

    @Override
    public void reset ()
    {
        // intentionally left blank
    }

    private void resetArgs ()
    {
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            arg.reset();
        }
    }

    @Override
    public ICmdLineArg<ICmdLine> resetCriteria ()
    {
        // intentionally left blank
        return this;
    }

    @Override
    public int salience (final Token word)
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return 0;
    }

    @Override
    public ICmdLineArg<ICmdLine> setCamelCapsAllowed (final boolean bool)
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    @Override
    public ICmdLineArg<ICmdLine> setCaseSensitive (final boolean bool)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setDefaultValue (final String defaultValue) throws ParseException, IOException
    {
        return this;
    }

    void setDepth (
        final int _depth)
    {
        this.depth = _depth;
    }

    @Override
    public ICmdLineArg<ICmdLine> setEnumCriteria (final String enumClassName) throws ParseException, IOException
    {
        return null;
    }

    @Override
    public ICmdLineArg<ICmdLine> setEnumCriteriaAllowError (final String enumClassName)
    {
        return null;
    }

    @Override
    public ICmdLineArg<ICmdLine> setFactoryArgName (final String argName)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setFactoryMethodName (final String methodName)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setFormat (final String format) throws ParseException
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setHelp (final String _help)
    {
        this.help = _help;
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setInstanceClass (final String classString)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setKeychar (final Character _keychar)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setKeyword (final String _keyword)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setListCriteria (final String[] values) throws ParseException, IOException
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setMetaphoneAllowed (final boolean bool)
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    @Override
    public ICmdLineArg<ICmdLine> setMultiple (final boolean bool)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setMultiple (final int min)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setMultiple (final int min, final int max)
    {
        return this;
    }

    public void setName (final String _name)
    {
        this.name = _name;
    }

    @Override
    public ICmdLineArg<ICmdLine> setParsed (final boolean bool)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setPositional (final boolean bool)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setRangeCriteria (final String min, final String max) throws ParseException,
        IOException
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setRegxCriteria (final String pattern) throws ParseException
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setRequired (final boolean bool)
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setRequiredValue (final boolean bool) throws ParseException
    {
        return this;
    }

    @Override
    public ICmdLineArg<ICmdLine> setSystemGenerated (final boolean bool) throws ParseException
    {
        return this;
    }

    @Override
    public void setValue (final ICmdLine value)
    {
        // intentionally left blank
    }

    @Override
    public void setValue (final int index, final ICmdLine value)
    {
        // intentionally left blank
    }

    @Override
    public ICmdLineArg<ICmdLine> setVariable (final String string)
    {
        return this;
    }

    @Override
    public int size ()
    {
        int cnt = 0;
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg instanceof DefaultCLA)
                continue;
            if (arg.isParsed())
                cnt++;
        }
        return cnt;
    }

    @Override
    public void uncompile (final StringBuilder stringBuilder, final boolean showType)
    {
        stringBuilder.append("BEGIN/END something needs to be done in CmdLine#uncompile");
    }

    @Override
    public String uniqueId ()
    {
        return "subparser[" + hashCode() + "]";
    }

    @Override
    public void update (final ICmdLine value)
    {
        // intentionally left blank
    }

    @Override
    public void update (final int index, final ICmdLine value)
    {
        // intentionally left blank
    }

    @Override
    public void useDefaults ()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
    }
}
