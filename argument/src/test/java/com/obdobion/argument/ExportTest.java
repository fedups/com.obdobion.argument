package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class ExportTest
{

    public ExportTest()
    {

    }

    @Test
    public void exportBoolean () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k a long");

        StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 export", "-a", str.toString());

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--long"));
        cl.exportCommandLine(str);
        Assert.assertEquals("2 export", "-a", str.toString());
    }

    @Test
    public void exportBooleans () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k long", "-t Boolean -k b short");

        StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--long"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 export", "--long", str.toString());

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--long"));
        cl.exportCommandLine(str);
        Assert.assertEquals("2 export", "--long", str.toString());

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--long-b"));
        cl.exportCommandLine(str);
        Assert.assertEquals("3 export", "--long -b", str.toString());

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-b--long"));
        cl.exportCommandLine(str);
        Assert.assertEquals("4 export", "--long -b", str.toString());

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-b"));
        cl.exportCommandLine(str);
        Assert.assertEquals("5 export", "-b", str.toString());
    }

    @Test
    public void exportFloat () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t float -k a -m1");

        StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a 1500.25"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 export", "-a1500.25", str.toString());

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a1.0 2.001"));
        cl.exportCommandLine(str);
        Assert.assertEquals("2 export", "-a1 2.001", str.toString());
    }

    @Test
    public void exportGroup () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-tbegin-kg", "-tboolean-ka", "-tboolean-kb", "-tend-kg");

        final StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g(-ba)"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 export", "-g[-a -b]", str.toString());
    }

    @Test
    public void exportInteger () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t integer -k a -m1");

        StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a 1500"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 export", "-a1500", str.toString());

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a1 2"));
        cl.exportCommandLine(str);
        Assert.assertEquals("2 export", "-a1 2", str.toString());
    }

    @Test
    public void exportMultiGroup () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
                "-tbegin-kg-m1",
                "-tboolean-ka",
                "-tboolean-kb",
                "-tboolean-kc",
                "-tboolean-kd",
                "-tboolean-ke--re",
                "-tend-kg");

        final StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g(-bae)(-cde)(-e)"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 export", "-g[-a -b -e] [-c -d -e] [-e]", str.toString());
    }

    @Test
    public void exportNegativeFloat () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t float -k a -m1");

        final StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a '-1500.25'"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 export", "-a'-1500.25'", str.toString());
    }

    @Test
    public void exportQuoted () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a");

        final StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a \"what\""));
        cl.exportCommandLine(str);
        Assert.assertEquals("export", "-a\"what\"", str.toString());
    }

    @Test
    public void exportQuoted2 () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a");

        final StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a 'echo \"what\"'"));
        cl.exportCommandLine(str);
        Assert.assertEquals("export", "-a\"echo \\\"what\\\"\"", str.toString());
    }

    @Test
    public void exportQuoted3 () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a");

        final StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a \"echo \\\"what\\\"\""));
        cl.exportCommandLine(str);
        Assert.assertEquals("export", "-a\"echo \\\"what\\\"\"", str.toString());
    }

    @Test
    public void exportQuoted4 () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a");

        final StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a \"echo 'what'\""));
        cl.exportCommandLine(str);
        Assert.assertEquals("export", "-a\"echo 'what'\"", str.toString());
    }

    @Test
    public void exportString () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a");

        StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a what"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 export", "-a\"what\"", str.toString());

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a 'wh\"at'"));
        cl.exportCommandLine(str);
        Assert.assertEquals("2 export", "-a\"wh\\\"at\"", str.toString());
    }

    @Test
    public void exportStringParm () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a -m1");

        StringBuilder str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a what"));
        cl.exportCommandLine(str);
        Assert.assertEquals("1 export", "-a\"what\"", str.toString());

        str = new StringBuilder();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a what when"));
        cl.exportCommandLine(str);
        Assert.assertEquals("2 export", "-a\"what\" \"when\"", str.toString());
    }

}
