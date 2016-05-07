package com.obdobion.argument;

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
    public void asDefinedType (StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_DOUBLE);
    }

    @Override
    public Double convert (final String valueStr, final boolean _caseSensitive, final Object target)
        throws ParseException
    {
        return FMT.parse(valueStr).doubleValue();
    }

    @Override
    public String defaultInstanceClass ()
    {
        return "double";
    }

    @Override
    protected void exportCommandLineData (final StringBuilder out, final int occ)
    {
        if (getValue(occ) < 0.0)
            out.append("'");
        out.append(FMT.format(getValue(occ)).replaceAll(",", ""));
        if (getValue(occ) < 0.0)
            out.append("'");
    }

    @Override
    protected void exportNamespaceData (final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(FMT.format(getValue(occ)).replaceAll(",", ""));
        out.append("\n");
    }

    @Override
    protected void exportXmlData (final StringBuilder out, final int occ)
    {
        out.append(FMT.format(getValue(occ)).replaceAll(",", ""));
    }
}
