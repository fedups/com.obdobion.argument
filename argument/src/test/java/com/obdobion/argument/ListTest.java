package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class ListTest
{

    public ListTest()
    {

    }

    @Test
    public void ambiguous () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-ts -ks --li abc abcd1 abcde2");
            cl.parse("-s abcd");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("list value", "abcd is not valid for -s", e.getMessage());
        }
    }

    @Test
    public void ambiguousCase () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-ts -ks --li abc abcd1 aBCde2");
        cl.parse("-s Abcde");
        Assert.assertEquals("list value", "abcde2", cl.arg("-s").getValue());
    }

    @Test
    public void ambiguousCase2 () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-ts -ks --li abc abcd1 aBCde2 --case");
        cl.parse("-s Abcde");
        Assert.assertEquals("list value", "aBCde2", cl.arg("-s").getValue());
    }

    @Test
    public void ambiguousCase3 () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-ts -ks --li abc abcd1 aBCde2 --case");
            cl.parse("-s aB");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("list value", "aB is not valid for -s", e.getMessage());
        }
    }

    @Test
    public void exact () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-ts -ks --li abc abc1 abc2");
        cl.parse("-s abc");
        Assert.assertEquals("list value", "abc", cl.arg("-s").getValue());
    }
}
