package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class EquTest
{
    public Equ   equ;
    public Equ[] equA;

    public EquTest()
    {

    }

    @Test
    public void parensEquationsWithDot () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t equ -k e --var equ");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-e'matches(\"a(123)b\", \"a.([0-9]+).b\")'"), this);
        Assert.assertEquals("123", equ.evaluate());
    }

    @Test
    public void parensEquationsWithEscape () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t equ -k e --var equ");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-e'matches(\"a(123)b\", \"a\\\\(([0-9]+)\\\\)b\")'"), this);
        Assert.assertEquals("123", equ.evaluate());
    }

    @Test
    public void parensEquationsWithSet () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t equ -k e --var equ");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-e'matches(\"a(123)b\", \"a[(]([0-9]+)[)]b\")'"), this);
        Assert.assertEquals("123", equ.evaluate());
    }

    @Test
    public void quotedEquation () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t equ -k e --var equ");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-e'matches(\"abc123def\", \"[0-9]+\")'"), this);
        Assert.assertEquals("123", equ.evaluate());
    }

    @Test
    public void simpleEquation () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t equ -k e --var equ");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-e'2+3'"), this);
        Assert.assertEquals(5.0, (Double) equ.evaluate(), 0D);
    }
}
