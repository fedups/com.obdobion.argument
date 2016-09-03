package com.obdobion.argument.annotation;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

/**
 * This class shows that an array can be of an interface type as long as the
 * instanceClass is specified, there can be multiple args contributing to the
 * array, each implementation class can have distinct or inherited args.
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class AnnotateInstanceOverrideTest
{
    public static class FirstNameOnly extends NamedInstance
    {}

    public static interface INamed
    {
        public String getName();
    }

    public static abstract class NamedInstance implements INamed
    {
        @Arg(positional = true, caseSensitive = true, required = true)
        private String name;

        @Override
        public String getName()
        {
            return name;
        }
    }

    public static class WithLastName extends NamedInstance
    {
        @Arg(caseSensitive = true, required = true)
        private String lastName;
    }

    @Arg(shortName = 'p',
            longName = "pPoly",
            instanceClass = "com.obdobion.argument.annotation.AnnotateInstanceOverrideTest$WithLastName",
            multimin = 1)
    @Arg(shortName = 'q',
            longName = "qPoly",
            instanceClass = "com.obdobion.argument.annotation.AnnotateInstanceOverrideTest$FirstNameOnly",
            multimin = 2)
    private INamed[] polymorphic;

    /**
     * <p>
     * instanceOverride.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void instanceOverride() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("instanceOverride", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "-p(pName --l McLastname) -q(oneFirstName)(anotherFirstName)" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals(WithLastName.class, polymorphic[0].getClass());
        Assert.assertEquals(FirstNameOnly.class, polymorphic[1].getClass());
        Assert.assertEquals(FirstNameOnly.class, polymorphic[2].getClass());
        Assert.assertEquals("pName", polymorphic[0].getName());
        Assert.assertEquals("oneFirstName", polymorphic[1].getName());
        Assert.assertEquals("anotherFirstName", polymorphic[2].getName());
    }
}
