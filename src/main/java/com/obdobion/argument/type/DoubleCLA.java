package com.obdobion.argument.type;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * <p>DoubleCLA class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class DoubleCLA extends AbstractCLA<Double>
{
    protected NumberFormat FMT = NumberFormat.getNumberInstance();

    /** {@inheritDoc} */
    @Override
    public Double convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        return FMT.parse(valueStr).doubleValue();
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "double";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        if (getValue(occ) < 0.0)
            uncompileQuoter(out, FMT.format(getValue(occ)).replaceAll(",", ""));
        else
            out.append(FMT.format(getValue(occ)).replaceAll(",", ""));
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(FMT.format(getValue(occ)).replaceAll(",", ""));
        out.append("\n");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        out.append(FMT.format(getValue(occ)).replaceAll(",", ""));
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.lang.Double";
    }

    /** {@inheritDoc} */
    @Override
    public double[] getValueAsdoubleArray() throws ParseException
    {
        final double[] result = new double[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).doubleValue();

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Double[] getValueAsDoubleArray() throws ParseException
    {
        final Double[] result = new Double[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }

}
