package com.obdobion.argument;

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

    static String createCamelCapVersionOfKeyword (final String _keyword)
    {
        if (_keyword == null || _keyword.trim().length() == 0)
            return null;

        final StringBuilder sb = new StringBuilder();
        if (Character.isLowerCase(_keyword.charAt(0)))
            sb.append(Character.toUpperCase(_keyword.charAt(0)));
        final Matcher matcher = CAMELCAPS.matcher(_keyword);
        while (matcher.find())
        {
            sb.append(matcher.group());
        }
        if (sb.length() < 2)
            return null;
        return sb.toString();
    }

    static String createMetaphoneVersionOfKeyword (final String _keyword)
    {
        if (_keyword == null || _keyword.trim().length() == 0)
            return null;
        return new Metaphone().metaphone(_keyword);
    }

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
    // for dates
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

    public AbstractCLA(final char _keychar)
    {
        this.keychar = _keychar;
        this.keyword = null;
        camelCaps = null;
    }

    public AbstractCLA(final char _keychar, final String _keyword)
    {
        setKeychar(_keychar);
        setKeyword(_keyword);
    }

    public AbstractCLA(final String _keyword)
    {
        this.keyword = _keyword;
        setKeychar(null);
        setKeyword(_keyword);
    }

    @Override
    public void applyDefaults ()
    {
        if (getValues().isEmpty())
            getValues().addAll(getDefaultValues());
    }

    @Override
    abstract public void asDefinedType (StringBuilder sb);

    @Override
    public Object asEnum (final String name, final Object[] possibleConstants) throws ParseException
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

    @Override
    public E convert (final String valueStr) throws ParseException, IOException
    {
        if (valueStr == null)
            return null;
        return convert(valueStr, isCaseSensitive(), null);
    }

    @Override
    abstract public E convert (String valueStr, boolean _caseSensitive, Object target)
        throws ParseException, IOException;

    @Override
    public String defaultInstanceClass ()
    {
        return "Object";
    }

    @Override
    public void dontAllowCamelCaps ()
    {
        camelCaps = null;
    }

    public void dontAllowMetaphone ()
    {
        metaphone = null;
    }

    @Override
    public void exportCommandLine (
        final File file)
    {
        // only the cmdline should implement this
    }

    @Override
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

    @Override
    public void exportNamespace (
        final File file)
    {
        // only the cmdline should implement this
    }

    @Override
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

    @Override
    public void exportXml (
        final String tag,
        final File file)
    {
        // only the cmdline should implement this
    }

    @Override
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

    @Override
    public String getCamelCaps ()
    {
        return camelCaps;
    }

    @Override
    public ICmdLineArgCriteria<?> getCriteria ()
    {
        return criteria;
    }

    @Override
    public List<E> getDefaultValues ()
    {
        return defaultValues;
    }

    public Object getDelegateOrValue ()
    {
        return getValue();
    }

    public Object getDelegateOrValue (final int occurrence)
    {
        return getValue(occurrence);
    }

    @Override
    public String getEnumClassName ()
    {
        return enumClassName;
    }

    @Override
    public String getFactoryArgName ()
    {
        return factoryArgName;
    }

    @Override
    public String getFactoryMethodName ()
    {
        return factoryMethodName;
    }

    @Override
    public String getFormat ()
    {
        return format;
    }

    @Override
    public String getHelp ()
    {
        return help;
    }

    @Override
    public String getInstanceClass ()
    {
        return instanceClass;
    }

    @Override
    public Character getKeychar ()
    {
        if (keychar == null)
            return ' '; // invalid call
        return keychar.charValue();
    }

    @Override
    public String getKeyword ()
    {
        return keyword;
    }

    @Override
    public String getMetaphone ()
    {
        return metaphone;
    }

    @Override
    public int getMultipleMax ()
    {
        return multipleMax;
    }

    @Override
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
    @Override
    public E getValue ()
    {
        return getValue(values.size() - 1);
    }

    @Override
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

    @Override
    public byte[] getValueAsbyteArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a byte[]", 0);
    }

    @Override
    public Byte[] getValueAsByteArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Byte[]", 0);
    }

    @Override
    public Date[] getValueAsDateArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Date[]", 0);
    }

    @Override
    public Equ getValueAsEquation () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Equ", 0);
    }

    @Override
    public Equ[] getValueAsEquationArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in an Equ[]", 0);
    }

    @Override
    public File[] getValueAsFileArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a File[]", 0);
    }

    @Override
    public float[] getValueAsfloatArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a float[]", 0);
    }

    @Override
    public Float[] getValueAsFloatArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Float[]", 0);
    }

    @Override
    public int[] getValueAsintArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a int[]", 0);
    }

    @Override
    public Integer[] getValueAsIntegerArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Integer[]", 0);
    }

    @Override
    public Long[] getValueAsLongArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Long[]", 0);
    }

    @Override
    public Pattern getValueAsPattern () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Pattern", 0);
    }

    @Override
    public Pattern[] getValueAsPatternArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a Pattern[]", 0);
    }

    @Override
    public String[] getValueAsStringArray () throws ParseException
    {
        throw new ParseException("invalid to store " + this.toString() + " in a String[]", 0);
    }

    public List<E> getValues ()
    {
        return values;
    }

    @Override
    public String getVariable ()
    {
        return variable;
    }

    @Override
    public boolean hasValue ()
    {
        return !getValues().isEmpty() || !getDefaultValues().isEmpty();
    }

    @Override
    public boolean isCamelCapsAllowed ()
    {
        return camelCapsAllowed;
    }

    @Override
    public boolean isCaseSensitive ()
    {
        return caseSensitive;
    }

    @Override
    public boolean isMetaphoneAllowed ()
    {
        return metaphoneAllowed;
    }

    @Override
    public boolean isMultiple ()
    {
        return multiple;
    }

    @Override
    public boolean isParsed ()
    {
        return parsed;
    }

    @Override
    public boolean isPositional ()
    {
        return positional;
    }

    @Override
    public boolean isRequired ()
    {
        return required;
    }

    @Override
    public boolean isRequiredValue ()
    {
        return true;
    }

    @Override
    public boolean isSystemGenerated ()
    {
        return systemGenerated;
    }

    @Override
    public void reset ()
    {
        setParsed(false);
        getValues().clear();
    }

    @Override
    public ICmdLineArg<E> resetCriteria ()
    {
        criteria = null;
        enumClassName = null;
        return this;
    }

    @Override
    public int salience (
        final Token token)
    {
        if (token.isCharCommand(this))
            return 1;
        if (token.isWordCommand(this))
            return token.getWordCommand().length();
        return 0;
    }

    @Override
    public ICmdLineArg<E> setCamelCapsAllowed (final boolean allowed)
    {
        camelCapsAllowed = allowed;
        return this;
    }

    @Override
    public ICmdLineArg<E> setCaseSensitive (final boolean _caseSensitive)
    {
        this.caseSensitive = _caseSensitive;
        return this;
    }

    private ICmdLineArg<E> setCriteria (final ICmdLineArgCriteria<?> _criteria) throws ParseException
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
    public ICmdLineArg<E> setDefaultValue (final String defaultValue) throws ParseException, IOException
    {
        getDefaultValues().add(convert(defaultValue, caseSensitive, null));
        return this;
    }

    protected void setDefaultValues (final List<E> _defaultValues)
    {
        this.defaultValues = _defaultValues;
    }

    public ICmdLineArg<E> setDefaultValues (final String[] defaults)
        throws ParseException, IOException
    {
        for (final String default1 : defaults)
            getDefaultValues().add(convert(default1, caseSensitive, null));
        return this;
    }

    @Override
    public ICmdLineArg<E> setEnumCriteria (final String _enumClassName)
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
    public ICmdLineArg<E> setEnumCriteriaAllowError (final String _enumClassName)
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
    public ICmdLineArg<E> setFactoryArgName (final String _factoryArgName)
    {
        this.factoryArgName = _factoryArgName;
        return this;
    }

    @Override
    public ICmdLineArg<E> setFactoryMethodName (final String instantiatorName) throws ParseException
    {
        this.factoryMethodName = instantiatorName;
        return this;
    }

    @Override
    public ICmdLineArg<E> setFormat (final String _format)
    {
        this.format = _format;
        return this;
    }

    @Override
    public ICmdLineArg<E> setHelp (final String helpString)
    {
        help = helpString;
        return this;
    }

    @Override
    public ICmdLineArg<E> setInstanceClass (final String _instanceClass) throws ParseException
    {
        this.instanceClass = _instanceClass;
        return this;
    }

    @Override
    public ICmdLineArg<E> setKeychar (final Character _keychar)
    {
        this.keychar = _keychar;
        return this;
    }

    @Override
    public ICmdLineArg<E> setKeyword (final String _keyword)
    {
        this.keyword = _keyword;
        camelCaps = createCamelCapVersionOfKeyword(_keyword);
        metaphone = createMetaphoneVersionOfKeyword(_keyword);
        return this;
    }

    @Override
    public ICmdLineArg<E> setListCriteria (final String[] arrayOfValidValues)
        throws ParseException, IOException
    {
        final List<E> list = new ArrayList<>();
        for (final String arrayOfValidValue : arrayOfValidValues)
            list.add(convert(arrayOfValidValue, caseSensitive, null));
        setCriteria(new ListCriteria<>(list));
        return this;
    }

    @Override
    public ICmdLineArg<E> setMetaphoneAllowed (final boolean allowed)
    {
        metaphoneAllowed = allowed;
        return this;
    }

    @Override
    public ICmdLineArg<E> setMultiple (final boolean bool) throws ParseException
    {
        return setMultiple(0, bool
                ? Integer.MAX_VALUE
                : 0);
    }

    @Override
    public ICmdLineArg<E> setMultiple (final int _multipleMin) throws ParseException
    {
        return setMultiple(_multipleMin, Integer.MAX_VALUE);
    }

    @Override
    public ICmdLineArg<E> setMultiple (final int _multipleMin, final int _multipleMax) throws ParseException
    {
        this.multipleMin = _multipleMin;
        this.multipleMax = _multipleMax;
        multiple = (_multipleMin > 0 || _multipleMax > 0);
        return this;
    }

    @Override
    public ICmdLineArg<E> setParsed (final boolean bool)
    {
        parsed = bool;
        return this;
    }

    @Override
    public ICmdLineArg<E> setPositional (final boolean bool)
    {
        positional = bool;
        return this;
    }

    @Override
    public ICmdLineArg<E> setRangeCriteria (final String min, final String max)
        throws ParseException, IOException
    {
        setCriteria(new RangedCriteria<>(convert(min), convert(max)));
        return this;
    }

    @Override
    public ICmdLineArg<E> setRegxCriteria (final String pattern) throws ParseException
    {
        setCriteria(new RegxCriteria<E>(pattern));
        return this;
    }

    @Override
    public ICmdLineArg<E> setRequired (final boolean bool)
    {
        required = bool;
        return this;
    }

    @Override
    public ICmdLineArg<E> setRequiredValue (final boolean bool) throws ParseException
    {
        if (!bool)
            throw new ParseException("requiredValue must be true for type: " + getClass().getName(), -1);
        requiredValue = bool;
        return this;
    }

    @Override
    public ICmdLineArg<E> setSystemGenerated (final boolean _systemGenerated)
    {
        systemGenerated = _systemGenerated;
        return this;
    }

    @Override
    public void setValue (
        final E value)
    {
        setParsed(true);
        getValues().add(value);
    }

    @Override
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

    @Override
    public ICmdLineArg<E> setVariable (
        final String _variable)
    {
        this.variable = _variable;
        return this;
    }

    @Override
    public int size ()
    {
        return values.size();
    }

    @Override
    public String toString ()
    {
        final StringBuilder sb = new StringBuilder();
        asDefinedType(sb);
        sb.append(" ");
        if (keyword != null)
        {
            if (keychar != null)
            {
                sb.append("--" + keyword + "(-" + keychar.charValue() + ")");
            } else
                sb.append("--" + keyword);
        } else if (keychar != null)
            sb.append("-" + keychar.charValue());
        else
            sb.append("undefined");

        return sb.toString();
    }

    @Override
    public void uncompile (final StringBuilder sb, final boolean showType)
    {
        if (showType)
        {
            sb.append("-t ");
            asDefinedType(sb);
        }
        sb.append(" ");
        sb.append("-k ");
        if (getKeychar() != null && getKeychar() != ' ')
            sb.append(getKeychar());
        if (getKeyword() != null && getKeyword().trim().length() > 0)
        {
            if (getKeychar() != null && getKeychar() != ' ')
                sb.append(" ");

            uncompileQuoter(sb, getKeyword());
        }
        if (isPositional())
            sb.append(" -p");
        if (isRequired())
            sb.append(" -r");
        if (isCaseSensitive())
            sb.append(" -c");
        if (isCamelCapsAllowed())
            sb.append(" --camelcaps");
        if (isMetaphoneAllowed())
            sb.append(" --metaphone");
        if (getHelp() != null && getHelp().trim().length() != 0)
        {
            sb.append(" -h ");
            uncompileQuoter(sb, getHelp());
        }
        final List<?> defaults = getDefaultValues();
        if (defaults.size() > 0 && !(this instanceof BooleanCLA))
        {
            sb.append(" -d");
            for (final Object oneDefault : defaults)
            {
                sb.append(" ");
                if (this instanceof ByteCLA)
                    sb.append(ByteCLA.byteToLit(oneDefault.toString()));
                else
                {
                    uncompileQuoter(sb, oneDefault.toString());
                }
            }
        }
        if (getInstanceClass() != null)
        {
            sb.append(" --class ");
            uncompileQuoter(sb, getInstanceClass());
        }
        if (getFactoryMethodName() != null)
        {
            sb.append(" --factoryMethod ");
            uncompileQuoter(sb, getFactoryMethodName());
        }
        if (getFactoryArgName() != null)
        {
            sb.append(" --factoryArgName ");
            uncompileQuoter(sb, getFactoryArgName());
        }
        if (getVariable() != null)
        {
            sb.append(" -v ");
            uncompileQuoter(sb, getVariable());
        }
        if (getFormat() != null)
        {
            sb.append(" -f ");
            uncompileQuoter(sb, getFormat());
        }
        if (isMultiple())
        {
            sb.append(" -m ");
            uncompileQuoter(sb, "" + getMultipleMin());
            if (getMultipleMax() != Integer.MAX_VALUE)
            {
                sb.append(" ");
                uncompileQuoter(sb, "" + getMultipleMax());
            }
        }
        /*
         * The enumList argument sets a --list criteria that should be ignored
         * for uncompiling.
         */
        if (getEnumClassName() != null)
        {
            sb.append(" --enumlist ");
            uncompileQuoter(sb, getEnumClassName());

        } else if (!(this instanceof BooleanCLA) && getCriteria() != null)
        {
            getCriteria().asDefinitionText(sb);
        }
    }

    private void uncompileQuoter (final StringBuilder sb, final String value)
    {
        sb.append("'");
        sb.append(value.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\""));
        sb.append("'");
    }

    @Override
    public String uniqueId ()
    {
        return "argument[" + hashCode() + "]";
    }

    @Override
    public void update (
        final E value)
    {
        update(0, value);
    }

    @Override
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

    @Override
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
