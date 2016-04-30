package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 *
 */
public class WildPathTest
{
    public WildPathTest()
    {

    }

    @Test
    public void startingPathAbsolute () throws Exception
    {
        final WildPath wp = new WildPath("/a/b/c/*.txt");
        Assert.assertEquals("/a/b/c/", wp.startingPath());
    }

    @Test
    public void startingPathAbsoluteWildcards () throws Exception
    {
        final WildPath wp = new WildPath("/**/b/c/*.txt");
        Assert.assertEquals("/", wp.startingPath());
    }

    @Test
    public void startingPathQuestionmarkWildcards () throws Exception
    {
        WildPath wp;

        wp = new WildPath("/a/b?c/d/*.txt");
        Assert.assertEquals("/a/", wp.startingPath());

        wp = new WildPath("/a/??/d/*.txt");
        Assert.assertEquals("/a/", wp.startingPath());

        wp = new WildPath("log????/*.txt");
        Assert.assertEquals(".", wp.startingPath());
    }

    @Test
    public void startingPathRelative () throws Exception
    {
        final WildPath wp = new WildPath("a/b/c/*.txt");
        Assert.assertEquals("a/b/c/", wp.startingPath());
    }

    @Test
    public void startingPathRelativeWildcards () throws Exception
    {
        final WildPath wp = new WildPath("**/b/c/*.txt");
        Assert.assertEquals(".", wp.startingPath());
    }

    @Test
    public void startingPathStarWildcards () throws Exception
    {
        WildPath wp;

        wp = new WildPath("/a/b/*/d/*.txt");
        Assert.assertEquals("/a/b/", wp.startingPath());

        wp = new WildPath("/a/*/*/d/*.txt");
        Assert.assertEquals("/a/", wp.startingPath());

        wp = new WildPath("/a/**/d/*.txt");
        Assert.assertEquals("/a/", wp.startingPath());
    }
}