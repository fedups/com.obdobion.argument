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

    @Test
    public void inList() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("inList", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--s ABC" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("abc", stringForInList);
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
}
