package com.obdobion.argument.annotation;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

/**
 * <p>AnnotateRegexCriteriaTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class AnnotateRegexCriteriaTest
{
    @Arg(matches = "^[A-Z]+$", caseSensitive = true)
    private String stringForRegex;

    /**
     * <p>regex.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void regex() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("regex", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--s ABC" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("ABC", stringForRegex);
    }

    /**
     * <p>regexNot.</p>
     *
     * @throws java.lang.Exception if any.
     */
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
