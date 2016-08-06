package com.obdobion.argument;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class RegexTest
{

    @Arg
    Pattern          regex;

    @Arg(longName = "regexB", caseSensitive = true)
    @Arg
    public Pattern[] regexA;

    public RegexTest()
    {

    }

    @Test
    public void invalidRegex() throws Exception
    {
        try
        {
            CmdLine.load(this, "--regex'[+'");
            Assert.fail("should have been an Exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("Unclosed character class", e.getMessage().substring(0, 24));
        }
    }

    @Test
    public void validRegex() throws Exception
    {
        CmdLine.load(this, "--regex '[0-9]+'");

        Assert.assertFalse(regex.matcher("grabNoNumbers").find());
        final Matcher matcher = regex.matcher("grab54numbers61Only");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("54", matcher.group(0));
    }

    @Test
    public void validRegexArrayCaseDoesNotMatter() throws Exception
    {
        CmdLine.load(this, "--regexa '[0-9]+' '[a-zA-Z]+'");

        Assert.assertFalse(regexA[1].matcher("123").find());
        final Matcher matcher = regexA[1].matcher("54Alpha61Only");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("Alpha", matcher.group(0));
    }

    @Test
    public void validRegexArrayCaseMatters() throws Exception
    {
        CmdLine.load(this, "--regexb '[0-9]+''[a-z]+'");

        Assert.assertFalse(regexA[1].matcher("123").find());
        final Matcher matcher = regexA[1].matcher("54Alpha61Only");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("lpha", matcher.group(0));
    }

    @Test
    public void validRegexArrayCaseMattersA() throws Exception
    {
        CmdLine.load(this, "--regexb '[0-9]+''[a-zA-Z]+'");

        Assert.assertFalse(regexA[1].matcher("123").find());
        final Matcher matcher = regexA[1].matcher("54Alpha61Only");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("Alpha", matcher.group(0));
    }
}
