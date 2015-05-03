package com.obdobion.argument;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author Chris DeGreef
 * 
 */
public class IntegerCLA extends AbstractCLA<Integer>
{
    protected NumberFormat FMT = new DecimalFormat("0");

    public IntegerCLA(final char _keychar)
    {
        super(_keychar);
    }

    public IntegerCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public IntegerCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType (StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_INTEGER);
    }

    @Override
    public float[] getValueAsfloatArray () throws ParseException
    {
        final float[] result = new float[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).floatValue();

        return result;
    }

    @Override
    public Float[] getValueAsFloatArray () throws ParseException
    {
        final Float[] result = new Float[size()];

        for (int r = 0; r < size(); r++)
            result[r] = new Float(getValue(r));

        return result;
    }

    @Override
    public int[] getValueAsintArray () throws ParseException
    {
        final int[] result = new int[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).intValue();

        return result;
    }

    @Override
    public Integer[] getValueAsIntegerArray () throws ParseException
    {
        final Integer[] result = new Integer[size()];

        for (int r = 0; r < size(); r++)
            result[r] = new Integer(getValue(r));

        return result;
    }

    @Override
    public Long[] getValueAsLongArray () throws ParseException
    {
        final Long[] result = new Long[size()];

        for (int r = 0; r < size(); r++)
            result[r] = new Long(getValue(r));

        return result;
    }

    @Override
    public Integer convert (final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        return FMT.parse(valueStr).intValue();
    }

    @Override
    public String defaultInstanceClass ()
    {
        return "int";
    }

    @Override
    protected void exportCommandLineData (final StringBuilder out, final int occ)
    {
        if (getValue(occ) < 0)
            out.append("'");
        out.append(FMT.format(getValue(occ)));
        if (getValue(occ) < 0)
            out.append("'");
    }

    @Override
    protected void exportNamespaceData (final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(FMT.format(getValue(occ)));
        out.append("\n");
    }

    @Override
    protected void exportXmlData (final StringBuilder out, final int occ)
    {
        out.append(FMT.format(getValue(occ)));
    }
}
