package com.obdobion.argument.usage;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.type.AbstractCLA;
import com.obdobion.argument.type.ICmdLineArg;

/**
 * <p>
 * UsageBuilderLevel2 class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.3.4
 */
public class UsageBuilderLevel2 extends UsageBuilderLevel1
{
    /**
     * <p>
     * Constructor for UsageBuilderLevel2.
     * </p>
     */
    public UsageBuilderLevel2()
    {
        super();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * usageDetail.
     * </p>
     */
    @Override
    public void usageDetail(
            final char commandPrefix,
            final ICmdLineArg<?> arg,
            final int _indentLevel)
    {
        nameIt(commandPrefix, arg);
        final String help = ((AbstractCLA<?>) arg).getHelp();
        if (help != null && help.trim().length() > 0)
        {
            allign(29);
            append(help);
            unallign();
            newLine();
        }
    }

    /**
     * @param commandPrefix
     * @param icmdLine
     * @param indentLevel
     */
    @Override
    void usageHeader(
            final char commandPrefix,
            final ICmdLine icmdLine,
            final int indentLevel)
    {
        final CmdLine commandLineParser = (CmdLine) icmdLine;

        if (commandLineParser.getName() != null)
        {
            append(commandLineParser.getName());
            newLine(indentLevel);
            newLine();
        }
    }
}
