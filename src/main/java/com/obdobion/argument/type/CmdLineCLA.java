package com.obdobion.argument.type;

import java.io.IOException;
import java.text.ParseException;

import com.obdobion.argument.ICmdLine;

/**
 * <p>
 * CmdLineCLA class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class CmdLineCLA extends AbstractCLA<ICmdLine>
{
    public ICmdLine templateCmdLine;

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<ICmdLine> clone()
            throws CloneNotSupportedException
    {
        final CmdLineCLA clone = (CmdLineCLA) super.clone();
        if (templateCmdLine != null)
            clone.templateCmdLine = templateCmdLine.clone();
        return clone;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLine convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException, IOException
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

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "com.obdobion.argument.CmdLine";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder str, final int occ)
    {
        str.append("[");
        getValue(occ).exportCommandLine(str);
        str.append("]");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        getValue(occ).exportNamespace(prefix + ".", out);
    }

    /** {@inheritDoc} */
    @Override
    public void exportXml(final StringBuilder out)
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

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder str, final int occ)
    {
        getValue(occ).exportXml(str);
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "com.obdobion.argument.ICmdLine";
    }

    /** {@inheritDoc} */
    @Override
    public void reset()
    {
        values.clear();
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsDefaultValues()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsExcludeArgs()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsFactoryArgName()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsFactoryMethod()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInstanceClass()
    {
        return true;
    }

}
