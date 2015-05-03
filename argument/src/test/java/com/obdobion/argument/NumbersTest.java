package com.obdobion.argument;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class NumbersTest
{
    CmdLine sharedCL;

    public NumbersTest()
    {
    }

    @Before
    public void createParser ()
    {
        sharedCL = new CmdLine();
        try
        {
            sharedCL.compile("-t Integer -k a", "-t Integer -k c 'bee-name'");

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void positiveIntegers () throws Exception
    {
        sharedCL.parse(CommandLineParser.getInstance(sharedCL.getCommandPrefix(), true, "-a 1 --bee-name 2"));
        Assert.assertEquals("1 cmd count", 2, sharedCL.size());

        IntegerCLA a = (IntegerCLA) sharedCL.arg("-a");
        Assert.assertEquals("a", 1, a.getValue().intValue());
        IntegerCLA b = (IntegerCLA) sharedCL.arg("-c");
        Assert.assertEquals("bee-name", 2, b.getValue().intValue());
    }

    @Test
    public void positiveIntegersNoSpace () throws Exception
    {
        sharedCL.parse(CommandLineParser.getInstance(sharedCL.getCommandPrefix(), true, "-a1 --bee-name 2"));
        Assert.assertEquals("1 cmd count", 2, sharedCL.size());

        IntegerCLA a = (IntegerCLA) sharedCL.arg("-a");
        Assert.assertEquals("a", 1, a.getValue().intValue());
        IntegerCLA b = (IntegerCLA) sharedCL.arg("-c");
        Assert.assertEquals("bee-name", 2, b.getValue().intValue());
    }

    @Test
    public void negativeIntegers () throws Exception
    {
        sharedCL.parse(CommandLineParser.getInstance(sharedCL.getCommandPrefix(), true, "-a -1 --bee-name -2"));
        Assert.assertEquals("1 cmd count", 2, sharedCL.size());

        IntegerCLA a = (IntegerCLA) sharedCL.arg("-a");
        Assert.assertEquals("a", -1, a.getValue().intValue());
        IntegerCLA b = (IntegerCLA) sharedCL.arg("-c");
        Assert.assertEquals("bee-name", -2, b.getValue().intValue());
    }

    @Test
    public void negativeIntegersViaKeyCharNoDelim () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t integer -ki");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i-100"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        IntegerCLA sarg = (IntegerCLA) cl.arg("-i");
        Assert.assertEquals("1i", -100, sarg.getValue().intValue());
    }

    @Test
    public void negativeIntegersViaKeyCharEqualSign () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t integer -ki");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i=-100"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        IntegerCLA sarg = (IntegerCLA) cl.arg("-i");
        Assert.assertEquals("1i", -100, sarg.getValue().intValue());
    }

    @Test
    public void negativeIntegersViaKeyWord () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t integer -ki i100");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--i100-23"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        IntegerCLA sarg = (IntegerCLA) cl.arg("-i");
        Assert.assertEquals("1i", -23, sarg.getValue().intValue());
    }

    @Test
    public void negativeIntegersViaKeyWordWithEmbeddedDash () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t integer -ki 'i100-123'");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "--i100-123 -45"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        IntegerCLA sarg = (IntegerCLA) cl.arg("-i");
        Assert.assertEquals("i", -45, sarg.getValue().intValue());
    }

    @Test
    public void negativeIntegersViaKeyWordWithEmbeddedDashShortened () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t integer -ki 'i100-123'");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "--i1 -45"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        IntegerCLA sarg = (IntegerCLA) cl.arg("-i");
        Assert.assertEquals("i", -45, sarg.getValue().intValue());
    }

}
