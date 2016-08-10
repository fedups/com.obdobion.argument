package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.annotation.Arg;

/**
 * <p>EquTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class EquTest
{
    @Arg(shortName = 'e')
    public Equ equ;

    /**
     * <p>parensEquationsWithDot.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void parensEquationsWithDot() throws Exception
    {
        CmdLine.load(this, "-e'matches(\"a(123)b\", \"a.([0-9]+).b\")'");
        Assert.assertEquals("123", equ.evaluate());
    }

    /**
     * <p>parensEquationsWithEscape.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void parensEquationsWithEscape() throws Exception
    {
        CmdLine.load(this, "-e'matches(\"a(123)b\", \"a\\(([0-9]+)\\)b\")'");
        Assert.assertEquals("123", equ.evaluate());
    }

    /**
     * <p>parensEquationsWithSet.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void parensEquationsWithSet() throws Exception
    {
        CmdLine.load(this, "-e'matches(\"a(123)b\", \"a[(]([0-9]+)[)]b\")'");
        Assert.assertEquals("123", equ.evaluate());
    }

    /**
     * <p>quotedEquation.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void quotedEquation() throws Exception
    {
        CmdLine.load(this, "-e'matches(\"abc123def\", \"[0-9]+\")'");
        Assert.assertEquals("123", equ.evaluate());
    }

    /**
     * <p>simpleEquation.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void simpleEquation() throws Exception
    {
        CmdLine.load(this, "-e'2+3'");
        Assert.assertEquals(5.0, (Double) equ.evaluate(), 0D);
    }
}
