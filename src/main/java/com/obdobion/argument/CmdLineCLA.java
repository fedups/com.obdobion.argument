package com.obdobion.argument;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author Chris DeGreef
 *
 */
public class CmdLineCLA extends AbstractCLA<ICmdLine>
{
    public ICmdLine templateCmdLine;

    public CmdLineCLA(final char _keychar)
    {
        super(_keychar);
    }

    public CmdLineCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public CmdLineCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType (final StringBuilder sb)
    {
        sb.append("begin");
    }

    @Override
    public ICmdLineArg<ICmdLine> clone ()
            throws CloneNotSupportedException
    {
        final CmdLineCLA clone = (CmdLineCLA) super.clone();
        if (templateCmdLine != null)
            clone.templateCmdLine = templateCmdLine.clone();
        return clone;
    }

    @Override
    public ICmdLine convert (
            final String valueStr,
            final boolean _caseSensitive,
            final Object target)
                    throws ParseException,
                    IOException
    {
        ICmdLine cmdline = null;
        final Object newtarget = null;
        try
        {
            cmdline = templateCmdLine.clone();
            cmdline.parse(newtarget, valueStr);

        } catch (final CloneNotSupportedException e)
        {
            e.printStackTrace();
            return null;
        }
        return cmdline;
    }

    @Override
    public String defaultInstanceClass ()
    {
        return "com.obdobion.argument.CmdLine";
    }

    @Override
    protected void exportCommandLineData (
            final StringBuilder str,
            final int occ)
    {
        str.append("[");
        getValue(occ).exportCommandLine(str);
        str.append("]");
    }

    @Override
    protected void exportNamespaceData (
            final String prefix,
            final StringBuilder out,
            final int occ)
    {
        getValue(occ).exportNamespace(prefix + ".", out);
    }

    @Override
    public void exportXml (
            final StringBuilder out)
    {
        out.append("<");
        if (isPositional())
            out.append("noname");
        else if (keychar != null)
            out.append(keychar.charValue());
        else if (keyword != null)
            out.append(keyword);
        out.append(">");
        for (int d = 0; d < size(); d++)
        {
            if (d > 0)
                if (isPositional())
                {
                    out.append("</noname>");
                    out.append("<noname>");
                } else if (keychar != null)
                {
                    out.append("</").append(keychar).append(">");
                    out.append("<").append(keychar).append(">");
                } else if (keyword != null)
                {
                    out.append("</").append(keyword).append(">");
                    out.append("<").append(keyword).append(">");
                }
            exportXmlData(out, d);
        }
        out.append("</");
        if (isPositional())
            out.append("noname");
        else if (keychar != null)
            out.append(keychar.charValue());
        else if (keyword != null)
            out.append(keyword);
        out.append(">");
    }

    @Override
    protected void exportXmlData (
            final StringBuilder str,
            final int occ)
    {
        getValue(occ).exportXml(str);
    }

    @Override
    public void reset ()
    {
        values.clear();
    }

    @Override
    public String uniqueId ()
    {
        return "subparser[" + hashCode() + "]";
    }

}
