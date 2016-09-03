package com.obdobion.argument.type;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * PatternCLA class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.3.1
 */
public class DateTimeFormatterCLA extends AbstractCLA<ComparableDateTimeFormatter>
{
    /** {@inheritDoc} */
    @Override
    public ComparableDateTimeFormatter convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        try
        {
            return ComparableDateTimeFormatter.compile(valueStr);

        } catch (final IllegalArgumentException pse)
        {
            throw new ParseException(pse.getMessage(), 0);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "java.time.format.DateTimeFormatter";
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
        return "java.time.format.DateTimeFormatter";
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
    public DateTimeFormatter getValueAsDateTimeFormatter() throws ParseException
    {
        return getValue().delegate;
    }

    /** {@inheritDoc} */
    @Override
    public DateTimeFormatter[] getValueAsDateTimeFormatterArray() throws ParseException
    {
        final DateTimeFormatter[] result = new DateTimeFormatter[size()];

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
