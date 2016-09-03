package com.obdobion.argument.annotation;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

/**
 * <p>
 * AnnotateFactoryCreationTests class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class AnnotateFactoryCreationTests
{
    public static abstract class AbstractMyClass
    {}

    public static class MyClass extends AbstractMyClass
    {
        static public MyClass create()
        {
            final MyClass thisOne = new MyClass();
            thisOne.name = "NONAME";
            return thisOne;
        }

        static public MyClass create(final String aName)
        {
            final MyClass thisOne = new MyClass();
            thisOne.nameOverride = true;
            thisOne.name = aName;
            return thisOne;
        }

        boolean nameOverride;

        @Arg(positional = true, caseSensitive = true)
        String  name;

        public String name()
        {
            return name;
        }
    }

    @Arg(factoryMethod = "create")
    private MyClass              noArg;

    @Arg(factoryMethod = "create", factoryArgName = "name")
    private MyClass              withArg;

    @Arg(longName = "headerIn",
            allowCamelCaps = true,
            help = "Column definitions defining the file header layout.",
            factoryMethod = "com.obdobion.argument.annotation.AnnotateFactoryCreationTests$MyClass.create",
            multimin = 1,
            excludeArgs = { "csvFieldNumber", "direction" })
    public List<AbstractMyClass> headerInDefs;

    /**
     * <p>
     * listWithFactory.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void listWithFactory() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("listWithFactory", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "--headerIn()" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("NONAME", ((MyClass) headerInDefs.get(0)).name());

    }

    /**
     * <p>
     * testNoParmOnFactory.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void testNoParmOnFactory() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testNoParmOnFactory", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "--noArg()" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("NONAME", noArg.name());

    }

    /**
     * <p>
     * testParmOnFactory.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void testParmOnFactory() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testNoParmOnFactory", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "--withArg('WHAT!') " });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("WHAT!", withArg.name());

    }

}
