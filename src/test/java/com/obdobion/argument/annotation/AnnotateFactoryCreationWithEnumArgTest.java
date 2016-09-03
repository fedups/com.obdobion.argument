package com.obdobion.argument.annotation;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

/**
 * <p>
 * AnnotateFactoryCreationWithEnumArgTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class AnnotateFactoryCreationWithEnumArgTest
{
    public static class Config
    {
        public static Config create(final String keyType)
        {
            final Config cfg = new Config();
            cfg.name = "createdAs" + keyType;
            return cfg;
        }

        private String  name;
        @Arg
        private KeyType type;
    }

    public static enum KeyType
    {
        INT,
        STR,
        CHR,
        FLT
    }

    @Arg(factoryMethod = "create", factoryArgName = "type")
    private Config cfg;

    /**
     * <p>
     * enumArg.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void enumArg() throws Exception
    {
        final ICmdLine cmdParser = new CmdLine("enumArg", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance(cmdParser.getCommandPrefix(),
                new String[] { "--cfg(--type I)" });
        cmdParser.parse(userInput, this);
        Assert.assertEquals("createdAsINT", cfg.name);

    }

}
