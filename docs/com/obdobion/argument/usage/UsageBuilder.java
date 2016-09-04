package com.obdobion.argument.usage;

import com.obdobion.argument.ICmdLine;

/**
 * <p>
 * Abstract UsageBuilder class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
abstract public class UsageBuilder
{
    static final private int MaxLineLength = 80;
    /** Constant <code>newline="System.getProperty(line.separator)"</code> */
    public static String     newline       = System.getProperty("line.separator");

    /**
     * <p>
     * getWriter.
     * </p>
     *
     * @param arg a {@link com.obdobion.argument.ICmdLine} object.
     * @return a {@link java.lang.Object} object.
     * @param verboseLevel a int.
     */
    public static Object getWriter(final ICmdLine arg, final int verboseLevel)
    {
        final UsageBuilder ub;
        switch (verboseLevel)
        {
            case 0:
                ub = new UsageBuilderLevel1();
                break;
            case 1:
                ub = new UsageBuilderLevel1();
                break;
            case 2:
                ub = new UsageBuilderLevel2();
                break;
            case 3:
                ub = new UsageBuilderLevel3();
                break;
            default:
                ub = new UsageBuilderLevel1();
                break;
        }
        ub.prettyPrint(arg);
        return ub;
    }

    final private StringBuilder sb;
    int                         currentIndentLevelForWrapping;
    int                         currentLineLength;
    int                         allignment;

    UsageBuilder()
    {
        sb = new StringBuilder();
    }

    boolean allign()
    {
        boolean aSpaceWasAdded = false;
        for (int x = currentLineLength; x < allignment; x++)
        {
            append(" ");
            aSpaceWasAdded = true;
        }
        return aSpaceWasAdded;
    }

    void allign(final int column)
    {
        allignment = column;
        if (!allign())
            append("  ");
    }

    /**
     * Send values to this method with embedded new lines (\n) if that is
     * desired.
     *
     * @param value a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.usage.UsageBuilder} object.
     */
    public UsageBuilder append(
            final String value)
    {
        final String[] lines = value.split("\\n");
        String remainderOfLongLine = "";
        String aLine = null;

        for (int line = 0; remainderOfLongLine.trim().length() > 0 || line < lines.length; line++)
        {
            if (remainderOfLongLine.trim().length() > 0)
            {
                aLine = remainderOfLongLine;
                /*
                 * don't use up the next line until remainder is finished.
                 */
                line--;
            } else
            {
                aLine = lines[line];
                if (line != 0)
                    newLine();
            }

            if (currentLineLength == 0)
                indent();
            if (currentLineLength + aLine.length() >= MaxLineLength)
            {
                final String[] parts = split(aLine, MaxLineLength - currentLineLength);
                sb.append(parts[0]);
                newLine();
                remainderOfLongLine = parts[1];
                continue;
            }
            remainderOfLongLine = "";
            sb.append(aLine);
            currentLineLength += aLine.length();
        }
        return this;
    }

    int getIndentSize()
    {
        return 4;
    }

    void indent()
    {
        indent(currentIndentLevelForWrapping);
    }

    void indent(final int indentLevel)
    {
        if (allignment > 0)
        {
            for (int a = currentLineLength; a < allignment; a++)
                sb.append(" ");
            currentLineLength = allignment;
        } else
        {
            for (int i = 0; i < indentLevel; i++)
                for (int s = 0; s < getIndentSize(); s++)
                    sb.append(" ");
            currentLineLength = indentLevel * getIndentSize();
        }
    }

    UsageBuilder newLine()
    {
        return newLine(currentIndentLevelForWrapping);
    }

    UsageBuilder newLine(
            final int indentLevel)
    {
        currentIndentLevelForWrapping = indentLevel;
        currentLineLength = 0;
        sb.append(newline);
        return this;
    }

    abstract void prettyPrint(final ICmdLine arg);

    /**
     * <p>
     * setIndentLevel.
     * </p>
     *
     * @param indentLevel a int.
     */
    public void setIndentLevel(final int indentLevel)
    {
        currentIndentLevelForWrapping = indentLevel;
    }

    String[] split(final String line, final int availableBytesLeft)
    {
        int splitPoint = 0;
        for (int b = 0; b < availableBytesLeft; b++)
            /*
             * look for the last space within the printable part of the line.
             */
            if (line.charAt(b) == ' ')
                splitPoint = b;
        return new String[] {
                line.substring(0, splitPoint).trim(),
                line.substring(splitPoint).trim()
        };
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return sb.toString().trim();
    }

    void unallign()
    {
        allignment = 0;
    }
}
