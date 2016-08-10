package com.obdobion.argument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.obdobion.argument.input.IParserInput;
import com.obdobion.argument.type.ICmdLineArg;

/**
 * <p>ICmdLine interface.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public interface ICmdLine extends Comparable<ICmdLine>, ICmdLineArg<ICmdLine>
{
    /**
     * <p>add.</p>
     *
     * @param arg a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    void add(ICmdLineArg<?> arg);

    /**
     * <p>add.</p>
     *
     * @param index a int.
     * @param arg a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    void add(int index, ICmdLineArg<?> arg);

    /**
     * <p>addDefaultIncludeDirectory.</p>
     *
     * @param directory a {@link java.io.File} object.
     */
    void addDefaultIncludeDirectory(File directory);

    /**
     * <p>allArgs.</p>
     *
     * @return a {@link java.util.List} object.
     */
    List<ICmdLineArg<?>> allArgs();

    /**
     * <p>arg.</p>
     *
     * @param commandToken a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    ICmdLineArg<?> arg(String commandToken);

    /**
     * <p>argForVariableName.</p>
     *
     * @param variableName a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @throws java.text.ParseException if any.
     */
    public ICmdLineArg<?> argForVariableName(final String variableName) throws ParseException;

    /**
     * <p>assignVariables.</p>
     *
     * @param target a {@link java.lang.Object} object.
     * @throws java.text.ParseException if any.
     */
    void assignVariables(Object target) throws ParseException;

    /** {@inheritDoc} */
    @Override
    ICmdLine clone() throws CloneNotSupportedException;

    /**
     * <p>getCommandPrefix.</p>
     *
     * @return a char.
     */
    public char getCommandPrefix();

    /**
     * <p>getName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getName();

    /**
     * <p>getParseExceptions.</p>
     *
     * @return a {@link java.util.List} object.
     */
    List<ParseException> getParseExceptions();

    /**
     * <p>indexOf.</p>
     *
     * @param arg a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @return a int.
     */
    int indexOf(ICmdLineArg<?> arg);

    /**
     * <p>parse.</p>
     *
     * @param cmd a {@link com.obdobion.argument.input.IParserInput} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.io.IOException if any.
     * @throws java.text.ParseException if any.
     */
    Object parse(IParserInput cmd) throws IOException, ParseException;

    /**
     * <p>parse.</p>
     *
     * @param cmd a {@link com.obdobion.argument.input.IParserInput} object.
     * @param target a {@link java.lang.Object} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.io.IOException if any.
     * @throws java.text.ParseException if any.
     */
    Object parse(IParserInput cmd, Object target) throws IOException, ParseException;

    /**
     * <p>parse.</p>
     *
     * @param target a {@link java.lang.Object} object.
     * @param args a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.io.IOException if any.
     * @throws java.text.ParseException if any.
     */
    Object parse(Object target, String... args) throws IOException, ParseException;

    /**
     * <p>parse.</p>
     *
     * @param args a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.io.IOException if any.
     * @throws java.text.ParseException if any.
     */
    Object parse(String... args) throws IOException, ParseException;

    /**
     * <p>pull.</p>
     *
     * @param variableSource a {@link java.lang.Object} object.
     * @throws java.text.ParseException if any.
     */
    void pull(Object variableSource) throws ParseException;

    /**
     * <p>remove.</p>
     *
     * @param arg a {@link com.obdobion.argument.type.ICmdLineArg} object.
     */
    void remove(ICmdLineArg<?> arg);

    /**
     * <p>remove.</p>
     *
     * @param argIndex a int.
     */
    void remove(int argIndex);

    /** {@inheritDoc} */
    @Override
    int size();
}
