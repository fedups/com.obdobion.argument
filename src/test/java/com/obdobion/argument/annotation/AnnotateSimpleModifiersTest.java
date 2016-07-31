package com.obdobion.argument.annotation;

import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

public class AnnotateSimpleModifiersTest
{
    @Arg(longName = "name", shortName = 'n', help = "Some help for the wayward user")
    private String   nameOverride;

    @Arg(required = true, shortName = 'r')
    private boolean  isrequired;

    @Arg(multimin = 2)
    private String[] atLeastTwo;

    @Arg(multimax = 2)
    private String[] atMostTwo;

    @Arg(allowCamelCaps = true, allowMetaphone = true)
    private String   allowAlternateNaming;

    @Arg(caseSensitive = true)
    private String   caseSensitive;

    @Arg(positional = true, caseSensitive = true)
    private String   positional;

    @Arg(format = "mm/DD/yyyy")
    private Date     formatted;

    @Test
    public void testAlternateNaming() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testAlternateNaming", "", '-', '!');
        IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "-r --allowAlternateNaming regular" });
        cmdParser.parse(userInput, this);

        userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "-r --AAN camelCaps" });
        cmdParser.parse(userInput, this);

        userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "-r --alowAlturnateNames metaphone" });
        cmdParser.parse(userInput, this);
    }

    @Test
    public void testAtLeast2() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testAtLeast2", "", '-', '!');
        IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "-r --atLeastTwo one two" });
        cmdParser.parse(userInput, this);

        try
        {
            userInput = CommandLineParser.getInstance('-', new String[] { "-r --atLeastTwo oneOnly" });
            cmdParser.parse(userInput, this);
            Assert.fail("expected an exception");

        } catch (final ParseException e)
        {
            Assert.assertEquals("insufficient required values for string --atLeastTwo", e.getMessage());
        }
    }

    @Test
    public void testAtMost2() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testAtMost2", "", '-', '!');
        IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "-r --atMostTwo one" });
        cmdParser.parse(userInput, this);

        userInput = CommandLineParser.getInstance('-', new String[] { "-r --atMostTwo one two" });
        cmdParser.parse(userInput, this);

        userInput = CommandLineParser.getInstance('-', new String[] { "-r --atMostTwo one two three" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals(2, atMostTwo.length);
        Assert.assertEquals("three", positional);
    }

    @Test
    public void testCaseSensitive() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testCaseSensitive", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "-r --CaseSensitive AbCdE" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("AbCdE", caseSensitive);
    }

    @Test
    public void testDateFormatting() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testDateFormatting", "", '-', '!');
        IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "-r --formatted '07/28/2016'" });
        cmdParser.parse(userInput, this);

        try
        {
            userInput = CommandLineParser.getInstance('-', new String[] { "-r --formatted '2016'" });
            cmdParser.parse(userInput, this);
            Assert.fail("expected an exception");

        } catch (final ParseException e)
        {
            Assert.assertEquals("date --formatted mm/DD/yyyy: Unparseable date: \"2016\"", e.getMessage());
        }
    }

    @Test
    public void testLongName() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testLongName", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--name abc --isRequired" });
        cmdParser.parse(userInput, this);

        Assert.assertEquals("abc", nameOverride);
    }

    @Test
    public void testPostional() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testPostional", "", '-', '!');
        IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "-r myPositionalValue" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("myPositionalValue", positional);

        try
        {
            userInput = CommandLineParser.getInstance('-', new String[] { "-r --positional shouldFail" });
            cmdParser.parse(userInput, this);
            Assert.fail("expected an exception");

        } catch (final ParseException e)
        {
            Assert.assertEquals("unexpected input: --positional ", e.getMessage());
        }
    }

    @Test
    public void testShortName() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testShortName", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "-n abc -r" });
        cmdParser.parse(userInput, this);

        Assert.assertEquals("abc", nameOverride);
    }

}
