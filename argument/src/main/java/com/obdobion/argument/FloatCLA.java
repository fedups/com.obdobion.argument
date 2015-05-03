package com.obdobion.argument;

import java.text.NumberFormat;
import java.text.ParseException;

public class FloatCLA extends AbstractCLA<Float>
{
    protected NumberFormat FMTin  = NumberFormat.getNumberInstance();
    protected NumberFormat FMTout = NumberFormat.getNumberInstance();

    public FloatCLA(final char _keychar)
    {
        super(_keychar);
    }

    public FloatCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public FloatCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType (StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_FLOAT);
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
            result[r] = getValue(r);

        return result;
    }

    @Override
    public Float convert (final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        return FMTin.parse(valueStr).floatValue();
    }

    @Override
    public String defaultInstanceClass ()
    {
        return "float";
    }

    @Override
    protected void exportCommandLineData (final StringBuilder out, final int occ)
    {
        if (getValue(occ) < 0.0)
            out.append("'");
        out.append(FMTout.format(getValue(occ)).replaceAll(",", ""));
        if (getValue(occ) < 0.0)
            out.append("'");
    }

    @Override
    protected void exportNamespaceData (final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(FMTout.format(getValue(occ)).replaceAll(",", ""));
        out.append("\n");
    }

    @Override
    protected void exportXmlData (final StringBuilder out, final int occ)
    {
        out.append(FMTout.format(getValue(occ)).replaceAll(",", ""));
    }
}
