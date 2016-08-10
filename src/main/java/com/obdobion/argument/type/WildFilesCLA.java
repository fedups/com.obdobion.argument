package com.obdobion.argument.type;

import java.text.ParseException;

/**
 * <p>WildFilesCLA class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class WildFilesCLA extends AbstractCLA<WildFiles>
{
    WildFiles wildFile = new WildFiles();

    /** {@inheritDoc} */
    @Override
    public WildFiles convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        wildFile.add(valueStr);
        return wildFile;
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "com.obdobion.argument.WildFiles";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ).get(0));
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
        xmlEncode(getValue(occ).get(0), out);
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "com.obdobion.argument.WildFiles";
    }

    /** {@inheritDoc} */
    @Override
    public void reset()
    {
        super.reset();
        wildFile = new WildFiles();
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsCaseSensitive()
    {
        return true;
    }
}
