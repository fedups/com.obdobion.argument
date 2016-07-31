package com.obdobion.argument;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author Chris DeGreef
 *
 */
public class LongCLA extends AbstractCLA<Long>
{
    protected NumberFormat FMTin  = NumberFormat.getNumberInstance();
    protected NumberFormat FMTout = new DecimalFormat("0");

    public LongCLA(final char _keychar)
    {
        super(_keychar);
    }

    public LongCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public LongCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType(final StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_LONG);
    }

    @Override
    public Long convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        return FMTin.parse(valueStr).longValue();
    }

    @Override
    public String defaultInstanceClass()
    {
        return "long";
    }

    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        if (getValue(occ) < 0)
            out.append("'");
        out.append(FMTout.format(getValue(occ)));
        if (getValue(occ) < 0)
            out.append("'");
    }

    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(FMTout.format(getValue(occ)));
        out.append("\n");
    }

    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        out.append(FMTout.format(getValue(occ)));
    }

    public String genericClassName()
    {
        return "java.lang.Long";
    }

    @Override
    public long[] getValueAslongArray() throws ParseException
    {
        final long[] result = new long[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).longValue();

        return result;
    }

    @Override
    public Long[] getValueAsLongArray() throws ParseException
    {
        final Long[] result = new Long[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }
}
