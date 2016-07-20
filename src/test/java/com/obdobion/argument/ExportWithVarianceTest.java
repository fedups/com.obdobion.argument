package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class ExportWithVarianceTest
{

    public ExportWithVarianceTest()
    {

    }

    @Test
    public void exportBooleanInverse() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a long --default true");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a"));
        ((BooleanCLA) cl.arg("-a")).setValue(true);

        StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "", str.toString());

        ((BooleanCLA) cl.arg("-a")).setValue(false);

        str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "-a", str.toString());
    }

    @Test
    public void exportBooleanNormal() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a long");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a"));
        ((BooleanCLA) cl.arg("-a")).setValue(false);

        StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "", str.toString());

        ((BooleanCLA) cl.arg("-a")).setValue(true);

        str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "-a", str.toString());
    }

    @Test
    public void exportBooleanNotSpecified() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a long");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), ""));

        StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "", str.toString());

        ((BooleanCLA) cl.arg("-a")).setValue(true);

        str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "-a", str.toString());

        ((BooleanCLA) cl.arg("-a")).setValue(false);

        str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "", str.toString());
    }

    @Test
    public void exportEnumDefault() throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-tenum -ka --default 'heap' --class java.lang.MemoryType");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), ""));

        final StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "", str.toString());
    }

    @Test
    public void exportEnumDefault1() throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-tenum -ka --default 'heap' --class java.lang.MemoryType");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a HEAP"));

        final StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "", str.toString());
    }

    @Test
    public void exportEnumDefault2() throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-tenum -ka --default 'heap' --class java.lang.MemoryType");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a heap"));

        final StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "", str.toString());
    }

    @Test
    public void exportEnumDefaultWithCaseMattering1() throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-tenum -ka --case --default 'HEAP' --class java.lang.MemoryType");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a HEAP"));

        final StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "", str.toString());
    }

    @Test
    public void exportEnumDefaultWithCaseMattering2() throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-tenum -ka --case --default 'HEAP' --class java.lang.MemoryType");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a HEAP"));

        final StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "", str.toString());
    }

    @Test
    public void exportFloatAddOne() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t float -k a -m1");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a1.0 2.001"));
        ((FloatCLA) cl.arg("-a")).setValue(-12.123F);

        final StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "-a1 2.001 '-12.123'", str.toString());
    }

    @Test
    public void exportFloatResetAndAdd() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t float -k a -m1");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a1.0 2.001"));
        cl.arg("-a").reset();
        ((FloatCLA) cl.arg("-a")).setValue(-12.123F);

        final StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "-a'-12.123'", str.toString());
    }

    @Test
    public void exportIntegerDefault() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-ti-ka--default 0");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a0"));

        final StringBuilder str = new StringBuilder();
        cl.exportCommandLine(str);
        Assert.assertEquals("commandline export", "", str.toString());
    }
}
