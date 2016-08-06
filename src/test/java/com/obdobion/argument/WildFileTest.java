package com.obdobion.argument;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.type.WildFiles;
import com.obdobion.argument.type.WildPath;

/**
 * @author Chris DeGreef
 *
 */
public class WildFileTest
{
    @Arg(positional = true, multimin = 1)
    public WildFiles wildFile;

    @Test
    public void patternConversionsNix()
    {
        Assert.assertEquals("^.*$", WildPath.convertFileWildCardToRegx("*").pattern());
        Assert.assertEquals("^.*\\..*$", WildPath.convertFileWildCardToRegx("*.*").pattern());
        Assert.assertEquals("^.$", WildPath.convertFileWildCardToRegx("?").pattern());
        Assert.assertEquals("^.*.*$", WildPath.convertFileWildCardToRegx("**").pattern());
        Assert.assertEquals("^.*.*/.*\\.java$", WildPath.convertFileWildCardToRegx("**/*.java").pattern());
    }

    @Test
    public void patternConversionsNixMatching()
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
    public void patternConversionsWin()
    {
        Assert.assertEquals("^.*$", WildPath.convertFileWildCardToRegx("*").pattern());
        Assert.assertEquals("^.*\\..*$", WildPath.convertFileWildCardToRegx("*.*").pattern());
        Assert.assertEquals("^.$", WildPath.convertFileWildCardToRegx("?").pattern());
        Assert.assertEquals("^.*.*$", WildPath.convertFileWildCardToRegx("**").pattern());
        Assert.assertEquals("^.*.*\\\\.*\\.java$", WildPath.convertFileWildCardToRegx("**\\\\*.java").pattern());
    }

    @Test
    public void recursiveDirSearchFileNotFound() throws Exception
    {
        CmdLine.load(this, "**/*NOTFINDABLE");
        Assert.assertEquals(0, wildFile.files().size());
    }

    @Test
    public void specificDirSearch() throws Exception
    {
        CmdLine.load(this, "src/main/java/com/obdobion/argument/*java");
        Assert.assertTrue(0 < wildFile.files().size());
    }

    @Test
    public void validRegex() throws Exception
    {
        CmdLine.load(this, ".project .c*");
        Assert.assertEquals(2, wildFile.files().size());
    }

    @Test
    public void wildDirSearch1() throws Exception
    {
        CmdLine.load(this, "src/*/java/**/*java");
        Assert.assertTrue(0 < wildFile.files().size());
    }

    @Test
    public void wildDirSearch2() throws Exception
    {
        CmdLine.load(this, "**/*java");
        Assert.assertTrue(0 < wildFile.files().size());
    }
}