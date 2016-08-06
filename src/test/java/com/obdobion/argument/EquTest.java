package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class EquTest
{
    @Arg(shortName = 'e')
    public Equ equ;

    @Test
    public void parensEquationsWithDot() throws Exception
    {
        CmdLine.load(this, "-e'matches(\"a(123)b\", \"a.([0-9]+).b\")'");
        Assert.assertEquals("123", equ.evaluate());
    }

    @Test
    public void parensEquationsWithEscape() throws Exception
    {
        CmdLine.load(this, "-e'matches(\"a(123)b\", \"a\\(([0-9]+)\\)b\")'");
        Assert.assertEquals("123", equ.evaluate());
    }

    @Test
    public void parensEquationsWithSet() throws Exception
    {
        CmdLine.load(this, "-e'matches(\"a(123)b\", \"a[(]([0-9]+)[)]b\")'");
        Assert.assertEquals("123", equ.evaluate());
    }

    @Test
    public void quotedEquation() throws Exception
    {
        CmdLine.load(this, "-e'matches(\"abc123def\", \"[0-9]+\")'");
        Assert.assertEquals("123", equ.evaluate());
    }

    @Test
    public void simpleEquation() throws Exception
    {
        CmdLine.load(this, "-e'2+3'");
        Assert.assertEquals(5.0, (Double) equ.evaluate(), 0D);
    }
}
