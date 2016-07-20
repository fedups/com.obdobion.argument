package com.obdobion.argument;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class QuotedLiteralsTest
{

    public QuotedLiteralsTest() throws Exception
    {

    }

    @Test
    public void dosFileNames() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a -m1 ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a c:\\temp\\somefile.txt\""));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("url", "c:\\temp\\somefile.txt", cl.arg("-a").getValue(0));
    }

    @Test
    public void dosFileNames2() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a -m1 ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a \"c:\\temp\\somefile.txt\""));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("url", "c:\\temp\\somefile.txt", cl.arg("-a").getValue());
    }

    @Test
    public void doubleQuotes() throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a item1 ", "-t boolean -k b item2 ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a \"quoted literal\""));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1a", "quoted literal", cl.arg("-a").getValue());
    }

    @Test
    public void doubleQuotesFromStream() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k cmd -c");

        final File test = new File("src/test/java/com/obdobion/argument/QuoteTestData");
        // System.out.println(test.getAbsolutePath());

        final BufferedReader in = new BufferedReader(new FileReader(test));
        try
        {

            String line = null;

            line = in.readLine();
            Assert.assertEquals("line 1 raw", "--cmd 'echo \"This is a quoted string in a command\"'", line);
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), line));
            Assert.assertEquals(
                    "line 1 parsed",
                    "echo \"This is a quoted string in a command\"",
                    cl.arg("--cmd")
                            .getValue());
        } finally
        {
            in.close();
        }
    }

    @Test
    public void negative() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t integer -k a --range '-5' 5", "-t float -k b --range '-100' '-50' ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a '-5'"));
        Assert.assertEquals("1 int -5", 1, cl.size());
        Assert.assertEquals("1a", -5, ((Integer) cl.arg("-a").getValue()).intValue());

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-b '-50'"));
        Assert.assertEquals("1 float -5", 1, cl.size());
        Assert.assertEquals("1a", -50F, ((Float) cl.arg("-b").getValue()).floatValue(), 0);
    }

    @Test
    public void newLine() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a item1 ", "-t boolean -k b item2 ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a 'quoted\nliteral'"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1a", "quoted\nliteral", cl.arg("-a").getValue());
    }

    @Test
    public void quotesInQuotes() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a item1 ", "-t string -k b item2 ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(),
                "-a '\"quoted literal\"' -b \"'quoted literal'\""));
        Assert.assertEquals("1 cmd count", 2, cl.size());
        Assert.assertEquals("1a", "\"quoted literal\"", cl.arg("-a").getValue());
        Assert.assertEquals("1b", "'quoted literal'", cl.arg("-b").getValue());
    }

    @Test
    public void singleQuotes() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a item1 ", "-t boolean -k b item2 ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a 'quoted literal'"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1a", "quoted literal", cl.arg("-a").getValue());
    }

    @Test
    public void stringMultipleQuotes() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a item1 -m1");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "--item'what''when'"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1a0", "what", cl.arg("-a").getValue(0));
        Assert.assertEquals("1a1", "when", cl.arg("-a").getValue(1));
    }

    @Test
    public void unixFileNames() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a -m1 ");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a /etc/apache2/conf/httpd.conf"));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("unix", "/etc/apache2/conf/httpd.conf", cl.arg("-a").getValue());
    }

    @Test
    public void urlFileNames() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k a -m1 ");

        cl.parse(CommandLineParser.getInstance(
                cl.getCommandPrefix(),
                "-a http://www.littlegraycould.com/index.html 'http://www.littlegraycould.com/index.html?a=1&b=2'"));

        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("url", "http://www.littlegraycould.com/index.html", cl.arg("-a").getValue(0));
        Assert.assertEquals(
                "url",
                "http://www.littlegraycould.com/index.html?a=1&b=2",
                cl.arg("-a").getValue(1));
    }
}
