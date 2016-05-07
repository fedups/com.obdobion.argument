package com.obdobion.argument;

/**
 * @author Chris DeGreef
 * 
 */
public class StringCLA extends AbstractCLA<String>
{
    public StringCLA(final char _keychar)
    {
        super(_keychar);
    }

    public StringCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public StringCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType (StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_STRING);
    }

    @Override
    public String convert (final String valueStr, final boolean _caseSensitive, final Object target)
    {
        if (_caseSensitive)
            return valueStr;
        return valueStr.toLowerCase();
    }

    @Override
    public String defaultInstanceClass ()
    {
        return "String";
    }

    @Override
    protected void exportCommandLineData (final StringBuilder out, final int occ)
    {
        out.append('"');
        out.append(getValue(occ).replaceAll("\"", "\\\\\""));
        out.append('"');
    }

    @Override
    protected void exportNamespaceData (final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ));
        out.append("\n");
    }

    @Override
    protected void exportXmlData (final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ), out);
    }

    @Override
    public String[] getValueAsStringArray ()
    {
        final String[] result = new String[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }
}
