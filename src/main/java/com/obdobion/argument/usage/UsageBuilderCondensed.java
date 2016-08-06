package com.obdobion.argument.usage;

import java.util.List;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.type.CmdLineCLA;
import com.obdobion.argument.type.ICmdLineArg;

/**
 * @author Chris DeGreef
 *
 */
public class UsageBuilderCondensed extends UsageBuilder
{
    UsageBuilderCondensed()
    {
        super();
    }

    @Override
    int getIndentSize()
    {
        return 10;
    }

    @Override
    void prettyPrint(
            final ICmdLine icmdLine)
    {
        prettyPrintNoHelp(icmdLine);

        newLine();
        append("-" + CmdLine.MinHelpCommandName + " This help and exit.");
        newLine();
        append("--" + CmdLine.MaxHelpCommandName + " for more help.");
    }

    private void prettyPrintNoHelp(
            final ICmdLine icmdLine)
    {
        final CmdLine cmdLine = (CmdLine) icmdLine;
        usageHeader(cmdLine.getCommandPrefix(), cmdLine, 0);
        showEachOwnedArg(cmdLine.allArgs(), cmdLine.getCommandPrefix(), cmdLine, 0);
        showEmbeddedParsers(cmdLine.allArgs(), cmdLine.getCommandPrefix(), cmdLine, 0);
    }

    /**
     * @param isRequired
     * @param header
     * @param aIter
     * @param _commandPrefix
     * @param _cmdLine
     * @param indentLevel
     * @return
     */
    boolean showCondensedOwnedArgs(
            final boolean isRequired,
            final String header,
            final List<ICmdLineArg<?>> aIter,
            final char _commandPrefix,
            final ICmdLineArg<?> _cmdLine,
            final int indentLevel)
    {
        setIndentLevel(indentLevel + 1);
        boolean anyShown = false;
        for (final ICmdLineArg<?> arg : aIter)
        {
            if (arg.isRequired() != isRequired)
                continue;

            if (arg.getKeychar().charValue() == CmdLine.NegateCommandName)
                continue;
            if (arg.getKeychar().charValue() == CmdLine.MinHelpCommandName)
                continue;
            if (arg.getKeyword() != null && arg.getKeyword().equalsIgnoreCase(CmdLine.MaxHelpCommandName))
                continue;

            if (arg.getKeychar() != null && arg.getKeychar().charValue() != ' ')
            {
                if (!anyShown)
                    append(header);
                append("" + arg.getKeychar().charValue());
                if (arg.isPositional())
                    append("*");
                anyShown = true;
            }
        }
        setIndentLevel(indentLevel);
        return anyShown;
    }

    void showEachOwnedArg(
            final List<ICmdLineArg<?>> aIter,
            final char commandPrefix,
            final ICmdLineArg<?> cmdLine,
            final int indentLevel)
    {
        boolean anyShown, anyEverShown = false;
        String header;
        header = "required: " + commandPrefix;
        anyShown = showCondensedOwnedArgs(true, header, aIter, commandPrefix, cmdLine, indentLevel);
        anyEverShown |= anyShown;
        if (anyShown)
        {
            newLine(indentLevel);
            header = "          " + commandPrefix + commandPrefix;
        } else
            header = "required: " + commandPrefix + commandPrefix;
        anyShown = showVerboseOwnedArgs(true, header, aIter, commandPrefix, cmdLine, indentLevel);
        anyEverShown |= anyShown;

        if (anyEverShown)
            newLine(indentLevel);

        header = "optional: " + commandPrefix;
        anyShown = showCondensedOwnedArgs(false, header, aIter, commandPrefix, cmdLine, indentLevel);
        anyEverShown |= anyShown;
        if (anyShown)
        {
            newLine(indentLevel);
            header = "          " + commandPrefix + commandPrefix;
        } else
            header = "optional: " + commandPrefix + commandPrefix;
        anyShown = showVerboseOwnedArgs(false, header, aIter, commandPrefix, cmdLine, indentLevel);
        anyEverShown |= anyShown;

        if (anyEverShown)
            newLine(indentLevel);
    }

    /**
     * @param aIter
     * @param commandPrefix
     * @param cmdLine
     * @param indentLevel
     */
    void showEmbeddedParsers(
            final List<ICmdLineArg<?>> aIter,
            final char commandPrefix,
            final ICmdLineArg<?> cmdLine,
            final int indentLevel)
    {
        for (final ICmdLineArg<?> arg : aIter)
        {
            if (!(arg instanceof CmdLineCLA))
                continue;
            prettyPrintNoHelp(((CmdLineCLA) arg).templateCmdLine);
        }
    }

    /**
     * @param isRequired
     * @param header
     * @param aIter
     * @param commandPrefix
     * @param cmdLine
     * @param indentLevel
     * @return
     */
    boolean showVerboseOwnedArgs(
            final boolean isRequired,
            final String header,
            final List<ICmdLineArg<?>> aIter,
            final char commandPrefix,
            final ICmdLineArg<?> cmdLine,
            final int indentLevel)
    {
        setIndentLevel(indentLevel + 1);
        boolean anyShown = false;
        for (final ICmdLineArg<?> arg : aIter)
        {
            if (arg.isRequired() != isRequired)
                continue;

            if (arg.getKeychar().charValue() == CmdLine.NegateCommandName)
                continue;
            if (arg.getKeychar().charValue() == CmdLine.MinHelpCommandName)
                continue;
            if (arg.getKeyword() != null && arg.getKeyword().equalsIgnoreCase(CmdLine.MaxHelpCommandName))
                continue;

            /*
             * Don't show verbose if there was a condensed version shown
             */
            if (arg.getKeychar() != null && arg.getKeychar() != ' ')
                continue;

            if (arg.getKeyword() != null)
            {
                if (arg.getKeychar().charValue() == CmdLine.NegateCommandName)
                    continue;
                if (arg.getKeychar().charValue() == CmdLine.MinHelpCommandName)
                    continue;
                if (arg.getKeyword() != null && arg.getKeyword().equalsIgnoreCase(CmdLine.MaxHelpCommandName))
                    continue;

                if (!anyShown)
                    append(header);
                else
                    append(",");
                anyShown = true;
                append(arg.getKeyword());
                if (arg.isPositional())
                    append("*");
            }
        }
        setIndentLevel(indentLevel);
        return anyShown;
    }

    /**
     * @param commandPrefix
     * @param icmdLine
     * @param indentLevel
     */
    void usageHeader(
            final char commandPrefix,
            final ICmdLine icmdLine,
            final int indentLevel)
    {
        final CmdLine arg = (CmdLine) icmdLine;

        if (arg.getName() != null && !"[argument]".equals(arg.getName()))
        {
            append(arg.getName());
            newLine(indentLevel);
        }
    }
}
