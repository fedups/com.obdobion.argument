package com.obdobion.argument;

import java.io.File;
import java.text.ParseException;

public class FileCLA extends AbstractCLA<File>
{
    public FileCLA(final char _keychar)
    {
        super(_keychar);
    }

    public FileCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public FileCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType(final StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_FILE);
    }

    @Override
    public File convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        if (_caseSensitive)
            return new File(valueStr);
        return new File(valueStr.toLowerCase());
    }

    @Override
    public String defaultInstanceClass()
    {
        return "java.io.File";
    }

    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        out.append('"');
        out.append(getValue(occ).getAbsolutePath().replaceAll("\"", "\\\\\""));
        out.append('"');
    }

    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ).getAbsolutePath());
        out.append("\n");
    }

    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ).getAbsolutePath(), out);
    }

    public String genericClassName()
    {
        return "java.io.File";
    }

    @Override
    public File[] getValueAsFileArray() throws ParseException
    {
        final File[] result = new File[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }
}
