package com.obdobion.argument.usage;

import com.obdobion.argument.ICmdLine;

/**
 * <p>Abstract UsageBuilder class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
abstract public class UsageBuilder
{
    static final private int MaxLineLength = 80;
    /** Constant <code>newline="System.getProperty(line.separator)"</code> */
    public static String     newline       = System.getProperty("line.separator");

    /**
     * <p>getWriter.</p>
     *
     * @param arg a {@link com.obdobion.argument.ICmdLine} object.
     * @param verbose a boolean.
     * @return a {@link java.lang.Object} object.
     */
    public static Object getWriter(
            final ICmdLine arg,
            final boolean verbose)
    {
        final UsageBuilder ub;
        if (verbose)
            ub = new UsageBuilderVerbose();
        else
            ub = new UsageBuilderCondensed();
        ub.prettyPrint(arg);
        return ub;
    }

    final private StringBuilder sb;
    int                         currentIndentLevelForWrapping;
    int                         currentLineLength;

    UsageBuilder()
    {
        sb = new StringBuilder();
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
                {
                    newLine();
                }
            }

            if (currentLineLength == (getIndentSize() * currentIndentLevelForWrapping))
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

    void indent(
            final int indentLevel)
    {
        for (int i = 0; i < indentLevel; i++)
            for (int s = 0; s < getIndentSize(); s++)
                sb.append(" ");
    }

    UsageBuilder newLine()
    {
        return newLine(currentIndentLevelForWrapping);
    }

    UsageBuilder newLine(
            final int indentLevel)
    {
        currentIndentLevelForWrapping = indentLevel;
        currentLineLength = getIndentSize() * indentLevel;
        sb.append(newline);
        return this;
    }

    abstract void prettyPrint(
            final ICmdLine arg);

    /**
     * <p>setIndentLevel.</p>
     *
     * @param indentLevel a int.
     */
    public void setIndentLevel(
            final int indentLevel)
    {
        this.currentIndentLevelForWrapping = indentLevel;
    }

    String[] split(
            final String line,
            final int availableBytesLeft)
    {
        int splitPoint = 0;
        for (int b = 0; b < availableBytesLeft; b++)
        {
            /*
             * look for the last space within the printable part of the line.
             */
            if (line.charAt(b) == ' ')
                splitPoint = b;
        }
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
}
