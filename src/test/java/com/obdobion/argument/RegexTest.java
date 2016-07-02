package com.obdobion.argument;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class RegexTest
{

    public Pattern   regex;

    public Pattern[] regexA;

    public RegexTest()
    {

    }

    @Test
    public void invalidRegex () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t regex -k r --var regex");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'[+'"), this);
            Assert.fail("should have been an Exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("regex is not valid for string --type(-t)", e.getMessage());
        }
    }

    @Test
    public void validRegex () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t pattern -k r --var regex");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'[0-9]+'"), this);

        Assert.assertFalse(regex.matcher("grabNoNumbers").find());
        final Matcher matcher = regex.matcher("grab54numbers61Only");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("54", matcher.group(0));
    }

    @Test
    public void validRegexArrayCaseDoesNotMatter () throws Exception
    {

        regexA = null;
        final CmdLine cl = new CmdLine();
        cl.compile("-t pattern -k r --var regexA -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r '[0-9]+' '[a-zA-Z]+' "), this);

        Assert.assertFalse(regexA[1].matcher("123").find());
        final Matcher matcher = regexA[1].matcher("54Alpha61Only");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("Alpha", matcher.group(0));
    }

    @Test
    public void validRegexArrayCaseMatters () throws Exception
    {

        regexA = null;
        final CmdLine cl = new CmdLine();
        cl.compile("-t pattern -k r --var regexA -m1 --case");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'[0-9]+''[a-z]+'"), this);

        Assert.assertFalse(regexA[1].matcher("123").find());
        final Matcher matcher = regexA[1].matcher("54Alpha61Only");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("lpha", matcher.group(0));
    }

    @Test
    public void validRegexArrayCaseMattersA () throws Exception
    {

        regexA = null;
        final CmdLine cl = new CmdLine();
        cl.compile("-t pattern -k r --var regexA -m1 --case");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'[0-9]+''[a-zA-Z]+'"), this);

        Assert.assertFalse(regexA[1].matcher("123").find());
        final Matcher matcher = regexA[1].matcher("54Alpha61Only");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("Alpha", matcher.group(0));
    }
}
