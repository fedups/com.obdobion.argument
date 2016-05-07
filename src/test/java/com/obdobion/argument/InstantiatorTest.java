package com.obdobion.argument;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class InstantiatorTest
{

    static abstract public class AbstractColor
    {

        static public AbstractColor create (String name)
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
        //
    }

    abstract public static class Finder
    {

        static public Finder find (final String key)
        {

            Finder instance = null;
            if ("what".equalsIgnoreCase(key))
            {
                instance = new WhatFinder();
            } else if ("when".equalsIgnoreCase(key))
            {
                instance = new WhenFinder();
            } else if ("where".equalsIgnoreCase(key))
            {
                instance = new WhereFinder();
            } else
            {
                instance = new SuitFinder(key);
            }
            instance.value = key;
            return instance;
        }

        public String value;

        abstract String testValue () throws Exception;
    }

    static public class RedColor extends AbstractColor
    {
        //
    }

    public enum Suit
    {
            HEARTS,
            SPADES,
            CLUBS,
            DIAMONDS
    }
    public static class SuitFinder extends Finder
    {

        public SuitFinder(String key)
        {

            value = key;
        }

        @Override
        public String testValue () throws Exception
        {

            return value;
        }
    }

    public static class WhatFinder extends Finder
    {

        public String test;

        @Override
        public String testValue () throws Exception
        {

            return test;
        }
    }

    public static class WhenFinder extends Finder
    {

        public String test;

        @Override
        public String testValue () throws Exception
        {

            return test;
        }
    }

    public static class WhereFinder extends Finder
    {

        public String test;

        @Override
        public String testValue () throws Exception
        {

            return test;
        }
    }

    public List<Finder>  suits;

    public AbstractColor color;

    public Object        untypedFinder;
    public Finder        finder;
    public List<Finder>  listFinder;
    public List<Finder>  listGroupFinder;
    public Finder[]      arrayFinder;
    public List<String>  stringList;
    public List<Integer> integerList;

    public InstantiatorTest()
    {

    }

    @Test
    public void arrayVariable () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k i --var arrayFinder --factoryM com.obdobion.argument.InstantiatorTest$Finder.find -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT WHEN WHERE"), this);
        Assert.assertNotNull("arrayFinder instance is null", arrayFinder);
        Assert.assertEquals("arrayFinderSize", 3, arrayFinder.length);
        Assert.assertTrue("arrayFinder 0", arrayFinder[0] instanceof WhatFinder);
        Assert.assertTrue("arrayFinder 1", arrayFinder[1] instanceof WhenFinder);
        Assert.assertTrue("arrayFinder 2", arrayFinder[2] instanceof WhereFinder);
    }

    @Test
    public void badMethod () throws Exception
    {

        untypedFinder = null;
        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t string -k i --var finder --factoryM fXnd");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT"), this);
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals(
                "NoSuchMethodException expected: public static com.obdobion.argument.InstantiatorTest$Finder fXnd(java.lang.String) on com.obdobion.argument.InstantiatorTest$Finder",
                e.getMessage());
        }

    }

    @Test
    public void classFromVariable () throws Exception
    {

        finder = null;
        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k i --var finder --factoryM find");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT"), this);
        Assert.assertNotNull("finder instance is null", finder);
        Assert.assertTrue("finder type", finder instanceof WhatFinder);
        Assert.assertEquals("finder key", "WHAT", finder.value);
    }

    @Test
    public void classNotValid () throws Exception
    {

        finder = null;
        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t string -k i --var finder --class doesntmatter --factoryM find");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT"), this);
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("--class is not valid for -i", e.getMessage());
        }

    }

    @Test
    public void colorExample1 () throws Exception
    {

        CmdLine.create(
            "-tString -kcolor --var color --factoryMethod " + AbstractColor.class.getName()
                + ".create --list red blue").parse(this, "--color red");

        Assert.assertNotNull(color);
        Assert.assertTrue("red", color instanceof RedColor);
    }

    @Test
    public void defaultClassOnMethod () throws Exception
    {

        finder = null;
        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k i --var finder --factoryM find");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT"), this);
        Assert.assertNotNull("finder instance is null", finder);
        Assert.assertTrue("finder type", finder instanceof WhatFinder);
        Assert.assertEquals("finder key", "WHAT", finder.value);
    }

    @Test
    public void enumKey () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t enum -k i --var suits -m1 --factoryM com.obdobion.argument.InstantiatorTest$Finder.find --enumlist "
            + Suit.class.getName()
            + " --case");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i H C"), this);
        Assert.assertNotNull("finder instance is null", suits);
        Assert.assertEquals("finder size", 2, suits.size());
        Assert.assertTrue("finder type", suits.get(0) instanceof SuitFinder);
        Assert.assertTrue("finder type", suits.get(1) instanceof SuitFinder);
        Assert.assertEquals("finder value", "HEARTS", suits.get(0).value);
        Assert.assertEquals("finder value", "CLUBS", suits.get(1).value);
    }

    @Test
    public void integerListByDefault () throws Exception
    {

        finder = null;
        final CmdLine cl = new CmdLine();
        cl.compile("-ti -ki --var integerList -m1");
        cl.parse(this, "-i 1 2 3");
        Assert.assertNotNull(integerList);
        Assert.assertEquals("list size", 3, integerList.size());
        Assert.assertEquals("integer 1", new Integer(1), integerList.get(0));
        Assert.assertEquals("integer 2", new Integer(2), integerList.get(1));
        Assert.assertEquals("integer 3", new Integer(3), integerList.get(2));
    }

    @Test
    public void listGroupVariable () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t begin -k g --var listGroupFinder -m1 " + "--factoryM " + Finder.class.getName()
            + ".find --factoryA '-i'", "-t string -k i --var test -p", "-t end -k g");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g (WHAT)(WHEN)(WHERE)"), this);
        Assert.assertNotNull("listGroupFinder instance is null", listGroupFinder);
        Assert.assertEquals("listGroupFinder", 3, listGroupFinder.size());
        Assert.assertTrue("arrayFinder 0", listGroupFinder.get(0) instanceof WhatFinder);
        Assert.assertTrue("arrayFinder 1", listGroupFinder.get(1) instanceof WhenFinder);
        Assert.assertTrue("arrayFinder 2", listGroupFinder.get(2) instanceof WhereFinder);
        Assert.assertEquals("test 0", "WHAT", listGroupFinder.get(0).testValue());
        Assert.assertEquals("test 1", "WHEN", listGroupFinder.get(1).testValue());
        Assert.assertEquals("test 2", "WHERE", listGroupFinder.get(2).testValue());
    }

    @Test
    public void listStringVariable () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k i --var stringList -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT WHEN WHERE"), this);
        Assert.assertNotNull("stringList instance is null", stringList);
        Assert.assertEquals("stringList", 3, stringList.size());
        Assert.assertEquals("stringList 0", "WHAT", stringList.get(0));
        Assert.assertEquals("stringList 1", "WHEN", stringList.get(1));
        Assert.assertEquals("stringList 2", "WHERE", stringList.get(2));
    }

    @Test
    public void listVariable () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k i --var listFinder --factoryM com.obdobion.argument.InstantiatorTest$Finder.find -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT WHEN WHERE"), this);
        Assert.assertNotNull("listFinder instance is null", listFinder);
        Assert.assertEquals("listFinderSize", 3, listFinder.size());
        Assert.assertEquals("listFinder 0", "WHAT", listFinder.get(0).value);
        Assert.assertEquals("listFinder 1", "WHEN", listFinder.get(1).value);
        Assert.assertEquals("listFinder 2", "WHERE", listFinder.get(2).value);
    }

    @Test
    public void listVariableDefaultClass () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k i --var listFinder --factoryM com.obdobion.argument.InstantiatorTest$Finder.find -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT WHEN WHERE"), this);
        Assert.assertNotNull("listFinder instance is null", listFinder);
        Assert.assertEquals("listFinderSize", 3, listFinder.size());
        Assert.assertEquals("listFinder 0", "WHAT", listFinder.get(0).value);
        Assert.assertEquals("listFinder 1", "WHEN", listFinder.get(1).value);
        Assert.assertEquals("listFinder 2", "WHERE", listFinder.get(2).value);
    }

    @Test
    public void listVariableDefaultClassDefaultMethodClass () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t string -k i --var listFinder --factoryM find -m1");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT WHEN WHERE"), this);
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("Generics with a factory method must use --class", e.getMessage());
        }

    }

    @Test
    public void listVariablePutsNonPolymorphicValues () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k i --var listFinder -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT WHEN WHERE"), this);
        Assert.assertNotNull("listFinder instance is null", listFinder);
        Assert.assertEquals("listFinderSize", 3, listFinder.size());
        Assert.assertEquals("listFinder 0", "WHAT", listFinder.get(0));
        Assert.assertEquals("listFinder 1", "WHEN", listFinder.get(1));
        Assert.assertEquals("listFinder 2", "WHERE", listFinder.get(2));
    }

    @Test
    public void stringKey () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k i --var finder --factoryM com.obdobion.argument.InstantiatorTest$Finder.find");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i WHAT"), this);
        Assert.assertNotNull("finder instance is null", finder);
        Assert.assertEquals("finder key", "WHAT", finder.value);
    }

}
