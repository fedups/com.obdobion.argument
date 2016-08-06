package com.obdobion.argument;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class ListTest
{
    @Arg(shortName = 's', longName = "s", inList = { "abc", "abcd1", "abcde2" })
    @Arg(shortName = 't', longName = "t", inList = { "abc", "abcd1", "aBCde2" })
    @Arg(shortName = 'u', longName = "u", inList = { "abc", "abcd1", "aBCde2" }, caseSensitive = true)
    @Arg(shortName = 'v', longName = "v", inList = { "abc", "abc1", "abc2" })
    List<String> list;

    @Test
    public void ambiguousCase0() throws Exception
    {
        try
        {
            CmdLine.load(this, "-s abcd");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("abcd is not valid for string --s(-s)", e.getMessage());
        }
    }

    @Test
    public void ambiguousCase1() throws Exception
    {
        CmdLine.load(this, "-t Abcde2");
        Assert.assertEquals("abcde2", list.get(0));
    }

    @Test
    public void ambiguousCase2() throws Exception
    {
        CmdLine.load(this, "-u Abcde");
        Assert.assertEquals("aBCde2", list.get(0));
    }

    @Test
    public void ambiguousCase3() throws Exception
    {
        try
        {
            CmdLine.load(this, "-u aB");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("aB is not valid for string --u(-u)", e.getMessage());
        }
    }

    @Test
    public void exact() throws Exception
    {
        CmdLine.load(this, "-v abc");
        Assert.assertEquals("list value", "abc", list.get(0));
    }
}
