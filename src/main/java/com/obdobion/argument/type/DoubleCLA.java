package com.obdobion.argument.type;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author Chris DeGreef
 *
 */
public class DoubleCLA extends AbstractCLA<Double>
{
    protected NumberFormat FMT = NumberFormat.getNumberInstance();

    public DoubleCLA(final char _keychar)
    {
        super(_keychar);
    }

    public DoubleCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public DoubleCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType(final StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_DOUBLE);
    }

    /**
     * @param _caseSensitive
     * @param target
     */
    @Override
    public Double convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        return FMT.parse(valueStr).doubleValue();
    }

    @Override
    public String defaultInstanceClass()
    {
        return "double";
    }

    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        if (getValue(occ) < 0.0)
            uncompileQuoter(out, FMT.format(getValue(occ)).replaceAll(",", ""));
        else
            out.append(FMT.format(getValue(occ)).replaceAll(",", ""));
    }

    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(FMT.format(getValue(occ)).replaceAll(",", ""));
        out.append("\n");
    }

    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        out.append(FMT.format(getValue(occ)).replaceAll(",", ""));
    }

    @Override
    public String genericClassName()
    {
        return "java.lang.Double";
    }

    @Override
    public double[] getValueAsdoubleArray() throws ParseException
    {
        final double[] result = new double[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).doubleValue();

        return result;
    }

    @Override
    public Double[] getValueAsDoubleArray() throws ParseException
    {
        final Double[] result = new Double[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }

}
