package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class EnumTest
{
    static public enum TestEnum
    {
        KEY1, KEY2, KEY3
    }

    @Arg(shortName = 'i')
    public TestEnum enum1;

    @Test
    public void caseSensitivity() throws Exception
    {
        CmdLine.load(this, "-i Key2");
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }

    @Test
    public void enumListValidation() throws Exception
    {
        CmdLine.load(this, "-i KeY2");
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }

    @Test
    public void invalidEnum() throws Exception
    {
        try
        {
            CmdLine.load(this, "-i KEYX");
        } catch (final Exception e)
        {
            Assert.assertEquals("\"keyx\" is not a valid enum constant for variable \"enum1\" (KEY1, KEY2, KEY3)",
                    e.getMessage());
        }
    }

    @Test
    public void validEnum() throws Exception
    {
        CmdLine.load(this, "-i KEY2");
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }
}
