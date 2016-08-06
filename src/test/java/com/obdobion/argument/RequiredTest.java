package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class RequiredTest
{
    @Arg(required = true)
    int maxUpdates = 0;

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
                    "missing required parameters: \"integer --maxUpdates\" ",
                    e.getMessage());
        }
    }
}
