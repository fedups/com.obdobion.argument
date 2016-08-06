package com.obdobion.argument.type;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.List;

import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.criteria.ListCriteria;
import com.obdobion.argument.criteria.RegxCriteria;

/**
 * @author Chris DeGreef
 *
 */
public class CLAFactory
{
    static final public String SELF_REFERENCING_ARGNAME = "this";
    static CLAFactory          instance;

    static public CLAFactory getInstance()
    {
        if (instance == null)
            instance = new CLAFactory();
        return instance;
    }

    static final public Class<?> instanceType(final Field oneField)
    {
        if (oneField.getType().isEnum())
            return oneField.getType();
        if (oneField.getType().isArray())
            return oneField.getType().getComponentType();
        if (oneField.getType() == List.class)
            return ((Class<?>) ((ParameterizedType) oneField.getGenericType()).getActualTypeArguments()[0]);
        return oneField.getType();
    }

    static public void reset()
    {
        instance = null;
    }

    int              nextUniqueId;

    public Integer   uniqueId;
    public ClaType   type;
    public String    variable;
    public String[]  key;
    public Boolean   positional;

    public Integer[] multiple;
    public Boolean   camelCaps;

    public Boolean   metaphone;
    public String    instanceClass;
    public String    factoryMethod;

    public String    factoryArgName;
    public String[]  defaultValue;
    public String[]  range;
    public String    regx;
    public String[]  list;
    public String    enumList;
    public String    format;        // for dates
    public Boolean   required;
    public Boolean   caseSensitive;

    public String    help;

    private CLAFactory()
    {
        super();
        nextUniqueId = 1;
    }

    private String[] convertEnumToListOfNames(final Field oneField, final String inEnumClassName) throws ParseException
    {
        try
        {
            final Class<?> enumClass = oneField.getDeclaringClass().getClassLoader().loadClass(inEnumClassName);
            final Object[] constants = enumClass.getEnumConstants();
            final String[] listConstants = new String[constants.length];
            for (int x = 0; x < constants.length; x++)
                listConstants[x] = constants[x].toString();
            return listConstants;

        } catch (final ClassNotFoundException e)
        {
            throw new ParseException("Enum class not found: " + inEnumClassName, 0);
        }
    }

    private ICmdLineArg<?> createArgFor(final char commandPrefix, char keychar, final String keyword)
            throws ParseException
    {
        if (Character.isDigit(keychar))
            throw new ParseException("The Key Character can not be a digit '" + keychar + "'", -1);
        if (keychar <= ' ')
            keychar = commandPrefix;
        if (keyword != null && keyword.length() > 0)
            if (Character.isDigit(keyword.charAt(0)))
                throw new ParseException("The first character of a Key Word can not be a digit \"" + keyword + "\"",
                        -1);
        return type.argumentInstance(commandPrefix, keychar, keyword);
    }

    private Field findTheCorrectVariable(final Field oneField, final String[] split) throws ParseException
    {
        final Class<?> instanceType = instanceType(oneField);
        for (final Field embeddedField : instanceType.getDeclaredFields())
            if (embeddedField.getName().equals(split[0]))
                return embeddedField;
        throw new ParseException("referencing an unknown variable: "
                + split[0]
                + " in "
                + instanceType.getName(),
                0);
    }

    /**
     * Set all of the instance variables as if the parser had done it instead of
     * how we are doing it here with annotations.
     */
    public ICmdLineArg<?> instanceFor(final char commandPrefix, final Field p_oneField, final Arg argAnnotation)
            throws ParseException, IOException
    {
        /*
         * set the instance variables in preparation for population of arg.
         */
        resetInstanceVariables();

        Field oneField = null;
        /*
         * This allows for specifying args for subparsers without annotating
         * them directly.
         */
        if (!argAnnotation.variable().equals(""))
            oneField = findTheCorrectVariable(p_oneField, argAnnotation.variable().split("\\."));
        else
            oneField = p_oneField;

        type = ClaType.forField(oneField, argAnnotation);

        final Class<?> fieldType = oneField.getType();

        if (fieldType.isArray() || fieldType == java.util.List.class
                || argAnnotation.multimin() > 0 || argAnnotation.multimax() > 0)
            multiple = new Integer[] {
                    (argAnnotation.multimin() == 0 ? 1 : argAnnotation.multimin()),
                    (argAnnotation.multimax() == 0 ? Integer.MAX_VALUE : argAnnotation.multimax())
            };

        variable = oneField.getName();
        required = argAnnotation.required();

        String keyword = argAnnotation.longName();
        if (keyword == null || keyword.trim().length() == 0)
            keyword = variable;

        camelCaps = argAnnotation.allowCamelCaps();
        metaphone = argAnnotation.allowMetaphone();
        positional = argAnnotation.positional();
        caseSensitive = argAnnotation.caseSensitive();
        defaultValue = argAnnotation.defaultValues();
        range = argAnnotation.range();
        if (!argAnnotation.matches().isEmpty())
            regx = argAnnotation.matches();
        if (argAnnotation.inList().length != 0)
            list = argAnnotation.inList();
        if (!argAnnotation.inEnum().isEmpty())
            list = convertEnumToListOfNames(oneField, argAnnotation.inEnum());

        help = argAnnotation.help();
        if (help.isEmpty())
            help = null;

        format = argAnnotation.format();
        if (format.isEmpty())
            format = null;

        factoryMethod = argAnnotation.factoryMethod();
        if (factoryMethod.isEmpty())
            factoryMethod = null;
        factoryArgName = argAnnotation.factoryArgName();
        if (factoryArgName.isEmpty())
            factoryArgName = null;

        instanceClass = argAnnotation.instanceClass();
        if (instanceClass == null || instanceClass.isEmpty())
        {
            /*
             * Setting the instanceClass allows factoryArgs that use this enum
             * to resolve to the correct class.
             */
            final Class<?> instanceType = instanceType(oneField);
            if (instanceType == fieldType && !fieldType.isEnum())
                instanceClass = null;
            else
                instanceClass = instanceType.getName();
        }
        final ICmdLineArg<?> arg = createArgFor(commandPrefix, argAnnotation.shortName(), keyword);
        populateArgument(arg);
        verifyArgument(arg, argAnnotation, fieldType);
        return arg;
    }

    private void populateArgument(final ICmdLineArg<?> arg) throws ParseException, IOException
    {
        if (uniqueId != null && uniqueId > 0)
        {
            arg.setUniqueId(uniqueId);
            if (arg.getUniqueId() >= nextUniqueId)
                nextUniqueId = arg.getUniqueId() + 1;
        } else
            arg.setUniqueId(nextUniqueId++);

        arg.setHelp(help);
        arg.setCamelCapsAllowed(camelCaps);
        arg.setMetaphoneAllowed(metaphone);
        arg.setRequired(required == null || !required.booleanValue() ? false : true);
        arg.setPositional(positional);
        arg.setCaseSensitive(caseSensitive);
        if (multiple != null)
            if (multiple.length == 1)
                arg.setMultiple(multiple[0]);
            else
                arg.setMultiple(multiple[0], multiple[1]);
        if (variable != null)
            arg.setVariable(variable);
        if (instanceClass != null)
            arg.setInstanceClass(instanceClass);

        if (factoryMethod != null)
        {
            arg.setFactoryMethodName(factoryMethod);
            if (factoryArgName != null)
                arg.setFactoryArgName(factoryArgName);
        }
        if (arg instanceof CmdLineCLA)
            return;

        arg.setFormat(format);

        if (defaultValue != null)
            for (final String defVal : defaultValue)
                arg.setDefaultValue(defVal);

        if (range != null && range.length > 0)
            if (range.length == 1)
                arg.setRangeCriteria(range[0], null);
            else if (range.length == 2)
                arg.setRangeCriteria(range[0], range[1]);
            else
                throw new ParseException("invalid number of range arguments for \"" + arg.toString() + "\"", 0);
        if (regx != null)
            arg.setRegxCriteria(regx);
        if (list != null)
        {
            final String[] critters = new String[list.length];
            for (int c = 0; c < list.length; c++)
                if (caseSensitive)
                    critters[c] = list[c];
                else
                    critters[c] = list[c].toLowerCase();
            arg.setListCriteria(critters);
        }
        if (enumList != null)
            arg.setEnumCriteriaAllowError(enumList);
    }

    private void resetInstanceVariables()
    {
        uniqueId = null;
        type = null;
        variable = null;
        key = null;
        positional = null;

        multiple = null;
        camelCaps = null;

        metaphone = null;
        instanceClass = null;
        factoryMethod = null;

        factoryArgName = null;
        defaultValue = null;
        range = null;
        regx = null;
        list = null;
        enumList = null;
        format = null; // for dates
        required = null;
        caseSensitive = null;

        help = null;
    }

    /**
     *
     *
     * @param arg
     * @param argAnnotation
     *            is only needed to evaluate parameters that are not stored on
     *            the arg itself; like excludeArgs.
     * @throws ParseException
     */
    private void verifyArgument(final ICmdLineArg<?> arg, final Arg argAnnotation, final Class<?> fieldType)
            throws ParseException
    {
        if (arg.isCamelCapsAllowed())
        {
            if (!arg.supportsCamelCaps())
                throw new ParseException(verifyMsg("allowCamelCaps is not allowed", arg), 0);
            if (arg.getKeyword() == null)
                throw new ParseException(verifyMsg("allowCamelCaps requires a longName", arg), 0);
        }
        if (arg.isMetaphoneAllowed())
        {
            if (!arg.supportsMetaphone())
                throw new ParseException(verifyMsg("allowMetaphone is not allowed", arg), 0);
            if (arg.getKeyword() == null)
                throw new ParseException(verifyMsg("allowMetaphone requires a longName", arg), 0);
        }
        if (arg.isCaseSensitive())
            if (!arg.supportsCaseSensitive())
                throw new ParseException(verifyMsg("caseSensitive is not allowed", arg), 0);
        if (arg.getDefaultValues() != null && arg.getDefaultValues().size() > 0)
            if (!arg.supportsDefaultValues())
                throw new ParseException(verifyMsg("defaultValues is not allowed", arg), 0);
        if (arg.getFactoryMethodName() != null)
            if (!arg.supportsFactoryMethod())
                throw new ParseException(verifyMsg("factoryMethod is not allowed", arg), 0);
        if (arg.getFactoryArgName() != null)
        {
            if (!arg.supportsFactoryArgName())
                throw new ParseException(verifyMsg("factoryArgName is not allowed", arg), 0);
            if (arg.getFactoryMethodName() == null)
                throw new ParseException(verifyMsg("factoryArgName requires factoryMethod", arg), 0);
        }
        if (arg.getFormat() != null)
            if (!arg.supportsFormat())
                throw new ParseException(verifyMsg("format is not allowed", arg), 0);
        if (arg.getHelp() != null)
            if (!arg.supportsHelp())
                throw new ParseException(verifyMsg("help is not allowed", arg), 0);
        if (arg.getCriteria() != null)
        {
            if (arg.getCriteria() instanceof ListCriteria)
                if (!arg.supportsInList())
                    throw new ParseException(verifyMsg("inList is not allowed", arg), 0);
            if (arg.getCriteria() instanceof RegxCriteria)
                if (!arg.supportsMatches())
                    throw new ParseException(verifyMsg("matches is not allowed", arg), 0);
        }
        if (argAnnotation != null)
        {
            if (argAnnotation.excludeArgs() != null && argAnnotation.excludeArgs().length > 0)
                if (!arg.supportsExcludeArgs())
                    throw new ParseException(verifyMsg("excludeArgs is not allowed", arg), 0);
            if (argAnnotation.instanceClass() != null && !argAnnotation.instanceClass().isEmpty())
                if (!arg.supportsInstanceClass())
                    throw new ParseException(verifyMsg("instanceClass is not allowed", arg), 0);
            if (argAnnotation.multimin() != 0)
            {
                if (!fieldType.isArray() && fieldType != java.util.List.class && fieldType != WildFiles.class)
                    throw new ParseException(verifyMsg("multimin is not allowed", arg), 0);
                if (argAnnotation.multimin() < 1)
                    throw new ParseException(verifyMsg("multimin must be > 0", arg), 0);
            }
            if (argAnnotation.multimax() > 0)
            {
                if (!fieldType.isArray() && fieldType != java.util.List.class && fieldType != WildFiles.class)
                    throw new ParseException(verifyMsg("multimax is not allowed", arg), 0);
                if (argAnnotation.multimax() < argAnnotation.multimin())
                    throw new ParseException(verifyMsg("multimax must be >= multimin", arg), 0);
            }
        }
    }

    private String verifyMsg(final String msg, final ICmdLineArg<?> arg)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("\"");
        if (arg.getVariable() != null)
            sb.append(arg.getVariable());
        else
            sb.append(arg.toString());
        sb.append("\"");
        sb.append(" - ");
        sb.append(msg);
        return sb.toString();
    }
}