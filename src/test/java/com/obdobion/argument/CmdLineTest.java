package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class CmdLineTest
{

    public CmdLineTest()
    {

    }

    @Test
    public void booleanConcats () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
            "-t boolean -k i inputfile ",
            "-t boolean -k e ebcdic ",
            "-t boolean -k x dup1 ",
            "-t boolean -k d dup2 ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i -e"));
        Assert.assertEquals("1 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i -e"));
        Assert.assertEquals("2 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--input --eb"));
        Assert.assertEquals("3 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--in -xe"));
        Assert.assertEquals("4 cmd count", 3, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-d"));
        Assert.assertEquals("5 cmd count", 1, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-dxie"));
        Assert.assertEquals("6 cmd count", 4, cl.size());
    }

    @Test
    public void dashDoubleDash () throws Exception
    {

        final ICmdLine cl = new CmdLine();
        cl.compile("-t boolean -k a", "-t boolean -k b bee");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a-b"));
        Assert.assertEquals("1 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a--bee"));
        Assert.assertEquals("2 cmd count", 2, cl.size());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--bee-a"));
        Assert.assertEquals("3 cmd count", 2, cl.size());
    }

    @Test
    public void grouping () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t begin -kg", "-t boolean -k a item1 ", "-t boolean -k b item2 ", "-t end -kg ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g ( -a -b )"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
    }

    @Test
    public void groupingGroups () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
            "-t begin -kg",
            "-t boolean -k a item1",
            "-t begin -kd",
            "-t boolean -k b item2 ",
            "-t end -kd ",
            "-t end -kg ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g(-ad(-b))"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
    }

    @Test
    public void integers () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t integer -ki -r", "-t integer -km -m1 2 --range 1 3000");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 1 -m1 '2026'"));
        Assert.assertEquals("1 cmd count", 2, cl.size());
        IntegerCLA sarg = (IntegerCLA) cl.arg("-i");
        Assert.assertEquals("1i", 1, sarg.getValue().intValue());
        sarg = (IntegerCLA) cl.arg("-m");
        Assert.assertEquals("1m1", new Integer(1), sarg.getValue(0));
        Assert.assertEquals("1m2", new Integer(2026), sarg.getValue(1));
    }

    @Test
    public void longs () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t long -ki -r", "-t long -km -m1 2 --range 1 3000");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 1 -m1 2026"));
        Assert.assertEquals("1 cmd count", 2, cl.size());
        LongCLA sarg = (LongCLA) cl.arg("-i");
        Assert.assertEquals("1i", 1, sarg.getValue().intValue());
        sarg = (LongCLA) cl.arg("-m");
        Assert.assertEquals("1m1", new Long(1), sarg.getValue(0));
        Assert.assertEquals("1m2", new Long(2026), sarg.getValue(1));
    }

    @Test
    public void multiValued () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -ki -m1", "-t string -kx");

        StringCLA sarg = null;

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i i1"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        sarg = (StringCLA) cl.arg("-i");
        Assert.assertEquals("1i", "i1", sarg.getValue());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i i1 i2"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        sarg = (StringCLA) cl.arg("-i");
        Assert.assertEquals("1i1", "i1", sarg.getValue(0));
        sarg = (StringCLA) cl.arg("-i");
        Assert.assertEquals("1i2", "i2", sarg.getValue(1));

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i i1 i2 -x x1"));
        Assert.assertEquals("1 cmd count", 2, cl.size());
        sarg = (StringCLA) cl.arg("-i");
        Assert.assertEquals("1i1", "i1", sarg.getValue(0));
        sarg = (StringCLA) cl.arg("-i");
        Assert.assertEquals("1i2", "i2", sarg.getValue(1));
        sarg = (StringCLA) cl.arg("-x");
        Assert.assertEquals("1x1", "x1", sarg.getValue(0));
    }

    @Test
    public void numberInAKeyWordIsOK () throws Exception
    {
        try
        {
            final ICmdLine cl = new CmdLine();
            cl.compile("-t boolean -k c word1");
        } catch (ParseException p)
        {
            Assert.fail("should have allowed a digit after the 1st char in a key word");
        }
    }

    @Test
    public void numericKeyCharIsBad () throws Exception
    {
        try
        {
            final ICmdLine cl = new CmdLine();
            cl.compile("-t boolean -k 1");
        } catch (ParseException p)
        {
            return;
        }
        Assert.fail("should not have allowed a key char to be a digit");
    }

    @Test
    public void numericKeyWordIsBad () throws Exception
    {
        try
        {
            final ICmdLine cl = new CmdLine();
            cl.compile("-t boolean -k c 1word");
        } catch (ParseException p)
        {
            return;
        }
        Assert.fail("should not have allowed the 1st char of a key word to be a digit");
    }

    @Test
    public void parmed () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
            "-t string -k i inputfile --required",
            "-t boolean -k e ebcdic",
            "-t boolean -k x dup1",
            "-t string -k d dup2 -m1");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i infile"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        StringCLA sarg = (StringCLA) cl.arg("-i");
        Assert.assertEquals("1i", "infile", sarg.getValue());
        BooleanCLA barg = (BooleanCLA) cl.arg("-x");
        Assert.assertEquals("1x", Boolean.FALSE, barg.getValue());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i infile -x"));
        Assert.assertEquals("2 cmd count", 2, cl.size());
        sarg = (StringCLA) cl.arg("-i");
        Assert.assertEquals("2i", "infile", sarg.getValue());
        barg = (BooleanCLA) cl.arg("-x");
        Assert.assertEquals("2x", Boolean.TRUE, barg.getValue());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-xi infile"));
        Assert.assertEquals("3 cmd count", 2, cl.size());
        sarg = (StringCLA) cl.arg("-i");
        Assert.assertEquals("3i", "infile", sarg.getValue());
        barg = (BooleanCLA) cl.arg("-x");
        Assert.assertEquals("3x", Boolean.TRUE, barg.getValue());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-xiinfile"));
        Assert.assertEquals("4 cmd count", 2, cl.size());
        sarg = (StringCLA) cl.arg("-i");
        Assert.assertEquals("4i", "infile", sarg.getValue());
        barg = (BooleanCLA) cl.arg("-x");
        Assert.assertEquals("4x", Boolean.TRUE, barg.getValue());
    }

    @Test
    public void patternMatching () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -kp --matches one|two", "-t string -kq -m1 --matches one|two");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-ptwo"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        StringCLA sarg = (StringCLA) cl.arg("-p");
        Assert.assertEquals("1p", "two", sarg.getValue());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-q two one"));
        Assert.assertEquals("2 cmd count", 1, cl.size());
        sarg = (StringCLA) cl.arg("-q");
        Assert.assertEquals("2q size", 2, sarg.size());
        Assert.assertEquals("2qtwo", "two", sarg.getValue(0));
        Assert.assertEquals("2qone", "one", sarg.getValue(1));
    }

    @Test
    public void positionalBooleans () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k i infile", "-t boolean -k a p1", "-t string -k s outfile -p", "-t boolean -k b p2");

        StringCLA sarg;
        BooleanCLA arg;

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a what -b -i when"));
        Assert.assertEquals("1 cmd count", 4, cl.size());
        arg = (BooleanCLA) cl.arg("-a");
        Assert.assertEquals("1a", Boolean.TRUE, arg.getValue());
        arg = (BooleanCLA) cl.arg("-b");
        Assert.assertEquals("1b", Boolean.TRUE, arg.getValue());
        sarg = (StringCLA) cl.arg("-i");
        Assert.assertEquals("1i", "when", sarg.getValue());
        sarg = (StringCLA) cl.arg("-s");
        Assert.assertEquals("1i", "what", sarg.getValue());
    }

    @Test
    public void testBoolean () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
            "-t boolean -k i inputfile ",
            "-t boolean -ke ebcdic",
            "-t boolean -k x dup1 -d true",
            "-t boolean -kd");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i"));

        Assert.assertEquals("1 cmd count", 1, cl.size());
        BooleanCLA barg = (BooleanCLA) cl.arg("-i");
        // example of a casting the value rather than the arg.
        Assert.assertEquals("1i", Boolean.TRUE, cl.arg("-i").getValue());
        barg = (BooleanCLA) cl.arg("-e");
        Assert.assertEquals("1e", Boolean.FALSE, barg.getValue());
        barg = (BooleanCLA) cl.arg("-x");
        Assert.assertEquals("1x", Boolean.TRUE, barg.getValue());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-exi"));

        Assert.assertEquals("2 cmd count", 3, cl.size());
        barg = (BooleanCLA) cl.arg("-i");
        Assert.assertEquals("2i", Boolean.TRUE, barg.getValue());
        barg = (BooleanCLA) cl.arg("-e");
        Assert.assertEquals("2e", Boolean.TRUE, barg.getValue());
        barg = (BooleanCLA) cl.arg("-x");
        Assert.assertEquals("2x", Boolean.FALSE, barg.getValue());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--e --dup1 --i"));

        Assert.assertEquals("3 cmd count", 3, cl.size());
        barg = (BooleanCLA) cl.arg("--i");
        Assert.assertEquals("3i", Boolean.TRUE, barg.getValue());
        barg = (BooleanCLA) cl.arg("--e");
        Assert.assertEquals("3e", Boolean.TRUE, barg.getValue());
        barg = (BooleanCLA) cl.arg("--dup1");
        Assert.assertEquals("3x", Boolean.FALSE, barg.getValue());
    }

    @Test
    public void testDouble () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t double -ki -r", "-t double -km -m1 2 --range 1 3000");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 1.0 -m1 2026.25"));
        Assert.assertEquals("1 cmd count", 2, cl.size());
        DoubleCLA sarg = (DoubleCLA) cl.arg("-i");
        Assert.assertEquals("1i", new Double(1.0), sarg.getValue());
        sarg = (DoubleCLA) cl.arg("-m");
        Assert.assertEquals("1m1", new Double(1.0), sarg.getValue(0));
        Assert.assertEquals("1m2", new Double(2026.25), sarg.getValue(1));
    }

    @Test
    public void testFloats () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t float -ki -r", "-t float -km -m1 2 --range 1 3000");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 1.0 -m1 2026.25"));
        Assert.assertEquals("1 cmd count", 2, cl.size());
        FloatCLA sarg = (FloatCLA) cl.arg("-i");
        Assert.assertEquals("1i", new Float(1.0), sarg.getValue());
        sarg = (FloatCLA) cl.arg("-m");
        Assert.assertEquals("1m1", new Float(1.0), sarg.getValue(0));
        Assert.assertEquals("1m2", new Float(2026.25), sarg.getValue(1));
    }
}
