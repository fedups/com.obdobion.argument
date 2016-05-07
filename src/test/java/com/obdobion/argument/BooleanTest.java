package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class BooleanTest
{

    public BooleanTest() throws Exception
    {

    }

    @Test
    public void resetMoreThanOne () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -ks test ", "-t Boolean -kt stest --def true ");

        cl.parse("-ts -!test,stest");
        Assert.assertEquals("arg count", 0, cl.size());
        Assert.assertFalse("value", (Boolean) cl.arg("-s").getValue());
        Assert.assertTrue("value", (Boolean) cl.arg("-t").getValue());
    }

    @Test
    public void resetOnlyOneOfTwo () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -ks", "-t Boolean -ka");
        cl.parse("-as -!a");
        Assert.assertEquals("arg count", 1, cl.size());
        Assert.assertTrue("value", (Boolean) cl.arg("-s").getValue());
        Assert.assertFalse("value", (Boolean) cl.arg("-a").getValue());
    }

    @Test
    public void setToDefaultChar () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -ks test ");
        cl.parse("-s -!s");
        Assert.assertEquals("arg count", 0, cl.size());
        Assert.assertFalse("value", (Boolean) cl.arg("-s").getValue());
    }

    @Test
    public void setToDefaultWord () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -ks test ");
        cl.parse("-s -!te");
        Assert.assertEquals("arg count", 0, cl.size());
        Assert.assertFalse("value", (Boolean) cl.arg("-s").getValue());
    }

    @Test
    public void simpleDefaultFalse () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -ks test ");
        cl.parse("");
        Assert.assertEquals("arg count", 0, cl.size());
        Assert.assertFalse("value", (Boolean) cl.arg("-s").getValue());
    }

    @Test
    public void simpleDefaultTrue () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -ks test --def true ");
        cl.parse("");
        Assert.assertEquals("arg count", 0, cl.size());
        Assert.assertTrue("value", (Boolean) cl.arg("-s").getValue());
    }

    @Test
    public void simpleSetFalse () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -ks test --default true ");
        cl.parse("-s");
        Assert.assertEquals("arg count", 1, cl.size());
        Assert.assertFalse("value", (Boolean) cl.arg("-s").getValue());
    }

    @Test
    public void simpleSetTrue () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -ks test ");
        cl.parse("-s");
        Assert.assertEquals("arg count", 1, cl.size());
        Assert.assertTrue("value", (Boolean) cl.arg("-s").getValue());
    }

}
