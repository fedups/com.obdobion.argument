package com.obdobion.argument;

import junit.framework.Assert;

import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class WindowsTest {

    public WindowsTest() {

    }

    public boolean aBool, bBool;

    @Test
    public void simple () throws Exception {

        final CmdLine cl = new CmdLine("windows test", '/', '-');
        cl.compile("-t boolean -k a --var aBool", "-t boolean -k b --var bBool");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "/a /b"), this);
        Assert.assertEquals(true, aBool);
        Assert.assertEquals(true, bBool);
    }

    @Test
    public void compact () throws Exception {

        final CmdLine cl = new CmdLine("windows test", '/', '-');
        cl.compile("-t boolean -k a --var aBool", "-t boolean -k b --var bBool");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "/ab"), this);
        Assert.assertEquals(true, aBool);
        Assert.assertEquals(true, bBool);
    }

    @Test
    public void negate () throws Exception {

        final CmdLine cl = new CmdLine("windows test", '/', '-');
        cl.compile("-t boolean -k a --var aBool", "-t boolean -k b --var bBool");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "/ab /-b"), this);
        Assert.assertEquals(true, aBool);
        Assert.assertEquals(false, bBool);
    }
}
