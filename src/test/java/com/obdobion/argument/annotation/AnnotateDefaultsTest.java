package com.obdobion.argument.annotation;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

/**
 * <p>AnnotateDefaultsTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class AnnotateDefaultsTest
{
    @Arg(shortName = 's', defaultValues = { "abc", "def" })
    private String[] stringDefaults;

    /**
     * <p>allowDefaults.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void allowDefaults() throws Exception
    {
        /*-
        final ICmdLine cmdParser = new CmdLine("allowDefaults", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] {});
        cmdParser.parse(userInput, this);
        */

        new CmdLine().parse(this, new String[] {});
        Assert.assertEquals(2, stringDefaults.length);
        Assert.assertEquals("abc", stringDefaults[0]);
        Assert.assertEquals("def", stringDefaults[1]);
    }

    /**
     * <p>overrideDefaults.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void overrideDefaults() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("overrideDefaults", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "-s xyz" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals(1, stringDefaults.length);
        Assert.assertEquals("xyz", stringDefaults[0]);
    }

}
