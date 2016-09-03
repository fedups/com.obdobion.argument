package com.obdobion.argument.type;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.criteria.ICmdLineArgCriteria;
import com.obdobion.argument.input.Token;

/**
 * <p>
 * ICmdLineArg interface.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public interface ICmdLineArg<E>
{
    /**
     * <p>
     * applyDefaults.
     * </p>
     */
    void applyDefaults();

    /**
     * <p>
     * asDefinedType.
     * </p>
     *
     * @param sb
     *            a {@link java.lang.StringBuilder} object.
     */
    void asDefinedType(StringBuilder sb);

    /**
     * <p>
     * asEnum.
     * </p>
     *
     * @param name
     *            a {@link java.lang.String} object.
     * @param possibleConstants
     *            an array of {@link java.lang.Object} objects.
     * @return a {@link java.lang.Object} object.
     * @throws java.text.ParseException
     *             if any.
     */
    Object asEnum(String name, Object[] possibleConstants) throws ParseException;

    /**
     * <p>
     * asEnumArray.
     * </p>
     *
     * @param name
     *            a {@link java.lang.String} object.
     * @param possibleConstants
     *            an array of {@link java.lang.Object} objects.
     * @return an array of {@link java.lang.Object} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Object[] asEnumArray(String name, Object[] possibleConstants) throws ParseException;

    /**
     * <p>
     * clone.
     * </p>
     *
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.lang.CloneNotSupportedException
     *             if any.
     */
    ICmdLineArg<E> clone() throws CloneNotSupportedException;

    /**
     * <p>
     * convert.
     * </p>
     *
     * @param valueStr
     *            a {@link java.lang.String} object.
     * @return a E object.
     * @throws java.text.ParseException
     *             if any.
     * @throws java.io.IOException
     *             if any.
     */
    E convert(String valueStr) throws ParseException, IOException;

    /**
     * <p>
     * convert.
     * </p>
     *
     * @param valueStr
     *            a {@link java.lang.String} object.
     * @param caseSensitive
     *            a boolean.
     * @param target
     *            a {@link java.lang.Object} object.
     * @return a E object.
     * @throws java.text.ParseException
     *             if any.
     * @throws java.io.IOException
     *             if any.
     */
    E convert(String valueStr, boolean caseSensitive, Object target) throws ParseException, IOException;

    /**
     * <p>
     * defaultInstanceClass.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String defaultInstanceClass();

    /**
     * <p>
     * dontAllowCamelCaps.
     * </p>
     */
    void dontAllowCamelCaps();

    /**
     * <p>
     * exportCommandLine.
     * </p>
     *
     * @param file
     *            a {@link java.io.File} object.
     * @throws java.io.IOException
     *             if any.
     */
    void exportCommandLine(File file) throws IOException;

    /**
     * <p>
     * exportCommandLine.
     * </p>
     *
     * @param str
     *            a {@link java.lang.StringBuilder} object.
     */
    void exportCommandLine(StringBuilder str);

    /**
     * <p>
     * exportNamespace.
     * </p>
     *
     * @param file
     *            a {@link java.io.File} object.
     * @throws java.io.IOException
     *             if any.
     */
    void exportNamespace(File file) throws IOException;

    /**
     * <p>
     * exportNamespace.
     * </p>
     *
     * @param prefix
     *            a {@link java.lang.String} object.
     * @param str
     *            a {@link java.lang.StringBuilder} object.
     */
    void exportNamespace(String prefix, StringBuilder str);

    /**
     * <p>
     * exportXml.
     * </p>
     *
     * @param tag
     *            a {@link java.lang.String} object.
     * @param file
     *            a {@link java.io.File} object.
     * @throws java.io.IOException
     *             if any.
     */
    void exportXml(String tag, File file) throws IOException;

    /**
     * <p>
     * exportXml.
     * </p>
     *
     * @param str
     *            a {@link java.lang.StringBuilder} object.
     */
    void exportXml(StringBuilder str);

    /**
     * <p>
     * genericClassName.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String genericClassName();

    /**
     * <p>
     * getCamelCaps.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getCamelCaps();

    /**
     * <p>
     * getCriteria.
     * </p>
     *
     * @return a {@link com.obdobion.argument.criteria.ICmdLineArgCriteria}
     *         object.
     */
    ICmdLineArgCriteria<?> getCriteria();

    /**
     * <p>
     * getDefaultValues.
     * </p>
     *
     * @return a {@link java.util.List} object.
     */
    List<E> getDefaultValues();

    /**
     * <p>
     * getDelegateOrValue.
     * </p>
     *
     * @return a {@link java.lang.Object} object.
     */
    Object getDelegateOrValue();

    /**
     * <p>
     * getDelegateOrValue.
     * </p>
     *
     * @param occurrence
     *            a int.
     * @return a {@link java.lang.Object} object.
     */
    Object getDelegateOrValue(int occurrence);

    /**
     * <p>
     * getEnumClassName.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getEnumClassName();

    /**
     * <p>
     * getFactoryArgName.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getFactoryArgName();

    /**
     * <p>
     * getFactoryMethodName.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getFactoryMethodName();

    /**
     * <p>
     * getFormat.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getFormat();

    /**
     * <p>
     * getHelp.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getHelp();

    /**
     * <p>
     * getInstanceClass.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getInstanceClass();

    /**
     * <p>
     * getKeychar.
     * </p>
     *
     * @return a {@link java.lang.Character} object.
     */
    Character getKeychar();

    /**
     * <p>
     * getKeyword.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getKeyword();

    /**
     * <p>
     * getMetaphone.
     * </p>
     *
     * @return a {@link java.lang.Object} object.
     */
    Object getMetaphone();

    /**
     * <p>
     * getMultipleMax.
     * </p>
     *
     * @return a int.
     */
    int getMultipleMax();

    /**
     * <p>
     * getMultipleMin.
     * </p>
     *
     * @return a int.
     */
    int getMultipleMin();

    /**
     * <p>
     * getUniqueId.
     * </p>
     *
     * @return a int.
     */
    int getUniqueId();

    /**
     * <p>
     * getValue.
     * </p>
     *
     * @return a E object.
     */
    E getValue();

    /**
     * <p>
     * getValue.
     * </p>
     *
     * @param index
     *            a int.
     * @return a E object.
     */
    E getValue(int index);

    /**
     * <p>
     * getValueAsbyteArray.
     * </p>
     *
     * @return an array of byte.
     * @throws java.text.ParseException
     *             if any.
     */
    byte[] getValueAsbyteArray() throws ParseException;

    /**
     * <p>
     * getValueAsByteArray.
     * </p>
     *
     * @return an array of {@link java.lang.Byte} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Byte[] getValueAsByteArray() throws ParseException;

    /**
     * <p>
     * getValueAsCalendarArray.
     * </p>
     *
     * @return an array of {@link java.util.Calendar} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Calendar[] getValueAsCalendarArray() throws ParseException;

    /**
     * <p>
     * getValueAsCharacterArray.
     * </p>
     *
     * @return an array of {@link java.lang.Character} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Character[] getValueAsCharacterArray() throws ParseException;

    /**
     * <p>
     * getValueAscharArray.
     * </p>
     *
     * @return an array of char.
     * @throws java.text.ParseException
     *             if any.
     */
    char[] getValueAscharArray() throws ParseException;

    /**
     * <p>
     * getValueAsDateArray.
     * </p>
     *
     * @return an array of {@link java.util.Date} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Date[] getValueAsDateArray() throws ParseException;

    /**
     * <p>
     * getValueAsDateTimeFormatter.
     * </p>
     *
     * @return a {@link java.time.format.DateTimeFormatter} object.
     * @throws java.text.ParseException
     *             if any.
     * @since 4.3.1
     */
    DateTimeFormatter getValueAsDateTimeFormatter() throws ParseException;

    /**
     * <p>
     * getValueAsDateTimeFormatterArray.
     * </p>
     *
     * @return an array of {@link java.time.format.DateTimeFormatter} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    DateTimeFormatter[] getValueAsDateTimeFormatterArray() throws ParseException;

    /**
     * <p>
     * getValueAsdoubleArray.
     * </p>
     *
     * @return an array of double.
     * @throws java.text.ParseException
     *             if any.
     */
    double[] getValueAsdoubleArray() throws ParseException;

    /**
     * <p>
     * getValueAsDoubleArray.
     * </p>
     *
     * @return an array of {@link java.lang.Double} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Double[] getValueAsDoubleArray() throws ParseException;

    /**
     * <p>
     * getValueAsEquation.
     * </p>
     *
     * @return a {@link com.obdobion.algebrain.Equ} object.
     * @throws java.text.ParseException
     *             if any.
     */
    Equ getValueAsEquation() throws ParseException;

    /**
     * <p>
     * getValueAsEquationArray.
     * </p>
     *
     * @return an array of {@link com.obdobion.algebrain.Equ} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Equ[] getValueAsEquationArray() throws ParseException;

    /**
     * <p>
     * getValueAsFileArray.
     * </p>
     *
     * @return an array of {@link java.io.File} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    File[] getValueAsFileArray() throws ParseException;

    /**
     * <p>
     * getValueAsfloatArray.
     * </p>
     *
     * @return an array of float.
     * @throws java.text.ParseException
     *             if any.
     */
    float[] getValueAsfloatArray() throws ParseException;

    /**
     * <p>
     * getValueAsFloatArray.
     * </p>
     *
     * @return an array of {@link java.lang.Float} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Float[] getValueAsFloatArray() throws ParseException;

    /**
     * <p>
     * getValueAsintArray.
     * </p>
     *
     * @return an array of int.
     * @throws java.text.ParseException
     *             if any.
     */
    int[] getValueAsintArray() throws ParseException;

    /**
     * <p>
     * getValueAsIntegerArray.
     * </p>
     *
     * @return an array of {@link java.lang.Integer} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Integer[] getValueAsIntegerArray() throws ParseException;

    /**
     * <p>
     * getValueAsLocalDateArray.
     * </p>
     *
     * @return an array of {@link java.time.LocalDate} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    LocalDate[] getValueAsLocalDateArray() throws ParseException;

    /**
     * <p>
     * getValueAsLocalDateTimeArray.
     * </p>
     *
     * @return an array of {@link java.time.LocalDateTime} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    LocalDateTime[] getValueAsLocalDateTimeArray() throws ParseException;

    /**
     * <p>
     * getValueAsLocalTimeArray.
     * </p>
     *
     * @return an array of {@link java.time.LocalTime} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    LocalTime[] getValueAsLocalTimeArray() throws ParseException;

    /**
     * <p>
     * getValueAslongArray.
     * </p>
     *
     * @return an array of long.
     * @throws java.text.ParseException
     *             if any.
     */
    long[] getValueAslongArray() throws ParseException;

    /**
     * <p>
     * getValueAsLongArray.
     * </p>
     *
     * @return an array of {@link java.lang.Long} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Long[] getValueAsLongArray() throws ParseException;

    /**
     * <p>
     * getValueAsPattern.
     * </p>
     *
     * @return a {@link java.util.regex.Pattern} object.
     * @throws java.text.ParseException
     *             if any.
     */
    Pattern getValueAsPattern() throws ParseException;

    /**
     * <p>
     * getValueAsPatternArray.
     * </p>
     *
     * @return an array of {@link java.util.regex.Pattern} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    Pattern[] getValueAsPatternArray() throws ParseException;

    /**
     * <p>getValueAsSimpleDateFormat.</p>
     *
     * @return a {@link java.text.SimpleDateFormat} object.
     * @throws java.text.ParseException if any.
     * @since 4.3.2
     */
    SimpleDateFormat getValueAsSimpleDateFormat() throws ParseException;

    /**
     * <p>getValueAsSimpleDateFormatArray.</p>
     *
     * @return an array of {@link java.text.SimpleDateFormat} objects.
     * @throws java.text.ParseException if any.
     */
    SimpleDateFormat[] getValueAsSimpleDateFormatArray() throws ParseException;

    /**
     * <p>
     * getValueAsStringArray.
     * </p>
     *
     * @return an array of {@link java.lang.String} objects.
     * @throws java.text.ParseException
     *             if any.
     */
    String[] getValueAsStringArray() throws ParseException;

    /**
     * <p>
     * getVariable.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getVariable();

    /**
     * <p>
     * hasValue.
     * </p>
     *
     * @return a boolean.
     */
    boolean hasValue();

    /**
     * <p>
     * isCamelCapsAllowed.
     * </p>
     *
     * @return a boolean.
     */
    boolean isCamelCapsAllowed();

    /**
     * <p>
     * isCaseSensitive.
     * </p>
     *
     * @return a boolean.
     */
    boolean isCaseSensitive();

    /**
     * <p>
     * isMetaphoneAllowed.
     * </p>
     *
     * @return a boolean.
     */
    boolean isMetaphoneAllowed();

    /**
     * <p>
     * isMultiple.
     * </p>
     *
     * @return a boolean.
     */
    boolean isMultiple();

    /**
     * <p>
     * isParsed.
     * </p>
     *
     * @return a boolean.
     */
    boolean isParsed();

    /**
     * <p>
     * isPositional.
     * </p>
     *
     * @return a boolean.
     */
    boolean isPositional();

    /**
     * <p>
     * isRequired.
     * </p>
     *
     * @return a boolean.
     */
    boolean isRequired();

    /**
     * <p>
     * isRequiredValue.
     * </p>
     *
     * @return a boolean.
     */
    boolean isRequiredValue();

    /**
     * <p>
     * isSystemGenerated.
     * </p>
     *
     * @return a boolean.
     */
    boolean isSystemGenerated();

    /**
     * <p>
     * reset.
     * </p>
     */
    void reset();

    /**
     * <p>
     * resetCriteria.
     * </p>
     *
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> resetCriteria();

    /**
     * <p>
     * salience.
     * </p>
     *
     * @param word
     *            a {@link com.obdobion.argument.input.Token} object.
     * @return a int.
     */
    int salience(Token word);

    /**
     * <p>
     * setCamelCapsAllowed.
     * </p>
     *
     * @param bool
     *            a boolean.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setCamelCapsAllowed(boolean bool);

    /**
     * <p>
     * setCaseSensitive.
     * </p>
     *
     * @param bool
     *            a boolean.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setCaseSensitive(boolean bool);

    /**
     * <p>
     * setDefaultValue.
     * </p>
     *
     * @param defaultValue
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     * @throws java.io.IOException
     *             if any.
     */
    ICmdLineArg<E> setDefaultValue(String defaultValue) throws ParseException, IOException;

    /**
     * <p>
     * setEnumCriteria.
     * </p>
     *
     * @param enumClassName
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     * @throws java.io.IOException
     *             if any.
     */
    ICmdLineArg<E> setEnumCriteria(String enumClassName) throws ParseException, IOException;

    /**
     * <p>
     * setEnumCriteriaAllowError.
     * </p>
     *
     * @param enumClassName
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setEnumCriteriaAllowError(String enumClassName);

    /**
     * <p>
     * setFactoryArgName.
     * </p>
     *
     * @param argName
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setFactoryArgName(String argName);

    /**
     * <p>
     * setFactoryMethodName.
     * </p>
     *
     * @param methodName
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     */
    ICmdLineArg<E> setFactoryMethodName(String methodName) throws ParseException;

    /**
     * <p>
     * setFormat.
     * </p>
     *
     * @param format
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     */
    ICmdLineArg<E> setFormat(String format) throws ParseException;

    /**
     * <p>
     * setHelp.
     * </p>
     *
     * @param p_helpString
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setHelp(String p_helpString);

    /**
     * <p>
     * setInstanceClass.
     * </p>
     *
     * @param p_instanceClassString
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     */
    ICmdLineArg<E> setInstanceClass(String p_instanceClassString) throws ParseException;

    /**
     * <p>
     * setKeychar.
     * </p>
     *
     * @param _keychar
     *            a {@link java.lang.Character} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setKeychar(final Character _keychar);

    /**
     * <p>
     * setKeyword.
     * </p>
     *
     * @param _keyword
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setKeyword(final String _keyword);

    /**
     * <p>
     * setListCriteria.
     * </p>
     *
     * @param values
     *            an array of {@link java.lang.String} objects.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     * @throws java.io.IOException
     *             if any.
     */
    ICmdLineArg<E> setListCriteria(String[] values) throws ParseException, IOException;

    /**
     * <p>
     * setMetaphoneAllowed.
     * </p>
     *
     * @param bool
     *            a boolean.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setMetaphoneAllowed(boolean bool);

    /**
     * <p>
     * setMultiple.
     * </p>
     *
     * @param bool
     *            a boolean.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     */
    ICmdLineArg<E> setMultiple(boolean bool) throws ParseException;

    /**
     * <p>
     * setMultiple.
     * </p>
     *
     * @param min
     *            a int.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     */
    ICmdLineArg<E> setMultiple(int min) throws ParseException;

    /**
     * <p>
     * setMultiple.
     * </p>
     *
     * @param min
     *            a int.
     * @param max
     *            a int.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     */
    ICmdLineArg<E> setMultiple(int min, int max) throws ParseException;

    /**
     * <p>
     * setObject.
     * </p>
     *
     * @param value
     *            a {@link java.lang.Object} object.
     */
    void setObject(Object value);

    /**
     * <p>
     * setParsed.
     * </p>
     *
     * @param bool
     *            a boolean.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setParsed(boolean bool);;

    /**
     * <p>
     * setPositional.
     * </p>
     *
     * @param bool
     *            a boolean.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setPositional(boolean bool);

    /**
     * <p>
     * setRangeCriteria.
     * </p>
     *
     * @param min
     *            a {@link java.lang.String} object.
     * @param max
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     * @throws java.io.IOException
     *             if any.
     */
    ICmdLineArg<E> setRangeCriteria(String min, String max) throws ParseException, IOException;

    /**
     * <p>
     * setRegxCriteria.
     * </p>
     *
     * @param pattern
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     */
    ICmdLineArg<E> setRegxCriteria(String pattern) throws ParseException;;

    /**
     * <p>
     * setRequired.
     * </p>
     *
     * @param bool
     *            a boolean.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setRequired(boolean bool);

    /**
     * <p>
     * setRequiredValue.
     * </p>
     *
     * @param bool
     *            a boolean.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     */
    ICmdLineArg<E> setRequiredValue(boolean bool) throws ParseException;

    /**
     * <p>
     * setSystemGenerated.
     * </p>
     *
     * @param bool
     *            a boolean.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException
     *             if any.
     */
    ICmdLineArg<E> setSystemGenerated(boolean bool) throws ParseException;

    /**
     * <p>
     * setType.
     * </p>
     *
     * @param claType
     *            a {@link com.obdobion.argument.type.ClaType} object.
     */
    void setType(ClaType claType);

    /**
     * <p>
     * setUniqueId.
     * </p>
     *
     * @param i
     *            a int.
     */
    void setUniqueId(int i);

    /**
     * <p>
     * setValue.
     * </p>
     *
     * @param value
     *            a E object.
     */
    void setValue(E value);

    /**
     * <p>
     * setValue.
     * </p>
     *
     * @param index
     *            a int.
     * @param value
     *            a E object.
     */
    void setValue(int index, E value);

    /**
     * <p>
     * setVariable.
     * </p>
     *
     * @param p_variableString
     *            a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<E> setVariable(String p_variableString);

    /**
     * <p>
     * size.
     * </p>
     *
     * @return a int.
     */
    int size();

    /**
     * <p>
     * supportsCamelCaps.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsCamelCaps();

    /**
     * <p>
     * supportsCaseSensitive.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsCaseSensitive();

    /**
     * <p>
     * supportsDefaultValues.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsDefaultValues();

    /**
     * <p>
     * supportsExcludeArgs.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsExcludeArgs();

    /**
     * <p>
     * supportsFactoryArgName.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsFactoryArgName();

    /**
     * <p>
     * supportsFactoryMethod.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsFactoryMethod();

    /**
     * <p>
     * supportsFormat.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsFormat();

    /**
     * <p>
     * supportsHelp.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsHelp();

    /**
     * <p>
     * supportsInList.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsInList();

    /**
     * <p>
     * supportsInstanceClass.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsInstanceClass();

    /**
     * <p>
     * supportsLongName.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsLongName();

    /**
     * <p>
     * supportsMatches.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsMatches();

    /**
     * <p>
     * supportsMetaphone.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsMetaphone();

    /**
     * <p>
     * supportsMultimax.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsMultimax();

    /**
     * <p>
     * supportsMultimin.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsMultimin();

    /**
     * <p>
     * supportsPositional.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsPositional();

    /**
     * <p>
     * supportsRange.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsRange();

    /**
     * <p>
     * supportsRequired.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsRequired();

    /**
     * <p>
     * supportsShortName.
     * </p>
     *
     * @return a boolean.
     */
    boolean supportsShortName();

    /**
     * <p>
     * useDefaults.
     * </p>
     */
    void useDefaults();
}
