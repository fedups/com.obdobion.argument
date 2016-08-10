package com.obdobion.argument.annotation;

import java.awt.Dimension;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

/**
 * <p>AnnotateStandardClassesTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class AnnotateStandardClassesTest
{
    @Arg
    @Arg(variable = "width", positional = true, help = "The width of the dimension")
    @Arg(variable = "height", positional = true, help = "The height of the dimension")
    private Dimension       dim;

    @Arg
    @Arg(variable = "width", positional = true, help = "The width of the dimension")
    @Arg(variable = "height", positional = true, help = "The height of the dimension")
    private Dimension[]     dimArray;

    @Arg
    @Arg(variable = "width", positional = true, help = "The width of the dimension")
    @Arg(variable = "height", positional = true, help = "The height of the dimension")
    private List<Dimension> dimList;

    /**
     * <p>dim.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dim() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine(null);
        final IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "--dim(1,2)" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals(1, dim.getWidth(), 0.1D);
        Assert.assertEquals(2, dim.getHeight(), 0.1D);
    }

    /**
     * <p>dimArray.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dimArray() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine(null);
        final IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "--dimArray(1,2)(3,4)" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals(1, dimArray[0].getWidth(), 0.1D);
        Assert.assertEquals(2, dimArray[0].getHeight(), 0.1D);
        Assert.assertEquals(3, dimArray[1].getWidth(), 0.1D);
        Assert.assertEquals(4, dimArray[1].getHeight(), 0.1D);
    }

    /**
     * <p>dimList.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dimList() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine(null);
        final IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "--dimList(1,2)(3,4)" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals(1, dimList.get(0).getWidth(), 0.1D);
        Assert.assertEquals(2, dimList.get(0).getHeight(), 0.1D);
        Assert.assertEquals(3, dimList.get(1).getWidth(), 0.1D);
        Assert.assertEquals(4, dimList.get(1).getHeight(), 0.1D);
    }
}
