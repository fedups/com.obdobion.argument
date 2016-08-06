package com.obdobion.argument.type;

import java.text.ParseException;

public class WildFilesCLA extends AbstractCLA<WildFiles>
{
    WildFiles wildFile = new WildFiles();

    /**
     * @param _caseSensitive
     * @param target
     */
    @Override
    public WildFiles convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        wildFile.add(valueStr);
        return wildFile;
    }

    @Override
    public String defaultInstanceClass()
    {
        return "com.obdobion.argument.WildFiles";
    }

    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ).get(0));
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
        xmlEncode(getValue(occ).get(0), out);
    }

    @Override
    public String genericClassName()
    {
        return "com.obdobion.argument.WildFiles";
    }

    @Override
    public void reset()
    {
        super.reset();
        wildFile = new WildFiles();
    }

    @Override
    public boolean supportsCaseSensitive()
    {
        return true;
    }
}