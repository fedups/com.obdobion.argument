package com.obdobion.argument.annotation;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

public class AnnotateRegexCriteriaTest
{
    @Arg(matches = "^[A-Z]+$", caseSensitive = true)
    private String stringForRegex;

    @Test
    public void regex() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("regex", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--s ABC" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("ABC", stringForRegex);
    }

    @Test
    public void regexNot() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("regex", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--s AzC" });
        try
        {
            cmdParser.parse(userInput, this);
            Assert.fail("expected exception");

        } catch (final ParseException e)
        {
            Assert.assertEquals("AzC is not valid for string --stringForRegex", e.getMessage());
        }
    }
}
