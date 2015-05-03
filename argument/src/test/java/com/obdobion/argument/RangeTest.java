package com.obdobion.argument;

import junit.framework.Assert;

import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class RangeTest {

    public RangeTest() {

    }

    @Test
    public void floatRange1 () throws Exception {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Float -k i --ra 0 -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 0 1 9 0.1 999.999999"));
        try {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 0 '-1' 999"));
            Assert.fail("should have Assert.failed");
        } catch (final Exception e) {
            Assert.assertEquals("-1.0 is not valid for -i", e.getMessage());
        }
        try {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 0  '-0.00001' 999.0"));
            Assert.fail("should have Assert.failed");
        } catch (final Exception e) {
            Assert.assertEquals("-1.0E-5 is not valid for -i", e.getMessage());
        }

    }

    @Test
    public void integerRange1 () throws Exception {

        final CmdLine cl = new CmdLine();
        cl.compile("-t integer -k i --ra 0 -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 0 1 9"));
        try {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 0 '-1' 999"));
            Assert.fail("should have Assert.failed");
        } catch (final Exception e) {
            Assert.assertEquals("-1 is not valid for -i", e.getMessage());
        }

    }

    @Test
    public void integerRange2 () throws Exception {

        final CmdLine cl = new CmdLine();
        cl.compile("-t integer -k i --ra 0 1000 -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 0 1 999"));
        try {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 0 '-1' 999"));
            Assert.fail("should have Assert.failed");
        } catch (final Exception e) {
            Assert.assertEquals("-1 is not valid for -i", e.getMessage());
        }

        try {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 0 1 1000 1001"));
            Assert.fail("should have Assert.failed");
        } catch (final Exception e) {
            Assert.assertEquals("1001 is not valid for -i", e.getMessage());
        }

    }

    @Test
    public void stringRange1 () throws Exception {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k s --ra 'b' -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-s b c"));
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-s binary c"));
        try {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-s b c android"));
            Assert.fail("should have Assert.failed");
        } catch (final Exception e) {
            Assert.assertEquals("android is not valid for -s", e.getMessage());
        }

    }

    @Test
    public void stringRange2 () throws Exception {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k s --ra b famish -m1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-s b c"));
        try {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-s g b c"));
            Assert.fail("should have Assert.failed");
        } catch (final Exception e) {
            Assert.assertEquals("g is not valid for -s", e.getMessage());
        }
        try {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-s a b c"));
            Assert.fail("should have Assert.failed");
        } catch (final Exception e) {
            Assert.assertEquals("a is not valid for -s", e.getMessage());
        }
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-s b fam c"));
        try {
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-s famished"));
            Assert.fail("should have Assert.failed");
        } catch (final Exception e) {
            Assert.assertEquals("famished is not valid for -s", e.getMessage());
        }

    }
}
