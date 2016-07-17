package com.obdobion.argument;

import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chris DeGreef
 *
 */
public class BooleanCLA extends AbstractCLA<Boolean>
{
    static final private Pattern YES        = Pattern.compile("y(es)?|true|on", Pattern.CASE_INSENSITIVE);

    String[]                     validWords = new String[] {
                                                    "yes", "no"
                                            };

    public BooleanCLA(final char _keychar)
    {
        super(_keychar);
        getDefaultValues().add(Boolean.FALSE);
        try
        {
            setListCriteria(validWords);
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public BooleanCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
        getDefaultValues().add(Boolean.FALSE);
        try
        {
            setListCriteria(validWords);
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public BooleanCLA(final String _keyword)
    {
        super(_keyword);
        getDefaultValues().add(Boolean.FALSE);
        try
        {
            setListCriteria(validWords);
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void asDefinedType(final StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_BOOLEAN);
    }

    @Override
    public Boolean convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        Matcher m = null;
        m = YES.matcher(valueStr);
        if (m.matches())
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    @Override
    public String defaultInstanceClass()
    {
        return "boolean";
    }

    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        // intentionally left blank
    }

    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append("\n");
    }

    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        // intentionally left blank
    }

    public String genericClassName()
    {
        return "java.lang.Boolean";
    }

    @Override
    public Boolean getValue()
    {
        if (isParsed())
            return super.getValue().booleanValue()
                    ? Boolean.FALSE
                    : Boolean.TRUE;
        return getDefaultValues().get(0);
    }

    @Override
    public boolean isRequiredValue()
    {
        return false;
    }

    @Override
    public ICmdLineArg<Boolean> setDefaultValue(final String defaultValue) throws ParseException, IOException
    {
        getDefaultValues().clear();
        getDefaultValues().add(convert(defaultValue));
        return this;
    }

    @Override
    public ICmdLineArg<Boolean> setMultiple(final boolean bool) throws ParseException
    {
        throw new ParseException("setMultiple is not valid for boolean types", -1);
    }

    @Override
    public ICmdLineArg<Boolean> setRequiredValue(final boolean bool) throws ParseException
    {
        requiredValue = bool;
        return this;
    }
}
