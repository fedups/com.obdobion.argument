package com.obdobion.argument;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class WildFileTest
{
    public WildFiles wildFile;

    public WildFileTest()
    {

    }

    @Test
    public void patternConversionsNix ()
    {
        Assert.assertEquals("^.*$", WildPath.convertFileWildCardToRegx("*").pattern());
        Assert.assertEquals("^.*\\..*$", WildPath.convertFileWildCardToRegx("*.*").pattern());
        Assert.assertEquals("^.$", WildPath.convertFileWildCardToRegx("?").pattern());
        Assert.assertEquals("^.*.*$", WildPath.convertFileWildCardToRegx("**").pattern());
        Assert.assertEquals("^.*.*/.*\\.java$", WildPath.convertFileWildCardToRegx("**/*.java").pattern());
    }

    @Test
    public void patternConversionsNixMatching ()
    {
        Assert.assertTrue(Pattern
                .matches(WildPath.convertFileWildCardToRegx("*").pattern(), "any.file.name/With.path"));

        Assert.assertTrue(Pattern
                .matches(WildPath.convertFileWildCardToRegx("*.*").pattern(), "any.file.name/With.path"));

        Assert.assertFalse(Pattern
                .matches(WildPath.convertFileWildCardToRegx("*.*").pattern(), "anyfilename/Withpath"));

        Assert.assertTrue(Pattern.matches(WildPath.convertFileWildCardToRegx("*/?")
                .pattern(), "any path as long as it is a single char file name/a"));
    }

    @Test
    public void patternConversionsWin ()
    {
        Assert.assertEquals("^.*$", WildPath.convertFileWildCardToRegx("*").pattern());
        Assert.assertEquals("^.*\\..*$", WildPath.convertFileWildCardToRegx("*.*").pattern());
        Assert.assertEquals("^.$", WildPath.convertFileWildCardToRegx("?").pattern());
        Assert.assertEquals("^.*.*$", WildPath.convertFileWildCardToRegx("**").pattern());
        Assert.assertEquals("^.*.*\\\\.*\\.java$", WildPath.convertFileWildCardToRegx("**\\\\*.java").pattern());
    }

    @Test
    public void recursiveDirSearchFileNotFound () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t wildfile -k w -m1 -p --var wildFile");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "**/*NOTFINDABLE"), this);
        Assert.assertEquals(0, wildFile.files().size());
    }

    @Test
    public void specificDirSearch () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t wildfile -k w -m1 -p --var wildFile");
        cl.parse(CommandLineParser
                .getInstance(cl.getCommandPrefix(), "src/main/java/com/obdobion/argument/*java"), this);
        Assert.assertNotNull("wildfile files is null", wildFile.files());
        Assert.assertEquals("number of java classes", 41, wildFile.files().size());
    }

    @Test
    public void validRegex () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t wildfile -k w -m1 -p --var wildFile");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), ".project .c*"), this);
        /*
         * The current working directory in this case is the root of the
         * project. This is just a convenience and could easily break if that is
         * changed.
         */
        Assert.assertEquals(2, wildFile.files().size());
    }

    @Test
    public void wildDirSearch1 () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t wildfile -k w -m1 -p --var wildFile");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "src/*/java/**/*java"), this);
        Assert.assertEquals("number of java classes", 91, wildFile.files().size());
    }

    @Test
    public void wildDirSearch2 () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t wildfile -k w -m1 -p --var wildFile");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "**/*java"), this);
        Assert.assertEquals("number of java classes", 91, wildFile.files().size());
    }
}