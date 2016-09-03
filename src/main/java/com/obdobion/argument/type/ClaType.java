package com.obdobion.argument.type;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.annotation.Arg;

/**
 * <p>
 * ClaType class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public enum ClaType
{
    DEFAULT("default", DefaultCLA.class),
    FILE("file", FileCLA.class),
    WILDFILE("wildfile", WildFilesCLA.class),
    DOUBLE("double", DoubleCLA.class),
    FLOAT("float", FloatCLA.class),
    PATTERN("pattern", PatternCLA.class),
    DATETIMEFORMATTER("datetimeformatter", DateTimeFormatterCLA.class),
    SIMPLEDATEFORMAT("simpledateformat", SimpleDateFormatCLA.class),
    DATE("date", DateCLA.class),
    CALENDAR("calendar", CalendarCLA.class),
    LOCALDATETIME("localdatetime", LocalDateTimeCLA.class),
    LOCALDATE("localdate", LocalDateCLA.class),
    LOCALTIME("localtime", LocalTimeCLA.class),
    LONG("long", LongCLA.class),
    ENUM("enum", EnumCLA.class),
    INTEGER("integer", IntegerCLA.class),
    STRING("string", StringCLA.class),
    BYTE("byte", ByteCLA.class),
    CHAR("character", CharacterCLA.class),
    BOOLEAN("boolean", BooleanCLA.class),
    SUBPARSER("begin", CmdLineCLA.class),
    EQU("equation", EquCLA.class),
    POJO("pojo", PojoCLA.class);

    /**
     * <p>
     * forField.
     * </p>
     *
     * @param field a {@link java.lang.reflect.Field} object.
     * @param argAnnotation a {@link com.obdobion.argument.annotation.Arg}
     *            object.
     * @return a {@link com.obdobion.argument.type.ClaType} object.
     */
    static public ClaType forField(final Field field, final Arg argAnnotation)
    {
        final Class<?> fieldType = field.getType();

        if (fieldType == String.class
                || fieldType == String[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.String>"))
            return STRING;
        if (fieldType == int.class
                || fieldType == Integer.class
                || fieldType == int[].class
                || fieldType == Integer[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Integer>"))
            return INTEGER;
        if (fieldType == long.class
                || fieldType == Long.class
                || fieldType == long[].class
                || fieldType == Long[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Long>"))
            return LONG;
        if (fieldType == File.class
                || fieldType == File[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.io.File>"))
            return FILE;
        if (fieldType == WildFiles.class
                || fieldType == WildFiles[].class
                || field.getGenericType().getTypeName().equals("java.util.List<com.obdobion.argument.WildFiles>"))
            return WILDFILE;
        if (fieldType == double.class
                || fieldType == Double.class
                || fieldType == double[].class
                || fieldType == Double[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Double>"))
            return DOUBLE;
        if (fieldType == float.class
                || fieldType == Float.class
                || fieldType == float[].class
                || fieldType == Float[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Float>"))
            return FLOAT;
        if (fieldType == Pattern.class
                || fieldType == Pattern[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.util.regex.Pattern>"))
            return PATTERN;
        if (fieldType == DateTimeFormatter.class
                || fieldType == DateTimeFormatter[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.time.format.DateTimeFormatter>"))
            return DATETIMEFORMATTER;
        if (fieldType == SimpleDateFormat.class
                || fieldType == SimpleDateFormat[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.text.SimpleDateFormat>"))
            return SIMPLEDATEFORMAT;
        if (fieldType == Date.class
                || fieldType == Date[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.util.Date>"))
            return DATE;
        if (fieldType == Calendar.class
                || fieldType == Calendar[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.util.Calendar>"))
            return CALENDAR;
        if (fieldType == LocalDateTime.class
                || fieldType == LocalDateTime[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.time.LocalDateTime>"))
            return LOCALDATETIME;
        if (fieldType == LocalDate.class
                || fieldType == LocalDate[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.time.LocalDate>"))
            return LOCALDATE;
        if (fieldType == LocalTime.class
                || fieldType == LocalTime[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.time.LocalTime>"))
            return LOCALTIME;
        if (fieldType.isEnum()
        // || (fieldType.isArray() && fieldType.getComponentType().isEnum())
        )
            /*
             * Currently only supporting enum[] as a String[].
             */
            return ENUM;
        if (fieldType == byte.class
                || fieldType == Byte.class
                || fieldType == byte[].class
                || fieldType == Byte[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Byte>"))
            return BYTE;
        if (fieldType == char.class
                || fieldType == Character.class
                || fieldType == char[].class
                || fieldType == Character[].class
                || field.getGenericType().getTypeName().equals("java.util.List<java.lang.Character>"))
            return CHAR;
        if (fieldType == boolean.class
                || fieldType == Boolean.class)
            return BOOLEAN;
        if (fieldType == CmdLineCLA.class
                || fieldType == CmdLineCLA[].class
                || field.getGenericType().getTypeName().equals("java.util.List<com.obdobion.argument.CmdLineCLA>"))
            return SUBPARSER;
        if (fieldType == Equ.class
                || fieldType == Equ[].class
                || field.getGenericType().getTypeName().equals("java.util.List<com.obdobion.algebrain.Equ>"))
            return EQU;
        /*
         * This point is arrived at for two somewhat different reasons. The most
         * common is when a subparser is being defined. A lesser reason is when
         * a complex object is being produced that is not a subparser. This
         * would require a factoryMethod and a factoryArgName of
         * SELF_REFERENCING_ARGNAME.
         */
        if (!argAnnotation.factoryMethod().equals("")
                && argAnnotation.factoryArgName().equalsIgnoreCase(CLAFactory.SELF_REFERENCING_ARGNAME))
            return POJO;

        return SUBPARSER;
    }

    private String                typeName;

    private Class<ICmdLineArg<?>> argumentClass;

    @SuppressWarnings("unchecked")
    private ClaType(final String name, final Class<?> argClass)
    {
        typeName = name;
        argumentClass = (Class<ICmdLineArg<?>>) argClass;
    }

    /**
     * <p>
     * argumentInstance.
     * </p>
     *
     * @param commandPrefix a char.
     * @param keychar a char.
     * @param keyword a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException if any.
     */
    public ICmdLineArg<?> argumentInstance(final char commandPrefix, final char keychar, final String keyword)
            throws ParseException
    {
        try
        {
            final ICmdLineArg<?> arg = argumentClass.newInstance();

            if (keychar != commandPrefix)
                arg.setKeychar(keychar);
            if (keyword != null)
                arg.setKeyword(keyword);
            arg.setType(this);

            return arg;

        } catch (final Exception e)
        {
            throw new ParseException(name() + ": " + e.getMessage(), -1);
        }
    }

    /**
     * <p>
     * Getter for the field <code>typeName</code>.
     * </p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getTypeName()
    {
        return typeName;
    }

}
