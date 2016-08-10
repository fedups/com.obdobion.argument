package com.obdobion.argument.type;

import java.io.File;
import java.text.ParseException;

/**
 * <p>FileCLA class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class FileCLA extends AbstractCLA<File>
{
    /** {@inheritDoc} */
    @Override
    public File convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        if (_caseSensitive)
            return new File(valueStr);
        return new File(valueStr.toLowerCase());
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "java.io.File";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ).getAbsolutePath());
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ).getAbsolutePath());
        out.append("\n");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ).getAbsolutePath(), out);
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.io.File";
    }

    /** {@inheritDoc} */
    @Override
    public File[] getValueAsFileArray() throws ParseException
    {
        final File[] result = new File[size()];

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
        return false;
    }

}
