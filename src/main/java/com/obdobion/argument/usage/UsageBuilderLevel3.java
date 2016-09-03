package com.obdobion.argument.usage;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.type.AbstractCLA;
import com.obdobion.argument.type.ByteCLA;
import com.obdobion.argument.type.EnumCLA;
import com.obdobion.argument.type.ICmdLineArg;
import com.obdobion.calendar.TemporalHelper;

/**
 * <p>
 * UsageBuilderLevel2 class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.3.4
 */
public class UsageBuilderLevel3 extends UsageBuilderLevel2
{
    /**
     * <p>
     * Constructor for UsageBuilderLevel2.
     * </p>
     */
    public UsageBuilderLevel3()
    {
        super();
    }

    private void showEnumCriteria(final String instanceClass)
    {
        if (instanceClass != null)
            try
            {
                append("Allowable values: ");
                final Object[] constants = CmdLine.ClassLoader.loadClass(instanceClass).getEnumConstants();
                for (int o = 0; o < constants.length; o++)
                {
                    if (o > 0)
                        append(", ");
                    append(constants[o].toString());
                }
            } catch (final ClassNotFoundException e)
            {
                append("ENUM CLASS NOT FOUND: " + instanceClass);
            }
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

        allign(29);
        final String help = ((AbstractCLA<?>) arg).getHelp();
        if (help != null && help.trim().length() > 0)
            append(help);

        append(" ");

        usageDetailModifiers(commandPrefix, arg, _indentLevel + 1);

        if (((AbstractCLA<?>) arg).getFormat() != null)
            append("The value must adhere to \"").append(((AbstractCLA<?>) arg).getFormat() + "\". ");

        unallign();
        newLine();
    }

    /**
     * @param commandPrefix
     * @param arg
     * @param indentLevel
     */
    void usageDetailModifiers(
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
            if (arg.getMultipleMax() == Integer.MAX_VALUE)
                if (arg.isRequired())
                    append("Although multiple values are allowed, you must specify at least "
                            + arg.getMultipleMin()
                            + ". ");
                else
                    append("Although no value is required, if you specify one you must specify at least "
                            + arg.getMultipleMin()
                            + ". ");
            else if (arg.isRequired())
                append("It is required that you specify at least "
                        + arg.getMultipleMin()
                        + " and at most "
                        + arg.getMultipleMax()
                        + " values. ");
            else
                append("Although no value is required, if you specify one you must specify at least "
                        + arg.getMultipleMin()
                        + " and at most "
                        + arg.getMultipleMax()
                        + " values. ");

        if (arg.isCaseSensitive())
            append("Upper vs lower case matters. ");

        if (arg.getCriteria() != null)
        {
            arg.getCriteria().usage(this, indentLevel);
            append(". ");
        }

        if (arg instanceof EnumCLA)
        {
            showEnumCriteria(arg.getInstanceClass());
            append(". ");
        }

        if (arg instanceof AbstractCLA)
        {
            final AbstractCLA<?> acla = (AbstractCLA<?>) arg;
            if (acla.getDefaultValues() != null && !acla.getDefaultValues().isEmpty())
            {
                append("If unspecified, the default will be");
                for (final Object dv : acla.getDefaultValues())
                {
                    append(" ");
                    if (dv instanceof Byte)
                        append(ByteCLA.asLiteral(((Byte) dv).byteValue()));
                    else if (dv instanceof Calendar)
                        append(TemporalHelper.getOutputSDF().format(((Calendar) dv).getTime()));
                    else if (dv instanceof Date)
                        append(TemporalHelper.getOutputSDF().format((Date) dv));
                    else
                        append(dv.toString());
                }
                append(". ");
            }
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
            append(". ");
        }

        if (indentLevel == 0 && commandLineParser.getHelp() != null)
        {
            append(commandLineParser.getHelp());
            newLine(indentLevel);
        }
        newLine(indentLevel);
        if (indentLevel == 0 && !commandLineParser.getDefaultIncludeDirectories().isEmpty())
        {
            newLine(indentLevel);
            append("Import path =");
            for (final File dir : commandLineParser.getDefaultIncludeDirectories())
            {
                append(" ");
                append(dir.getAbsolutePath());
                append(";");
            }
            newLine(indentLevel);
        }
    }
}
