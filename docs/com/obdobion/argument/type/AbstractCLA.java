package com.obdobion.argument.type;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.language.Metaphone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.CmdLine;
import com.obdobion.argument.criteria.EnumCriteria;
import com.obdobion.argument.criteria.ICmdLineArgCriteria;
import com.obdobion.argument.criteria.ListCriteria;
import com.obdobion.argument.criteria.RangedCriteria;
import com.obdobion.argument.criteria.RegxCriteria;
import com.obdobion.argument.input.Token;

/**
 * <p>
 * Abstract AbstractCLA class.
 * </p>
 *
 * @author degreefc
 * @param <E>
 */
abstract public class AbstractCLA<E> implements ICmdLineArg<E>, Cloneable
{
    static Logger                logger          = LoggerFactory.getLogger("com.obdobion.argument.AbstractCLA");

    static private final Pattern CDATA_NOTNEEDED = Pattern.compile("^[\\p{Alnum}_-]*$");

    static private final Pattern CAMELCAPS       = Pattern.compile("([A-Z0-9])");

    /** Constant <code>newline="System.getProperty(line.separator)"</code> */
    public static String         newline         = System.getProperty("line.separator");

    /**
     * <p>
     * createCamelCapVersionOfKeyword.
     * </p>
     *
     * @param _keyword a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    static public String createCamelCapVersionOfKeyword(final String _keyword)
    {
        if (_keyword == null || _keyword.trim().length() == 0)
            return null;

        final StringBuilder sb = new StringBuilder();
        if (Character.isLowerCase(_keyword.charAt(0)))
            sb.append(Character.toUpperCase(_keyword.charAt(0)));
        final Matcher matcher = CAMELCAPS.matcher(_keyword);
        while (matcher.find())
            sb.append(matcher.group());
        return sb.toString();
    }

    /**
     * <p>
     * createMetaphoneVersionOfKeyword.
     * </p>
     *
     * @param _keyword a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    static public String createMetaphoneVersionOfKeyword(final String _keyword)
    {
        if (_keyword == null || _keyword.trim().length() == 0)
            return null;
        return new Metaphone().metaphone(_keyword);
    }

    /**
     * <p>
     * uncompileQuoter.
     * </p>
     *
     * @param out a {@link java.lang.StringBuilder} object.
     * @param value a {@link java.lang.String} object.
     */
    static public void uncompileQuoter(final StringBuilder out, final String value)
    {
        out.append("'");
        if (value != null)
            out.append(value.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\""));
        out.append("'");
    }

    /**
     * Set when this argument is created in the factory.
     */
    protected int                    uniqueId;
    protected ClaType                type;

    protected List<E>                defaultValues = new ArrayList<>();

    protected String                 help;

    protected Character              keychar;
    protected String                 keyword;
    protected String                 camelCaps;
    protected boolean                camelCapsAllowed;
    protected String                 metaphone;
    protected boolean                metaphoneAllowed;
    protected String                 variable;
    protected String                 instanceClass;
    protected String                 format;
    protected String                 factoryMethodName;
    protected String                 factoryArgName;
    protected boolean                multiple;
    protected int                    multipleMin;
    protected int                    multipleMax;
    protected boolean                parsed;
    protected boolean                positional;
    protected boolean                caseSensitive;
    protected boolean                required;
    protected boolean                requiredValue;
    protected boolean                systemGenerated;
    protected List<E>                values        = new ArrayList<>();
    protected String                 enumClassName;
    protected ICmdLineArgCriteria<?> criteria;

    /** {@inheritDoc} */
    @Override
    public void applyDefaults()
    {
        if (noValuesEntered() || valuesAreTheSameAsDefault())
        {
            reset();
            getValues().addAll(getDefaultValues());
        }
    }

    /** {@inheritDoc} */
    @Override
    final public void asDefinedType(final StringBuilder sb)
    {
        sb.append(getType().getTypeName());
    }

    /** {@inheritDoc} */
    @Override
    public Object asEnum(final String name, final Object[] possibleConstants) throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Enum", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Enum<?>[] asEnumArray(final String _name, final Object[] _possibleConstants) throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in an Enum[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> clone()
            throws CloneNotSupportedException
    {
        @SuppressWarnings("unchecked")
        final AbstractCLA<E> clone = (AbstractCLA<E>) super.clone();

        clone.values = new ArrayList<>();
        clone.reset();
        return clone;
    }

    /** {@inheritDoc} */
    @Override
    public E convert(final String valueStr) throws ParseException, IOException
    {
        if (valueStr == null)
            return null;
        return convert(valueStr, isCaseSensitive(), null);
    }

    /** {@inheritDoc} */
    @Override
    abstract public E convert(String valueStr, boolean _caseSensitive, Object target)
            throws ParseException, IOException;

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "Object";
    }

    /** {@inheritDoc} */
    @Override
    public void dontAllowCamelCaps()
    {
        camelCaps = null;
    }

    /**
     * <p>
     * dontAllowMetaphone.
     * </p>
     */
    public void dontAllowMetaphone()
    {
        metaphone = null;
    }

    /** {@inheritDoc} */
    @Override
    public void exportCommandLine(final File file)
    {
        // only the cmdline should implement this
    }

    /** {@inheritDoc} */
    @Override
    public void exportCommandLine(final StringBuilder out)
    {
        if (!isPositional())
            if (keychar != null && keychar != ' ')
            {
                out.append("-");
                out.append(keychar.charValue());
            } else if (keyword != null && keyword.trim().length() > 0)
            {
                out.append("--");
                out.append(keyword);
            }
        for (int d = 0; d < size(); d++)
        {
            if (d > 0 || isPositional() || keychar == null || keychar == ' ')
                out.append(" ");
            exportCommandLineData(out, d);
        }
    }

    /**
     * <p>
     * exportCommandLineData.
     * </p>
     *
     * @param str a {@link java.lang.StringBuilder} object.
     * @param occ a int.
     */
    abstract protected void exportCommandLineData(StringBuilder str, int occ);

    /** {@inheritDoc} */
    @Override
    public void exportNamespace(final File file)
    {
        // only the cmdline should implement this
    }

    /** {@inheritDoc} */
    @Override
    public void exportNamespace(final String prefix, final StringBuilder out)
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

    /**
     * <p>
     * exportNamespaceData.
     * </p>
     *
     * @param prefix a {@link java.lang.String} object.
     * @param str a {@link java.lang.StringBuilder} object.
     * @param occ a int.
     */
    abstract protected void exportNamespaceData(String prefix, StringBuilder str, int occ);

    /** {@inheritDoc} */
    @Override
    public void exportXml(final String tag, final File file)
    {
        // only the cmdline should implement this
    }

    /** {@inheritDoc} */
    @Override
    public void exportXml(final StringBuilder out)
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

    /**
     * <p>
     * exportXmlData.
     * </p>
     *
     * @param str a {@link java.lang.StringBuilder} object.
     * @param occ a int.
     */
    abstract protected void exportXmlData(StringBuilder str, int occ);

    /** {@inheritDoc} */
    @Override
    public String getCamelCaps()
    {
        return camelCaps;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArgCriteria<?> getCriteria()
    {
        return criteria;
    }

    /** {@inheritDoc} */
    @Override
    public List<E> getDefaultValues()
    {
        return defaultValues;
    }

    /** {@inheritDoc} */
    @Override
    public Object getDelegateOrValue()
    {
        return getValue();
    }

    /** {@inheritDoc} */
    @Override
    public Object getDelegateOrValue(final int occurrence)
    {
        return getValue(occurrence);
    }

    /** {@inheritDoc} */
    @Override
    public String getEnumClassName()
    {
        return enumClassName;
    }

    /** {@inheritDoc} */
    @Override
    public String getFactoryArgName()
    {
        return factoryArgName;
    }

    /** {@inheritDoc} */
    @Override
    public String getFactoryMethodName()
    {
        return factoryMethodName;
    }

    /** {@inheritDoc} */
    @Override
    public String getFormat()
    {
        return format;
    }

    /** {@inheritDoc} */
    @Override
    public String getHelp()
    {
        return help;
    }

    /** {@inheritDoc} */
    @Override
    public String getInstanceClass()
    {
        return instanceClass;
    }

    /** {@inheritDoc} */
    @Override
    public Character getKeychar()
    {
        if (keychar == null)
            return ' '; // invalid call
        return keychar.charValue();
    }

    /** {@inheritDoc} */
    @Override
    public String getKeyword()
    {
        return keyword;
    }

    /** {@inheritDoc} */
    @Override
    public String getMetaphone()
    {
        return metaphone;
    }

    /** {@inheritDoc} */
    @Override
    public int getMultipleMax()
    {
        return multipleMax;
    }

    /** {@inheritDoc} */
    @Override
    public int getMultipleMin()
    {
        return multipleMin;
    }

    /**
     * <p>
     * Getter for the field <code>type</code>.
     * </p>
     *
     * @return a {@link com.obdobion.argument.type.ClaType} object.
     */
    public ClaType getType()
    {
        return type;
    }

    /** {@inheritDoc} */
    @Override
    public int getUniqueId()
    {
        return uniqueId;
    }

    /**
     * {@inheritDoc}
     *
     * Return the last value in the list when only one is expected. And this
     * method is for when only one value is expected. It is possible for the
     * user to re-specify the same command on the input. That would build an
     * array as if it were a multi-value argument. But since it is not, we
     * return the last one specified.
     */
    @Override
    public E getValue()
    {
        return getValue(values.size() - 1);
    }

    /** {@inheritDoc} */
    @Override
    public E getValue(final int index)
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

    /** {@inheritDoc} */
    @Override
    public byte[] getValueAsbyteArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a byte[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Byte[] getValueAsByteArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Byte[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Calendar[] getValueAsCalendarArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Calendar[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Character[] getValueAsCharacterArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Character[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public char[] getValueAscharArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a char[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Date[] getValueAsDateArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Date[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public DateTimeFormatter getValueAsDateTimeFormatter() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a DateTimeFormatter", 0);
    }

    /** {@inheritDoc} */
    @Override
    public DateTimeFormatter[] getValueAsDateTimeFormatterArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a DateTimeFormatter[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public double[] getValueAsdoubleArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an double[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Double[] getValueAsDoubleArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Double[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Equ getValueAsEquation() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Equ", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Equ[] getValueAsEquationArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Equ[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public File[] getValueAsFileArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a File[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public float[] getValueAsfloatArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a float[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Float[] getValueAsFloatArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Float[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public int[] getValueAsintArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a int[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Integer[] getValueAsIntegerArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Integer[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public LocalDate[] getValueAsLocalDateArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a LocalDate[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public LocalDateTime[] getValueAsLocalDateTimeArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a LocalDateTime[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public LocalTime[] getValueAsLocalTimeArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a LocalTime[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public long[] getValueAslongArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an long[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Long[] getValueAsLongArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Long[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Pattern getValueAsPattern() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Pattern", 0);
    }

    /** {@inheritDoc} */
    @Override
    public Pattern[] getValueAsPatternArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Pattern[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public SimpleDateFormat getValueAsSimpleDateFormat() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a SimpleDateFormat", 0);
    }

    /** {@inheritDoc} */
    @Override
    public SimpleDateFormat[] getValueAsSimpleDateFormatArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a SimpleDateFormat[]", 0);
    }

    /** {@inheritDoc} */
    @Override
    public String[] getValueAsStringArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a String[]", 0);
    }

    /**
     * <p>
     * Getter for the field <code>values</code>.
     * </p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<E> getValues()
    {
        return values;
    }

    /** {@inheritDoc} */
    @Override
    public String getVariable()
    {
        return variable;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasValue()
    {
        return !getValues().isEmpty() || !getDefaultValues().isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCamelCapsAllowed()
    {
        return camelCapsAllowed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMetaphoneAllowed()
    {
        return metaphoneAllowed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMultiple()
    {
        return multiple;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isParsed()
    {
        return parsed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPositional()
    {
        return positional;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRequired()
    {
        return required;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRequiredValue()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSystemGenerated()
    {
        return systemGenerated;
    }

    boolean noValuesEntered()
    {
        return getValues().isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void reset()
    {
        setParsed(false);
        getValues().clear();
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> resetCriteria()
    {
        criteria = null;
        enumClassName = null;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public int salience(final Token token)
    {
        if (token.isCharCommand(this))
            return 1;
        if (token.isWordCommand(this))
            return token.getWordCommand().length();
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setCamelCapsAllowed(final boolean allowed)
    {
        camelCapsAllowed = allowed;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setCaseSensitive(final boolean _caseSensitive)
    {
        this.caseSensitive = _caseSensitive;
        return this;
    }

    private ICmdLineArg<E> setCriteria(final ICmdLineArgCriteria<?> _criteria) throws ParseException
    {
        if (criteria != null)
        {
            final StringBuilder sb = new StringBuilder();
            sb.append("Only one criteria is allowed for \"");
            sb.append(toString());
            sb.append("\", found \"");
            criteria.asDefinitionText(sb);
            sb.append("\" and \"");
            _criteria.asDefinitionText(sb);
            sb.append("\"");
            throw new ParseException(sb.toString(), 0);
        }
        criteria = _criteria;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setDefaultValue(final String defaultValue) throws ParseException, IOException
    {
        getDefaultValues().add(convert(defaultValue, caseSensitive, null));
        return this;
    }

    /**
     * <p>
     * Setter for the field <code>defaultValues</code>.
     * </p>
     *
     * @param _defaultValues a {@link java.util.List} object.
     */
    protected void setDefaultValues(final List<E> _defaultValues)
    {
        this.defaultValues = _defaultValues;
    }

    /**
     * <p>
     * Setter for the field <code>defaultValues</code>.
     * </p>
     *
     * @param defaults an array of {@link java.lang.String} objects.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException if any.
     * @throws java.io.IOException if any.
     */
    public ICmdLineArg<E> setDefaultValues(final String[] defaults)
            throws ParseException, IOException
    {
        for (final String default1 : defaults)
            getDefaultValues().add(convert(default1, caseSensitive, null));
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setEnumCriteria(final String _enumClassName)
            throws ParseException, IOException
    {
        this.enumClassName = _enumClassName;
        Class<?> enumClass;
        try
        {
            enumClass = CmdLine.ClassLoader.loadClass(_enumClassName);
        } catch (final ClassNotFoundException e)
        {
            throw new ParseException("Enum class not found: " + e.getMessage(), 0);
        }

        final List<E> list = new ArrayList<>();
        if (!enumClass.isEnum())
            throw new ParseException("Enum class expected, found " + enumClass.getName(), 0);
        final Object[] constants = enumClass.getEnumConstants();
        for (final Object constant : constants)
        {
            final String econst = constant.toString();
            list.add(convert(econst, true, null));
        }
        setCriteria(new EnumCriteria<>(list));
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setEnumCriteriaAllowError(final String _enumClassName)
    {
        try
        {
            return setEnumCriteria(_enumClassName);
        } catch (final Exception e)
        {
            logger.warn("Enum class ({}) not found", _enumClassName);
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setFactoryArgName(final String _factoryArgName)
    {
        this.factoryArgName = _factoryArgName;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setFactoryMethodName(final String instantiatorName) throws ParseException
    {
        this.factoryMethodName = instantiatorName;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setFormat(final String _format)
    {
        this.format = _format;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setHelp(final String helpString)
    {
        help = helpString;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setInstanceClass(final String _instanceClass) throws ParseException
    {
        this.instanceClass = _instanceClass;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setKeychar(final Character _keychar)
    {
        this.keychar = _keychar;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setKeyword(final String _keyword)
    {
        this.keyword = _keyword;
        camelCaps = createCamelCapVersionOfKeyword(_keyword);
        metaphone = createMetaphoneVersionOfKeyword(_keyword);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setListCriteria(final String[] arrayOfValidValues)
            throws ParseException, IOException
    {
        final List<E> list = new ArrayList<>();
        for (final String arrayOfValidValue : arrayOfValidValues)
            list.add(convert(arrayOfValidValue, caseSensitive, null));
        setCriteria(new ListCriteria<>(list));
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setMetaphoneAllowed(final boolean allowed)
    {
        metaphoneAllowed = allowed;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setMultiple(final boolean bool) throws ParseException
    {
        if (bool)
            return setMultiple(1, Integer.MAX_VALUE);
        return setMultiple(0, 0);
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setMultiple(final int _multipleMin) throws ParseException
    {
        return setMultiple(_multipleMin, Integer.MAX_VALUE);
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setMultiple(final int _multipleMin, final int _multipleMax) throws ParseException
    {
        this.multipleMin = _multipleMin;
        this.multipleMax = _multipleMax;
        multiple = (_multipleMin > 0 || _multipleMax > 0);
        return this;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void setObject(final Object valueAsObject)
    {
        setParsed(true);
        getValues().add((E) valueAsObject);
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setParsed(final boolean bool)
    {
        parsed = bool;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setPositional(final boolean bool)
    {
        positional = bool;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setRangeCriteria(final String min, final String max)
            throws ParseException, IOException
    {
        setCriteria(new RangedCriteria<>(convert(min), convert(max)));
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setRegxCriteria(final String pattern) throws ParseException
    {
        setCriteria(new RegxCriteria<E>(pattern));
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setRequired(final boolean bool)
    {
        required = bool;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setRequiredValue(final boolean bool) throws ParseException
    {
        if (!bool)
            throw new ParseException("requiredValue must be true for type: " + getClass().getName(), -1);
        requiredValue = bool;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setSystemGenerated(final boolean _systemGenerated)
    {
        systemGenerated = _systemGenerated;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public void setType(final ClaType claType)
    {
        type = claType;
    }

    /** {@inheritDoc} */
    @Override
    public void setUniqueId(final int uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final E value)
    {
        setParsed(true);
        getValues().add(value);
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final int index, final E value)
    {
        setParsed(true);
        getValues().set(index, value);
    }

    /**
     * <p>
     * setValue.
     * </p>
     *
     * @param value a {@link java.util.List} object.
     */
    public void setValue(final List<E> value)
    {
        setParsed(true);
        getValues().addAll(value);
    }

    /**
     * <p>
     * Setter for the field <code>values</code>.
     * </p>
     *
     * @param _values a {@link java.util.List} object.
     */
    protected void setValues(final List<E> _values)
    {
        this.values = _values;
    }

    /** {@inheritDoc} */
    @Override
    public ICmdLineArg<E> setVariable(final String _variable)
    {
        this.variable = _variable;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return values.size();
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsCamelCaps()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsCaseSensitive()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsDefaultValues()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsExcludeArgs()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsFactoryArgName()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsFactoryMethod()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsFormat()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsHelp()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInList()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInstanceClass()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsLongName()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsMatches()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsMetaphone()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsMultimax()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsMultimin()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsPositional()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsRange()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsRequired()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsShortName()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        // asDefinedType(sb);
        // sb.append(" ");
        if (keyword != null)
        {
            if (keychar != null)
                sb.append("--" + keyword + "(-" + keychar.charValue() + ")");
            else
                sb.append("--" + keyword);
        } else if (keychar != null)
            sb.append("-" + keychar.charValue());
        else
            sb.append("undefined");

        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public void useDefaults()
    {
        setValues(getDefaultValues());
        setParsed(false);
    }

    /**
     * All default values must be in the values list and they must be in the
     * same order for them to be considered equal.
     */
    boolean valuesAreTheSameAsDefault()
    {
        if (getDefaultValues() == null || getDefaultValues().size() == 0)
            return false;
        if (getValues() == null || getValues().size() == 0)
            return false;
        if (getDefaultValues().size() != getValues().size())
            return false;
        for (int v = 0; v < getDefaultValues().size(); v++)
            if (!getDefaultValues().get(v).equals(getValues().get(v)))
                return false;
        return true;
    }

    /**
     * <p>
     * xmlEncode.
     * </p>
     *
     * @param in a {@link java.lang.String} object.
     * @param builder a {@link java.lang.StringBuilder} object.
     */
    protected void xmlEncode(
            final String in,
            final StringBuilder builder)
    {
        if (!CDATA_NOTNEEDED.matcher(in).matches())
            builder.append("<![CDATA[").append(in).append("]]>");
        else
            builder.append(in);
    }
}
