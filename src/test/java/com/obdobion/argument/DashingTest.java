package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class DashingTest
{

    public DashingTest()
    {

    }

    @Test
    public void camelCaps () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a camelCaps --camelCaps");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "--cc"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
    }

    @Test
    public void camelCapsCompetesWithExactKeyword () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a camelCap", "-t String -k c cc");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "--cc WHAT"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
    }

    @Test
    public void camelCapsCompetesWithPartialKeyword () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a camelCap", "-t String -k c ccx");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "--cc WHAT"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
    }

    @Test
    public void dashDoubleDash () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a", "-t Boolean -k b bee");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-a -b"));
        Assert.assertEquals("1 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-a --bee"));
        Assert.assertEquals("2 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "--bee -a"));
        Assert.assertEquals("3 cmd count", 2, cl.size());

    }

    @Test
    public void dashDoubleDash2 () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a", "-t String -k b bee");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-a'what'-b'what'"));
        Assert.assertEquals("1 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-a'what'--bee'what'"));
        Assert.assertEquals("2 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "--b'what'-a'what'"));
        Assert.assertEquals("3 cmd count", 2, cl.size());

    }

    @Test
    public void embeddedDash () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a", "-t Boolean -k b 'bee-name'");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-a -b"));
        Assert.assertEquals("1 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-a --bee-name"));
        Assert.assertEquals("2 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "--bee-name -a"));
        Assert.assertEquals("3 cmd count", 2, cl.size());
    }

    @Test
    public void embeddedDashNameCollision () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a", "-t Boolean -k b 'bee-name'", "-t boolean -k n 'name'");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-a --b -n"));
        Assert.assertEquals("1 cmd count", 3, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-a --bee-n -n"));
        Assert.assertEquals("2 cmd count", 3, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-a --bee-n"));
        Assert.assertEquals("3 cmd count", 2, cl.size());
    }

    @Test
    public void metaphone () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a inputFileName --metaphone", "-t Boolean -k a inputFileNum");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "--inptflnm"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
    }

    @Test
    public void metaphoneVersion () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k version --metaphone");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "--vrshn"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
    }

    @Test
    public void requiredFirst () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-tboolean -ka --req", "-tboolean -kb");

        StringBuilder str = null;

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-ba"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 b first", "-a -b", str.toString());

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), true, "-ab"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 a first", "-a -b", str.toString());
    }
}
