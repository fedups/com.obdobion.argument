package com.obdobion.argument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author degreefc
 * 
 * @param <E>
 */
abstract public class AbstractCLA<E> implements ICmdLineArg<E>, Cloneable
{
    static private final Pattern     CDATA_NOTNEEDED = Pattern.compile("^[\\p{Alnum}_-]*$");

    public static String             newline         = System.getProperty("line.separator");
    protected List<E>                defaultValues   = new ArrayList<>();
    protected String                 help;
    protected Character              keychar;
    protected String                 keyword;
    protected String                 variable;
    protected String                 instanceClass;
    protected String                 format;
    protected String                 factoryMethodName;
    protected String                 factoryArgName;
    // for dates
    protected boolean                multiple;
    protected int                    multipleMin;
    protected int                    multipleMax;
    protected boolean                parsed;
    protected boolean                positional;
    protected boolean                caseSensitive;
    protected boolean                required;
    protected boolean                requiredValue;
    protected List<E>                values          = new ArrayList<>();
    protected String                 enumClassName;

    protected ICmdLineArgCriteria<?> criteria;

    public AbstractCLA(
            final char _keychar)
    {
        this.keychar = _keychar;
        this.keyword = null;
    }

    public AbstractCLA(
            final char _keychar,
            final String _keyword)
    {
        this.keychar = _keychar;
        this.keyword = _keyword;
    }

    public AbstractCLA(
            final String _keyword)
    {
        this.keychar = null;
        this.keyword = _keyword;
    }

    public void applyDefaults ()
    {
        if (getValues().isEmpty())
            getValues().addAll(getDefaultValues());
    }

    abstract public void asDefinedType (StringBuilder sb);

    public Object asEnum (String name, Object[] possibleConstants) throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Enum", 0);
    }

    @Override
    public ICmdLineArg<E> clone ()
        throws CloneNotSupportedException
    {
        @SuppressWarnings("unchecked")
        final AbstractCLA<E> clone = (AbstractCLA<E>) super.clone();

        clone.values = new ArrayList<>();
        clone.reset();
        return clone;
    }

    public E convert (
        final String valueStr)
        throws ParseException,
        IOException
    {
        if (valueStr == null)
            return null;
        return convert(valueStr, isCaseSensitive(), null);
    }

    abstract public E convert (
        String valueStr,
        boolean _caseSensitive,
        Object target)
        throws ParseException,
        IOException;

    public String defaultInstanceClass ()
    {
        return "Object";
    }

    public void exportCommandLine (
        final File file)
    {
        // only the cmdline should implement this
    }

    public void exportCommandLine (
        final StringBuilder out)
    {
        if (!isPositional())
            if (keychar != null)
            {
                out.append("-");
                out.append(keychar.charValue());
            } else if (keyword != null)
            {
                out.append("--");
                out.append(keyword);
            }
        for (int d = 0; d < size(); d++)
        {
            if (d > 0 || isPositional())
                out.append(" ");
            exportCommandLineData(out, d);
        }
    }

    abstract protected void exportCommandLineData (StringBuilder str, int occ);

    public void exportNamespace (
        final File file)
    {
        // only the cmdline should implement this
    }

    public void exportNamespace (final String prefix, final StringBuilder out)
    {
        for (int d = 0; d < size(); d++)
        {
            final StringBuilder pre = new StringBuilder();
            pre.append(prefix);
            if (isPositional())
                pre.append("");
            else if (keychar != null)
                pre.append(keychar.charValue());
            else if (keyword != null)
                pre.append(keyword);
            if (isMultiple())
                pre.append("[").append(d).append("]");
            exportNamespaceData(pre.toString(), out, d);
        }
    }

    abstract protected void exportNamespaceData (
        String prefix,
        StringBuilder str,
        int occ);

    public void exportXml (
        final String tag,
        final File file)
    {
        // only the cmdline should implement this
    }

    public void exportXml (
        final StringBuilder out)
    {
        /*
         * Never write a multiple value xml tag with a delim since we can't
         * really tell what delim will work. Instead, write each one as a tag.
         */
        for (int d = 0; d < size(); d++)
        {
            out.append("<");
            if (isPositional())
                out.append("noname");
            else if (keychar != null)
                out.append(keychar.charValue());
            else if (keyword != null)
                out.append(keyword);
            out.append(">");
            exportXmlData(out, d);
            out.append("</");
            if (isPositional())
                out.append("noname");
            else if (keychar != null)
                out.append(keychar.charValue());
            else if (keyword != null)
                out.append(keyword);
            out.append(">");
        }
    }

    abstract protected void exportXmlData (StringBuilder str, int occ);

    public ICmdLineArgCriteria<?> getCriteria ()
    {
        return criteria;
    }

    public List<E> getDefaultValues ()
    {
        return defaultValues;
    }

    public String getEnumClassName ()
    {
        return enumClassName;
    }

    public String getFactoryArgName ()
    {
        return factoryArgName;
    }

    public String getFactoryMethodName ()
    {
        return factoryMethodName;
    }

    public String getFormat ()
    {
        return format;
    }

    protected String getHelp ()
    {
        return help;
    }

    public String getInstanceClass ()
    {
        return instanceClass;
    }

    public Character getKeychar ()
    {
        if (keychar == null)
            return ' '; // invalid call
        return keychar.charValue();
    }

    public String getKeyword ()
    {

        return keyword;
    }

    public int getMultipleMax ()
    {
        return multipleMax;
    }

    public int getMultipleMin ()
    {
        return multipleMin;
    }

    /**
     * Return the last value in the list when only one is expected. And this
     * method is for when only one value is expected. It is possible for the
     * user to re-specify the same command on the input. That would build an
     * array as if it were a multi-value argument. But since it is not, we
     * return the last one specified.
     */
    public E getValue ()
    {
        return getValue(values.size() - 1);
    }

    public E getValue (
        final int index)
    {
        if (index < 0 || values == null || values.size() == 0)
            if (defaultValues != null && defaultValues.size() > index)
            {
                if (index < 0)
                    return defaultValues.get(0);
                return defaultValues.get(index);
            }
        if (index >= 0)
            if (values.size() > index)
                return values.get(index);
        return null;
    }

    public byte[] getValueAsbyteArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a byte[]", 0);
    }

    public Byte[] getValueAsByteArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Byte[]", 0);
    }

    public Date[] getValueAsDateArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Date[]", 0);
    }

    public File[] getValueAsFileArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a File[]", 0);
    }

    public float[] getValueAsfloatArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a float[]", 0);
    }

    public Float[] getValueAsFloatArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Float[]", 0);
    }

    public int[] getValueAsintArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a int[]", 0);
    }

    public Integer[] getValueAsIntegerArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Integer[]", 0);
    }

    public Long[] getValueAsLongArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Long[]", 0);
    }

    public Pattern getValueAsPattern () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Pattern", 0);
    }

    public Pattern[] getValueAsPatternArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Pattern[]", 0);
    }

    public String[] getValueAsStringArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a String[]", 0);
    }

    public List<E> getValues ()
    {
        return values;
    }

    public String getVariable ()
    {
        return variable;
    }

    public boolean hasValue ()
    {
        return !getValues().isEmpty() || !getDefaultValues().isEmpty();
    }

    public boolean isCaseSensitive ()
    {
        return caseSensitive;
    }

    public boolean isMultiple ()
    {
        return multiple;
    }

    public boolean isParsed ()
    {
        return parsed;
    }

    public boolean isPositional ()
    {
        return positional;
    }

    public boolean isRequired ()
    {
        return required;
    }

    public boolean isRequiredValue ()
    {
        return true;
    }

    public void reset ()
    {
        setParsed(false);
        getValues().clear();
    }

    public int salience (
        final Token token)
    {
        if (token.isCharCommand(keychar))
            return 1;
        if (token.isWordCommand(keyword))
            return token.getWordCommand().length();
        return 0;
    }

    public ICmdLineArg<E> setCaseSensitive (
        final boolean _caseSensitive)
    {
        this.caseSensitive = _caseSensitive;
        return this;
    }

    private ICmdLineArg<E> setCriteria (
        final ICmdLineArgCriteria<?> _criteria)
    {
        this.criteria = _criteria;
        return this;
    }

    public ICmdLineArg<E> setDefaultValue (final String defaultValue) throws ParseException, IOException
    {
        getDefaultValues().add(convert(defaultValue, caseSensitive, null));
        return this;
    }

    protected void setDefaultValues (
        final List<E> _defaultValues)
    {
        this.defaultValues = _defaultValues;
    }

    public ICmdLineArg<E> setDefaultValues (
        final String[] defaults)
        throws ParseException,
        IOException
    {
        for (final String default1 : defaults)
            getDefaultValues().add(convert(default1, caseSensitive, null));
        return this;
    }

    public ICmdLineArg<E> setEnumCriteria (
        final String _enumClassName)
        throws ParseException,
        IOException
    {
        this.enumClassName = _enumClassName;
        Class<?> enumClass;
        try
        {
            enumClass = getClass().getClassLoader().loadClass(_enumClassName);
        } catch (final ClassNotFoundException e)
        {
            throw new ParseException("Enum class not found: " + e.getMessage(),
                0);
        }

        final List<E> list = new ArrayList<>();
        if (!enumClass.isEnum())
            throw new ParseException("Enum class expected, found " + enumClass.getName(),
                0);
        final Object[] constants = enumClass.getEnumConstants();
        for (int c = 0; c < constants.length; c++)
        {
            final String econst = constants[c].toString();
            list.add(convert(econst, true, null));
        }
        setCriteria(new EnumCriteria<>(list));
        return this;
    }

    public ICmdLineArg<E> setEnumCriteriaAllowError (
        final String _enumClassName)
    {
        try
        {
            return setEnumCriteria(_enumClassName);
        } catch (Exception e)
        {
            System.err.println("warning Enum class (" + _enumClassName + ") not found");
            return null;
        }
    }

    public ICmdLineArg<E> setFactoryArgName (
        final String _factoryArgName)
    {
        this.factoryArgName = _factoryArgName;
        return this;
    }

    public ICmdLineArg<E> setFactoryMethodName (
        final String instantiatorName)
    {
        this.factoryMethodName = instantiatorName;
        return this;
    }

    public ICmdLineArg<E> setFormat (
        final String _format)
    {
        this.format = _format;
        return this;
    }

    public ICmdLineArg<E> setHelp (
        final String helpString)
    {
        help = helpString;
        return this;
    }

    public ICmdLineArg<E> setInstanceClass (
        final String _instanceClass)
    {
        this.instanceClass = _instanceClass;
        return this;
    }

    protected void setKeychar (
        final Character _keychar)
    {
        this.keychar = _keychar;
    }

    protected void setKeyword (
        final String _keyword)
    {
        this.keyword = _keyword;
    }

    public ICmdLineArg<E> setListCriteria (final String[] arrayOfValidValues)
        throws ParseException, IOException
    {
        final List<E> list = new ArrayList<>();
        for (final String arrayOfValidValue : arrayOfValidValues)
            list.add(convert(arrayOfValidValue, caseSensitive, null));
        setCriteria(new ListCriteria<>(list));
        return this;
    }

    public ICmdLineArg<E> setMultiple (
        final boolean bool)
        throws ParseException
    {
        return setMultiple(0, bool
                ? Integer.MAX_VALUE
                : 0);
    }

    public ICmdLineArg<E> setMultiple (
        final int _multipleMin)
        throws ParseException
    {
        return setMultiple(_multipleMin, Integer.MAX_VALUE);
    }

    public ICmdLineArg<E> setMultiple (
        final int _multipleMin,
        final int _multipleMax)
        throws ParseException
    {
        this.multipleMin = _multipleMin;
        this.multipleMax = _multipleMax;
        multiple = (_multipleMin > 0 || _multipleMax > 0);
        return this;
    }

    public ICmdLineArg<E> setParsed (
        final boolean bool)
    {
        parsed = bool;
        return this;
    }

    public ICmdLineArg<E> setPositional (final boolean bool)
        throws ParseException
    {
        positional = bool;
        return this;
    }

    public ICmdLineArg<E> setRangeCriteria (
        final String min,
        final String max)
        throws ParseException,
        IOException
    {
        setCriteria(new RangedCriteria<>(convert(min),
            convert(max)));
        return this;
    }

    public ICmdLineArg<E> setRegxCriteria (
        final String pattern)
        throws ParseException
    {
        setCriteria(new RegxCriteria<E>(pattern));
        return this;
    }

    public ICmdLineArg<E> setRequired (
        final boolean bool)
        throws ParseException
    {
        required = bool;
        return this;
    }

    public ICmdLineArg<E> setRequiredValue (
        final boolean bool)
        throws ParseException
    {
        if (!bool)
            throw new ParseException("requiredValue must be true for type: " + getClass().getName(),
                -1);
        requiredValue = bool;
        return this;
    }

    public void setValue (
        final E value)
    {
        setParsed(true);
        getValues().add(value);
    }

    public void setValue (final int index, final E value)
    {
        setParsed(true);
        getValues().set(index, value);
    }

    public void setValue (
        final List<E> value)
    {
        setParsed(true);
        getValues().addAll(value);
    }

    protected void setValues (
        final List<E> _values)
    {
        this.values = _values;
    }

    public ICmdLineArg<E> setVariable (
        final String _variable)
    {
        this.variable = _variable;
        return this;
    }

    public int size ()
    {
        return values.size();
    }

    @Override
    public String toString ()
    {
        if (keyword != null)
        {
            if (keychar != null)
                return "--" + keyword + "(-" + keychar.charValue() + ")";
            return "--" + keyword;
        }
        else if (keychar != null)
            return "-" + keychar.charValue();
        return "undefined";
    }

    public void update (
        final E value)
    {
        update(0, value);
    }

    public void update (
        final int index,
        final E value)
    {
        setParsed(true);
        if (index >= size())
            getValues().add(value);
        else
            getValues().set(index, value);
    }

    public void useDefaults ()
    {
        setValues(getDefaultValues());
        setParsed(false);
    }

    protected void xmlEncode (
        final String in,
        final StringBuilder builder)
    {
        if (!CDATA_NOTNEEDED.matcher(in).matches())
            builder.append("<![CDATA[").append(in).append("]]>");
        else
            builder.append(in);
    }
}
