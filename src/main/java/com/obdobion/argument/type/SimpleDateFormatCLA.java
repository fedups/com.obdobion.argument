package com.obdobion.argument.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p>
 * SimpleDateFormatCLA class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.3.1
 */
public class SimpleDateFormatCLA extends AbstractCLA<ComparableSimpleDateFormat>
{
    /** {@inheritDoc} */
    @Override
    public ComparableSimpleDateFormat convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        try
        {
            return ComparableSimpleDateFormat.compile(valueStr);

        } catch (final IllegalArgumentException pse)
        {
            throw new ParseException(pse.getMessage(), 0);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "java.text.SimpleDateFormat";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ).pattern);
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ).pattern);
        out.append("\n");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ).pattern, out);
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.text.SimpleDateFormat";
    }

    /** {@inheritDoc} */
    @Override
    public Object getDelegateOrValue()
    {
        return getValue().delegate;
    }

    /** {@inheritDoc} */
    @Override
    public Object getDelegateOrValue(final int occurrence)
    {
        return getValue(occurrence).delegate;
    }

    /** {@inheritDoc} */
    @Override
    public SimpleDateFormat getValueAsSimpleDateFormat() throws ParseException
    {
        return getValue().delegate;
    }

    /** {@inheritDoc} */
    @Override
    public SimpleDateFormat[] getValueAsSimpleDateFormatArray() throws ParseException
    {
        final SimpleDateFormat[] result = new SimpleDateFormat[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).delegate;

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsCaseSensitive()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInList()
    {
        return false;
    }
}
