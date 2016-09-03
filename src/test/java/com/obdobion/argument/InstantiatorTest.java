package com.obdobion.argument;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.type.CLAFactory;

/**
 * <p>InstantiatorTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class InstantiatorTest
{
    static abstract public class AbstractColor
    {
        static public AbstractColor create(final String name)
        {
            if ("red".equals(name))
                return new RedColor();
            if ("blue".equals(name))
                return new BlueColor();
            return null;
        }
    }

    static public class BlueColor extends AbstractColor
    {
    }

    abstract public static class Finder
    {
        static public Finder find(final String key)
        {
            Finder instance = null;
            if ("what".equalsIgnoreCase(key))
                instance = new WhatFinder();
            else if ("when".equalsIgnoreCase(key))
                instance = new WhenFinder();
            else if ("where".equalsIgnoreCase(key))
                instance = new WhereFinder();
            else
                instance = new SuitFinder(key);
            instance.value = key;
            return instance;
        }

        @Arg(positional = true, caseSensitive = true)
        public String value;

        final String testValue() throws Exception
        {
            return value;
        }
    }

    static public class RedColor extends AbstractColor
    {
    }

    public enum Suit
    {
        HEARTS, SPADES, CLUBS, DIAMONDS
    }

    public static class SuitFinder extends Finder
    {
        public SuitFinder(final String key)
        {
            value = key;
        }
    }

    public static class WhatFinder extends Finder
    {
    }

    public static class WhenFinder extends Finder
    {
    }

    public static class WhereFinder extends Finder
    {
    }

    @Arg(factoryMethod = "find",
            caseSensitive = true,
            factoryArgName = CLAFactory.SELF_REFERENCING_ARGNAME,
            inEnum = "com.obdobion.argument.InstantiatorTest$Suit")
    public List<Finder>  suits;

    @Arg(factoryMethod = "create", factoryArgName = CLAFactory.SELF_REFERENCING_ARGNAME, inList = { "red", "blue" })
    public AbstractColor color;

    @Arg(shortName = 'm',
            factoryMethod = "fXnd",
            factoryArgName = CLAFactory.SELF_REFERENCING_ARGNAME)
    @Arg(shortName = 'p',
            longName = "finderP",
            caseSensitive = true,
            factoryMethod = "find",
            factoryArgName = CLAFactory.SELF_REFERENCING_ARGNAME)
    public Finder        finder;

    @Arg(longName = "listFinderWithCase",
            caseSensitive = true,
            factoryMethod = "find",
            factoryArgName = CLAFactory.SELF_REFERENCING_ARGNAME)
    @Arg(shortName = 'o',
            factoryMethod = "find",
            factoryArgName = CLAFactory.SELF_REFERENCING_ARGNAME)
    public List<Finder>  listFinder;

    @Arg(shortName = 'g', factoryMethod = "find", factoryArgName = "value")
    public List<Finder>  listGroupFinder;

    @Arg(shortName = 'n',
            longName = "arrayFinderN",
            factoryMethod = "find",
            factoryArgName = CLAFactory.SELF_REFERENCING_ARGNAME)
    @Arg(shortName = 'i',
            factoryMethod = "com.obdobion.argument.InstantiatorTest$Finder.find",
            factoryArgName = CLAFactory.SELF_REFERENCING_ARGNAME)
    public Finder[]      arrayFinder;

    @Arg(caseSensitive = true)
    public List<String>  stringList;

    @Arg
    public List<Integer> integerList;

    /**
     * <p>arrayVariableExactClassName.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void arrayVariableExactClassName() throws Exception
    {
        CmdLine.load(this, "-i WHAT WHEN WHERE");
        Assert.assertNotNull("arrayFinder instance is null", arrayFinder);
        Assert.assertEquals("arrayFinderSize", 3, arrayFinder.length);
        Assert.assertTrue("arrayFinder 0", arrayFinder[0] instanceof WhatFinder);
        Assert.assertTrue("arrayFinder 1", arrayFinder[1] instanceof WhenFinder);
        Assert.assertTrue("arrayFinder 2", arrayFinder[2] instanceof WhereFinder);
    }

    /**
     * <p>arrayVariableShortClassName.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void arrayVariableShortClassName() throws Exception
    {
        CmdLine.load(this, "-n WHAT WHEN WHERE");
        Assert.assertNotNull("arrayFinder instance is null", arrayFinder);
        Assert.assertEquals("arrayFinderSize", 3, arrayFinder.length);
        Assert.assertTrue("arrayFinder 0", arrayFinder[0] instanceof WhatFinder);
        Assert.assertTrue("arrayFinder 1", arrayFinder[1] instanceof WhenFinder);
        Assert.assertTrue("arrayFinder 2", arrayFinder[2] instanceof WhereFinder);
    }

    /**
     * <p>badMethod.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void badMethod() throws Exception
    {
        try
        {
            CmdLine.load(this, "-m WHAT");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals(
                    "NoSuchMethodException expected: public static com.obdobion.argument.InstantiatorTest$Finder fXnd(java.lang.String) on com.obdobion.argument.InstantiatorTest$Finder",
                    e.getMessage());
        }
    }

    /**
     * <p>classFromVariable.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void classFromVariable() throws Exception
    {
        CmdLine.load(this, "-p WHAT");
        Assert.assertNotNull(finder);
        Assert.assertTrue(finder instanceof WhatFinder);
        Assert.assertEquals("WHAT", finder.value);
    }

    /**
     * <p>colorExample1.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void colorExample1() throws Exception
    {
        CmdLine.load(this, "--color red");
        Assert.assertNotNull(color);
        Assert.assertTrue("red", color instanceof RedColor);
    }

    /**
     * <p>enumKey.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void enumKey() throws Exception
    {
        CmdLine.load(this, "--suits H C");
        Assert.assertNotNull("finder instance is null", suits);
        Assert.assertEquals("finder size", 2, suits.size());
        Assert.assertTrue("finder type", suits.get(0) instanceof SuitFinder);
        Assert.assertTrue("finder type", suits.get(1) instanceof SuitFinder);
        Assert.assertEquals("finder value", "HEARTS", suits.get(0).value);
        Assert.assertEquals("finder value", "CLUBS", suits.get(1).value);
    }

    /**
     * <p>integerListByDefault.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void integerListByDefault() throws Exception
    {
        CmdLine.load(this, "--integerList 1 2 3");
        Assert.assertNotNull(integerList);
        Assert.assertEquals("list size", 3, integerList.size());
        Assert.assertEquals("integer 1", new Integer(1), integerList.get(0));
        Assert.assertEquals("integer 2", new Integer(2), integerList.get(1));
        Assert.assertEquals("integer 3", new Integer(3), integerList.get(2));
    }

    /**
     * <p>listGroupVariable.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void listGroupVariable() throws Exception
    {
        CmdLine.load(this, "-g (WHAT)(WHEN)(WHERE)");
        Assert.assertNotNull("listGroupFinder instance is null", listGroupFinder);
        Assert.assertEquals("listGroupFinder", 3, listGroupFinder.size());
        Assert.assertTrue("arrayFinder 0", listGroupFinder.get(0) instanceof WhatFinder);
        Assert.assertTrue("arrayFinder 1", listGroupFinder.get(1) instanceof WhenFinder);
        Assert.assertTrue("arrayFinder 2", listGroupFinder.get(2) instanceof WhereFinder);
        Assert.assertEquals("test 0", "WHAT", listGroupFinder.get(0).testValue());
        Assert.assertEquals("test 1", "WHEN", listGroupFinder.get(1).testValue());
        Assert.assertEquals("test 2", "WHERE", listGroupFinder.get(2).testValue());
    }

    /**
     * <p>listStringVariable.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void listStringVariable() throws Exception
    {
        CmdLine.load(this, "--stringList WHAT WHEN WHERE");
        Assert.assertNotNull("stringList instance is null", stringList);
        Assert.assertEquals("stringList", 3, stringList.size());
        Assert.assertEquals("stringList 0", "WHAT", stringList.get(0));
        Assert.assertEquals("stringList 1", "WHEN", stringList.get(1));
        Assert.assertEquals("stringList 2", "WHERE", stringList.get(2));
    }

    /**
     * <p>listVariable.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void listVariable() throws Exception
    {
        CmdLine.load(this, "--listFinderWithCase WHAT WHEN WHERE");
        Assert.assertNotNull("listFinder instance is null", listFinder);
        Assert.assertEquals("listFinderSize", 3, listFinder.size());
        Assert.assertEquals("listFinder 0", "WHAT", listFinder.get(0).value);
        Assert.assertEquals("listFinder 1", "WHEN", listFinder.get(1).value);
        Assert.assertEquals("listFinder 2", "WHERE", listFinder.get(2).value);
    }

    /**
     * <p>listVariableDefaultClassDefaultMethodClass.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void listVariableDefaultClassDefaultMethodClass() throws Exception
    {
        CmdLine.load(this, "-o WHAT WHEN WHERE");
        Assert.assertNotNull(listFinder);
        Assert.assertEquals(3, listFinder.size());
        Assert.assertTrue(listFinder.get(0) instanceof WhatFinder);
        Assert.assertTrue(listFinder.get(1) instanceof WhenFinder);
        Assert.assertTrue(listFinder.get(2) instanceof WhereFinder);
    }

    /**
     * <p>stringKey.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void stringKey() throws Exception
    {
        CmdLine.load(this, "--finderP WHAT");
        Assert.assertNotNull("finder instance is null", finder);
        Assert.assertEquals("finder key", "WHAT", finder.value);
    }

}
