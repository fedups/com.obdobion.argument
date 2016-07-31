package com.obdobion.argument;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class UsageTest
{

    public UsageTest()
    {

    }

    @Test
    @Ignore
    public void cmdLineLevel()
            throws Exception
    {

        final List<String> out = new ArrayList<>();
        out.add("CmdLineLevel");
        out.add("");
        out.add("@<filename> will import a specification from the file.");
        out.add("");
        out.add("You can return an argument to its default value with -! <argument>.  This is");
        out.add("important when settings are provided with @files and the current sort needs to");
        out.add("override some of them; eg: -debug and -help can be turned off with -!debug,help");
        out.add("");
        out.add("  [-?]");
        out.add("");
        out.add("    Show this help message.");

        final ByteArrayOutputStream sw = new ByteArrayOutputStream();
        // System.setOut(new PrintStream(sw));

        final CmdLine cl = new CmdLine("CmdLineLevel");
        cl.compile();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--" + CmdLine.MaxHelpCommandName));

        final String[] result = sw.toString().split("\r\n");
        Assert.assertEquals(out.size(), result.length);
        for (int r = 0; r < result.length; r++)
        {
            Assert.assertEquals("line " + r, out.get(r), result[r]);
        }
    }

    @Test
    @Ignore
    public void cmdLineLevelHelp()
            throws Exception
    {

        final List<String> out = new ArrayList<>();
        out.add("CmdLineLevel");
        out.add("");
        out.add("This is a help message.");
        out.add("");
        out.add("@<filename> will import a specification from the file.");
        out.add("");
        out.add("You can return an argument to its default value with -! <argument>.  This is");
        out.add("important when settings are provided with @files and the current sort needs to");
        out.add("override some of them; eg: -debug and -help can be turned off with -!debug,help");
        out.add("");
        out.add("  [-?]");
        out.add("");
        out.add("    Show this help message.");

        final ByteArrayOutputStream sw = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sw));

        final CmdLine cl = new CmdLine("CmdLineLevel",
                "This is a help message.");
        cl.compile();
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-?"));
        System.err.println(sw.toString());

        final String[] result = sw.toString().split("\r\n");
        Assert.assertEquals(out.size(), result.length);
        for (int r = 0; r < result.length; r++)
        {
            Assert.assertEquals("line " + r, out.get(r), result[r]);
        }
    }

    @Test
    public void embedded()
            throws Exception
    {

        final List<String> out = new ArrayList<>();
        out.add("CmdLineLevel");
        out.add("optional: -e");
        out.add("");
        out.add("e");
        out.add("required: -i");

        out.add("");
        out.add("");
        out.add("");
        out.add("-" + CmdLine.MinHelpCommandName + " This help and exit.");
        out.add("--" + CmdLine.MaxHelpCommandName + " for more help.");

        final ByteArrayOutputStream sw = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sw));

        final CmdLine cl = new CmdLine("CmdLineLevel");
        cl.compile("--type begin --key e",
                "--type Integer --key i integer --req --def 5 --ran 3,7 --helpMsg 'This is integer help'",
                "--type end --key e");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-?"));
        System.err.println(sw.toString());

        final String[] result = sw.toString().split("\r\n");
        Assert.assertEquals(out.size(), result.length);
        for (int r = 0; r < result.length; r++)
        {
            Assert.assertEquals("line " + r, out.get(r), result[r]);
        }
    }

    @Test
    // @Ignore
    public void minimal()
            throws Exception
    {

        final List<String> out = new ArrayList<>();
        out.add("required: -p*wx");
        out.add("          --oParm*,yParm,zParm");
        out.add("optional: -abn*");
        out.add("          --cParm,dParm,mParm*");
        out.add("");
        out.add("-" + CmdLine.MinHelpCommandName + " This help and exit.");
        out.add("--" + CmdLine.MaxHelpCommandName + " for more help.");

        final ByteArrayOutputStream sw = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sw));

        final CmdLine cl = new CmdLine();
        cl.compile("-t boolean -k a aParm  ",
                "-t boolean -k b ",
                "-t boolean -k cParm ",
                "-t boolean -k dParm ",
                "-t boolean -k mParm --pos ",
                "-t boolean -k n --pos  ",
                "-t boolean -k oParm --pos --req ",
                "-t boolean -k p --pos --req ",
                "-t boolean -k w wParm --req ",
                "-t boolean -k x --req",
                "-t boolean -k yParm --req",
                "-t boolean -k zParm --req");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-?"));
        System.err.println(sw.toString());

        final String[] result = sw.toString().split("\r\n");
        Assert.assertEquals(out.size(), result.length);
        for (int r = 0; r < result.length; r++)
        {
            Assert.assertEquals("line " + r, out.get(r), result[r]);
        }
    }

    @Test
    @Ignore
    public void testInt()
            throws Exception
    {
        final ByteArrayOutputStream sw = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sw));

        final CmdLine cl = new CmdLine("CmdLineLevel");
        cl.compile(
                "--type Integer --key i integer --req --def 5 --ran 3,7 --help 'This is integer help.  Sometimes it is needed to write a lot of help for very complex parameters, and that help would need to wrap and indent nicely when --help is requested.'",
                "--type String --format .* -m1,5 --case --key s string --def what --list what,when,where,how,why --help 'This is integer help.  Sometimes it is needed to write a lot of help for very complex parameters, and that help would need to wrap and indent nicely when --help is requested.'");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--" + CmdLine.MaxHelpCommandName));

        System.err.println(sw.toString());

        // final String[] result = sw.toString().split("\r\n");
        // Assert.assertEquals(out.size(), result.length);
        // for (int r = 0; r < result.length; r++)
        // {
        // Assert.assertEquals("line " + r, out.get(r), result[r]);
        // }
    }

    @Test
    // @Ignore
    public void wrapping()
            throws Exception
    {

        final List<String> out = new ArrayList<>();
        out.add("optional: -a");
        out.add("          --aParameter,bParameter,cParameter,dParameter,eParameter,fParameter,");
        out.add("          gParameter,hParameter,iParameter,jParameter,kParameter,lParameter,");
        out.add("          mParameter,nParameter,oParameter,pParameter,qParameter,rParameter,");
        out.add("          sParameter,tParameter,uParameter,vParameter,wParameter,xParameter,");
        out.add("          yParameter,zParameter");
        out.add("");
        out.add("-" + CmdLine.MinHelpCommandName + " This help and exit.");
        out.add("--" + CmdLine.MaxHelpCommandName + " for more help.");

        final ByteArrayOutputStream sw = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sw));

        final CmdLine cl = new CmdLine();
        cl.compile("-t boolean -k a aParm  ",
                "-t boolean -k aParameter ",
                "-t boolean -k bParameter ",
                "-t boolean -k cParameter ",
                "-t boolean -k dParameter ",
                "-t boolean -k eParameter ",
                "-t boolean -k fParameter ",
                "-t boolean -k gParameter ",
                "-t boolean -k hParameter ",
                "-t boolean -k iParameter ",
                "-t boolean -k jParameter ",
                "-t boolean -k kParameter ",
                "-t boolean -k lParameter ",
                "-t boolean -k mParameter ",
                "-t boolean -k nParameter ",
                "-t boolean -k oParameter ",
                "-t boolean -k pParameter ",
                "-t boolean -k qParameter ",
                "-t boolean -k rParameter ",
                "-t boolean -k sParameter ",
                "-t boolean -k tParameter ",
                "-t boolean -k uParameter ",
                "-t boolean -k vParameter ",
                "-t boolean -k wParameter ",
                "-t boolean -k xParameter ",
                "-t boolean -k yParameter ",
                "-t boolean -k zParameter");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-" + CmdLine.MinHelpCommandName));

        System.err.println(sw.toString());

        final String[] result = sw.toString().split("\r\n");
        Assert.assertEquals(out.size(), result.length);
        for (int r = 0; r < result.length; r++)
        {
            Assert.assertEquals("line " + r, out.get(r), result[r]);
        }
    }

}
