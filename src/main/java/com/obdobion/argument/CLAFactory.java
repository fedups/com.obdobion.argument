package com.obdobion.argument;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

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
    static final String TYPE_BOOLEAN  = "boolean";
    static final String TYPE_BEGIN    = "begin";
    static final String TYPE_EQU      = "equ";

    CmdLine             factoryParser;
    StringCLA           type;
    StringCLA           variable;
    StringCLA           key;
    BooleanCLA          positional;
    IntegerCLA          multiple;

    StringCLA           instanceClass;
    StringCLA           factoryMethod;
    StringCLA           factoryArgName;

    StringCLA           defaultValue;
    StringCLA           range;
    StringCLA           regx;
    StringCLA           list;
    StringCLA           enumList;
    StringCLA           format;                    // for dates
    BooleanCLA          required;
    BooleanCLA          caseSensitive;
    StringCLA           help;

    public CLAFactory()
    {
        super();
        factoryParser = new CmdLine("parser compiler", '-', '!');
        try
        {
            type = new StringCLA('t', "type");
            type.setRequired(true);
            type.setListCriteria(new String[]
            {
                TYPE_BEGIN,
                TYPE_BOOLEAN,
                TYPE_BYTE,
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
            factoryParser.add(type);

            variable = new StringCLA('v', "variable");
            variable.setCaseSensitive(true);
            factoryParser.add(variable);

            instanceClass = new StringCLA("class");
            instanceClass.setCaseSensitive(true);
            factoryParser.add(instanceClass);

            format = new StringCLA('f', "format");
            format.setCaseSensitive(true);
            factoryParser.add(format);

            key = new StringCLA('k', "key");
            key.setCaseSensitive(true);
            key.setMultiple(1, 2);
            key.setRequired(true);
            factoryParser.add(key);

            multiple = new IntegerCLA('m', "multiple");
            multiple.setMultiple(1, 2);
            multiple.setRangeCriteria("1", "" + Integer.MAX_VALUE);
            factoryParser.add(multiple);

            factoryMethod = new StringCLA("factoryMethod");
            factoryMethod.setCaseSensitive(true);
            factoryParser.add(factoryMethod);

            factoryArgName = new StringCLA("factoryArgName");
            factoryArgName.setCaseSensitive(true);
            factoryParser.add(factoryArgName);

            caseSensitive = new BooleanCLA('c', "casesensitive");
            factoryParser.add(caseSensitive);

            defaultValue = new StringCLA('d', "default");
            defaultValue.setMultiple(1);
            defaultValue.setCaseSensitive(true);
            factoryParser.add(defaultValue);

            range = new StringCLA("range");
            range.setCaseSensitive(true);
            range.setMultiple(1, 2);
            factoryParser.add(range);

            regx = new StringCLA("matches");
            regx.setCaseSensitive(true);
            factoryParser.add(regx);

            list = new StringCLA("list");
            list.setCaseSensitive(true);
            list.setMultiple(1);
            factoryParser.add(list);

            enumList = new StringCLA("enumlist");
            enumList.setCaseSensitive(true);
            factoryParser.add(enumList);

            required = new BooleanCLA('r', "required");
            factoryParser.add(required);

            positional = new BooleanCLA('p', "positional");
            factoryParser.add(positional);

            help = new StringCLA('h', "help");
            help.setCaseSensitive(true);
            factoryParser.add(help);

            // UsageBuilder bld = new UsageBuilder();
            // factoryParser.usage(bld);
            // System.err.println(bld.toString());

        } catch (final ParseException e)
        {
            e.printStackTrace();
        } catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean atEnd (final char commandPrefix, final CmdLineCLA group, final String definition)
        throws ParseException, IOException
    {
        factoryParser.parse(CommandLineParser.getInstance(commandPrefix, definition));
        if (TYPE_END.equalsIgnoreCase(type.getValue()))
        {
            char keychar = commandPrefix; // dummy value to indicate non-usage
            String keyword = null;

            if (key.getValue(0).length() == 1)
                keychar = key.getValue(0).charAt(0);
            else
                keyword = key.getValue(0);

            if (key.size() == 2)
                if (key.getValue(1).length() == 1 && keychar == commandPrefix)
                    keychar = key.getValue(1).charAt(0);
                else
                    keyword = key.getValue(1);

            if (keychar == group.getKeychar())
                return true;
            if (keyword != null)
                if (keyword.equals(group.getKeyword()))
                    return true;
        }
        return false;
    }

    private ICmdLineArg<?> createArgFor (final char commandPrefix) throws ParseException
    {
        char keychar = commandPrefix; // dummy value to indicate non-usage
        String keyword = null;

        if (key.getValue(0).length() == 1)
            keychar = key.getValue(0).charAt(0);
        else
            keyword = key.getValue(0);

        if (key.size() == 2)
            if (key.getValue(1).length() == 1 && keychar == commandPrefix)
                keychar = key.getValue(1).charAt(0);
            else
                keyword = key.getValue(1);

        if (Character.isDigit(keychar))
            throw new ParseException("The Key Character can not be a digit \"" + key.getValue(0) + "\"", -1);
        if (keyword != null)
            if (Character.isDigit(keyword.charAt(0)))
                throw new ParseException("The first character of a Key Word can not be a digit \"" + key.getValue(1)
                    + "\"", -1);

        if (TYPE_BEGIN.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new CmdLineCLA(keychar, keyword);
                return new CmdLineCLA(keychar);
            }
            return new CmdLineCLA(keyword);
        }

        if (TYPE_BOOLEAN.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new BooleanCLA(keychar, keyword);
                return new BooleanCLA(keychar);
            }
            return new BooleanCLA(keyword);
        }

        if (TYPE_EQU.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new EquCLA(keychar, keyword);
                return new EquCLA(keychar);
            }
            return new EquCLA(keyword);
        }

        if (TYPE_DEFAULT.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new DefaultCLA(keychar, keyword);
                return new DefaultCLA(keychar);
            }
            return new DefaultCLA(keyword);
        }

        if (TYPE_STRING.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new StringCLA(keychar, keyword);
                return new StringCLA(keychar);
            }
            return new StringCLA(keyword);
        }

        if (TYPE_BYTE.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new ByteCLA(keychar, keyword);
                return new ByteCLA(keychar);
            }
            return new ByteCLA(keyword);
        }

        if (TYPE_FILE.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new FileCLA(keychar, keyword);
                return new FileCLA(keychar);
            }
            return new FileCLA(keyword);
        }

        if (TYPE_WILDFILE.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new WildFilesCLA(keychar, keyword);
                return new WildFilesCLA(keychar);
            }
            return new WildFilesCLA(keyword);
        }

        if (TYPE_DATE.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new DateCLA(keychar, keyword);
                return new DateCLA(keychar);
            }
            return new DateCLA(keyword);
        }

        if (TYPE_ENUM.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new EnumCLA(keychar, keyword);
                return new EnumCLA(keychar);
            }
            return new EnumCLA(keyword);
        }

        if (TYPE_PATTERN.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new PatternCLA(keychar, keyword);
                return new PatternCLA(keychar);
            }
            return new PatternCLA(keyword);
        }

        if (TYPE_INTEGER.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new IntegerCLA(keychar, keyword);
                return new IntegerCLA(keychar);
            }
            return new IntegerCLA(keyword);
        }

        if (TYPE_LONG.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new LongCLA(keychar, keyword);
                return new LongCLA(keychar);
            }
            return new LongCLA(keyword);
        }

        if (TYPE_FLOAT.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new FloatCLA(keychar, keyword);
                return new FloatCLA(keychar);
            }
            return new FloatCLA(keyword);
        }

        if (TYPE_DOUBLE.equalsIgnoreCase(type.getValue()))
        {
            if (keychar != commandPrefix)
            {
                if (keyword != null)
                    return new DoubleCLA(keychar, keyword);
                return new DoubleCLA(keychar);
            }
            return new DoubleCLA(keyword);
        }

        throw new ParseException("invalid type: " + type.getValue(), -1);
    }

    public ICmdLineArg<?> instanceFor (final char commandPrefix, final String definition) throws ParseException,
        IOException
    {
        factoryParser.parse(definition);
        final ICmdLineArg<?> arg = createArgFor(commandPrefix);
        if (help.hasValue())
            arg.setHelp(help.getValue());
        arg.setRequired(required.getValue().booleanValue());
        arg.setPositional(positional.getValue().booleanValue());
        arg.setCaseSensitive(caseSensitive.getValue().booleanValue());
        if (multiple.hasValue())
        {
            if (multiple.size() == 1)
                arg.setMultiple(multiple.getValue(0));
            else
                arg.setMultiple(multiple.getValue(0), multiple.getValue(1));
        }
        if (variable.hasValue())
            arg.setVariable(variable.getValue());
        if (instanceClass.hasValue())
        {
            if (!(arg instanceof CmdLineCLA) || factoryMethod.hasValue())
                throw new ParseException("--class is not valid for " + arg.toString(), 0);
            arg.setInstanceClass(instanceClass.getValue());
        }
        if (factoryMethod.hasValue())
        {
            arg.setFactoryMethodName(factoryMethod.getValue());
            if (factoryArgName.hasValue())
                arg.setFactoryArgName(factoryArgName.getValue());
        }

        if (arg instanceof CmdLineCLA)
            return arg;

        if (format.hasValue())
            arg.setFormat(format.getValue());
        if (defaultValue.hasValue())
            for (final String defVal : defaultValue.values)
            {
                arg.setDefaultValue(defVal);
            }

        if (range.hasValue())
            arg.setRangeCriteria(range.getValue(0), range.getValue(1));
        if (regx.hasValue())
            arg.setRegxCriteria(regx.getValue());
        if (list.hasValue())
        {
            final String[] critters = new String[list.size()];
            final Iterator<String> cIter = list.getValues().iterator();
            int c = 0;
            while (cIter.hasNext())
            {
                if (caseSensitive.getValue().booleanValue())
                    critters[c++] = cIter.next();
                else
                    critters[c++] = cIter.next().toLowerCase();
            }
            arg.setListCriteria(critters);
        }
        if (enumList.hasValue())
            arg.setEnumCriteria(enumList.getValue());
        /*
         * It is not possible to figure out the values until the parse is given
         * an instance. So stop trying to show the enum constants.
         */
        return arg;
    }
}