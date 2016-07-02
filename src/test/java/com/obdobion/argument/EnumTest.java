package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class EnumTest
{

    static public enum TestEnum
    {
        KEY1,
        KEY2,
        KEY3
    }

    public TestEnum enum1;

    public EnumTest()
    {

    }

    @Test
    public void caseSensitivity () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t enum -k i --var enum1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i key2"), this);
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }

    @Test
    public void enumListValidation () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t enum -k i --var enum1 --enum com.obdobion.argument.EnumTest$TestEnum");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i KEY2"), this);
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }

    @Test
    public void enumListValidationNotFound () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t enum -k i --var enum1 --enum TestEnum");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i KEY2"), this);
            // Assert.fail("exception expected");
        } catch (final Exception e)
        {
            Assert.assertEquals("Errors not trapped now", "Enum class not found: TestEnum", e.getMessage());
        }

    }

    @Test
    public void invalidEnum () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t enum -k i --var enum1");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i KEYX"), this);
            Assert.fail("KEYX should have been invalid");
        } catch (final Exception e)
        {
            Assert.assertEquals(
                    "\"KEYX\" is not a valid enum constant for variable \"enum1\" (KEY1, KEY2, KEY3)",
                    e.getMessage());
        }

    }

    @Test
    public void validEnum () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t enum -k i --var enum1");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i KEY2"), this);
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }
}
