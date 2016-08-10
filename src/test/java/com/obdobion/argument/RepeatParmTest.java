package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>RepeatParmTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
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

    /**
     * <p>repeatedBoolean.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void repeatedBoolean() throws Exception
    {
        CmdLine.load(this, "-mmmmmmmmmm");
        Assert.assertTrue(bool);
    }

    /**
     * <p>repeatedMultiple.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void repeatedMultiple() throws Exception
    {
        CmdLine.load(this, "--somestrings first --somestrings second");
        Assert.assertEquals("someStrings 0", "first", someStrings[0]);
        Assert.assertEquals("someStrings 1", "second", someStrings[1]);
    }

    /**
     * <p>repeatedNonMultiple.</p>
     *
     * @throws java.lang.Exception if any.
     */
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

    /**
     * <p>repeatedPositional.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void repeatedPositional() throws Exception
    {
        CmdLine.load(this, "first second");
        Assert.assertEquals("someStrings 0", "first", someStrings[0]);
        Assert.assertEquals("someStrings 1", "second", someStrings[1]);
    }

    /**
     * <p>testTurnOffIntegerListOnly.</p>
     *
     * @throws java.lang.Exception if any.
     */
    public void testTurnOffIntegerListOnly() throws Exception
    {
        CmdLine.load(this, "-!o");
        Assert.assertNull("intArray", intArray);
    }

    /**
     * <p>turnOffFirst.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void turnOffFirst() throws Exception
    {
        CmdLine.load(this, "-!m -m");
        Assert.assertFalse("bool", bool);
    }

    /**
     * <p>turnOffInteger.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void turnOffInteger() throws Exception
    {
        CmdLine.load(this, "-v1 -!v");
        Assert.assertEquals(-999, maxUpdatesWithDefault);
    }

    /**
     * <p>turnOffIntegerList.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void turnOffIntegerList() throws Exception
    {
        CmdLine.load(this, "--intArray 1,2,3 -!intArray");
        Assert.assertNull("intArray", intArray);
    }

    /**
     * <p>turnOffIntegerListWithDef.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void turnOffIntegerListWithDef() throws Exception
    {
        CmdLine.load(this, "-p 1,2,3 -!p");
        Assert.assertEquals(4, intArrayWithDefault.length);
    }

    /**
     * <p>turnOffIntegerOnly.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void turnOffIntegerOnly() throws Exception
    {
        CmdLine.load(this, "-!v");
        Assert.assertEquals(-999, maxUpdatesWithDefault);
    }

    /**
     * <p>turnOffLast.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void turnOffLast() throws Exception
    {
        CmdLine.load(this, "-m -!m");
        Assert.assertFalse("bool", bool);
    }
}
