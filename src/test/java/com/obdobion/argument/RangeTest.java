package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>
 * RangeTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class RangeTest
{
    @Arg(range = { "0" })
    float[]  f1;

    @Arg(range = { "0", "1000" })
    int[]    i1;

    @Arg(range = { "b", "famish" })
    String[] string;

    /**
     * <p>
     * floatRange1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void floatRange1() throws Exception
    {
        CmdLine.load(this, "--f1 0 1 9 0.1 999.999999");
    }

    /**
     * <p>
     * floatRange2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void floatRange2() throws Exception
    {
        try
        {
            CmdLine.load(this, "--f1 0 '-1' 999");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("-1.0 is not valid for --f1", e.getMessage());
        }
    }

    /**
     * <p>
     * floatRange3.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void floatRange3() throws Exception
    {
        try
        {
            CmdLine.load(this, "--f1 0 '-0.00001' 999.0");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("-1.0E-5 is not valid for --f1", e.getMessage());
        }

    }

    /**
     * <p>
     * integerRange1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void integerRange1() throws Exception
    {
        CmdLine.load(this, "--i1 0 1 9");
    }

    /**
     * <p>
     * integerRange2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void integerRange2() throws Exception
    {
        try
        {
            CmdLine.load(this, "--i1 0 '-1' 999");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("-1 is not valid for --i1", e.getMessage());
        }

    }

    /**
     * <p>
     * integerRange3.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void integerRange3() throws Exception
    {
        CmdLine.load(this, "--i1 0 1 999");
    }

    /**
     * <p>
     * integerRange4.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void integerRange4() throws Exception
    {
        try
        {
            CmdLine.load(this, "--i1 0 '-1' 999");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("-1 is not valid for --i1", e.getMessage());
        }
    }

    /**
     * <p>
     * integerRange5.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void integerRange5() throws Exception
    {
        try
        {
            CmdLine.load(this, "--i1 0 1 1000 1001");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("1001 is not valid for --i1", e.getMessage());
        }

    }

    /**
     * <p>
     * stringRange1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void stringRange1() throws Exception
    {
        CmdLine.load(this, "--string b c");
    }

    /**
     * <p>
     * stringRange2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void stringRange2() throws Exception
    {
        CmdLine.load(this, "--string binary c");
    }

    /**
     * <p>
     * stringRange3.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void stringRange3() throws Exception
    {
        try
        {
            CmdLine.load(this, "--string  b c android");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("android is not valid for --string", e.getMessage());
        }
    }

    /**
     * <p>
     * stringRange4.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void stringRange4() throws Exception
    {
        CmdLine.load(this, "--string  b c");
    }

    /**
     * <p>
     * stringRange5.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void stringRange5() throws Exception
    {
        try
        {
            CmdLine.load(this, "--string g b c");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("g is not valid for --string", e.getMessage());
        }
    }

    /**
     * <p>
     * stringRange6.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void stringRange6() throws Exception
    {
        try
        {
            CmdLine.load(this, "--string a b c");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("a is not valid for --string", e.getMessage());
        }
    }

    /**
     * <p>
     * stringRange7.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void stringRange7() throws Exception
    {
        CmdLine.load(this, "--string famish");
    }

    /**
     * <p>
     * stringRange8.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void stringRange8() throws Exception
    {
        try
        {
            CmdLine.load(this, "--string famished");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("famished is not valid for --string", e.getMessage());
        }

    }
}
