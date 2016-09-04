package com.obdobion.argument.type;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * BooleanCLA class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class BooleanCLA extends AbstractCLA<Boolean>
{
    static final private Pattern YES        = Pattern.compile("y(es)?|true|on", Pattern.CASE_INSENSITIVE);

    String[]                     validWords = new String[] { "yes", "no" };

    /**
     * <p>
     * Constructor for BooleanCLA.
     * </p>
     */
    public BooleanCLA()
    {
        super();
        getDefaultValues().add(Boolean.FALSE);
        try
        {
            setListCriteria(validWords);
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void applyDefaults()
    {
        if (noValuesEntered() || valuesAreTheSameAsDefault())
        {
            reset();
        }
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "boolean";
    }

    /** {@inheritDoc} */
    @Override
    public void exportCommandLine(final StringBuilder out)
    {
        if (isParsed())
            if (keychar != null && keychar != ' ')
            {
                out.append("-");
                out.append(keychar.charValue());
            } else if (keyword != null && keyword.trim().length() > 0)
            {
                out.append("--");
                out.append(keyword);
            }
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        // intentionally left blank
    }

    /** {@inheritDoc} */
    @Override
    public void exportNamespace(final String prefix, final StringBuilder out)
    {
        if (isParsed())
        {
            out.append(prefix);
            if (keychar != null)
                out.append(keychar.charValue());
            else if (keyword != null)
                out.append(keyword);
            out.append("=");
            /*
             * No actual value for booleans. Just being there takes on the
             * opposite of the default value.
             */
            out.append("\n");
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        // not called for booleans
    }

    /** {@inheritDoc} */
    @Override
    public void exportXml(final StringBuilder out)
    {
        if (isParsed())
        {
            out.append("<");
            if (keychar != null)
                out.append(keychar.charValue());
            else if (keyword != null)
                out.append(keyword);
            out.append("/>");
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        // intentionally left blank
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.lang.Boolean";
    }

    /** {@inheritDoc} */
    @Override
    public Boolean getValue()
    {
        if (isParsed())
        {
            if (getDefaultValues().get(0).equals(Boolean.TRUE))
                return Boolean.FALSE;
            return Boolean.TRUE;
        }
        return getDefaultValues().get(0);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRequiredValue()
    {
        return false;
    }

    @Override
    boolean noValuesEntered()
    {
        return !isParsed();
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<Boolean> setDefaultValue(final String defaultValue) throws ParseException, IOException
    {
        getDefaultValues().clear();
        getDefaultValues().add(convert(defaultValue));
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<Boolean> setMultiple(final boolean bool) throws ParseException
    {
        throw new ParseException("setMultiple is not valid for boolean types", -1);
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<Boolean> setRequiredValue(final boolean bool) throws ParseException
    {
        requiredValue = bool;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final Boolean value)
    {
        /*
         * a reset is not necessary before setting a value on a boolean since
         * there is only one possible value at any one time. In fact, values are
         * not even stored for booleans. The simple knowledge that it is parsed
         * indicates that the boolean is the opposite of the default.
         */
        setParsed(!getDefaultValues().get(0).equals(value));
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final int index, final Boolean value)
    {
        // multiple values for a boolean are not value
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final List<Boolean> value)
    {
        // multiple values for a boolean are not value
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInList()
    {
        return true;
    }
}
