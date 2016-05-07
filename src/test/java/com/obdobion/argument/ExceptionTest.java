package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class ExceptionTest
{

    public ExceptionTest() throws Exception
    {

    }

    @Test
    public void badCharCommand () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t Boolean -ks test ");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-v"));
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("unexpected input: -v ", e.getMessage());
        }
    }

    @Test
    public void badWordCommand () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t Boolean -ks test ");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--version"));
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("unexpected input: --version ", e.getMessage());
        }
    }

    @Test
    public void missingRightBracket () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-tbegin-kg-m1", "-tboolean-ka", "-tboolean-kb", "-tend-kg");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g(-a)(-b"));
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("Unmatched bracket", e.getMessage());
        }
    }

    @Test
    public void tooManyRightBracket () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-tbegin-kg-m1", "-tboolean-ka", "-tboolean-kb", "-tend-kg");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g(-a))(-b"));
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("Unmatched bracket", e.getMessage());
        }
    }

}
