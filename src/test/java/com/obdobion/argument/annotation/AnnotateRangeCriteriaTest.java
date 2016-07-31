package com.obdobion.argument.annotation;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

public class AnnotateRangeCriteriaTest
{
    @Arg(range = { "5", "15" })
    private int     rangedPrimitive;

    @Arg(range = { "5", "15" })
    private Integer rangedInstance;

    @Arg(range = { "5" })
    private int     minOnly;

    @Test
    public void minOnly() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("allowDefaults", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--min 5" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals(5, minOnly);
    }

    @Test
    public void minOnlyError() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("allowDefaults", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--min 3" });
        try
        {
            cmdParser.parse(userInput, this);
            Assert.fail("expected exception");
        } catch (final ParseException e)
        {
            Assert.assertEquals("3 is not valid for integer --minOnly", e.getMessage());
        }
    }

    @Test
    public void unitializeInstance() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("unitializeInstance", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] {});
        cmdParser.parse(userInput, this);
        Assert.assertNull(rangedInstance);
    }

    @Test
    public void unitializePrimative() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("allowDefaults", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] {});
        cmdParser.parse(userInput, this);
        Assert.assertEquals(0, rangedPrimitive);
    }

}
