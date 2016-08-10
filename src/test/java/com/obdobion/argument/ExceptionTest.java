package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>ExceptionTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class ExceptionTest
{
    /**
     * <p>badCharCommand.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void badCharCommand() throws Exception
    {
        try
        {
            CmdLine.load(this, "-v");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("unexpected input: -v ", e.getMessage());
        }
    }

    /**
     * <p>badWordCommand.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void badWordCommand() throws Exception
    {
        try
        {
            CmdLine.load(this, "--version");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("unexpected input: --version ", e.getMessage());
        }
    }

    /**
     * <p>missingRightBracket.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void missingRightBracket() throws Exception
    {
        try
        {
            CmdLine.load(this, "-g(-a)(-b");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("Unmatched bracket", e.getMessage());
        }
    }

    /**
     * <p>tooManyRightBracket.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void tooManyRightBracket() throws Exception
    {
        try
        {
            CmdLine.load(this, "-g(-a))(-b");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("unexpected input: -g ( -a ) ) ( -b ", e.getMessage());
        }
    }
}
