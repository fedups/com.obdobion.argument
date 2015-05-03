package com.obdobion.argument;

import junit.framework.Assert;

import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class SimpleTest {

    public SimpleTest() {

    }

    @Test
    public void compile () throws Exception {

        final CmdLine cl = new CmdLine();
        cl.compile("-t boolean -k i");
        Assert.assertNotNull(cl.arg("-i"));
    }

    @Test
    public void parse () throws Exception {

        final CmdLine cl = new CmdLine();
        cl.compile("-t boolean -k a", "-t boolean -k b");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a -b"));
        Assert.assertNotNull(cl.arg("-a"));
        Assert.assertNotNull(cl.arg("-b"));
    }

    @Test
    public void defaultParser () throws Exception {

        final ICmdLine cl = new CmdLine().compile("-t boolean -k a");
        cl.parse("-a");
        Assert.assertNotNull(cl.arg("-a"));
    }

    @Test
    public void compileNew () throws Exception {

        final ICmdLine cl = CmdLine.create("-t boolean -k a");
        cl.parse("-a");
        Assert.assertNotNull(cl.arg("-a"));
    }

    @Test
    public void oneLine () throws Exception {

        final ICmdLine cl = CmdLine.create("-t boolean -k a");
        cl.parse("-a");
        Assert.assertNotNull(cl.arg("-a"));
    }
}
