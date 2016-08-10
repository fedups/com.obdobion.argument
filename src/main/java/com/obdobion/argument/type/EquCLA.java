package com.obdobion.argument.type;

import java.text.ParseException;

import com.obdobion.algebrain.Equ;

/**
 * <p>EquCLA class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class EquCLA extends AbstractCLA<ComparableEqu>
{
    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "com.obdobion.algebrain.Equ";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ).toString());
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ).toString());
        out.append("\n");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ).toString(), out);
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "com.obdobion.algebrain.Equ";
    }

    /** {@inheritDoc} */
    @Override
    public Object getDelegateOrValue()
    {
        return getValue().delegate;
    }

    /** {@inheritDoc} */
    @Override
    public Object getDelegateOrValue(final int occurrence)
    {
        return getValue(occurrence).delegate;
    }

    /** {@inheritDoc} */
    @Override
    public Equ getValueAsEquation() throws ParseException
    {
        return getValue().delegate;
    }

    /** {@inheritDoc} */
    @Override
    public Equ[] getValueAsEquationArray() throws ParseException
    {
        final Equ[] result = new Equ[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).delegate;

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInList()
    {
        return false;
    }

}
