package com.obdobion.argument.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Chris DeGreef
 *
 */
public class CalendarCLA extends AbstractCLA<Calendar>
{
    SimpleDateFormat sdf;

    /**
     * @param _caseSensitive
     * @param target
     */
    @Override
    public Calendar convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        if (sdf == null)
            if (getFormat() == null)
                TemporalHelper.createPredefinedDateFormats();
            else
                try
                {
                    sdf = new SimpleDateFormat(getFormat());
                } catch (final Exception e)
                {
                    throw new ParseException("date format: " + e.getMessage(), 0);
                }

        if (sdf == null)
            return parseWithPredefinedParsers(valueStr);
        try
        {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(valueStr));
            return cal;
        } catch (final Exception e)
        {
            throw new ParseException(toString() + " " + getFormat() + ": " + e.getMessage(), 0);
        }
    }

    @Override
    public String defaultInstanceClass()
    {
        return "java.util.Calendar";
    }

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

    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        synchronized (TemporalHelper.outputSDF)
        {
            out.append(TemporalHelper.outputSDF.format(getValue(occ)));
        }
    }

    @Override
    public String genericClassName()
    {
        return "java.util.Calendar";
    }

    @Override
    public Calendar[] getValueAsCalendarArray() throws ParseException
    {
        final Calendar[] result = new Calendar[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }

    protected Calendar parseSpecialDate(final String pattern)
    {
        if (TemporalHelper.specialAlgoTODAY.equalsIgnoreCase(pattern))
        {
            final Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal;
        }
        if (TemporalHelper.specialAlgoNOW.equalsIgnoreCase(pattern))
            return Calendar.getInstance();
        return Calendar.getInstance();
    }

    Calendar parseWithPredefinedParsers(final String valueStr) throws ParseException
    {
        for (int f = 0; f < TemporalHelper.predefinedMat.length; f++)
        {
            if (TemporalHelper.predefinedMat[f] == null)
                continue;
            TemporalHelper.predefinedMat[f].reset(valueStr);
            if (TemporalHelper.predefinedMat[f].matches())
            {
                if (TemporalHelper.predefinedFmt[f] == null)
                    return parseSpecialDate(TemporalHelper.predefinedAlg[f]);
                final Date date = TemporalHelper.predefinedFmt[f].parse(valueStr);
                final Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal;
            }
        }
        throw new ParseException(
                toString() + " is not in a predefined date / time format (" + valueStr + ")",
                0);
    }

    @Override
    public boolean supportsFormat()
    {
        return true;
    }

    @Override
    public boolean supportsInList()
    {
        return false;
    }
}