package com.obdobion.argument;

import java.io.File;
import java.util.Iterator;

/**
 * @author Chris DeGreef
 * 
 */
public class UsageBuilderVerbose extends UsageBuilder
{
    UsageBuilderVerbose()
    {
        super();
    }

    @Override
    void prettyPrint (
        final ICmdLine icmdLine)
    {
        final CmdLine cmdLine = (CmdLine) icmdLine;
        usageHeader((cmdLine).commandPrefix, cmdLine, 0);
        showEachOwnedArg((cmdLine).allPossibleArgs.iterator(), cmdLine.commandPrefix, cmdLine, 0);
    }

    /**
     * @param aIter
     * @param commandPrefix
     * @param cmdLine
     * @param indentLevel
     */
    void showEachOwnedArg (
        final Iterator<ICmdLineArg<?>> aIter,
        final char commandPrefix,
        final ICmdLineArg<?> cmdLine,
        final int indentLevel)
    {
        int cnt = 0;
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();

            if (arg.getKeychar().charValue() == CmdLine.NegateCommandName)
                continue;
            if (arg.getKeychar().charValue() == CmdLine.MinHelpCommandName)
                continue;
            if (arg.getKeyword() != null && arg.getKeyword().equalsIgnoreCase(CmdLine.MaxHelpCommandName))
                continue;

            if (cnt++ > 0)
                newLine(indentLevel);

            if (arg instanceof CmdLineCLA)
                usageDetail(commandPrefix, (CmdLineCLA) arg, indentLevel + 1);
            else
                usageDetail(commandPrefix, arg, indentLevel + 1);
        }
    }

    void usageDetail (
        final char commandPrefix,
        final CmdLineCLA cmdLine,
        final int indentLevel)
    {
        usageDetail(commandPrefix, ((ICmdLineArg<?>) cmdLine), indentLevel);
        showEachOwnedArg((cmdLine.templateCmdLine).allArgs().iterator(), commandPrefix, cmdLine, indentLevel);
    }

    void usageDetail (
        final char commandPrefix,
        final ICmdLineArg<?> arg,
        int _indentLevel)
    {
        int indentLevel = _indentLevel;

        newLine(indentLevel);

        if (!arg.isRequired())
            append("[");
        if (!arg.isPositional())
        {
            if (arg.getKeyword() != null)
                if (arg.getKeychar() != null && arg.getKeychar().charValue() != ' ')
                    append(""
                        + commandPrefix
                        + arg.getKeychar().charValue()
                        + " "
                        + commandPrefix
                        + commandPrefix
                        + arg.getKeyword());
                else
                    append("" + commandPrefix + commandPrefix + arg.getKeyword());

            else if (arg.getKeychar() != null && arg.getKeychar().charValue() != ' ')
                append("" + commandPrefix + arg.getKeychar().charValue());
            else
                append("unnamed");
        } else
        {
            append("positional");
        }

        final String n = arg.getClass().getSimpleName();
        // ClassnameCLA is how the arguments must be named
        final String name = n.substring(0, n.length() - 3);
        if ("Boolean".equals(name))
        {
            // intentionally left blank
        }
        else if ("CmdLine".equals(name))
            append(" ()");
        else
        {
            append(" <");
            append(name.toLowerCase());
            append(">");
        }
        if (!arg.isRequired())
            append("]");

        String help = ((AbstractCLA<?>) arg).getHelp();

        newLine(++indentLevel);
        if (help != null && help.trim().length() > 0)
            append(help);

        if ("CmdLine".equals(name))
            newLine(indentLevel);

        usageDetailModifiers(commandPrefix, arg, indentLevel);

        if (((AbstractCLA<?>) arg).getFormat() != null)
            newLine(indentLevel).append("The value must adhere to \"").append(
                ((AbstractCLA<?>) arg).getFormat() + "\".");
    }

    /**
     * @param commandPrefix
     * @param arg
     * @param indentLevel
     */
    void usageDetailModifiers (
        final char commandPrefix,
        final ICmdLineArg<?> arg,
        final int indentLevel)
    {

        final String n = arg.getClass().getSimpleName();
        // ClassnameCLA is how the arguments must be named
        final String name = n.substring(0, n.length() - 3);
        if ("Boolean".equals(name))
            return;

        if (arg.isMultiple())
        {
            if (arg.getMultipleMax() == Integer.MAX_VALUE)
                if (arg.isRequired())
                    newLine(indentLevel).append(
                        "Although multiple values are allowed, you must specify at least " + arg.getMultipleMin()
                            + ".");
                else
                    newLine(indentLevel).append(
                        "Although no value is required, if you specify one you must specify at least "
                            + arg.getMultipleMin() + ".");
            else
            {
                if (arg.isRequired())
                    newLine(indentLevel).append("It is required that you specify at least "
                        + arg.getMultipleMin()
                        + " and at most "
                        + arg.getMultipleMax()
                        + " values.");
                else
                    newLine(indentLevel).append(
                        "Although no value is required, if you specify one you must specify at least "
                            + arg.getMultipleMin()
                            + " and at most "
                            + arg.getMultipleMax()
                            + " values.");
            }
        }

        if (arg.isCaseSensitive())
        {
            newLine(indentLevel).append("Upper vs lower case matters.");
        }

        if (arg.getCriteria() != null)
        {
            newLine(indentLevel);
            arg.getCriteria().usage(this, indentLevel);
        }

        if (arg instanceof AbstractCLA)
        {
            final AbstractCLA<?> acla = (AbstractCLA<?>) arg;
            if (acla.getDefaultValues() != null && !acla.getDefaultValues().isEmpty())
            {
                newLine(indentLevel).append("If unspecified, the default will be");
                for (final Object dv : acla.getDefaultValues())
                {
                    append(" ");
                    if (dv instanceof Byte)
                        append(ByteCLA.asLiteral(((Byte) dv).byteValue()));
                    else
                        append(dv.toString());
                }
                append(".");
            }
        }
    }

    /**
     * @param commandPrefix
     * @param icmdLine
     * @param indentLevel
     */
    void usageHeader (
        final char commandPrefix,
        final ICmdLine icmdLine,
        final int indentLevel)
    {
        final CmdLine commandLineParser = (CmdLine) icmdLine;

        if (commandLineParser.getName() != null)
        {
            append(commandLineParser.getName());
            newLine(indentLevel);
        }
        if (indentLevel == 0 && commandLineParser.getHelp() != null)
        {
            if (commandLineParser.getName() != null)
                newLine(indentLevel);
            append(commandLineParser.getHelp());
            newLine(indentLevel);
        }
        if (indentLevel == 0 && !commandLineParser.defaultIncludeDirectories.isEmpty())
        {
            newLine(indentLevel);
            append("Import path =");
            for (final File dir : commandLineParser.defaultIncludeDirectories)
            {
                append(" ");
                append(dir.getAbsolutePath());
                append(";");
            }
            newLine(indentLevel);
        }

    }
}
