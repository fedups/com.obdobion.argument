package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class BooleanTest
{
    static public final class ResetMoreThanOne
    {
        @Arg(shortName = 't')
        boolean tx;
        @Arg(shortName = 's', defaultValues = "true")
        boolean sx;
    }

    static public final class ResetOnlyOneOfTwo
    {
        @Arg(shortName = 'a')
        boolean ax;
        @Arg(shortName = 's')
        boolean sx;
    }

    static public final class SetToDefaultChar
    {
        @Arg(shortName = 'a')
        boolean ax;
    }

    static public final class SimpleSetFalse
    {
        @Arg(defaultValues = "true")
        boolean ax;
    }

    static public final class SimpleSetTrue
    {
        @Arg
        boolean ax;
    }

    public BooleanTest() throws Exception
    {
    }

    @Test
    public void resetMoreThanOne() throws Exception
    {
        final ResetMoreThanOne resetMoreThanOne = new ResetMoreThanOne();
        CmdLine.load(resetMoreThanOne, "-ts -!t,s");
        Assert.assertTrue("s should be true", resetMoreThanOne.sx);
        Assert.assertFalse("t should be false", resetMoreThanOne.tx);
    }

    @Test
    public void resetOnlyOneOfTwo() throws Exception
    {
        final ResetOnlyOneOfTwo resetOnlyOneOfTwo = new ResetOnlyOneOfTwo();
        CmdLine.load(resetOnlyOneOfTwo, "-as -!a");
        Assert.assertTrue("s should be true", resetOnlyOneOfTwo.sx);
        Assert.assertFalse("t should be false", resetOnlyOneOfTwo.ax);
    }

    @Test
    public void setToDefaultChar() throws Exception
    {
        final SetToDefaultChar setToDefaultChar = new SetToDefaultChar();
        CmdLine.load(setToDefaultChar, "-a -!a");
        Assert.assertFalse("a should be false", setToDefaultChar.ax);
    }

    @Test
    public void setToDefaultWord() throws Exception
    {
        final SetToDefaultChar setToDefaultChar = new SetToDefaultChar();
        CmdLine.load(setToDefaultChar, "--ax -!ax");
        Assert.assertFalse("ax should be false", setToDefaultChar.ax);
    }

    @Test
    public void simpleSetFalse() throws Exception
    {
        final SimpleSetFalse simpleSetFalse = new SimpleSetFalse();
        CmdLine.load(simpleSetFalse, "--ax");
        Assert.assertFalse("ax should be false", simpleSetFalse.ax);
    }

    @Test
    public void simpleSetTrue() throws Exception
    {
        final SimpleSetTrue simpleSetTrue = new SimpleSetTrue();
        CmdLine.load(simpleSetTrue, "--ax");
        Assert.assertTrue("ax should be true", simpleSetTrue.ax);
    }

}
