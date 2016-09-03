package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>
 * CaseSensitiveTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
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

    /**
     * <p>
     * caseDoesNotMatter1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void caseDoesNotMatter1() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--lowerCaseResult LOWER");
        Assert.assertEquals("lower", target.lowerCaseResult);
    }

    /**
     * <p>
     * caseDoesNotMatter2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void caseDoesNotMatter2() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--lowerCaseResult Lower");
        Assert.assertEquals("lower", target.lowerCaseResult);
    }

    /**
     * <p>
     * caseDoesNotMatter3.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void caseDoesNotMatter3() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--lowerCaseResult UPPER");
        Assert.assertEquals("upper", target.lowerCaseResult);
    }

    /**
     * <p>
     * caseDoesNotMatter4.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void caseDoesNotMatter4() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--lowerCaseResult upper");
        Assert.assertEquals("upper", target.lowerCaseResult);
    }

    /**
     * <p>
     * caseMattersValid1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void caseMattersValid1() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--caseMattersResult lower");
        Assert.assertEquals("lower", target.caseMattersResult);
    }

    /**
     * <p>
     * caseMattersValid2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void caseMattersValid2() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--caseMattersResult UPPER");
        Assert.assertEquals("UPPER", target.caseMattersResult);
    }

    /**
     * <p>
     * extendingPartialSelectionWhenCaseDoesNotMatter.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void extendingPartialSelectionWhenCaseDoesNotMatter() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--lowerCaseResult LOW");
        Assert.assertEquals("lower", target.lowerCaseResult);
    }

    /**
     * <p>
     * extendingPartialSelectionWhenCaseMatters.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void extendingPartialSelectionWhenCaseMatters() throws Exception
    {
        final Cfg target = new Cfg();
        CmdLine.load(target, "--caseMattersResult up");
        Assert.assertEquals("UPPER", target.caseMattersResult);
    }
}
