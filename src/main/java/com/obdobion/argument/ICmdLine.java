package com.obdobion.argument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.obdobion.argument.input.IParserInput;

/**
 * @author Chris DeGreef
 *
 */
public interface ICmdLine extends Comparable<ICmdLine>, ICmdLineArg<ICmdLine>
{
    void add(ICmdLineArg<?> arg);

    void add(int index, ICmdLineArg<?> arg);

    void addDefaultIncludeDirectory(File directory);

    List<ICmdLineArg<?>> allArgs();

    ICmdLineArg<?> arg(String commandToken);

    void assignVariables(Object target) throws ParseException;

    ICmdLine clone() throws CloneNotSupportedException;

    ICmdLine compile(List<String> definition) throws ParseException, IOException;

    ICmdLine compile(String... definition) throws ParseException, IOException;

    public char getCommandPrefix();

    String getName();

    List<ParseException> getParseExceptions();

    int indexOf(ICmdLineArg<?> arg);

    Object parse(IParserInput cmd) throws IOException, ParseException;

    Object parse(IParserInput cmd, Object target) throws IOException, ParseException;

    Object parse(Object target, String... args) throws IOException, ParseException;

    Object parse(String... args) throws IOException, ParseException;

    void remove(ICmdLineArg<?> arg);

    void remove(int argIndex);

    int size();
}