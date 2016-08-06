package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class RepeatParmTest
{
    @Arg(shortName = 'p', defaultValues = { "1", "2", "3", "4" })
    int[]           intArrayWithDefault;

    @Arg(shortName = 'o')
    public int[]    intArray;

    @Arg(shortName = 'u')
    public int      maxUpdates            = 0;

    @Arg(shortName = 'v', defaultValues = "-999")
    public int      maxUpdatesWithDefault = 0;

    @Arg(positional = true, longName = "pos")
    @Arg
    public String[] someStrings;

    @Arg(shortName = 'm')
    public boolean  bool;

    @Test
    public void repeatedBoolean() throws Exception
    {
        CmdLine.load(this, "-mmmmmmmmmm");
        Assert.assertTrue(bool);
    }

    @Test
    public void repeatedMultiple() throws Exception
    {
        CmdLine.load(this, "--somestrings first --somestrings second");
        Assert.assertEquals("someStrings 0", "first", someStrings[0]);
        Assert.assertEquals("someStrings 1", "second", someStrings[1]);
    }

    @Test
    public void repeatedNonMultiple() throws Exception
    {
        try
        {
            CmdLine.load(this, "-u1-!u-u2");
            Assert.fail("expected exception");
        } catch (final ParseException e)
        {
            Assert.assertEquals(
                    "maxUpdates",
                    "multiple values not allowed for integer --maxUpdates(-u)",
                    e.getMessage());
        }
    }

    @Test
    public void repeatedPositional() throws Exception
    {
        CmdLine.load(this, "first second");
        Assert.assertEquals("someStrings 0", "first", someStrings[0]);
        Assert.assertEquals("someStrings 1", "second", someStrings[1]);
    }

    public void testTurnOffIntegerListOnly() throws Exception
    {
        CmdLine.load(this, "-!o");
        Assert.assertNull("intArray", intArray);
    }

    @Test
    public void turnOffFirst() throws Exception
    {
        CmdLine.load(this, "-!m -m");
        Assert.assertFalse("bool", bool);
    }

    @Test
    public void turnOffInteger() throws Exception
    {
        CmdLine.load(this, "-v1 -!v");
        Assert.assertEquals(-999, maxUpdatesWithDefault);
    }

    @Test
    public void turnOffIntegerList() throws Exception
    {
        CmdLine.load(this, "--intArray 1,2,3 -!intArray");
        Assert.assertNull("intArray", intArray);
    }

    @Test
    public void turnOffIntegerListWithDef() throws Exception
    {
        CmdLine.load(this, "-p 1,2,3 -!p");
        Assert.assertEquals(4, intArrayWithDefault.length);
    }

    @Test
    public void turnOffIntegerOnly() throws Exception
    {
        CmdLine.load(this, "-!v");
        Assert.assertEquals(-999, maxUpdatesWithDefault);
    }

    @Test
    public void turnOffLast() throws Exception
    {
        CmdLine.load(this, "-m -!m");
        Assert.assertFalse("bool", bool);
    }
}
