package com.obdobion.argument.annotation;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

public class AnnotateInListCriteriaTest
{
    @Arg(inList = { "abc", "def" })
    private String stringForInList;

    @Arg(inList = { "1", "3", "5" })
    private int    intForInList;

    @Test
    public void inList() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("inList", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--s ABC" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("abc", stringForInList);
    }

    @Test
    public void inListInt() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine(null);
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--i 3" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals(3, intForInList);
    }

    @Test
    public void notInList() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("notInList", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--s AzC" });
        try
        {
            cmdParser.parse(userInput, this);
            Assert.fail("expected exception");

        } catch (final ParseException e)
        {
            Assert.assertEquals("azc is not valid for string --stringForInList", e.getMessage());
        }
    }

    @Test
    public void notInListInt() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine(null);
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--i 2" });
        try
        {
            cmdParser.parse(userInput, this);
            Assert.fail("expected exception");

        } catch (final ParseException e)
        {
            Assert.assertEquals("2 is not valid for integer --intForInList", e.getMessage());
        }
    }
}
