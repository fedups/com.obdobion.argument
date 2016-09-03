package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>
 * EnumTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class EnumTest
{
    static public enum SplineBoxTest
    {
        edgetobottom,
        EdgeToCenter,
        EdgeToTop
    }

    static public enum TestEnum
    {
        KEY1,
        KEY2,
        KEY3
    }

    static public enum TestEnumForCamelCapsA
    {
        AllowingCamelCaps,
        NotAllowingCamelCaps
    }

    static public enum TestEnumForCamelCapsB
    {
        AllowingCamelCaps,
        AllowingCamelCaps2
    }

    @Arg(shortName = 'i')
    public TestEnum              enum1;

    @Arg(shortName = 'A')
    public TestEnumForCamelCapsA enumA;

    @Arg(shortName = 'B')
    public TestEnumForCamelCapsB enumB;

    @Arg(shortName = 'S')
    public SplineBoxTest         sbox;

    /**
     * <p>
     * splineBoxTest.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void camelCapConvertsToNull() throws Exception
    {
        CmdLine.load(this, "-S ETT");
        Assert.assertEquals(SplineBoxTest.EdgeToTop, sbox);
    }

    /**
     * <p>
     * camelCaps.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void camelCaps() throws Exception
    {
        CmdLine.load(this, "-A ACC");
        Assert.assertEquals(TestEnumForCamelCapsA.AllowingCamelCaps, enumA);
    }

    /**
     * <p>
     * camelCapsWithNumber.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void camelCapsWithNumber() throws Exception
    {
        CmdLine.load(this, "-B acc2");
        Assert.assertEquals(TestEnumForCamelCapsB.AllowingCamelCaps2, enumB);
    }

    /**
     * <p>
     * caseSensitivity.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void caseSensitivity() throws Exception
    {
        CmdLine.load(this, "-i Key2");
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }

    /**
     * <p>
     * enumListValidation.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void enumListValidation() throws Exception
    {
        CmdLine.load(this, "-i KeY2");
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }

    /**
     * <p>
     * invalidEnum.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void invalidEnum() throws Exception
    {
        try
        {
            CmdLine.load(this, "-i KEYX");
        } catch (final Exception e)
        {
            Assert.assertEquals(
                    "\"keyx\" is not a valid enum constant for variable \"enum1\" (KEY1, KEY2, KEY3)",
                    e.getMessage());
        }
    }

    /**
     * <p>
     * tooManyCamelCaps.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void tooManyCamelCaps() throws Exception
    {
        try
        {
            CmdLine.load(this, "-B ACC");
        } catch (final Exception e)
        {
            Assert.assertEquals(
                    "\"acc\" is not a unique enum constant for variable \"enumB\" (AllowingCamelCaps, AllowingCamelCaps2)",
                    e.getMessage());
        }
    }

    /**
     * <p>
     * validEnum.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void validEnum() throws Exception
    {
        CmdLine.load(this, "-i KEY2");
        Assert.assertEquals(TestEnum.KEY2, enum1);
    }
}
