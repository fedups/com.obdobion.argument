package com.obdobion.argument.type;

/**
 * @author Chris DeGreef
 *
 */
public class StringCLA extends AbstractCLA<String>
{
    /**
     * @param target
     */
    @Override
    public String convert(final String valueStr, final boolean _caseSensitive, final Object target)
    {
        if (_caseSensitive)
            return valueStr;
        return valueStr.toLowerCase();
    }

    @Override
    public String defaultInstanceClass()
    {
        return "String";
    }

    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ));
    }

    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ));
        out.append("\n");
    }

    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ), out);
    }

    @Override
    public String genericClassName()
    {
        return "java.lang.String";
    }

    @Override
    public String[] getValueAsStringArray()
    {
        final String[] result = new String[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }

    @Override
    public boolean supportsCaseSensitive()
    {
        return true;
    }

    @Override
    public boolean supportsInList()
    {
        return true;
    }

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
