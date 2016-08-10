package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>EnumTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class EnumTest
{
    static public enum TestEnum
    {
        KEY1, KEY2, KEY3
    }

    @Arg(shortName = 'i')
    public TestEnum enum1;

    /**
     * <p>caseSensitivity.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void caseSensitivity() throws Exception
    {
        CmdLine.load(this, "-i Key2");
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }

    /**
     * <p>enumListValidation.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void enumListValidation() throws Exception
    {
        CmdLine.load(this, "-i KeY2");
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }

    /**
     * <p>invalidEnum.</p>
     *
     * @throws java.lang.Exception if any.
     */
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

    /**
     * <p>validEnum.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void validEnum() throws Exception
    {
        CmdLine.load(this, "-i KEY2");
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }
}
