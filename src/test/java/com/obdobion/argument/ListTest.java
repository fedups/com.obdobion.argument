package com.obdobion.argument;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>
 * ListTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class ListTest
{
    @Arg(shortName = 's', longName = "s", inList = { "abc", "abcd1", "abcde2" })
    @Arg(shortName = 't', longName = "t", inList = { "abc", "abcd1", "aBCde2" })
    @Arg(shortName = 'u', longName = "u", inList = { "abc", "abcd1", "aBCde2" }, caseSensitive = true)
    @Arg(shortName = 'v', longName = "v", inList = { "abc", "abc1", "abc2" })
    List<String> list;

    /**
     * <p>
     * ambiguousCase0.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void ambiguousCase0() throws Exception
    {
        try
        {
            CmdLine.load(this, "-s abcd");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("abcd is not valid for --s(-s)", e.getMessage());
        }
    }

    /**
     * <p>
     * ambiguousCase1.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void ambiguousCase1() throws Exception
    {
        CmdLine.load(this, "-t Abcde2");
        Assert.assertEquals("abcde2", list.get(0));
    }

    /**
     * <p>
     * ambiguousCase2.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void ambiguousCase2() throws Exception
    {
        CmdLine.load(this, "-u Abcde");
        Assert.assertEquals("aBCde2", list.get(0));
    }

    /**
     * <p>
     * ambiguousCase3.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void ambiguousCase3() throws Exception
    {
        try
        {
            CmdLine.load(this, "-u aB");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("aB is not valid for --u(-u)", e.getMessage());
        }
    }

    /**
     * <p>
     * exact.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void exact() throws Exception
    {
        CmdLine.load(this, "-v abc");
        Assert.assertEquals("list value", "abc", list.get(0));
    }
}
