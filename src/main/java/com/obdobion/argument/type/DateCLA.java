package com.obdobion.argument.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>DateCLA class.</p>
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

    /**
     * <p>parseSpecialDate.</p>
     *
     * @param pattern a {@link java.lang.String} object.
     * @return a {@link java.util.Date} object.
     */
    protected Date parseSpecialDate(final String pattern)
    {
        if (TemporalHelper.specialAlgoTODAY.equalsIgnoreCase(pattern))
        {
            final Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        if (TemporalHelper.specialAlgoNOW.equalsIgnoreCase(pattern))
            return new Date();
        return new Date();
    }

    Date parseWithPredefinedParsers(final String valueStr) throws ParseException
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
                return TemporalHelper.predefinedFmt[f].parse(valueStr);
            }
        }
        throw new ParseException(
                toString() + " is not in a predefined date / time format (" + valueStr + ")",
                0);
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
