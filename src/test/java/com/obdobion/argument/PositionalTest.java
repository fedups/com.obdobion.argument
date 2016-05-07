package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class PositionalTest
{

    public PositionalTest()
    {

    }

    @Test
    public void groupingGroupsPositionalBooleans () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
            "-t begin -kg",
            "-t boolean -k a item1 --pos ",
            "-t begin -kd",
            "-t boolean -k b item2 --pos ",
            "-t end -kd ",
            "-t end -kg ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g( false -d(false) ) "));
        Assert.assertEquals("1 cmd count", 1, cl.size());
    }

    @Test
    public void groupingGroupsPositionalGroups () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
            "-t begin -kg",
            "-t boolean -k a item1 ",
            "-t begin -kd --pos",
            "-t boolean -k b item2 ",
            "-t end -kd ",
            "-t end -kg ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g( -a (-b) ) "));
        Assert.assertEquals("1 cmd count", 1, cl.size());
    }

    @Test
    public void groupingGroupsPositionalGroupsAndBooleans () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
            "-t begin -kg",
            "-t boolean -k a item1  --pos",
            "-t begin -kd --pos",
            "-t boolean -k b item2  --pos",
            "-t end -kd ",
            "-t end -kg ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g( false (false) ) "));
        Assert.assertEquals("1 cmd count", 1, cl.size());
    }

    @Test
    public void groupMultiple () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t begin -kg -p -m1", "-t string -ks -p -m1", "-t end -kg");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "(input1 input3),(input2)"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("inner cmd count 1", 2, cl.arg("-g").size());
        Assert.assertEquals("inner cmd count 2", 2, ((ICmdLine) cl.arg("-g").getValue(0)).arg("-s").size());
        Assert.assertEquals("inner cmd count 3", 1, ((ICmdLine) cl.arg("-g").getValue(1)).arg("-s").size());
        Assert.assertEquals("1st value", "input1", ((ICmdLine) cl.arg("-g").getValue(0)).arg("-s").getValue(0));
        Assert.assertEquals("2nd value", "input3", ((ICmdLine) cl.arg("-g").getValue(0)).arg("-s").getValue(1));
        Assert.assertEquals("3rd value", "input2", ((ICmdLine) cl.arg("-g").getValue(1)).arg("-s").getValue(0));
    }

    @Test
    public void stringExactMultiple () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t String -ks -p -m1 1", "-t String -kt -p -m1 1");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "input1,input2"));
        Assert.assertEquals("1 cmd count", 2, cl.size());
        Assert.assertEquals("1st value", "input1", cl.arg("-s").getValue());
        Assert.assertEquals("2nd value", "input2", cl.arg("-t").getValue());
    }

    @Test
    public void stringMultiple () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t String -ks -p -m1");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "input1,input2"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1st value", "input1", cl.arg("-s").getValue(0));
        Assert.assertEquals("2nd value", "input2", cl.arg("-s").getValue(1));
    }

    @Test
    public void testString () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t String -ks -p");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "input1"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("only value", "input1", cl.arg("-s").getValue());
    }

    @Test
    public void twoStrings () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t String -ks -p", "-t String -kt -p");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "input1,input2"));
        Assert.assertEquals("1 cmd count", 2, cl.size());
        Assert.assertEquals("only value", "input1", cl.arg("-s").getValue());
        Assert.assertEquals("only value", "input2", cl.arg("-t").getValue());
    }

    @Test
    public void twoStringsOnlyUsed1 () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t String -ks -p", "-t String -kt -p");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "input1"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("only value", "input1", cl.arg("-s").getValue());
    }

}
