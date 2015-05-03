package com.obdobion.argument;

import java.io.File;
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

    public File[] wildFile;

    public WildFileTest()
    {

    }

    @Test
    public void patternConversionsNix ()
    {
        String pathSep = "/";
        Assert.assertEquals(".*", WildFileCLA.convertWildCardToRegx("*", pathSep, true).pattern());
        Assert.assertEquals(".*\\..*", WildFileCLA.convertWildCardToRegx("*.*", pathSep, true).pattern());
        Assert.assertEquals(".?", WildFileCLA.convertWildCardToRegx("?", pathSep, true).pattern());
        Assert.assertEquals(".*.*", WildFileCLA.convertWildCardToRegx("**", pathSep, true).pattern());
        Assert.assertEquals(".*.*/.*\\.java", WildFileCLA.convertWildCardToRegx("**/*.java", pathSep, true).pattern());
    }

    @Test
    public void patternConversionsNixMatching ()
    {
        String pathSep = "/";

        Assert.assertTrue(Pattern.matches(
                WildFileCLA.convertWildCardToRegx("*", pathSep, true).pattern(),
                "any.file.name/With.path"));

        Assert.assertTrue(Pattern.matches(
                WildFileCLA.convertWildCardToRegx("*.*", pathSep, true).pattern(),
                "any.file.name/With.path"));

        Assert.assertFalse(Pattern.matches(
                WildFileCLA.convertWildCardToRegx("*.*", pathSep, true).pattern(),
                "anyfilename/Withpath"));

        Assert.assertTrue(Pattern.matches(
                WildFileCLA.convertWildCardToRegx("*/?", "/", true).pattern(),
                "any path as long as it is a single char file name/a"));
    }

    @Test
    public void patternConversionsWin ()
    {
        String pathSep = "\\";
        Assert.assertEquals(".*", WildFileCLA.convertWildCardToRegx("*", pathSep, true).pattern());
        Assert.assertEquals(".*\\..*", WildFileCLA.convertWildCardToRegx("*.*", pathSep, true).pattern());
        Assert.assertEquals(".?", WildFileCLA.convertWildCardToRegx("?", pathSep, true).pattern());
        Assert.assertEquals(".*.*", WildFileCLA.convertWildCardToRegx("**", pathSep, true).pattern());
        Assert.assertEquals(".*.*\\\\.*\\.java", WildFileCLA.convertWildCardToRegx("**\\\\*.java", pathSep, true)
                .pattern());
    }

    @Test
    public void validRegex () throws Exception
    {
        final CmdLine cl = new CmdLine();
        cl.compile("-t wildfile -k w -m1 -p --var wildFile");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "*.launch *.xml"), this);
        /*
         * The current working directory in this case is the root of the
         * project. This is just a convenience and could easily break if that is
         * changed.
         */
        Assert.assertEquals(4, wildFile.length);
    }
}
