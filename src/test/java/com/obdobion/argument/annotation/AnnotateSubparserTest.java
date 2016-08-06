package com.obdobion.argument.annotation;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

public class AnnotateSubparserTest
{
    static final public class EmbeddedA
    {
        @Arg
        private String     embeddedAString;

        @Arg
        private EmbeddedAC embeddedAC;
    }

    static final public class EmbeddedAC
    {
        @Arg
        private String embeddedACString;
    }

    static final public class EmbeddedB
    {
        @Arg
        private String embeddedBString;
    }

    static final public class RootConfig
    {
        @Arg
        private String    rootConfigString;

        @Arg
        private EmbeddedA embeddedA;

        @Arg
        private EmbeddedB embeddedB;
    }

    private final RootConfig cfg = new RootConfig();

    @Test
    public void testDefaults() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testDefaults", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "" });
        cmdParser.parse(userInput, cfg);
        Assert.assertNull(cfg.embeddedA);
    }

    @Test
    public void testUsage() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testUsage", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] { "--help" });
        cmdParser.parse(userInput, cfg);
    }

    @Test
    public void testValues() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("testDefaults", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-',
                new String[] {
                        "--rootConfigString hi",
                        "--embeddedA(--embeddedAString hello --embeddedAC(--embeddedACString heyThere))",
                        "--embeddedB(--embeddedBString hola)" });
        cmdParser.parse(userInput, cfg);

        Assert.assertEquals("hi", cfg.rootConfigString);
        Assert.assertEquals("hello", cfg.embeddedA.embeddedAString);
        Assert.assertEquals("heythere", cfg.embeddedA.embeddedAC.embeddedACString);
        Assert.assertEquals("hola", cfg.embeddedB.embeddedBString);
    }

}
