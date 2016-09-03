package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.type.WildPath;

/**
 * <p>
 * WildPathTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class WildPathTest
{
    /**
     * <p>
     * startingPathAbsolute.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void startingPathAbsolute() throws Exception
    {
        final WildPath wp = new WildPath("/a/b/c/*.txt");
        Assert.assertEquals("/a/b/c/", wp.startingPath());
    }

    /**
     * <p>
     * startingPathAbsoluteWildcards.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void startingPathAbsoluteWildcards() throws Exception
    {
        final WildPath wp = new WildPath("/**/b/c/*.txt");
        Assert.assertEquals("/", wp.startingPath());
    }

    /**
     * <p>
     * startingPathQuestionmarkWildcards.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void startingPathQuestionmarkWildcards() throws Exception
    {
        WildPath wp;

        wp = new WildPath("/a/b?c/d/*.txt");
        Assert.assertEquals("/a/", wp.startingPath());

        wp = new WildPath("/a/??/d/*.txt");
        Assert.assertEquals("/a/", wp.startingPath());

        wp = new WildPath("log????/*.txt");
        Assert.assertEquals(".", wp.startingPath());
    }

    /**
     * <p>
     * startingPathRelative.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void startingPathRelative() throws Exception
    {
        final WildPath wp = new WildPath("a/b/c/*.txt");
        Assert.assertEquals("a/b/c/", wp.startingPath());
    }

    /**
     * <p>
     * startingPathRelativeWildcards.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void startingPathRelativeWildcards() throws Exception
    {
        final WildPath wp = new WildPath("**/b/c/*.txt");
        Assert.assertEquals(".", wp.startingPath());
    }

    /**
     * <p>
     * startingPathStarWildcards.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void startingPathStarWildcards() throws Exception
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
