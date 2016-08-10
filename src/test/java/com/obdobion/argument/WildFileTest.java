package com.obdobion.argument;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.type.WildFiles;
import com.obdobion.argument.type.WildPath;

/**
 * <p>WildFileTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class WildFileTest
{
    @Arg(positional = true, multimin = 1)
    public WildFiles wildFile;

    /**
     * <p>patternConversionsNix.</p>
     */
    @Test
    public void patternConversionsNix()
    {
        Assert.assertEquals("^.*$", WildPath.convertFileWildCardToRegx("*").pattern());
        Assert.assertEquals("^.*\\..*$", WildPath.convertFileWildCardToRegx("*.*").pattern());
        Assert.assertEquals("^.$", WildPath.convertFileWildCardToRegx("?").pattern());
        Assert.assertEquals("^.*.*$", WildPath.convertFileWildCardToRegx("**").pattern());
        Assert.assertEquals("^.*.*/.*\\.java$", WildPath.convertFileWildCardToRegx("**/*.java").pattern());
    }

    /**
     * <p>patternConversionsNixMatching.</p>
     */
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

    /**
     * <p>patternConversionsWin.</p>
     */
    @Test
    public void patternConversionsWin()
    {
        Assert.assertEquals("^.*$", WildPath.convertFileWildCardToRegx("*").pattern());
        Assert.assertEquals("^.*\\..*$", WildPath.convertFileWildCardToRegx("*.*").pattern());
        Assert.assertEquals("^.$", WildPath.convertFileWildCardToRegx("?").pattern());
        Assert.assertEquals("^.*.*$", WildPath.convertFileWildCardToRegx("**").pattern());
        Assert.assertEquals("^.*.*\\\\.*\\.java$", WildPath.convertFileWildCardToRegx("**\\\\*.java").pattern());
    }

    /**
     * <p>recursiveDirSearchFileNotFound.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void recursiveDirSearchFileNotFound() throws Exception
    {
        CmdLine.load(this, "**/*NOTFINDABLE");
        Assert.assertEquals(0, wildFile.files().size());
    }

    /**
     * <p>specificDirSearch.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void specificDirSearch() throws Exception
    {
        CmdLine.load(this, "src/main/java/com/obdobion/argument/*java");
        Assert.assertTrue(0 < wildFile.files().size());
    }

    /**
     * <p>validRegex.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void validRegex() throws Exception
    {
        CmdLine.load(this, ".project .c*");
        Assert.assertEquals(2, wildFile.files().size());
    }

    /**
     * <p>wildDirSearch1.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void wildDirSearch1() throws Exception
    {
        CmdLine.load(this, "src/*/java/**/*java");
        Assert.assertTrue(0 < wildFile.files().size());
    }

    /**
     * <p>wildDirSearch2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void wildDirSearch2() throws Exception
    {
        CmdLine.load(this, "**/*java");
        Assert.assertTrue(0 < wildFile.files().size());
    }
}
