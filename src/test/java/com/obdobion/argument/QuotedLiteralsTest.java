package com.obdobion.argument;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>QuotedLiteralsTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class QuotedLiteralsTest
{
    @Arg(shortName = 'a', caseSensitive = true)
    String[] string;

    @Arg(shortName = 'i', range = { "-5", "5" })
    int      intA;

    @Arg(shortName = 'f', range = { "-100", "-50" })
    float    floatA;

    /**
     * <p>dosFileNames.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dosFileNames() throws Exception
    {
        CmdLine.load(this, "-a c:\\temp\\somefile.txt\"");
        Assert.assertEquals("c:\\temp\\somefile.txt", string[0]);
    }

    /**
     * <p>dosFileNames2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dosFileNames2() throws Exception
    {
        CmdLine.load(this, "-a \"c:\\temp\\somefile.txt\"");
        Assert.assertEquals("c:\\temp\\somefile.txt", string[0]);
    }

    /**
     * <p>doubleQuotes.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void doubleQuotes() throws Exception
    {
        CmdLine.load(this, "-a \"quoted literal\"");
        Assert.assertEquals("quoted literal", string[0]);
    }

    /**
     * <p>doubleQuotesFromStream.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void doubleQuotesFromStream() throws Exception
    {
        final File test = new File("src/test/java/com/obdobion/argument/QuoteTestData");
        try (final BufferedReader in = new BufferedReader(new FileReader(test)))
        {
            String line = null;
            line = in.readLine();
            Assert.assertEquals("--string 'echo \"This is a quoted string in a command\"'", line);
            CmdLine.load(this, line);
            Assert.assertEquals(
                    "line 1 parsed",
                    "echo \"This is a quoted string in a command\"",
                    string[0]);
        }
    }

    /**
     * <p>negative1.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void negative1() throws Exception
    {
        CmdLine.load(this, "-i '-5' -f '-50'");
        Assert.assertEquals(-5, intA);
        Assert.assertEquals(-50, floatA, 0);
    }

    /**
     * <p>newLine.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void newLine() throws Exception
    {
        CmdLine.load(this, "-a 'quoted\nliteral'");
        Assert.assertEquals("quoted\nliteral", string[0]);
    }

    /**
     * <p>quotesInQuotes.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void quotesInQuotes() throws Exception
    {
        CmdLine.load(this, "-a '\"quoted literal\"', \"'quoted literal'\"");
        Assert.assertEquals("1a", "\"quoted literal\"", string[0]);
        Assert.assertEquals("1b", "'quoted literal'", string[1]);
    }

    /**
     * <p>singleQuotes.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void singleQuotes() throws Exception
    {
        CmdLine.load(this, "-a 'quoted literal'");
        Assert.assertEquals("quoted literal", string[0]);
    }

    /**
     * <p>stringMultipleQuotes.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void stringMultipleQuotes() throws Exception
    {
        CmdLine.load(this, "-a'what''when'");
        Assert.assertEquals("1a0", "what", string[0]);
        Assert.assertEquals("1a1", "when", string[1]);
    }

    /**
     * <p>unixFileNames.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void unixFileNames() throws Exception
    {
        CmdLine.load(this, "-a/etc/apache2/conf/httpd.conf");
        Assert.assertEquals("unix", "/etc/apache2/conf/httpd.conf", string[0]);
    }

    /**
     * <p>urlFileNames.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void urlFileNames() throws Exception
    {
        CmdLine.load(this,
                "-a http://www.littlegraycould.com/index.html 'http://www.littlegraycould.com/index.html?a=1&b=2'");
        Assert.assertEquals("url", "http://www.littlegraycould.com/index.html", string[0]);
        Assert.assertEquals("http://www.littlegraycould.com/index.html?a=1&b=2", string[1]);
    }
}
