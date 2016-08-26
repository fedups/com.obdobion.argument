package com.obdobion.argument.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.obdobion.calendar.TemporalHelper;

/**
 * <p>
 * CalendarCLA class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class CalendarCLA extends AbstractCLA<Calendar>
{
    SimpleDateFormat sdf;

    /** {@inheritDoc} */
    @Override
    public Calendar convert(final String valueStr, final boolean _caseSensitive, final Object target)
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
                final Calendar cal = Calendar.getInstance();
                cal.setTime(TemporalHelper.parseWithPredefinedParsers(valueStr));
                return cal;
            }
            final Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(valueStr));
            return cal;
        } catch (final Exception e)
        {
            throw new ParseException(toString() + " " + getFormat() + ": " + e.getMessage(), 0);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "java.util.Calendar";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        synchronized (TemporalHelper.outputSDF)
        {
            out.append('"');
            out.append(TemporalHelper.outputSDF.format(getValue(occ).getTime()));
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
            out.append(TemporalHelper.outputSDF.format(getValue(occ).getTime()));
            out.append("\n");
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        synchronized (TemporalHelper.outputSDF)
        {
            out.append(TemporalHelper.outputSDF.format(getValue(occ).getTime()));
        }
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.util.Calendar";
    }

    /** {@inheritDoc} */
    @Override
    public Calendar[] getValueAsCalendarArray() throws ParseException
    {
        final Calendar[] result = new Calendar[size()];

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
