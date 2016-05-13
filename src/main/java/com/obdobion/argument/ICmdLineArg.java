package com.obdobion.argument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Chris DeGreef
 */
public interface ICmdLineArg<E>
{
    void applyDefaults ();

    File[] getValueAsFileArray () throws ParseException;

    byte[] getValueAsbyteArray () throws ParseException;

    Byte[] getValueAsByteArray () throws ParseException;

    Date[] getValueAsDateArray () throws ParseException;

    Pattern[] getValueAsPatternArray () throws ParseException;

    Pattern getValueAsPattern () throws ParseException;

    int[] getValueAsintArray () throws ParseException;

    Integer[] getValueAsIntegerArray () throws ParseException;

    Long[] getValueAsLongArray () throws ParseException;

    float[] getValueAsfloatArray () throws ParseException;

    Float[] getValueAsFloatArray () throws ParseException;

    String[] getValueAsStringArray () throws ParseException;

    Object asEnum (String name, Object[] possibleConstants) throws ParseException;

    ICmdLineArg<E> clone () throws CloneNotSupportedException;

    E convert (String valueStr) throws ParseException, IOException;

    E convert (String valueStr, boolean caseSensitive, Object target) throws ParseException, IOException;

    void asDefinedType (StringBuilder sb);

    void exportCommandLine (File file) throws IOException;

    void exportCommandLine (StringBuilder str);

    void exportNamespace (File file) throws IOException;

    void exportNamespace (String prefix, StringBuilder str);

    void exportXml (String tag, File file) throws IOException;

    void exportXml (StringBuilder str);

    ICmdLineArgCriteria<?> getCriteria ();

    String getFactoryArgName ();

    String getFactoryMethodName ();

    String getInstanceClass ();

    String defaultInstanceClass ();

    String getEnumClassName ();

    String getFormat ();

    Character getKeychar ();

    String getKeyword ();

    int getMultipleMax ();

    int getMultipleMin ();

    List<E> getDefaultValues ();

    E getValue ();

    E getValue (int index);

    String getVariable ();

    boolean hasValue ();

    boolean isCaseSensitive ();

    boolean isMultiple ();

    boolean isParsed ();

    boolean isPositional ();

    boolean isRequired ();

    boolean isRequiredValue ();

    void reset ();

    int salience (Token word);

    ICmdLineArg<E> setCaseSensitive (boolean bool) throws ParseException;

    ICmdLineArg<E> setDefaultValue (String defaultValue) throws ParseException, IOException;

    ICmdLineArg<E> setEnumCriteria (String enumClassName) throws ParseException, IOException;

    ICmdLineArg<E> setEnumCriteriaAllowError (String enumClassName);

    ICmdLineArg<E> setFactoryArgName (String argName) throws ParseException;

    ICmdLineArg<E> setFactoryMethodName (String methodName) throws ParseException;

    ICmdLineArg<E> setFormat (String format) throws ParseException;

    ICmdLineArg<E> setHelp (String p_helpString);

    ICmdLineArg<E> setInstanceClass (String p_instanceClassString);

    ICmdLineArg<E> setListCriteria (String[] values) throws ParseException, IOException;

    ICmdLineArg<E> setMultiple (boolean bool) throws ParseException;

    ICmdLineArg<E> setMultiple (int min) throws ParseException;

    ICmdLineArg<E> setMultiple (int min, int max) throws ParseException;

    ICmdLineArg<E> setParsed (boolean bool);

    ICmdLineArg<E> setPositional (boolean bool) throws ParseException;

    ICmdLineArg<E> setRangeCriteria (String min, String max) throws ParseException, IOException;

    ICmdLineArg<E> setRegxCriteria (String pattern) throws ParseException;

    ICmdLineArg<E> setRequired (boolean bool) throws ParseException;

    ICmdLineArg<E> setRequiredValue (boolean bool) throws ParseException;

    void setValue (E value);

    void setValue (int index, E value);

    ICmdLineArg<E> setVariable (String p_variableString);

    int size ();

    public void update (E value);

    public void update (int index, E value);

    void useDefaults ();
}