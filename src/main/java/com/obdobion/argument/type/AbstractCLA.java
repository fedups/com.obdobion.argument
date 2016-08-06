package com.obdobion.argument.type;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.language.Metaphone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.criteria.EnumCriteria;
import com.obdobion.argument.criteria.ICmdLineArgCriteria;
import com.obdobion.argument.criteria.ListCriteria;
import com.obdobion.argument.criteria.RangedCriteria;
import com.obdobion.argument.criteria.RegxCriteria;
import com.obdobion.argument.input.Token;

/**
 * @author degreefc
 *
 * @param <E>
 */
abstract public class AbstractCLA<E> implements ICmdLineArg<E>, Cloneable
{
    static Logger                logger          = LoggerFactory.getLogger("com.obdobion.argument.AbstractCLA");

    static private final Pattern CDATA_NOTNEEDED = Pattern.compile("^[\\p{Alnum}_-]*$");

    static private final Pattern CAMELCAPS       = Pattern.compile("([A-Z0-9])");

    public static String         newline         = System.getProperty("line.separator");

    static String createCamelCapVersionOfKeyword(final String _keyword)
    {
        if (_keyword == null || _keyword.trim().length() == 0)
            return null;

        final StringBuilder sb = new StringBuilder();
        if (Character.isLowerCase(_keyword.charAt(0)))
            sb.append(Character.toUpperCase(_keyword.charAt(0)));
        final Matcher matcher = CAMELCAPS.matcher(_keyword);
        while (matcher.find())
            sb.append(matcher.group());
        if (sb.length() < 2)
            return null;
        return sb.toString();
    }

    static String createMetaphoneVersionOfKeyword(final String _keyword)
    {
        if (_keyword == null || _keyword.trim().length() == 0)
            return null;
        return new Metaphone().metaphone(_keyword);
    }

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

    @Override
    public void applyDefaults()
    {
        if (noValuesEntered() || valuesAreTheSameAsDefault())
        {
            reset();
            getValues().addAll(getDefaultValues());
        }
    }

    @Override
    final public void asDefinedType(final StringBuilder sb)
    {
        sb.append(getType().getTypeName());
    }

    /**
     * @param name
     * @param possibleConstants
     */
    @Override
    public Object asEnum(final String name, final Object[] possibleConstants) throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Enum", 0);
    }

    /**
     * @param _name
     * @param _possibleConstants
     */
    @Override
    public Enum<?>[] asEnumArray(final String _name, final Object[] _possibleConstants) throws ParseException
    {
        // should not be called.
        throw new ParseException("invalid to store " + this.toString() + " in an Enum[]", 0);
    }

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

    @Override
    public E convert(final String valueStr) throws ParseException, IOException
    {
        if (valueStr == null)
            return null;
        return convert(valueStr, isCaseSensitive(), null);
    }

    @Override
    abstract public E convert(String valueStr, boolean _caseSensitive, Object target)
            throws ParseException, IOException;

    @Override
    public String defaultInstanceClass()
    {
        return "Object";
    }

    @Override
    public void dontAllowCamelCaps()
    {
        camelCaps = null;
    }

    public void dontAllowMetaphone()
    {
        metaphone = null;
    }

    /**
     * @param file
     */
    @Override
    public void exportCommandLine(final File file)
    {
        // only the cmdline should implement this
    }

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

    abstract protected void exportCommandLineData(StringBuilder str, int occ);

    /**
     * @param file
     */
    @Override
    public void exportNamespace(final File file)
    {
        // only the cmdline should implement this
    }

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

    abstract protected void exportNamespaceData(String prefix, StringBuilder str, int occ);

    /**
     * @param tag
     * @param file
     */
    @Override
    public void exportXml(final String tag, final File file)
    {
        // only the cmdline should implement this
    }

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

    abstract protected void exportXmlData(StringBuilder str, int occ);

    @Override
    public String getCamelCaps()
    {
        return camelCaps;
    }

    @Override
    public ICmdLineArgCriteria<?> getCriteria()
    {
        return criteria;
    }

    @Override
    public List<E> getDefaultValues()
    {
        return defaultValues;
    }

    @Override
    public Object getDelegateOrValue()
    {
        return getValue();
    }

    @Override
    public Object getDelegateOrValue(final int occurrence)
    {
        return getValue(occurrence);
    }

    @Override
    public String getEnumClassName()
    {
        return enumClassName;
    }

    @Override
    public String getFactoryArgName()
    {
        return factoryArgName;
    }

    @Override
    public String getFactoryMethodName()
    {
        return factoryMethodName;
    }

    @Override
    public String getFormat()
    {
        return format;
    }

    @Override
    public String getHelp()
    {
        return help;
    }

    @Override
    public String getInstanceClass()
    {
        return instanceClass;
    }

    @Override
    public Character getKeychar()
    {
        if (keychar == null)
            return ' '; // invalid call
        return keychar.charValue();
    }

    @Override
    public String getKeyword()
    {
        return keyword;
    }

    @Override
    public String getMetaphone()
    {
        return metaphone;
    }

    @Override
    public int getMultipleMax()
    {
        return multipleMax;
    }

    @Override
    public int getMultipleMin()
    {
        return multipleMin;
    }

    public ClaType getType()
    {
        return type;
    }

    @Override
    public int getUniqueId()
    {
        return uniqueId;
    }

    /**
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

    @Override
    public byte[] getValueAsbyteArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a byte[]", 0);
    }

    @Override
    public Byte[] getValueAsByteArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Byte[]", 0);
    }

    @Override
    public Character[] getValueAsCharacterArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Character[]", 0);
    }

    @Override
    public char[] getValueAscharArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a char[]", 0);
    }

    @Override
    public Date[] getValueAsDateArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Date[]", 0);
    }

    @Override
    public double[] getValueAsdoubleArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an double[]", 0);
    }

    @Override
    public Double[] getValueAsDoubleArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Double[]", 0);
    }

    @Override
    public Equ getValueAsEquation() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Equ", 0);
    }

    @Override
    public Equ[] getValueAsEquationArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Equ[]", 0);
    }

    @Override
    public File[] getValueAsFileArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a File[]", 0);
    }

    @Override
    public float[] getValueAsfloatArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a float[]", 0);
    }

    @Override
    public Float[] getValueAsFloatArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Float[]", 0);
    }

    @Override
    public int[] getValueAsintArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a int[]", 0);
    }

    @Override
    public Integer[] getValueAsIntegerArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Integer[]", 0);
    }

    @Override
    public long[] getValueAslongArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an long[]", 0);
    }

    @Override
    public Long[] getValueAsLongArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Long[]", 0);
    }

    @Override
    public Pattern getValueAsPattern() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Pattern", 0);
    }

    @Override
    public Pattern[] getValueAsPatternArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Pattern[]", 0);
    }

    @Override
    public String[] getValueAsStringArray() throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a String[]", 0);
    }

    public List<E> getValues()
    {
        return values;
    }

    @Override
    public String getVariable()
    {
        return variable;
    }

    @Override
    public boolean hasValue()
    {
        return !getValues().isEmpty() || !getDefaultValues().isEmpty();
    }

    @Override
    public boolean isCamelCapsAllowed()
    {
        return camelCapsAllowed;
    }

    @Override
    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    @Override
    public boolean isMetaphoneAllowed()
    {
        return metaphoneAllowed;
    }

    @Override
    public boolean isMultiple()
    {
        return multiple;
    }

    @Override
    public boolean isParsed()
    {
        return parsed;
    }

    @Override
    public boolean isPositional()
    {
        return positional;
    }

    @Override
    public boolean isRequired()
    {
        return required;
    }

    @Override
    public boolean isRequiredValue()
    {
        return true;
    }

    @Override
    public boolean isSystemGenerated()
    {
        return systemGenerated;
    }

    boolean noValuesEntered()
    {
        return getValues().isEmpty();
    }

    @Override
    public void reset()
    {
        setParsed(false);
        getValues().clear();
    }

    @Override
    public ICmdLineArg<E> resetCriteria()
    {
        criteria = null;
        enumClassName = null;
        return this;
    }

    @Override
    public int salience(final Token token)
    {
        if (token.isCharCommand(this))
            return 1;
        if (token.isWordCommand(this))
            return token.getWordCommand().length();
        return 0;
    }

    @Override
    public ICmdLineArg<E> setCamelCapsAllowed(final boolean allowed)
    {
        camelCapsAllowed = allowed;
        return this;
    }

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

    @Override
    public ICmdLineArg<E> setDefaultValue(final String defaultValue) throws ParseException, IOException
    {
        getDefaultValues().add(convert(defaultValue, caseSensitive, null));
        return this;
    }

    protected void setDefaultValues(final List<E> _defaultValues)
    {
        this.defaultValues = _defaultValues;
    }

    public ICmdLineArg<E> setDefaultValues(final String[] defaults)
            throws ParseException, IOException
    {
        for (final String default1 : defaults)
            getDefaultValues().add(convert(default1, caseSensitive, null));
        return this;
    }

    @Override
    public ICmdLineArg<E> setEnumCriteria(final String _enumClassName)
            throws ParseException, IOException
    {
        this.enumClassName = _enumClassName;
        Class<?> enumClass;
        try
        {
            enumClass = getClass().getClassLoader().loadClass(_enumClassName);
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

    @Override
    public ICmdLineArg<E> setFactoryArgName(final String _factoryArgName)
    {
        this.factoryArgName = _factoryArgName;
        return this;
    }

    @Override
    public ICmdLineArg<E> setFactoryMethodName(final String instantiatorName) throws ParseException
    {
        this.factoryMethodName = instantiatorName;
        return this;
    }

    @Override
    public ICmdLineArg<E> setFormat(final String _format)
    {
        this.format = _format;
        return this;
    }

    @Override
    public ICmdLineArg<E> setHelp(final String helpString)
    {
        help = helpString;
        return this;
    }

    @Override
    public ICmdLineArg<E> setInstanceClass(final String _instanceClass) throws ParseException
    {
        this.instanceClass = _instanceClass;
        return this;
    }

    @Override
    public ICmdLineArg<E> setKeychar(final Character _keychar)
    {
        this.keychar = _keychar;
        return this;
    }

    @Override
    public ICmdLineArg<E> setKeyword(final String _keyword)
    {
        this.keyword = _keyword;
        camelCaps = createCamelCapVersionOfKeyword(_keyword);
        metaphone = createMetaphoneVersionOfKeyword(_keyword);
        return this;
    }

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

    @Override
    public ICmdLineArg<E> setMetaphoneAllowed(final boolean allowed)
    {
        metaphoneAllowed = allowed;
        return this;
    }

    @Override
    public ICmdLineArg<E> setMultiple(final boolean bool) throws ParseException
    {
        if (bool)
            return setMultiple(1, Integer.MAX_VALUE);
        return setMultiple(0, 0);
    }

    @Override
    public ICmdLineArg<E> setMultiple(final int _multipleMin) throws ParseException
    {
        return setMultiple(_multipleMin, Integer.MAX_VALUE);
    }

    @Override
    public ICmdLineArg<E> setMultiple(final int _multipleMin, final int _multipleMax) throws ParseException
    {
        this.multipleMin = _multipleMin;
        this.multipleMax = _multipleMax;
        multiple = (_multipleMin > 0 || _multipleMax > 0);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setObject(final Object valueAsObject)
    {
        setParsed(true);
        getValues().add((E) valueAsObject);
    }

    @Override
    public ICmdLineArg<E> setParsed(final boolean bool)
    {
        parsed = bool;
        return this;
    }

    @Override
    public ICmdLineArg<E> setPositional(final boolean bool)
    {
        positional = bool;
        return this;
    }

    @Override
    public ICmdLineArg<E> setRangeCriteria(final String min, final String max)
            throws ParseException, IOException
    {
        setCriteria(new RangedCriteria<>(convert(min), convert(max)));
        return this;
    }

    @Override
    public ICmdLineArg<E> setRegxCriteria(final String pattern) throws ParseException
    {
        setCriteria(new RegxCriteria<E>(pattern));
        return this;
    }

    @Override
    public ICmdLineArg<E> setRequired(final boolean bool)
    {
        required = bool;
        return this;
    }

    @Override
    public ICmdLineArg<E> setRequiredValue(final boolean bool) throws ParseException
    {
        if (!bool)
            throw new ParseException("requiredValue must be true for type: " + getClass().getName(), -1);
        requiredValue = bool;
        return this;
    }

    @Override
    public ICmdLineArg<E> setSystemGenerated(final boolean _systemGenerated)
    {
        systemGenerated = _systemGenerated;
        return this;
    }

    public void setType(final ClaType claType)
    {
        type = claType;
    }

    @Override
    public void setUniqueId(final int uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    @Override
    public void setValue(final E value)
    {
        setParsed(true);
        getValues().add(value);
    }

    @Override
    public void setValue(final int index, final E value)
    {
        setParsed(true);
        getValues().set(index, value);
    }

    public void setValue(final List<E> value)
    {
        setParsed(true);
        getValues().addAll(value);
    }

    protected void setValues(final List<E> _values)
    {
        this.values = _values;
    }

    @Override
    public ICmdLineArg<E> setVariable(final String _variable)
    {
        this.variable = _variable;
        return this;
    }

    @Override
    public int size()
    {
        return values.size();
    }

    @Override
    public boolean supportsCamelCaps()
    {
        return true;
    }

    @Override
    public boolean supportsCaseSensitive()
    {
        return false;
    }

    @Override
    public boolean supportsDefaultValues()
    {
        return true;
    }

    @Override
    public boolean supportsExcludeArgs()
    {
        return false;
    }

    @Override
    public boolean supportsFactoryArgName()
    {
        return false;
    }

    @Override
    public boolean supportsFactoryMethod()
    {
        return false;
    }

    @Override
    public boolean supportsFormat()
    {
        return false;
    }

    @Override
    public boolean supportsHelp()
    {
        return true;
    }

    @Override
    public boolean supportsInList()
    {
        return true;
    }

    @Override
    public boolean supportsInstanceClass()
    {
        return false;
    }

    @Override
    public boolean supportsLongName()
    {
        return true;
    }

    @Override
    public boolean supportsMatches()
    {
        return false;
    }

    @Override
    public boolean supportsMetaphone()
    {
        return true;
    }

    @Override
    public boolean supportsMultimax()
    {
        return true;
    }

    @Override
    public boolean supportsMultimin()
    {
        return true;
    }

    @Override
    public boolean supportsPositional()
    {
        return true;
    }

    @Override
    public boolean supportsRange()
    {
        return false;
    }

    @Override
    public boolean supportsRequired()
    {
        return false;
    }

    @Override
    public boolean supportsShortName()
    {
        return true;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        asDefinedType(sb);
        sb.append(" ");
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
