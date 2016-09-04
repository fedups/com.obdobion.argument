package com.obdobion.argument.type;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.obdobion.calendar.TemporalHelper;

/**
 * <p>
 * LocalDateTimeCLA class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.3.1
 */
public class LocalDateTimeCLA extends AbstractCLA<LocalDateTime>
{
    DateTimeFormatter dtf;

    /** {@inheritDoc} */
    @Override
    public LocalDateTime convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        if (dtf == null)
            if (getFormat() != null)
                try
                {
                    dtf = DateTimeFormatter.ofPattern(getFormat());
                } catch (final Exception e)
                {
                    throw new ParseException("date format: " + e.getMessage(), 0);
                }

        try
        {
            if (dtf == null)
                return TemporalHelper.parseWithPredefinedParsers(valueStr);
            return LocalDateTime.parse(valueStr, dtf);
        } catch (final Exception e)
        {
            throw new ParseException(toString() + " " + getFormat() + ": " + e.getMessage(), 0);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "java.time.LocalDateTime";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        out.append('"');
        out.append(TemporalHelper.getOutputDTF().format(getValue(occ)));
        out.append('"');
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(TemporalHelper.getOutputDTF().format(getValue(occ)));
        out.append("\n");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        out.append(TemporalHelper.getOutputDTF().format(getValue(occ)));
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.time.LocalDateTime";
    }

    /** {@inheritDoc} */
    @Override
    public LocalDateTime[] getValueAsLocalDateTimeArray() throws ParseException
    {
        final LocalDateTime[] result = new LocalDateTime[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsCaseSensitive()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsFormat()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInList()
    {
        return false;
    }
}
