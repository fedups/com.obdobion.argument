package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class WindowsTest
{
    @Arg(shortName = 'a')
    boolean aBool;

    @Arg(shortName = 'b')
    boolean bBool;

    @Test
    public void compact() throws Exception
    {
        CmdLine.loadWin(this, "/ab");
        Assert.assertTrue(aBool);
        Assert.assertTrue(bBool);
    }

    @Test
    public void negate() throws Exception
    {
        CmdLine.loadWin(this, "/ab /-b");
        Assert.assertTrue(aBool);
        Assert.assertFalse(bBool);
    }

    @Test
    public void simple() throws Exception
    {
        CmdLine.loadWin(this, "/a /b");
        Assert.assertTrue(aBool);
        Assert.assertTrue(bBool);
    }
}
