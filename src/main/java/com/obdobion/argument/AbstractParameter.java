package com.obdobion.argument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public abstract class AbstractParameter
{
    private ICmdLine parser;

    public void load (
        final File dashedParameterFile)
        throws IOException,
            ParseException
    {
        if (parser == null)
            parser = new CmdLine().compile(parserDefinition());
        parser.reset();
        reset();
        parser.parse(CommandLineParser.getInstance('-', dashedParameterFile), this);
    }

    public void load (
        final String[] dashedCommandLineArgs)
        throws IOException,
            ParseException
    {
        if (parser == null)
            parser = new CmdLine().compile(parserDefinition());
        parser.reset();
        reset();
        parser.parse(CommandLineParser.getInstance('-', dashedCommandLineArgs), this);
    }

    public String parametersAsString ()
    {
        final StringBuilder str = new StringBuilder();
        parser.exportCommandLine(str);
        return str.toString();
    }

    abstract protected String[] parserDefinition ();

    abstract protected void reset ();
}
