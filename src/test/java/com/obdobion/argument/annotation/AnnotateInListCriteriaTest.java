package com.obdobion.argument.annotation;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

/**
 * <p>
 * AnnotateInListCriteriaTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class AnnotateInListCriteriaTest
{
    @Arg(inList = { "abc", "def" })
    private String stringForInList;

    @Arg(inList = { "1", "3", "5" })
    private int    intForInList;

    /**
     * <p>
     * inList.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void inList() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("inList", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--s ABC" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("abc", stringForInList);
    }

    /**
     * <p>
     * inListInt.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void inListInt() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine(null);
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--i 3" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals(3, intForInList);
    }

    /**
     * <p>
     * notInList.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void notInList() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("notInList", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--s AzC" });
        try
        {
            cmdParser.parse(userInput, this);
            Assert.fail("expected exception");

        } catch (final ParseException e)
        {
            Assert.assertEquals("azc is not valid for --stringForInList", e.getMessage());
        }
    }

    /**
     * <p>
     * notInListInt.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void notInListInt() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine(null);
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--i 2" });
        try
        {
            cmdParser.parse(userInput, this);
            Assert.fail("expected exception");

        } catch (final ParseException e)
        {
            Assert.assertEquals("2 is not valid for --intForInList", e.getMessage());
        }
    }
}
