package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>WindowsTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class WindowsTest
{
    @Arg(shortName = 'a')
    boolean aBool;

    @Arg(shortName = 'b')
    boolean bBool;

    /**
     * <p>compact.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void compact() throws Exception
    {
        CmdLine.loadWin(this, "/ab");
        Assert.assertTrue(aBool);
        Assert.assertTrue(bBool);
    }

    /**
     * <p>negate.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void negate() throws Exception
    {
        CmdLine.loadWin(this, "/ab /-b");
        Assert.assertTrue(aBool);
        Assert.assertFalse(bBool);
    }

    /**
     * <p>simple.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void simple() throws Exception
    {
        CmdLine.loadWin(this, "/a /b");
        Assert.assertTrue(aBool);
        Assert.assertTrue(bBool);
    }
}
