package com.obdobion.argument.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import com.obdobion.calendar.CalendarFactory;
import com.obdobion.calendar.TemporalHelper;

/**
 * <p>
 * DateCLA class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class DateCLA extends AbstractCLA<Date>
{
    SimpleDateFormat sdf;

    /** {@inheritDoc} */
    @Override
    public Date convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        if (sdf == null)
            if (getFormat() != null)
                try
                {
                    sdf = new SimpleDateFormat(getFormat());
                } catch (final Exception e)
                {
                    throw new ParseException("date format: " + e.getMessage(), 0);
                }

        try
        {
            if (sdf == null)
            {
                final LocalDateTime ldt = TemporalHelper.parseWithPredefinedParsers(valueStr);
                return new Date(CalendarFactory.asDateLong(ldt));
            }
            return sdf.parse(valueStr);
        } catch (final Exception e)
        {
            throw new ParseException(toString() + " " + getFormat() + ": " + e.getMessage(), 0);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "java.util.Date";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        synchronized (TemporalHelper.outputSDF)
        {
            out.append('"');
            out.append(TemporalHelper.outputSDF.format(getValue(occ)));
            out.append('"');
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        synchronized (TemporalHelper.outputSDF)
        {
            out.append(prefix);
            out.append("=");
            out.append(TemporalHelper.outputSDF.format(getValue(occ)));
            out.append("\n");
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        synchronized (TemporalHelper.outputSDF)
        {
            out.append(TemporalHelper.outputSDF.format(getValue(occ)));
        }
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.util.Date";
    }

    /** {@inheritDoc} */
    @Override
    public Date[] getValueAsDateArray() throws ParseException
    {
        final Date[] result = new Date[size()];

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
