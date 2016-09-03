package com.obdobion.argument.help;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.net.URL;

import org.junit.Assert;

/**
 * <p>HelpHelp class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.3.4
 */
public class HelpHelp
{
    PrintStream           standardOut;
    PrintStream           overrideOut;
    ByteArrayOutputStream baos;

    /**
     * <p>compare.</p>
     *
     * @param fileName a {@link java.lang.String} object.
     * @param debug a boolean.
     * @throws java.lang.Exception if any.
     */
    public void compare(final String fileName, final boolean debug) throws Exception
    {
        final URL url = getClass().getClassLoader().getResource(fileName);
        final File file = new File(url.toURI());

        final String[] actualLines = baos.toString().split("\n");

        if (debug)
        {
            for (final String actual : actualLines)
                System.out.print(actual);
            Assert.fail("always fails in debug mode");
        }

        try (final BufferedReader sorted = new BufferedReader(new FileReader(file)))
        {
            for (final String actual : actualLines)
            {
                final String expected = sorted.readLine();
                if (expected == null)
                    Assert.fail("more lines produced than expected");
                else
                    Assert.assertEquals(expected.trim(), actual.trim());
            }
        }
    }

    /**
     * <p>startCapture.</p>
     */
    public void startCapture()
    {
        baos = new ByteArrayOutputStream();
        overrideOut = new PrintStream(baos);
        standardOut = System.out;
        System.setOut(overrideOut);
    }

    /**
     * <p>stopCapture.</p>
     */
    public void stopCapture()
    {
        System.setOut(standardOut);
        overrideOut.flush();
        overrideOut.close();
    }
}
