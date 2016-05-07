package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class MultipleTest
{

    public MultipleTest()
    {
    }

    @Test
    public void minOnlyFail () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t string -ki -m3");

        try
        {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i s"));
            Assert.fail("error should have occurred");

        } catch (ParseException e)
        {
            Assert.assertEquals("insufficient required values for -i", e.getMessage());
        }
    }

    @Test
    public void minOnlyFailGroup () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t begin -kg -m3",
            "-t string -ki",
            "-t end -kg");

        try
        {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g( -i ONE )( -i TWO )"));
            Assert.fail("error should have occurred");

        } catch (ParseException e)
        {
            Assert.assertEquals("insufficient required values for -g", e.getMessage());
        }
    }

    @Test
    public void oneOnlyFailGroup () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t begin -kg",
            "-t string -ki",
            "-t end -kg");

        try
        {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g( -i s )( -i s )"));
            Assert.fail("error should have occurred");

        } catch (ParseException e)
        {
            Assert.assertEquals("multiple values not allowed for -g", e.getMessage());
        }
    }
}
