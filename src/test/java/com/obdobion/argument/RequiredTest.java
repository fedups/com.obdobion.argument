package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>
 * RequiredTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class RequiredTest
{
    @Arg(required = true)
    int maxUpdates = 0;

    /**
     * <p>
     * requireInteger.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     */
    @Test
    public void requireInteger() throws Exception
    {
        try
        {
            CmdLine.load(this, "");
            Assert.fail("should have failed");
        } catch (final Exception e)
        {
            Assert.assertEquals(
                    "missing required parameters: --maxUpdates ",
                    e.getMessage());
        }
    }
}
