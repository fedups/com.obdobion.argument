package com.obdobion.argument;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.WildFileCLA.WildFile;
import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class WildFileTest
{
    public WildFile wildFile;

    public WildFileTest()
    {

    }

    @Test
    public void patternConversionsNix ()
    {
        String pathSep = "/";
        Assert.assertEquals("^.*$", WildFileCLA.WildFile.convertFileWildCardToRegx("*", pathSep).pattern());
        Assert.assertEquals("^.*\\..*$", WildFileCLA.WildFile.convertFileWildCardToRegx("*.*", pathSep).pattern());
        Assert.assertEquals("^.?$", WildFileCLA.WildFile.convertFileWildCardToRegx("?", pathSep).pattern());
        Assert.assertEquals("^.*.*$", WildFileCLA.WildFile.convertFileWildCardToRegx("**", pathSep).pattern());
        Assert.assertEquals("^.*.*/.*\\.java$", WildFileCLA.WildFile.convertFileWildCardToRegx("**/*.java", pathSep)
                .pattern());
    }

    @Test
    public void patternConversionsNixMatching ()
    {
        String pathSep = "/";

        Assert.assertTrue(Pattern.matches(
                WildFileCLA.WildFile.convertFileWildCardToRegx("*", pathSep).pattern(),
                "any.file.name/With.path"));

        Assert.assertTrue(Pattern.matches(
                WildFileCLA.WildFile.convertFileWildCardToRegx("*.*", pathSep).pattern(),
                "any.file.name/With.path"));

        Assert.assertFalse(Pattern.matches(
                WildFileCLA.WildFile.convertFileWildCardToRegx("*.*", pathSep).pattern(),
                "anyfilename/Withpath"));

        Assert.assertTrue(Pattern.matches(
                WildFileCLA.WildFile.convertFileWildCardToRegx("*/?", "/").pattern(),
                "any path as long as it is a single char file name/a"));
    }

    @Test
    public void patternConversionsWin ()
    {
        String pathSep = "\\";
        Assert.assertEquals("^.*$", WildFileCLA.WildFile.convertFileWildCardToRegx("*", pathSep).pattern());
        Assert.assertEquals("^.*\\..*$", WildFileCLA.WildFile.convertFileWildCardToRegx("*.*", pathSep).pattern());
        Assert.assertEquals("^.?$", WildFileCLA.WildFile.convertFileWildCardToRegx("?", pathSep).pattern());
        Assert.assertEquals("^.*.*$", WildFileCLA.WildFile.convertFileWildCardToRegx("**", pathSep).pattern());
        Assert.assertEquals("^.*.*\\\\.*\\.java$", WildFileCLA.WildFile
                .convertFileWildCardToRegx("**\\\\*.java", pathSep).pattern());
    }

    @Test
    public void validRegex () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t wildfile -k w -m1 -p --var wildFile");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), ".p* .c*"), this);
        /*
         * The current working directory in this case is the root of the
         * project. This is just a convenience and could easily break if that is
         * changed.
         */
        Assert.assertEquals(2, wildFile.files().size());
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
        cl.parse(CommandLineParser.getInstance(
                cl.getCommandPrefix(),
                "src/main/java/com/obdobion/argument/*java"), this);
        Assert.assertNotNull("wildfile files is null", wildFile.files());
        Assert.assertEquals("number of java classes", 37, wildFile.files().size());
    }

    @Test
    public void wildDirSearch1 () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t wildfile -k w -m1 -p --var wildFile");
        cl.parse(CommandLineParser.getInstance(
                cl.getCommandPrefix(),
                "src/*/java/**/*java"), this);
        Assert.assertEquals("number of java classes", 92, wildFile.files().size());
    }

    @Test
    public void wildDirSearch2 () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t wildfile -k w -m1 -p --var wildFile");
        cl.parse(CommandLineParser.getInstance(
                cl.getCommandPrefix(),
                "**/*java"), this);
        Assert.assertEquals("number of java classes", 92, wildFile.files().size());
    }
}
