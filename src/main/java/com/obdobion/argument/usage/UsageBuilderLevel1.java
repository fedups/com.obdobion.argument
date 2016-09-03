package com.obdobion.argument.usage;

import java.util.Iterator;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.type.AbstractCLA;
import com.obdobion.argument.type.CmdLineCLA;
import com.obdobion.argument.type.ICmdLineArg;

/**
 * <p>
 * UsageBuilderLevel2 class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.3.4
 */
public class UsageBuilderLevel1 extends UsageBuilder
{
    /**
     * <p>
     * Constructor for UsageBuilderLevel2.
     * </p>
     */
    public UsageBuilderLevel1()
    {
        super();
    }

    void nameIt(final char commandPrefix, final ICmdLineArg<?> arg)
    {
        if (!arg.isRequired())
            append("[");
        if (!arg.isPositional())
            if (arg.getKeyword() != null)
            {
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
                if (arg.isCamelCapsAllowed())
                    append(" " + commandPrefix + commandPrefix + arg.getCamelCaps());
            } else if (arg.getKeychar() != null && arg.getKeychar().charValue() != ' ')
                append("" + commandPrefix + arg.getKeychar().charValue());
            else
                append("unnamed");

        final String n = arg.getClass().getSimpleName();
        // ClassnameCLA is how the arguments must be named
        final String name = n.substring(0, n.length() - 3);
        if (!("Boolean".equalsIgnoreCase(name)))
        {
            if (!arg.isPositional())
                append(" ");

            if ("CmdLine".equals(name))
                append("()");
            else
            {
                append("<");
                append(name.toLowerCase());
                append(">");
            }
        }
        if (arg.isMultiple())
            append("...");
        if (!arg.isRequired())
            append("]");
    }

    @Override
    void prettyPrint(
            final ICmdLine icmdLine)
    {
        final CmdLine cmdLine = (CmdLine) icmdLine;
        usageHeader((cmdLine).getCommandPrefix(), cmdLine, 0);
        showEachOwnedArg((cmdLine).allArgs().iterator(), cmdLine.getCommandPrefix(), cmdLine, 0);
    }

    /**
     * @param aIter
     * @param commandPrefix
     * @param cmdLine
     * @param indentLevel
     */
    void showEachOwnedArg(
            final Iterator<ICmdLineArg<?>> aIter,
            final char commandPrefix,
            final ICmdLineArg<?> cmdLine,
            final int indentLevel)
    {
        int cnt = 0;
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();

            if (arg.isSystemGenerated())
                continue;

            if (cnt++ > 0)
                newLine(indentLevel);

            if (arg instanceof CmdLineCLA)
                usageDetail(commandPrefix, (CmdLineCLA) arg, indentLevel + 1);
            else
                usageDetail(commandPrefix, arg, indentLevel + 1);
        }
    }

    void usageDetail(
            final char commandPrefix,
            final CmdLineCLA cmdLine,
            final int indentLevel)
    {
        usageDetail(commandPrefix, ((ICmdLineArg<?>) cmdLine), indentLevel);
        newLine(indentLevel);
        showEachOwnedArg((cmdLine.templateCmdLine).allArgs().iterator(), commandPrefix, cmdLine, indentLevel);
    }

    /**
     * <p>
     * usageDetail.
     * </p>
     *
     * @param commandPrefix
     *            a char.
     * @param arg
     *            a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @param _indentLevel
     *            a int.
     */
    public void usageDetail(
            final char commandPrefix,
            final ICmdLineArg<?> arg,
            final int _indentLevel)
    {
        nameIt(commandPrefix, arg);
        String help = ((AbstractCLA<?>) arg).getHelp();
        if (help != null)
        {
            if (help.length() > 40)
                help = help.substring(0, 40);
            allign(29);
            append(help);
            unallign();
        }
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
    }
}
