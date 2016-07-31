package com.obdobion.argument;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class CLAFactory
{
    static final String TYPE_DEFAULT  = "default";
    static final String TYPE_FILE     = "file";
    static final String TYPE_WILDFILE = "wildfile";
    static final String TYPE_END      = "end";
    static final String TYPE_DOUBLE   = "double";
    static final String TYPE_FLOAT    = "float";
    static final String TYPE_PATTERN  = "pattern";
    static final String TYPE_DATE     = "date";
    static final String TYPE_LONG     = "long";
    static final String TYPE_ENUM     = "enum";
    static final String TYPE_INTEGER  = "integer";
    static final String TYPE_STRING   = "string";
    static final String TYPE_BYTE     = "byte";
    static final String TYPE_CHAR     = "character";
    static final String TYPE_BOOLEAN  = "boolean";
    static final String TYPE_BEGIN    = "begin";
    static final String TYPE_EQU      = "equ";

    static CLAFactory   instance;

    static public CLAFactory getInstance()
    {
        if (instance == null)
            instance = new CLAFactory();
        return instance;
    }

    static public void reset()
    {
        instance = null;
    }

    int              nextUniqueId;

    CmdLine          factoryParser;

    IntegerCLA       uniqueIdArg;
    StringCLA        typeArg;
    StringCLA        variableArg;
    StringCLA        keyArg;
    BooleanCLA       positionalArg;

    IntegerCLA       multipleArg;
    BooleanCLA       camelCapsArg;

    BooleanCLA       metaphoneArg;
    StringCLA        instanceClassArg;
    StringCLA        factoryMethodArg;

    StringCLA        factoryArgNameArg;
    StringCLA        defaultValueArg;
    StringCLA        rangeArg;
    StringCLA        regxArg;
    StringCLA        listArg;
    StringCLA        enumListArg;
    StringCLA        formatArg;        // for dates
    BooleanCLA       requiredArg;
    BooleanCLA       caseSensitiveArg;

    StringCLA        helpArg;

    public Integer   uniqueId;
    public String    type;
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
    public String    format;           // for dates
    public Boolean   required;
    public Boolean   caseSensitive;

    public String    help;

    private CLAFactory()
    {
        super();
        nextUniqueId = 1;
        factoryParser = new CmdLine("parser compiler", '-', '!');
        try
        {
            typeArg = new StringCLA('t', "type");
            typeArg.setVariable("type");
            typeArg.setRequired(true);
            typeArg.setListCriteria(new String[] {
                    TYPE_BEGIN,
                    TYPE_BOOLEAN,
                    TYPE_BYTE,
                    TYPE_CHAR,
                    TYPE_STRING,
                    TYPE_INTEGER,
                    TYPE_ENUM,
                    TYPE_LONG,
                    TYPE_DATE,
                    TYPE_PATTERN,
                    TYPE_FLOAT,
                    TYPE_DOUBLE,
                    TYPE_END,
                    TYPE_FILE,
                    TYPE_WILDFILE,
                    TYPE_EQU,
                    TYPE_DEFAULT
            });
            factoryParser.add(typeArg);

            variableArg = new StringCLA('v', "variable");
            variableArg.setVariable("variable");
            variableArg.setCaseSensitive(true);
            factoryParser.add(variableArg);

            instanceClassArg = new StringCLA("class");
            instanceClassArg.setVariable("instanceClass");
            instanceClassArg.setCaseSensitive(true);
            factoryParser.add(instanceClassArg);

            formatArg = new StringCLA('f', "format");
            formatArg.setVariable("format");
            formatArg.setCaseSensitive(true);
            factoryParser.add(formatArg);

            keyArg = new StringCLA('k', "key");
            keyArg.setVariable("key");
            keyArg.setCaseSensitive(true);
            keyArg.setMultiple(1, 2);
            keyArg.setRequired(true);
            factoryParser.add(keyArg);

            uniqueIdArg = new IntegerCLA("uid");
            uniqueIdArg.setVariable("uniqueId");
            factoryParser.add(uniqueIdArg);

            camelCapsArg = new BooleanCLA("camelCaps");
            camelCapsArg.setVariable("camelCaps");
            camelCapsArg.setCamelCapsAllowed(true);
            camelCapsArg.setMetaphoneAllowed(true);
            factoryParser.add(camelCapsArg);

            metaphoneArg = new BooleanCLA("metaphone");
            metaphoneArg.setVariable("metaphone");
            metaphoneArg.setMetaphoneAllowed(true);
            factoryParser.add(metaphoneArg);

            multipleArg = new IntegerCLA('m', "multiple");
            multipleArg.setVariable("multiple");
            multipleArg.setMultiple(1, 2);
            multipleArg.setRangeCriteria("1", "" + Integer.MAX_VALUE);
            factoryParser.add(multipleArg);

            factoryMethodArg = new StringCLA("factoryMethod");
            factoryMethodArg.setVariable("factoryMethod");
            factoryMethodArg.setCaseSensitive(true);
            factoryParser.add(factoryMethodArg);

            factoryArgNameArg = new StringCLA("factoryArgName");
            factoryArgNameArg.setVariable("factoryArgName");
            factoryArgNameArg.setCaseSensitive(true);
            factoryParser.add(factoryArgNameArg);

            caseSensitiveArg = new BooleanCLA('c', "casesensitive");
            caseSensitiveArg.setVariable("caseSensitive");
            factoryParser.add(caseSensitiveArg);

            defaultValueArg = new StringCLA('d', "default");
            defaultValueArg.setVariable("defaultValue");
            defaultValueArg.setMultiple(1);
            defaultValueArg.setCaseSensitive(true);
            factoryParser.add(defaultValueArg);

            rangeArg = new StringCLA("range");
            rangeArg.setVariable("range");
            rangeArg.setCaseSensitive(true);
            rangeArg.setMultiple(1, 2);
            factoryParser.add(rangeArg);

            regxArg = new StringCLA("matches");
            regxArg.setVariable("regx");
            regxArg.setCaseSensitive(true);
            factoryParser.add(regxArg);

            listArg = new StringCLA("list");
            listArg.setVariable("list");
            listArg.setCaseSensitive(true);
            listArg.setMultiple(1);
            factoryParser.add(listArg);

            enumListArg = new StringCLA("enumlist");
            enumListArg.setVariable("enumList");
            enumListArg.setCaseSensitive(true);
            factoryParser.add(enumListArg);

            requiredArg = new BooleanCLA('r', "required");
            requiredArg.setVariable("required");
            factoryParser.add(requiredArg);

            positionalArg = new BooleanCLA('p', "positional");
            positionalArg.setVariable("positional");
            factoryParser.add(positionalArg);

            helpArg = new StringCLA('h', "helpMsg");
            helpArg.setVariable("help");
            helpArg.setCaseSensitive(true);
            factoryParser.add(helpArg);

        } catch (final ParseException e)
        {
            e.printStackTrace();
        } catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    private String argumentTypeForField(final Field field)
    {
        final Class<?> fieldType = field.getType();

        if (fieldType == String.class
                || fieldType == String[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.String>"))
            return TYPE_STRING;
        if (fieldType == int.class
                || fieldType == Integer.class
                || fieldType == int[].class
                || fieldType == Integer[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Integer>"))
            return TYPE_INTEGER;
        if (fieldType == long.class
                || fieldType == Long.class
                || fieldType == long[].class
                || fieldType == Long[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Long>"))
            return TYPE_LONG;
        if (fieldType == File.class
                || fieldType == File[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.io.File>"))
            return TYPE_FILE;
        if (fieldType == WildFiles.class
                || fieldType == WildFiles[].class
                || field.getGenericType().getTypeName().equals("java.util.List<com.obdobion.argument.WildFiles>"))
            return TYPE_WILDFILE;
        if (fieldType == double.class
                || fieldType == Double.class
                || fieldType == double[].class
                || fieldType == Double[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Double>"))
            return TYPE_DOUBLE;
        if (fieldType == float.class
                || fieldType == Float.class
                || fieldType == float[].class
                || fieldType == Float[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Float>"))
            return TYPE_FLOAT;
        if (fieldType == Pattern.class
                || fieldType == Pattern[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.util.regex.Pattern>"))
            return TYPE_PATTERN;
        if (fieldType == Date.class
                || fieldType == Calendar.class
                || fieldType == Date[].class
                || fieldType == Calendar[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.util.Date>"))
            return TYPE_DATE;
        if (fieldType.isEnum()
        // || (fieldType.isArray() && fieldType.getComponentType().isEnum())
        )
            /*
             * Currently only supporting enum[] as a String[].
             */
            return TYPE_ENUM;
        if (fieldType == byte.class
                || fieldType == Byte.class
                || fieldType == byte[].class
                || fieldType == Byte[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Byte>"))
            return TYPE_BYTE;
        if (fieldType == char.class
                || fieldType == Character.class
                || fieldType == char[].class
                || fieldType == Character[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Character>"))
            return TYPE_CHAR;
        if (fieldType == boolean.class
                || fieldType == Boolean.class)
            return TYPE_BOOLEAN;
        if (fieldType == CmdLineCLA.class
                || fieldType == CmdLineCLA[].class
                || field.getGenericType().getTypeName().equals("java.util.List<com.obdobion.argument.CmdLineCLA>"))
            return TYPE_BEGIN;
        if (fieldType == Equ.class
                || fieldType == Equ[].class
                || field.getGenericType().getTypeName().equals("java.util.List<com.obdobion.algebrain.Equ>"))
            return TYPE_EQU;

        return TYPE_BEGIN;
    }

    public boolean atEnd(final char commandPrefix, final CmdLineCLA group, final String definition)
            throws ParseException, IOException
    {
        factoryParser.parse(CommandLineParser.getInstance(commandPrefix, definition));
        if (TYPE_END.equalsIgnoreCase(typeArg.getValue()))
        {
            char keychar = commandPrefix; // dummy value to indicate non-usage
            String keyword = null;

            if (keyArg.getValue(0).length() == 1)
                keychar = keyArg.getValue(0).charAt(0);
            else
                keyword = keyArg.getValue(0);

            if (keyArg.size() == 2)
                if (keyArg.getValue(1).length() == 1 && keychar == commandPrefix)
                    keychar = keyArg.getValue(1).charAt(0);
                else
                    keyword = keyArg.getValue(1);

            if (keychar == group.getKeychar())
                return true;
            if (keyword != null)
                if (keyword.equals(group.getKeyword()))
                    return true;
        }
        return false;
    }

    private ICmdLineArg<?> createArgFor(final char commandPrefix, char keychar, final String keyword)
            throws ParseException
    {
        /*-
        logger.trace("creating argument {},{}", (keychar <= ' ' ? "" : "" + keychar), (keyword == null ? "" : keyword));
        */

        if (Character.isDigit(keychar))
            throw new ParseException("The Key Character can not be a digit \"" + keyArg.getValue(0) + "\"", -1);
        if (keychar <= ' ')
            keychar = commandPrefix;
        if (keyword != null)
            if (Character.isDigit(keyword.charAt(0)))
                throw new ParseException("The first character of a Key Word can not be a digit \""
                        + keyArg.getValue(1)
                        + "\"", -1);

        if (TYPE_BEGIN.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new CmdLineCLA(keychar, keyword);
                return new CmdLineCLA(keychar);
            }
            return new CmdLineCLA(keyword);
        }

        if (TYPE_BOOLEAN.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new BooleanCLA(keychar, keyword);
                return new BooleanCLA(keychar);
            }
            return new BooleanCLA(keyword);
        }

        if (TYPE_EQU.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new EquCLA(keychar, keyword);
                return new EquCLA(keychar);
            }
            return new EquCLA(keyword);
        }

        if (TYPE_DEFAULT.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new DefaultCLA(keychar, keyword);
                return new DefaultCLA(keychar);
            }
            return new DefaultCLA(keyword);
        }

        if (TYPE_STRING.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new StringCLA(keychar, keyword);
                return new StringCLA(keychar);
            }
            return new StringCLA(keyword);
        }

        if (TYPE_BYTE.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new ByteCLA(keychar, keyword);
                return new ByteCLA(keychar);
            }
            return new ByteCLA(keyword);
        }

        if (TYPE_CHAR.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new CharacterCLA(keychar, keyword);
                return new CharacterCLA(keychar);
            }
            return new CharacterCLA(keyword);
        }

        if (TYPE_FILE.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new FileCLA(keychar, keyword);
                return new FileCLA(keychar);
            }
            return new FileCLA(keyword);
        }

        if (TYPE_WILDFILE.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new WildFilesCLA(keychar, keyword);
                return new WildFilesCLA(keychar);
            }
            return new WildFilesCLA(keyword);
        }

        if (TYPE_DATE.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new DateCLA(keychar, keyword);
                return new DateCLA(keychar);
            }
            return new DateCLA(keyword);
        }

        if (TYPE_ENUM.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new EnumCLA(keychar, keyword);
                return new EnumCLA(keychar);
            }
            return new EnumCLA(keyword);
        }

        if (TYPE_PATTERN.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new PatternCLA(keychar, keyword);
                return new PatternCLA(keychar);
            }
            return new PatternCLA(keyword);
        }

        if (TYPE_INTEGER.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new IntegerCLA(keychar, keyword);
                return new IntegerCLA(keychar);
            }
            return new IntegerCLA(keyword);
        }

        if (TYPE_LONG.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new LongCLA(keychar, keyword);
                return new LongCLA(keychar);
            }
            return new LongCLA(keyword);
        }

        if (TYPE_FLOAT.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new FloatCLA(keychar, keyword);
                return new FloatCLA(keychar);
            }
            return new FloatCLA(keyword);
        }

        if (TYPE_DOUBLE.equalsIgnoreCase(type))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new DoubleCLA(keychar, keyword);
                return new DoubleCLA(keychar);
            }
            return new DoubleCLA(keyword);
        }

        throw new ParseException("invalid type: " + type, -1);
    }

    /**
     * Set all of the instance variables as if the parser had done it instead of
     * how we are doing it here with annotations.
     */
    public ICmdLineArg<?> instanceFor(final char commandPrefix, final Field oneField, final Arg argAnnotation)
            throws ParseException, IOException
    {
        /*
         * set the instance variables in preparation for population of arg.
         */
        resetInstanceVariables();
        type = argumentTypeForField(oneField);

        final Class<?> fieldType = oneField.getType();

        if (fieldType == List.class || fieldType.isArray()
                || (fieldType == WildFiles.class && (argAnnotation.multimin() > 0 || argAnnotation.multimax() > 0)))
        {
            multiple = new Integer[] { argAnnotation.multimin(), argAnnotation.multimax() };
        }

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
        if (instanceClass.isEmpty() || instanceClass == null)
        {
            /*
             * Setting the instanceClass allows factoryArgs that use this enum
             * to resolve to the correct class.
             */
            if (fieldType.isEnum())
                instanceClass = fieldType.getName();
            else if (fieldType.isArray())
                instanceClass = oneField.getType().getComponentType().getName();
            else if (fieldType == List.class)
                instanceClass = ((Class<?>) ((ParameterizedType) oneField.getGenericType())
                        .getActualTypeArguments()[0]).getName();
            else
                instanceClass = null;
        }
        final ICmdLineArg<?> arg = createArgFor(commandPrefix, argAnnotation.shortName(), keyword);
        populateArgument(arg);

        /*
         * While the parser is deprecated (and not deleted) then it is necessary
         * to back load it from the annotations. This is because some uses of
         * argument are directly asking the parser for the values of arguments.
         */
        // factoryParser.reset();
        // factoryParser.push();
        return arg;
    }

    public ICmdLineArg<?> instanceFor(final char commandPrefix, final String definition)
            throws ParseException, IOException
    {
        /*
         * set the instance variables in preparation for population of arg.
         */
        resetInstanceVariables();
        factoryParser.reset();
        factoryParser.parse(this, definition);

        char keychar = commandPrefix; // dummy value to indicate non-usage
        String keyword = null;

        if (keyArg.getValue(0).length() == 1)
            keychar = keyArg.getValue(0).charAt(0);
        else
            keyword = keyArg.getValue(0);

        if (keyArg.size() == 2)
            if (keyArg.getValue(1).length() == 1 && keychar == commandPrefix)
                keychar = keyArg.getValue(1).charAt(0);
            else
                keyword = keyArg.getValue(1);

        final ICmdLineArg<?> arg = createArgFor(commandPrefix, keychar, keyword);
        populateArgument(arg);
        /*
         * It is not possible to figure out the values until the parse is given
         * an instance. So stop trying to show the enum constants.
         */
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
            {
                arg.setDefaultValue(defVal);
            }

        if (range != null && range.length > 0)
        {
            if (range.length == 1)
                arg.setRangeCriteria(range[0], null);
            else if (range.length == 2)
                arg.setRangeCriteria(range[0], range[1]);
            else
                throw new ParseException("invalid number of range arguments for \"" + arg.toString() + "\"", 0);
        }
        if (regx != null)
            arg.setRegxCriteria(regx);
        if (list != null)
        {
            final String[] critters = new String[list.length];
            for (int c = 0; c < list.length; c++)
            {
                if (caseSensitive)
                    critters[c] = list[c];
                else
                    critters[c] = list[c].toLowerCase();
            }
            arg.setListCriteria(critters);
        }
        /*
         * Not yet (or maybe ever) from annotations.
         */
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
}