package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class RangeTest
{
    @Arg(range = { "0" })
    float[]  f1;

    @Arg(range = { "0", "1000" })
    int[]    i1;

    @Arg(range = { "b", "famish" })
    String[] string;

    @Test
    public void floatRange1() throws Exception
    {
        CmdLine.load(this, "--f1 0 1 9 0.1 999.999999");
    }

    @Test
    public void floatRange2() throws Exception
    {
        try
        {
            CmdLine.load(this, "--f1 0 '-1' 999");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("-1.0 is not valid for float --f1", e.getMessage());
        }
    }

    @Test
    public void floatRange3() throws Exception
    {
        try
        {
            CmdLine.load(this, "--f1 0 '-0.00001' 999.0");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("-1.0E-5 is not valid for float --f1", e.getMessage());
        }

    }

    @Test
    public void integerRange1() throws Exception
    {
        CmdLine.load(this, "--i1 0 1 9");
    }

    @Test
    public void integerRange2() throws Exception
    {
        try
        {
            CmdLine.load(this, "--i1 0 '-1' 999");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("-1 is not valid for integer --i1", e.getMessage());
        }

    }

    @Test
    public void integerRange3() throws Exception
    {
        CmdLine.load(this, "--i1 0 1 999");
    }

    @Test
    public void integerRange4() throws Exception
    {
        try
        {
            CmdLine.load(this, "--i1 0 '-1' 999");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("-1 is not valid for integer --i1", e.getMessage());
        }
    }

    @Test
    public void integerRange5() throws Exception
    {
        try
        {
            CmdLine.load(this, "--i1 0 1 1000 1001");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("1001 is not valid for integer --i1", e.getMessage());
        }

    }

    @Test
    public void stringRange1() throws Exception
    {
        CmdLine.load(this, "--string b c");
    }

    @Test
    public void stringRange2() throws Exception
    {
        CmdLine.load(this, "--string binary c");
    }

    @Test
    public void stringRange3() throws Exception
    {
        try
        {
            CmdLine.load(this, "--string  b c android");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("android is not valid for string --string", e.getMessage());
        }
    }

    @Test
    public void stringRange4() throws Exception
    {
        CmdLine.load(this, "--string  b c");
    }

    @Test
    public void stringRange5() throws Exception
    {
        try
        {
            CmdLine.load(this, "--string g b c");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("g is not valid for string --string", e.getMessage());
        }
    }

    @Test
    public void stringRange6() throws Exception
    {
        try
        {
            CmdLine.load(this, "--string a b c");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("a is not valid for string --string", e.getMessage());
        }
    }

    @Test
    public void stringRange7() throws Exception
    {
        CmdLine.load(this, "--string famish");
    }

    @Test
    public void stringRange8() throws Exception
    {
        try
        {
            CmdLine.load(this, "--string famished");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("famished is not valid for string --string", e.getMessage());
        }

    }
}
