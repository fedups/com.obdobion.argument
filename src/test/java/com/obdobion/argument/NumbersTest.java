package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>NumbersTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class NumbersTest
{
    @Arg(shortName = 'a')
    int anInt;

    @Arg
    int i100;

    @Arg(longName = "i100-23")
    int i10023;

    @Arg(shortName = 'c', longName = "bee-name")
    int beeName;

    /**
     * <p>negativeIntegers.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void negativeIntegers() throws Exception
    {
        CmdLine.load(this, "-a -1 --bee-name -2");
        Assert.assertEquals(-1, anInt);
        Assert.assertEquals(-2, beeName);
    }

    /**
     * <p>negativeIntegersViaKeyCharEqualSign.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void negativeIntegersViaKeyCharEqualSign() throws Exception
    {
        CmdLine.load(this, "-a=-100");
        Assert.assertEquals(-100, anInt);
    }

    /**
     * <p>negativeIntegersViaKeyCharNoDelim.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void negativeIntegersViaKeyCharNoDelim() throws Exception
    {
        CmdLine.load(this, "-a-100");
        Assert.assertEquals(-100, anInt);
    }

    /**
     * <p>negativeIntegersViaKeyWord.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void negativeIntegersViaKeyWord() throws Exception
    {
        CmdLine.load(this, "--i100 -23");
        Assert.assertEquals(-23, i100);
    }

    /**
     * <p>negativeIntegersViaKeyWordWithEmbeddedDash.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void negativeIntegersViaKeyWordWithEmbeddedDash() throws Exception
    {
        CmdLine.load(this, "--i100-23 -45");
        Assert.assertEquals(-45, i10023);
    }

    /**
     * <p>negativeIntegersViaKeyWordWithEmbeddedDashShortened.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void negativeIntegersViaKeyWordWithEmbeddedDashShortened() throws Exception
    {
        CmdLine.load(this, "--i100- -45");
        Assert.assertEquals(-45, i10023);
    }

    /**
     * <p>positiveIntegers.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void positiveIntegers() throws Exception
    {
        CmdLine.load(this, "-a 1 --bee-name 2");
        Assert.assertEquals(1, anInt);
        Assert.assertEquals(2, beeName);
    }

    /**
     * <p>positiveIntegersNoSpace.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void positiveIntegersNoSpace() throws Exception
    {
        CmdLine.load(this, "-a1 --bee-name 2");
        Assert.assertEquals(1, anInt);
        Assert.assertEquals(2, beeName);
    }

}
