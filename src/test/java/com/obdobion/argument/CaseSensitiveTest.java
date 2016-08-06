package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class CaseSensitiveTest
{
    static public class Cfg
    {
        @Arg(inList = { "lower", "UPPER" })
        String lowerCaseResult;

        @Arg(inList = { "lower", "UPPER" }, caseSensitive = true)
        String caseMattersResult;
    }

    @Test
    public void caseDoesNotMatter1() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--lowerCaseResult LOWER");
        Assert.assertEquals("lower", target.lowerCaseResult);
    }

    @Test
    public void caseDoesNotMatter2() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--lowerCaseResult Lower");
        Assert.assertEquals("lower", target.lowerCaseResult);
    }

    @Test
    public void caseDoesNotMatter3() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--lowerCaseResult UPPER");
        Assert.assertEquals("upper", target.lowerCaseResult);
    }

    @Test
    public void caseDoesNotMatter4() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--lowerCaseResult upper");
        Assert.assertEquals("upper", target.lowerCaseResult);
    }

    @Test
    public void caseMattersValid1() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--caseMattersResult lower");
        Assert.assertEquals("lower", target.caseMattersResult);
    }

    @Test
    public void caseMattersValid2() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--caseMattersResult UPPER");
        Assert.assertEquals("UPPER", target.caseMattersResult);
    }

    @Test
    public void extendingPartialSelectionWhenCaseDoesNotMatter() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--lowerCaseResult LOW");
        Assert.assertEquals("lower", target.lowerCaseResult);
    }

    @Test
    public void extendingPartialSelectionWhenCaseMatters() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--caseMattersResult up");
        Assert.assertEquals("UPPER", target.caseMattersResult);
    }
}
