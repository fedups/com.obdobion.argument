package com.obdobion.argument;

import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class RepeatParmTest
{

    public RepeatParmTest()
    {

    }

    public int[]    intArray;
    public int      maxUpdates = 0;
    public String[] someStrings;
    public boolean  bool;

    @Test
    public void repeatedBoolean () throws Exception
    {

        CmdLine cl = new CmdLine();
        cl.compile("-t Boolean --key m --var bool");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-mmmmmmmmmm"), this);
        Assert.assertTrue("bool", bool);

    }

    @Test
    public void turnOffLast () throws Exception
    {

        CmdLine cl = new CmdLine();
        cl.compile("-t Boolean --key m --var bool");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-m -!m"), this);
        Assert.assertFalse("bool", bool);

    }

    @Test
    public void turnOffFirst () throws Exception
    {

        CmdLine cl = new CmdLine();
        cl.compile("-t Boolean --key m --var bool");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-!m -m"), this);
        Assert.assertFalse("bool", bool);

    }

    @Test
    public void turnOffInteger () throws Exception
    {

        CmdLine cl = new CmdLine();
        cl.compile("-t Integer --key m maxUpdates --var maxUpdates --def '-999'");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-m1 -!m"), this);
        Assert.assertEquals("maxUpdates", -999, maxUpdates);

    }

    @Test
    public void turnOffIntegerOnly () throws Exception
    {

        CmdLine cl = new CmdLine();
        cl.compile("-t Integer --key m maxUpdates --var maxUpdates --def '-999'");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-!m"), this);
        Assert.assertEquals("maxUpdates", -999, maxUpdates);

    }

    @Test
    public void turnOffIntegerListWithDef () throws Exception
    {

        CmdLine cl = new CmdLine();
        cl.compile("-t Integer --key m  -m1  --def 4,5,6,7 --var intArray");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-m 1,2,3 -!m"), this);
        Assert.assertEquals("intArray size", 4, intArray.length);

    }

    @Test
    public void turnOffIntegerList () throws Exception
    {

        CmdLine cl = new CmdLine();
        cl.compile("-t Integer --key m  -m1 --var intArray ");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-m 1,2,3 -!m"), this);
        Assert.assertNull("intArray", intArray);

    }

    public void testTurnOffIntegerListOnly () throws Exception
    {

        CmdLine cl = new CmdLine();
        cl.compile("-t Integer --key m  -m1 --var intArray ");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-!m"), this);
        Assert.assertNull("intArray", intArray);

    }

    @Test
    public void repeatedNonMultiple () throws Exception
    {
        CmdLine cl = new CmdLine();
        cl.compile("-t Integer --key m maxUpdates --req --var maxUpdates");
        try
        {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-m1-!m-m2"), this);
            Assert.fail("expected exception");
        } catch (ParseException e)
        {
            Assert.assertEquals("maxUpdates", "multiple values not allowed for --maxUpdates(-m)", e.getMessage());
        }

    }

    @Test
    public void repeatedMultiple () throws Exception
    {

        someStrings = null;
        CmdLine cl = new CmdLine();
        cl.compile("-t String --key s --var someStrings --mult 1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-s first -s second"), this);
        Assert.assertEquals("someStrings 0", "first", someStrings[0]);
        Assert.assertEquals("someStrings 1", "second", someStrings[1]);

    }

    @Test
    public void repeatedPositional () throws Exception
    {

        someStrings = null;
        CmdLine cl = new CmdLine();
        cl.compile("-t String --key s --pos--var someStrings --mult 1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "first second"), this);
        Assert.assertEquals("someStrings 0", "first", someStrings[0]);
        Assert.assertEquals("someStrings 1", "second", someStrings[1]);

    }
}
