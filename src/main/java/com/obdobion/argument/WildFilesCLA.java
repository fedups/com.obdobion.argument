package com.obdobion.argument;

import java.text.ParseException;

public class WildFilesCLA extends AbstractCLA<WildFiles>
{
    WildFiles wildFile = new WildFiles();

    public WildFilesCLA(final char _keychar)
    {
        super(_keychar);
    }

    public WildFilesCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public WildFilesCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType (final StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_WILDFILE);
    }

    @Override
    public WildFiles convert (final String valueStr, final boolean _caseSensitive, final Object target)
        throws ParseException
    {
        wildFile.add(valueStr);
        return wildFile;
    }

    @Override
    public String defaultInstanceClass ()
    {
        return "com.obdobion.argument.WildFiles";
    }

    @Override
    protected void exportCommandLineData (final StringBuilder out, final int occ)
    {
        out.append('"');
        out.append(getValue(occ).get(0).replaceAll("\"", "\\\\\""));
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
        xmlEncode(getValue(occ).get(0), out);
    }

    @Override
    public void reset ()
    {
        super.reset();
        wildFile = new WildFiles();
    }
}