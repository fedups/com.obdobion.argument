package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>
 * MultipleTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class MultipleTest
{
    static public class StringGroup
    {
        @Arg(shortName = 'i')
        String stringArray;
    }

    @Arg(multimin = 3)
    String[]      stringArray;

    @Arg(multimin = 3)
    StringGroup[] stringGroupMulti;

    @Arg
    StringGroup   stringGroup;

    /**
     * <p>
     * minOnlyFail.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void minOnlyFail() throws Exception
    {
        try
        {
            CmdLine.load(this, "--stringArray s");
            Assert.fail("error should have occurred");

        } catch (final ParseException e)
        {
            Assert.assertEquals("insufficient required values for --stringArray", e.getMessage());
        }
    }

    /**
     * <p>
     * minOnlyFailGroup.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void minOnlyFailGroup() throws Exception
    {
        try
        {
            CmdLine.load(this, "--stringGroupMulti( -i ONE )( -i TWO )");
            Assert.fail("error should have occurred");

        } catch (final ParseException e)
        {
            Assert.assertEquals("insufficient required values for --stringGroupMulti", e.getMessage());
        }
    }

    /**
     * <p>
     * oneOnlyFailGroup.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void oneOnlyFailGroup() throws Exception
    {
        try
        {
            CmdLine.load(this, "--stringGroup( -i ONE )( -i TWO )");
            Assert.fail("error should have occurred");

        } catch (final ParseException e)
        {
            Assert.assertEquals("multiple values not allowed for --stringGroup", e.getMessage());
        }
    }
}
