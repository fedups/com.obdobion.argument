package com.obdobion.argument.type;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * <p>LongCLA class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class LongCLA extends AbstractCLA<Long>
{
    protected NumberFormat FMTin  = NumberFormat.getNumberInstance();
    protected NumberFormat FMTout = new DecimalFormat("0");

    /** {@inheritDoc} */
    @Override
    public Long convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        return FMTin.parse(valueStr).longValue();
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "long";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        if (getValue(occ) < 0)
            out.append("'");
        out.append(FMTout.format(getValue(occ)));
        if (getValue(occ) < 0)
            out.append("'");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(FMTout.format(getValue(occ)));
        out.append("\n");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        out.append(FMTout.format(getValue(occ)));
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.lang.Long";
    }

    /** {@inheritDoc} */
    @Override
    public long[] getValueAslongArray() throws ParseException
    {
        final long[] result = new long[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).longValue();

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Long[] getValueAsLongArray() throws ParseException
    {
        final Long[] result = new Long[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }
}
