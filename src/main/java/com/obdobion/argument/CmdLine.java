package com.obdobion.argument;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.annotation.Args;
import com.obdobion.argument.criteria.ICmdLineArgCriteria;
import com.obdobion.argument.directive.DateDirective;
import com.obdobion.argument.directive.DateTimeDirective;
import com.obdobion.argument.directive.TimeDirective;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;
import com.obdobion.argument.input.NamespaceParser;
import com.obdobion.argument.input.Token;
import com.obdobion.argument.input.XmlParser;
import com.obdobion.argument.type.BooleanCLA;
import com.obdobion.argument.type.CLAFactory;
import com.obdobion.argument.type.ClaType;
import com.obdobion.argument.type.CmdLineCLA;
import com.obdobion.argument.type.DefaultCLA;
import com.obdobion.argument.type.ICmdLineArg;
import com.obdobion.argument.usage.UsageBuilder;
import com.obdobion.argument.variables.VariableAssigner;
import com.obdobion.argument.variables.VariablePuller;

/**
 * <p>
 * CmdLine class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class CmdLine implements ICmdLine, Cloneable
{
    static public ClassLoader  ClassLoader         = CmdLine.class.getClassLoader();

    final static Logger        logger              = LoggerFactory.getLogger(CmdLine.class);
    /** Constant <code>INCLUDE_FILE_PREFIX="@"</code> */
    public static final String INCLUDE_FILE_PREFIX = "@";
    /** Constant <code>MaxHelpCommandName="help"</code> */
    public static final String MaxHelpCommandName  = "help";
    /** Constant <code>MinHelpCommandName='?'</code> */
    public static final char   MinHelpCommandName  = '?';
    /** Constant <code>NegateCommandName='!'</code> */
    public static final char   NegateCommandName   = '!';

    static private void checkForUnusedInput(final Token[] tokens) throws ParseException
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

    /**
     * <p>
     * format.
     * </p>
     *
     * @param format
     *            a {@link java.lang.String} object.
     * @param args
     *            a {@link java.lang.Object} object.
     * @return a {@link java.lang.String} object.
     */
    static public String format(final String format, final Object... args)
    {
        String result = format;
        for (final Object arg : args)
            result = result.replaceFirst("\\{\\}", arg.toString());
        return result;
    }

    /**
     * <p>
     * load.
     * </p>
     *
     * @param cmdLine
     *            a {@link com.obdobion.argument.ICmdLine} object.
     * @param target
     *            a {@link java.lang.Object} object.
     * @param args
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.ICmdLine} object.
     * @throws java.io.IOException
     *             if any.
     * @throws java.text.ParseException
     *             if any.
     */
    static public ICmdLine load(final ICmdLine cmdLine, final Object target, final String... args)
            throws IOException, ParseException
    {
        CmdLine.ClassLoader = target.getClass().getClassLoader();
        final IParserInput data = CommandLineParser.getInstance(cmdLine.getCommandPrefix(), true, args);
        cmdLine.parse(data, target);
        return cmdLine;
    }

    /**
     * <p>
     * load.
     * </p>
     *
     * @param target
     *            a {@link java.lang.Object} object.
     * @param args
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.ICmdLine} object.
     * @throws java.io.IOException
     *             if any.
     * @throws java.text.ParseException
     *             if any.
     */
    static public ICmdLine load(final Object target, final String... args) throws IOException, ParseException
    {
        return load(new CmdLine(), target, args);
    }

    /**
     * <p>
     * loadProperties.
     * </p>
     *
     * @param target
     *            a {@link java.lang.Object} object.
     * @param propertyFile
     *            a {@link java.io.File} object.
     * @return a {@link com.obdobion.argument.ICmdLine} object.
     * @throws java.io.IOException
     *             if any.
     * @throws java.text.ParseException
     *             if any.
     */
    static public ICmdLine loadProperties(final Object target, final File propertyFile)
            throws IOException, ParseException
    {
        CmdLine.ClassLoader = target.getClass().getClassLoader();
        final ICmdLine cmdLine = new CmdLine();
        final IParserInput data = NamespaceParser.getInstance(propertyFile);
        cmdLine.parse(data, target);
        return cmdLine;
    }

    /**
     * <p>
     * loadProperties.
     * </p>
     *
     * @param target
     *            a {@link java.lang.Object} object.
     * @param args
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.ICmdLine} object.
     * @throws java.io.IOException
     *             if any.
     * @throws java.text.ParseException
     *             if any.
     */
    static public ICmdLine loadProperties(final Object target, final String... args)
            throws IOException, ParseException
    {
        CmdLine.ClassLoader = target.getClass().getClassLoader();
        final ICmdLine cmdLine = new CmdLine();
        final IParserInput data = NamespaceParser.getInstance(args);
        cmdLine.parse(data, target);
        return cmdLine;
    }

    /**
     * <p>
     * loadWin.
     * </p>
     *
     * @param target
     *            a {@link java.lang.Object} object.
     * @param args
     *            a {@link java.lang.String} object.
     * @throws java.io.IOException
     *             if any.
     * @throws java.text.ParseException
     *             if any.
     */
    static public void loadWin(final Object target, final String... args) throws IOException, ParseException
    {
        CmdLine.ClassLoader = target.getClass().getClassLoader();
        final CmdLine cmdLine = new CmdLine(null, '/', '-');
        final IParserInput data = CommandLineParser.getInstance(cmdLine.commandPrefix, true, args);
        cmdLine.parse(data, target);
    }

    /**
     * <p>
     * loadXml.
     * </p>
     *
     * @param target
     *            a {@link java.lang.Object} object.
     * @param propertyFile
     *            a {@link java.io.File} object.
     * @return a {@link com.obdobion.argument.ICmdLine} object.
     * @throws java.io.IOException
     *             if any.
     * @throws java.text.ParseException
     *             if any.
     */
    static public ICmdLine loadXml(final Object target, final File propertyFile)
            throws IOException, ParseException
    {
        CmdLine.ClassLoader = target.getClass().getClassLoader();
        final ICmdLine cmdLine = new CmdLine();
        final IParserInput data = XmlParser.getInstance(propertyFile);
        cmdLine.parse(data, target);
        return cmdLine;
    }

    /**
     * <p>
     * loadXml.
     * </p>
     *
     * @param target
     *            a {@link java.lang.Object} object.
     * @param args
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.ICmdLine} object.
     * @throws java.io.IOException
     *             if any.
     * @throws java.text.ParseException
     *             if any.
     */
    static public ICmdLine loadXml(final Object target, final String... args)
            throws IOException, ParseException
    {
        CmdLine.ClassLoader = target.getClass().getClassLoader();
        final ICmdLine cmdLine = new CmdLine();
        final IParserInput data = XmlParser.getInstance(args);
        cmdLine.parse(data, target);
        return cmdLine;
    }

    /**
     * <p>
     * matchingArgs.
     * </p>
     *
     * @param bestArgs
     *            a {@link java.util.List} object.
     * @param possibleArgs
     *            a {@link java.util.List} object.
     * @param token
     *            a {@link com.obdobion.argument.input.Token} object.
     * @param includeAlreadyParsed
     *            a boolean.
     * @return a int.
     */
    static public int matchingArgs(
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
                bestArgs.add(arg);
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
            for (final ICmdLineArg<?> barg : bestArgs)
                if (barg.getKeyword().equalsIgnoreCase(token.getWordCommand()))
                {
                    bestArgs.clear();
                    bestArgs.add(barg);
                    break;
                }

        return maxTokenLengthUsed;
    }

    static private boolean mostSalient(
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

    @SuppressWarnings("null")
    static private int parseGroup(
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

    static private void parseOrphaned(final Token[] tokens) throws ParseException
    {
        final StringBuilder bldr = new StringBuilder();
        for (int t = 0; t < tokens.length; t++)
            if (!tokens[t].isUsed())
            {
                bldr.append(tokens[t].getValue());
                bldr.append(' ');
            }
        if (bldr.length() != 0)
            throw new ParseException("unexpected input: " + bldr.toString(), -1);
    }

    @SuppressWarnings({
            "rawtypes",
            "unchecked"
    })
    static private int parseValues(
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
            arg.setValue(arg.convert(tokens[tokenIndex].remainderValue(), arg.isCaseSensitive(), null));
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
                    arg.setValue(arg.convert(tokens[tokenIndex].getValue(), (arg.isCaseSensitive()), null));
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
            for (int v = 0; v < arg.size(); v++)
            {
                /*
                 * The user may have entered in a partial value. If the value
                 * can be normalized to something in the criteria then we will
                 * use the normalized value. This pretty much only applies to
                 * lists even though it is implemented on all criteria.
                 */
                arg.setValue(v, arg.getCriteria().normalizeValue(arg.getValue(v), arg.isCaseSensitive()));
                if (!arg.getCriteria().isSelected((Comparable) arg.getValue(v),
                        arg.isCaseSensitive()))
                    throw new ParseException(arg.getValue(v) + " is not valid for " + arg,
                            -1);
            }
        return tokenIndex;
    }

    static private String replaceEscapes(final String value)
    {
        final boolean backslash = value.contains("\\");
        if (backslash)
            return value.replace("\\", "\\\\");
        return value;
    }

    static private int tokenCount(final Token[] tokens)
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
    static private void validateMultipleEntries(final ICmdLineArg<?> arg) throws ParseException
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

    /**
     * Set by the factory when this is created.
     */
    int                  uniqueId;

    String               name;
    String               help;
    char                 commandPrefix;
    char                 notPrefix;
    final List<File>     defaultIncludeDirectories = new ArrayList<>();

    IParserInput         originalInput;
    List<ICmdLineArg<?>> allPossibleArgs           = new ArrayList<>();
    List<ICmdLineArg<?>> _namedBooleans            = null;
    List<ICmdLineArg<?>> _namedValueArgs           = null;
    List<ICmdLineArg<?>> _namedGroups              = null;
    List<ICmdLineArg<?>> _positional               = null;
    List<ParseException> parseExceptions           = new ArrayList<>();

    int                  depth;

    /**
     * <p>
     * Constructor for CmdLine.
     * </p>
     */
    public CmdLine()
    {
        this("[argument]", null);
    }

    /**
     * <p>
     * Constructor for CmdLine.
     * </p>
     *
     * @param _name
     *            a {@link java.lang.String} object.
     */
    public CmdLine(final String _name)
    {
        this(_name, null, '-', NegateCommandName);
    }

    /**
     * <p>
     * Constructor for CmdLine.
     * </p>
     *
     * @param _name
     *            a {@link java.lang.String} object.
     * @param _commandPrefix
     *            a char.
     * @param _notPrefix
     *            a char.
     */
    public CmdLine(final String _name, final char _commandPrefix, final char _notPrefix)
    {
        this(_name, null, _commandPrefix, _notPrefix);
    }

    /**
     * <p>
     * Constructor for CmdLine.
     * </p>
     *
     * @param _name
     *            a {@link java.lang.String} object.
     * @param _help
     *            a {@link java.lang.String} object.
     */
    public CmdLine(final String _name, final String _help)
    {
        this(_name, _help, '-', NegateCommandName);
    }

    /**
     * <p>
     * Constructor for CmdLine.
     * </p>
     *
     * @param _name
     *            a {@link java.lang.String} object.
     * @param _help
     *            a {@link java.lang.String} object.
     * @param _commandPrefix
     *            a char.
     * @param _notPrefix
     *            a char.
     */
    public CmdLine(final String _name, final String _help, final char _commandPrefix, final char _notPrefix)
    {
        super();

        depth = 0;

        commandPrefix = _commandPrefix;
        notPrefix = _notPrefix;
        setName(_name);

        if (_help != null)
            setHelp(_help + "\n");

        /*-
        final String defaultHelp = INCLUDE_FILE_PREFIX
                +
                "<filename> will import a specification from filename. You can return an argument to its default value with "
                + commandPrefix
                + notPrefix
                + " <argument>; eg: "
                + commandPrefix
                + "debug and "
                + commandPrefix
                + "help can be turned off with "
                + commandPrefix
                + notPrefix
                + "debug,help";
        if (help == null)
            setHelp(defaultHelp + "\n");
        else
            setHelp(help + "\n\n" + defaultHelp + "\n");
        */
    }

    /** {@inheritDoc} */
    @Override
    public void add(final ICmdLineArg<?> arg)
    {
        allPossibleArgs.add(arg);
    }

    /** {@inheritDoc} */
    @Override
    public void add(final int index, final ICmdLineArg<?> arg)
    {
        allPossibleArgs.add(index, arg);
    }

    /** {@inheritDoc} */
    @Override
    public void addDefaultIncludeDirectory(
            final File defaultIncludeDirectory)
    {
        defaultIncludeDirectories.add(defaultIncludeDirectory);
    }

    /** {@inheritDoc} */
    @Override
    public List<ICmdLineArg<?>> allArgs()
    {
        return allPossibleArgs;
    }

    private List<Field> allAvailableInstanceFields(final Class<?> targetClass)
    {
        final List<Field> fields = new ArrayList<>();
        allAvailableInstanceFields(targetClass, fields);
        return fields;
    }

    private void allAvailableInstanceFields(final Class<?> targetClass, final List<Field> fields)
    {
        for (final Field field : targetClass.getDeclaredFields())
            if (field.isAnnotationPresent(Arg.class) || field.isAnnotationPresent(Args.class))
                fields.add(field);
        if (targetClass.getSuperclass() != Object.class)
            /*
             * Recursive from here, up the hierarchy of classes all the way to
             * the top.
             */
            allAvailableInstanceFields(targetClass.getSuperclass(), fields);
    }

    /** {@inheritDoc} */
    @Override
    public void applyDefaults()
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

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<?> arg(final String commandToken)
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

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<?> argForVariableName(final String variableName) throws ParseException
    {
        if (variableName == null)
            return null;
        for (final ICmdLineArg<?> arg : allPossibleArgs)
            if (arg.getVariable() != null)
                if (arg.getVariable().equals(variableName))
                    return arg;
        throw new ParseException(variableName + " not found or not annotated with @Arg", 0);
    }

    /** {@inheritDoc} */
    @Override
    public void asDefinedType(final StringBuilder sb)
    {
        // should not be called.
    }

    /** {@inheritDoc} */
    @Override
    public Object asEnum(final String _name, final Object[] _possibleConstants) throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in an Enum", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Object[] asEnumArray(final String _name, final Object[] _possibleConstants) throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in an Enum[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public void assignVariables(final Object target) throws ParseException
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
                            .newGroupVariable(cmdArg, target, cl.argForVariableName(cmdArg.getFactoryArgName()));
                    if (newtarget == null)
                        newtarget = target;
                    cl.assignVariables(newtarget);
                }
            } else if (arg.getVariable() != null && arg.hasValue())
                if (target instanceof Object[])
                {
                    final Object[] targetArray = (Object[]) target;
                    VariableAssigner.getInstance().assign(arg, targetArray[targetArray.length - 1]);
                } else if (target instanceof List)
                    VariableAssigner.getInstance().assign(arg, ((List<?>) target).get(((List<?>) target).size() - 1));
                else
                    VariableAssigner.getInstance().assign(arg, target);
        }
    }

    void attemptAnnotationCompile(
            final Class<?> targetClass,
            final boolean topLevel,
            final List<Class<?>> alreadySeen,
            final String[] excludeArgsByVariableName)
                    throws ParseException, IOException
    {
        /*
         * Recursive needs to be allowed. Not sure how to stop incorrect
         * recursion other than to let the resulting stack overflow occur.
         */

        /*-
        if (alreadySeen.contains(targetClass))
            throw new ParseException("recursive argument definition at " +
                    targetClass.toString(), 0);
        */

        alreadySeen.add(targetClass);

        try
        {
            for (final Field oneField : allAvailableInstanceFields(targetClass))
            {
                if (isFieldExcluded(oneField, excludeArgsByVariableName))
                    continue;
                final Args args = oneField.getAnnotation(Args.class);
                if (args == null)
                {
                    final Arg argAnnotation = oneField.getAnnotation(Arg.class);
                    compileArgAnnotation(oneField, argAnnotation, alreadySeen, excludeArgsByVariableName);

                } else
                    for (final Arg argAnnotation : args.value())
                        compileArgAnnotation(oneField, argAnnotation, alreadySeen, excludeArgsByVariableName);
            }

        } finally
        {
            alreadySeen.remove(targetClass);
        }
        if (topLevel)
        {
            createSystemGeneratedArguments(CLAFactory.getInstance(), this);
            parseExceptions = postCompileAnalysis();
            if (!parseExceptions.isEmpty())
            {
                for (final ParseException pe : parseExceptions)
                    logger.warn(pe.getMessage());
                if (parseExceptions.size() == 1)
                    throw parseExceptions.get(0);
                throw new ParseException("multiple parse exceptions", 0);
            }
        }
    }

    private void checkRequired() throws ParseException
    {
        final StringBuilder bldr = new StringBuilder();
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg.isRequired() && !arg.isParsed())
            {
                bldr.append(arg.toString());
                bldr.append(" ");
            }
        }
        if (bldr.length() != 0)
            throw new ParseException("missing required parameters: " + bldr.toString(), -1);
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLine clone() throws CloneNotSupportedException
    {
        final CmdLine clone = (CmdLine) super.clone();
        clone.allPossibleArgs = new ArrayList<>();
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
            clone.allPossibleArgs.add(aIter.next().clone());
        clone.setDepth(getDepth() + 1);
        return clone;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(final ICmdLine o)
    {
        return 0;
    }

    /**
     * @param excludeArgsByVariableName
     */
    private void compileArgAnnotation(
            final Field oneField,
            final Arg argAnnotation,
            final List<Class<?>> alreadySeen,
            final String[] excludeArgsByVariableName)
                    throws ParseException, IOException
    {
        final ICmdLineArg<?> arg = CLAFactory.getInstance().instanceFor(
                commandPrefix,
                oneField,
                argAnnotation);
        if (!argAnnotation.variable().equals(""))
        {
            /*
             * This actually annotating an embedded class, probably because it
             * can not be modified. The previous annotation will be the
             * subparser that this argument should be added to.
             */
            final CmdLineCLA subparser = (CmdLineCLA) argForVariableName(oneField.getName());
            if (subparser == null)
                throw new ParseException("invalid variable reference: " + argAnnotation.variable(), 0);
            subparser.templateCmdLine.add(arg);
            return;
        }
        add(arg);
        if (arg instanceof CmdLineCLA)
        {
            final CmdLine embedded = new CmdLine(arg.getKeyword() == null
                    ? ("" + arg.getKeychar())
                    : ("" + arg.getKeychar() + "," + arg.getKeyword()), commandPrefix, notPrefix);
            ((CmdLineCLA) arg).templateCmdLine = embedded;
            Class<?> embeddedTarget;
            try
            {
                if (((CmdLineCLA) arg).getInstanceClass() != null)
                    embeddedTarget = CmdLine.ClassLoader.loadClass(((CmdLineCLA) arg).getInstanceClass());
                else
                    embeddedTarget = CLAFactory.instanceType(oneField);
            } catch (final ClassNotFoundException e)
            {
                throw new ParseException(e.getMessage(), 0);
            }
            embedded.attemptAnnotationCompile(embeddedTarget, false, alreadySeen, argAnnotation.excludeArgs());
        }
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLine convert(final String valueStr) throws ParseException, IOException
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLine convert(final String valueStr, final boolean caseSensitive, final Object target)
            throws ParseException, IOException
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /**
     * @param factory
     */
    void createSystemGeneratedArguments(final CLAFactory factory, final ICmdLine cmdline)
            throws ParseException
    {
        ICmdLineArg<?> sysgen;

        sysgen = ClaType.DEFAULT.argumentInstance(commandPrefix, notPrefix, null);
        sysgen.setMultiple(1);
        sysgen.setHelp("Return one or more arguments to their initial states.");
        sysgen.setSystemGenerated(true);
        cmdline.add(sysgen);

        sysgen = ClaType.BOOLEAN.argumentInstance(commandPrefix, MinHelpCommandName, null);
        sysgen.setHelp("Show help message.");
        sysgen.setSystemGenerated(true);
        cmdline.add(sysgen);

        sysgen = ClaType.BOOLEAN.argumentInstance(commandPrefix, commandPrefix, MaxHelpCommandName);
        sysgen.setHelp("Show full help message.");
        sysgen.setSystemGenerated(true);
        cmdline.add(sysgen);
    }

    /**
     * @throws ParseException
     */
    private void crossCheck() throws ParseException
    {
        /*
         * not yet, but probably needs a new criteria class
         */
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void dontAllowCamelCaps()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj)
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

    /** {@inheritDoc} */
    @Override
    public void exportCommandLine(final File file) throws IOException
    {
        final FileWriter fis = new FileWriter(file);
        try (final BufferedWriter buf = new BufferedWriter(fis))
        {
            final StringBuilder str = new StringBuilder();
            exportCommandLine(str);
            buf.write(str.toString().trim());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void exportCommandLine(final StringBuilder str)
    {
        /*
         * Making sure that anything the user may have changed still gets the
         * default if necessary.
         */
        applyDefaults();
        CommandLineParser.unparseTokens(allPossibleArgs, str);
    }

    /** {@inheritDoc} */
    @Override
    public void exportNamespace(final File file) throws IOException
    {
        final FileWriter fis = new FileWriter(file);
        try (final BufferedWriter buf = new BufferedWriter(fis))
        {
            final StringBuilder str = new StringBuilder();
            exportNamespace("", str);
            buf.write(str.toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void exportNamespace(final String prefix, final StringBuilder out)
    {
        /*
         * Making sure that anything the user may have changed still gets the
         * default if necessary.
         */
        applyDefaults();
        NamespaceParser.unparseTokens(prefix, allPossibleArgs, out);
    }

    /** {@inheritDoc} */
    @Override
    public void exportXml(final String tag, final File file) throws IOException
    {
        final FileWriter fis = new FileWriter(file);
        try (final BufferedWriter buf = new BufferedWriter(fis))
        {
            final StringBuilder str = new StringBuilder();
            str.append("<").append(tag).append(">");
            exportXml(str);
            str.append("</").append(tag).append(">");
            buf.write(str.toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void exportXml(final StringBuilder out)
    {
        /*
         * Making sure that anything the user may have changed still gets the
         * default if necessary.
         */
        applyDefaults();
        XmlParser.unparseTokens(allPossibleArgs, out);
    }

    private void extractArgumentsFromTokens(
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

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getCamelCaps()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public char getCommandPrefix()
    {
        return commandPrefix;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArgCriteria<?> getCriteria()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /**
     * <p>
     * Getter for the field <code>defaultIncludeDirectories</code>.
     * </p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<File> getDefaultIncludeDirectories()
    {
        return defaultIncludeDirectories;
    }

    /** {@inheritDoc} */
    @Override
    public List<ICmdLine> getDefaultValues()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Object getDelegateOrValue()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Object getDelegateOrValue(final int occurrence)
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    int getDepth()
    {
        return depth;
    }

    /** {@inheritDoc} */
    @Override
    public String getEnumClassName()
    {
        // should not be called.
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getFactoryArgName()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getFactoryMethodName()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getFormat()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getHelp()
    {
        return help;
    }

    /** {@inheritDoc} */
    @Override
    public String getInstanceClass()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Character getKeychar()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return ' ';
    }

    /** {@inheritDoc} */
    @Override
    public String getKeyword()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getMetaphone()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public int getMultipleMax()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public int getMultipleMin()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public List<ParseException> getParseExceptions()
    {
        return parseExceptions;
    }

    /** {@inheritDoc} */
    @Override
    public int getUniqueId()
    {
        return uniqueId;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLine getValue()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLine getValue(final int index)
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public byte[] getValueAsbyteArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a byte[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Byte[] getValueAsByteArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Byte[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Calendar[] getValueAsCalendarArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Calendar[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Character[] getValueAsCharacterArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Character[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public char[] getValueAscharArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a char[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Date[] getValueAsDateArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Date[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public double[] getValueAsdoubleArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a double[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Double[] getValueAsDoubleArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Double[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Equ getValueAsEquation() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in an Equ", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Equ[] getValueAsEquationArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Equ[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public File[] getValueAsFileArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a File[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public float[] getValueAsfloatArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a float[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Float[] getValueAsFloatArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Float[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public int[] getValueAsintArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a int[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Integer[] getValueAsIntegerArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Integer[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public long[] getValueAslongArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a long[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Long[] getValueAsLongArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Long[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Pattern getValueAsPattern() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Pattern", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Pattern[] getValueAsPatternArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a Pattern[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public String[] getValueAsStringArray() throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + toString() + " in a String[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public String getVariable()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    private Token handleDirective(
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

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null)
                ? 0
                : name.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasValue()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int indexOf(final ICmdLineArg<?> arg)
    {
        return allPossibleArgs.indexOf(arg);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCamelCapsAllowed()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCaseSensitive()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    /**
     * <p>
     * isCompiled.
     * </p>
     *
     * @return a boolean.
     */
    public boolean isCompiled()
    {
        return !allPossibleArgs.isEmpty();
    }

    private boolean isFieldExcluded(final Field oneField, final String[] excludeArgsByVariableName)
    {
        for (int f = 0; f < excludeArgsByVariableName.length; f++)
            if (oneField.getName().equalsIgnoreCase(excludeArgsByVariableName[f]))
                return true;
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMetaphoneAllowed()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMultiple()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isParsed()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPositional()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRequired()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRequiredValue()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSystemGenerated()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return false;
    }

    /**
     * <p>
     * isUsageRun.
     * </p>
     *
     * @return a boolean.
     */
    public boolean isUsageRun()
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
    private Token[] loadCommandLineParserIncludeFile(final String filename) throws ParseException
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

    private List<ICmdLineArg<?>> namedBooleans()
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

    private List<ICmdLineArg<?>> namedGroups()
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

    private List<ICmdLineArg<?>> namedValueArgs()
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

    /** {@inheritDoc} */
    @Override
    public Object parse(final IParserInput data) throws IOException, ParseException
    {
        parseTokens(data, null);
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Object parse(final IParserInput data, final Object target)
            throws IOException, ParseException
    {
        if (!isCompiled())
            if (target == null)
                attemptAnnotationCompile(data.getClass(), true, new ArrayList<Class<?>>(), new String[] {});
            else
            {
                CmdLine.ClassLoader = target.getClass().getClassLoader();
                attemptAnnotationCompile(target.getClass(), true, new ArrayList<Class<?>>(), new String[] {});
            }
        parseTokens(data, target);
        return target;
    }

    /** {@inheritDoc} */
    @Override
    public Object parse(final Object target, final String... args)
            throws IOException, ParseException
    {
        if (target != null)
            CmdLine.ClassLoader = target.getClass().getClassLoader();

        final IParserInput data = CommandLineParser.getInstance(getCommandPrefix(), args);
        if (!isCompiled())
            if (target == null)
                attemptAnnotationCompile(data.getClass(), true, new ArrayList<Class<?>>(), new String[] {});
            else
                attemptAnnotationCompile(target.getClass(), true, new ArrayList<Class<?>>(), new String[] {});
        parseTokens(data, target);
        return target;
    }

    /** {@inheritDoc} */
    @Override
    public Object parse(final String... args) throws IOException, ParseException
    {
        final IParserInput data = CommandLineParser.getInstance(getCommandPrefix(), args);
        parseTokens(data, null);
        return null;
    }

    /**
     * @param args
     *            null is ok
     * @param tokens
     * @param target
     *            null is ok
     * @throws ParseException
     * @throws IOException
     */
    private void parseDirectives(
            final List<ICmdLineArg<?>> args,
            final Token[] tokens,
            final Object target)
                    throws ParseException, IOException
    {
        for (int t = 0; t < tokens.length; t++)
            if (!tokens[t].isUsed())
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

    private void parseIncludeFiles(
            final List<ICmdLineArg<?>> args,
            final Token[] tokens,
            final Object target)
                    throws ParseException, IOException
    {
        for (int t = 0; t < tokens.length; t++)
            if (!tokens[t].isUsed())
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
                        newTokens = loadCommandLineParserIncludeFile(tokens[t].getValue().substring(1));
                    else
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

    private void parseNamedBoolean(final List<ICmdLineArg<?>> args, final Token[] tokens) throws ParseException
    {
        final List<ICmdLineArg<?>> possibleArgs = namedBooleans();
        int tlex = 0;
        for (int t = 0; t < tokens.length; t++)
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
                        if (holdArgCnt < args.size())
                        { // arg was found
                            final BooleanCLA arg = (BooleanCLA) args.get(args.size() - 1);
                            if (arg.getKeychar() != null && arg.getKeychar() == MinHelpCommandName)
                            {
                                System.out.println(UsageBuilder.getWriter(this, false).toString());
                                return;
                            }
                            if (arg.getKeyword() != null && arg.getKeyword().equalsIgnoreCase(MaxHelpCommandName))
                            {
                                System.out.println(UsageBuilder.getWriter(this, true).toString());
                                return;
                            }
                        }
                }
            }
        if (tlex != 0)
            throw new ParseException("Unmatched bracket", 0);
    }

    private void parseNamedGroups(
            final List<ICmdLineArg<?>> args,
            final Token[] tokens,
            final Object target)
                    throws ParseException, IOException
    {
        final List<ICmdLineArg<?>> possibleArgs = namedGroups();
        for (int t = 0; t < tokens.length; t++)
            if (!tokens[t].isUsed())
            {
                final int holdArgCnt = args.size();
                mostSalient(possibleArgs, tokens, t, args);
                if (holdArgCnt < args.size())
                    t = parseGroup((CmdLineCLA) args.get(args.size() - 1), tokens, t, target);
            }
    }

    private void parseNamedValueArgs(final List<ICmdLineArg<?>> args, final Token[] tokens)
            throws ParseException, IOException
    {
        final List<ICmdLineArg<?>> possibleArgs = namedValueArgs();
        int tlex = 0;
        for (int t = 0; t < tokens.length; t++)
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
                        t = parseValues(args.get(args.size() - 1), tokens, t);
                }
            }
        if (tlex != 0)
            throw new ParseException("Unmatched bracket", 0);
    }

    private void parsePositional(final List<ICmdLineArg<?>> args, final Token[] tokens)
            throws ParseException, IOException
    {
        final List<ICmdLineArg<?>> possibleArgs = positional();
        final Iterator<ICmdLineArg<?>> pIter = possibleArgs.iterator();
        int t = 0;
        while (pIter.hasNext())
        {
            final ICmdLineArg<?> arg = pIter.next();

            for (; t < tokens.length; t++)
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

    private List<ICmdLineArg<?>> parseTokens(final IParserInput data, final Object target)
            throws ParseException, IOException
    {
        if (!isCompiled())
            throw new ParseException("parser must be compiled", 0);

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

    private List<ICmdLineArg<?>> positional()
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

    List<ParseException> postCompileAnalysis()
    {
        final List<ParseException> localExceptions = new ArrayList<>();
        /*
         * scanforward keeps redundant errors from appearing
         */
        boolean scanforward = false;
        for (final ICmdLineArg<?> arg1 : allPossibleArgs)
        {
            if (arg1.isCamelCapsAllowed() && arg1.getCamelCaps() == null)
                localExceptions
                        .add(new ParseException(format("camelCaps not allowed for \"{}\"", arg1.getKeyword()), 0));

            scanforward = false;
            for (final ICmdLineArg<?> arg2 : allPossibleArgs)
            {
                if (arg1 == arg2)
                {
                    scanforward = true;
                    continue;
                }
                if (!scanforward)
                    continue;
                if (arg1.getKeychar() > ' ' && arg1.getKeychar() == arg2.getKeychar())
                    localExceptions.add(new ParseException(
                            format("duplicate short name, found \"{}\"' and \"{}\"", arg1, arg2),
                            0));
                if (arg1.getKeyword() != null && arg1.getKeyword().equalsIgnoreCase(arg2.getKeyword()))
                    localExceptions.add(new ParseException(
                            format("duplicate long name, found \"{}\"' and \"{}\"", arg1, arg2),
                            0));
                if (arg1.isCamelCapsAllowed() && arg2.isCamelCapsAllowed() && arg1.getCamelCaps() != null
                        && arg1.getCamelCaps().equalsIgnoreCase(arg2.getCamelCaps()))
                    localExceptions.add(new ParseException(
                            format("duplicate values for camelcap, found \"{}\"' and \"{}\"", arg1, arg2),
                            0));
                if (arg1.isCamelCapsAllowed() && arg1.getCamelCaps() != null
                        && arg1.getCamelCaps().equalsIgnoreCase(arg2.getKeyword()))
                    localExceptions.add(new ParseException(
                            format("camelcaps equals long name, found \"{}\"' and \"{}\"", arg1, arg2),
                            0));
                if (arg1.isMetaphoneAllowed() && arg2.isMetaphoneAllowed() && arg1.getMetaphone() != null
                        && arg1.getMetaphone().equals(arg2.getMetaphone()))
                    localExceptions.add(new ParseException(
                            format("duplicate values for metaphone, found \"{}\"' and \"{}\"", arg1, arg2),
                            0));
                if (arg1.isMetaphoneAllowed() && arg1.getMetaphone() != null
                        && arg1.getMetaphone().toString().equals(arg2.getKeyword()))
                    localExceptions.add(new ParseException(
                            format("metaphone equals long name, found \"{}\"' and \"{}\"", arg1, arg2),
                            0));
                if (arg1.isPositional() && arg1.isMultiple() && arg2.isPositional())
                    localExceptions.add(new ParseException(
                            format("a multi-value positional argument must be the only positional argument, found \"{}\"' and \"{}\"",
                                    arg1, arg2),
                            0));
            }
        }
        return localExceptions;
    }

    /**
     * {@inheritDoc}
     *
     * Pulls the values of the variables back into the arguments. This is
     * usually in preparation for an export. When used in conjunction with a
     * properties file, for instance, this allows the program to periodically
     * update the properties and then pull them back into the properties file on
     * disk.
     */
    @Override
    public void pull(final Object variableSource) throws ParseException
    {
        reset();

        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg.getVariable() != null)
                try
                {
                    VariablePuller.getInstance().pull(arg, variableSource);
                } catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new ParseException(e.getMessage(), 0);
                }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void remove(final ICmdLineArg<?> arg)
    {
        allPossibleArgs.remove(arg);
    }

    /** {@inheritDoc} */
    @Override
    public void remove(final int argIndex)
    {
        allPossibleArgs.remove(argIndex);
    }

    /** {@inheritDoc} */
    @Override
    public void reset()
    {
        // n/a
    }

    private void resetArgs()
    {
        final Iterator<ICmdLineArg<?>> aIter = allPossibleArgs.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            arg.reset();
        }
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> resetCriteria()
    {
        // intentionally left blank
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public int salience(final Token word)
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setCamelCapsAllowed(final boolean bool)
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setCaseSensitive(final boolean bool)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setDefaultValue(final String defaultValue) throws ParseException, IOException
    {
        return this;
    }

    void setDepth(
            final int _depth)
    {
        depth = _depth;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setEnumCriteria(final String enumClassName) throws ParseException, IOException
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setEnumCriteriaAllowError(final String enumClassName)
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setFactoryArgName(final String argName)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setFactoryMethodName(final String methodName)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setFormat(final String format) throws ParseException
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setHelp(final String _help)
    {
        help = _help;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setInstanceClass(final String classString)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setKeychar(final Character _keychar)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setKeyword(final String _keyword)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setListCriteria(final String[] values) throws ParseException, IOException
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setMetaphoneAllowed(final boolean bool)
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setMultiple(final boolean bool)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setMultiple(final int min)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setMultiple(final int min, final int max)
    {
        return this;
    }

    /**
     * <p>
     * Setter for the field <code>name</code>.
     * </p>
     *
     * @param _name
     *            a {@link java.lang.String} object.
     */
    public void setName(final String _name)
    {
        name = _name;
    }

    /** {@inheritDoc} */
    @Override
    public void setObject(final Object value)
    {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setParsed(final boolean bool)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setPositional(final boolean bool)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setRangeCriteria(final String min, final String max) throws ParseException,
            IOException
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setRegxCriteria(final String pattern) throws ParseException
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setRequired(final boolean bool)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setRequiredValue(final boolean bool) throws ParseException
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setSystemGenerated(final boolean bool) throws ParseException
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public void setType(final ClaType claType)
    {
        // n/a
    }

    /** {@inheritDoc} */
    @Override
    public void setUniqueId(final int uId)
    {
        uniqueId = uId;
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final ICmdLine value)
    {
        // intentionally left blank
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final int index, final ICmdLine value)
    {
        // intentionally left blank
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> setVariable(final String string)
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
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

    /** {@inheritDoc} */
    @Override
    public boolean supportsCamelCaps()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsCaseSensitive()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsDefaultValues()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsExcludeArgs()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsFactoryArgName()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsFactoryMethod()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsFormat()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsHelp()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInList()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInstanceClass()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsLongName()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsMatches()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsMetaphone()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsMultimax()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsMultimin()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsPositional()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsRange()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsRequired()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsShortName()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void useDefaults()
    {
        /*
         * This is only here as a place-holder so that this class can be a sub
         * command line as well as a top level.
         */
    }
}
