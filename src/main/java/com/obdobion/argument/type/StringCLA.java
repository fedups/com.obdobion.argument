package com.obdobion.argument.type;

/**
 * <p>
 * StringCLA class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class StringCLA extends AbstractCLA<String>
{
    /** {@inheritDoc} */
    @Override
    public String convert(final String valueStr, final boolean _caseSensitive, final Object target)
    {
        if (_caseSensitive)
            return valueStr;
        return valueStr.toLowerCase();
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "String";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ));
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ));
        out.append("\n");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ), out);
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.lang.String";
    }

    /** {@inheritDoc} */
    @Override
    public String[] getValueAsStringArray()
    {
        final String[] result = new String[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsCaseSensitive()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInList()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsMatches()
    {
        return true;
    }

    /**
     * All default values must be in the values list and they must be in the
     * same order for them to be considered equal.
     */
    @Override
    boolean valuesAreTheSameAsDefault()
    {
        if (getDefaultValues() == null || getDefaultValues().size() == 0)
            return false;
        if (getValues() == null || getValues().size() == 0)
            return false;
        if (getDefaultValues().size() != getValues().size())
            return false;
        for (int v = 0; v < getDefaultValues().size(); v++)
        {
            if (isCaseSensitive() && !getDefaultValues().get(v).equals(getValues().get(v)))
                return false;
            if (!isCaseSensitive() && !getDefaultValues().get(v).equalsIgnoreCase(getValues().get(v)))
                return false;
        }
        return true;
    }

}
