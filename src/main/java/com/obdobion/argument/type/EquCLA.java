package com.obdobion.argument.type;

import java.text.ParseException;

import com.obdobion.algebrain.Equ;

/**
 * @author Chris DeGreef
 *
 */
public class EquCLA extends AbstractCLA<ComparableEqu>
{
    public EquCLA(final char _keychar)
    {
        super(_keychar);
    }

    public EquCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public EquCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType(final StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_EQU);
    }

    /**
     * @param _caseSensitive
     * @param target
     */
    @Override
    public ComparableEqu convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        try
        {
            return ComparableEqu.compile(valueStr);

        } catch (final Exception pse)
        {
            throw new ParseException(pse.getMessage(), 0);
        }
    }

    @Override
    public String defaultInstanceClass()
    {
        return "com.obdobion.algebrain.Equ";
    }

    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ).toString());
    }

    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ).toString());
        out.append("\n");
    }

    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ).toString(), out);
    }

    @Override
    public String genericClassName()
    {
        return "com.obdobion.algebrain.Equ";
    }

    @Override
    public Object getDelegateOrValue()
    {
        return getValue().delegate;
    }

    @Override
    public Object getDelegateOrValue(final int occurrence)
    {
        return getValue(occurrence).delegate;
    }

    @Override
    public Equ getValueAsEquation() throws ParseException
    {
        return getValue().delegate;
    }

    @Override
    public Equ[] getValueAsEquationArray() throws ParseException
    {
        final Equ[] result = new Equ[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).delegate;

        return result;
    }

    @Override
    public boolean supportsInList()
    {
        return false;
    }

}
