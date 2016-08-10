package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>ByteCLATest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class ByteCLATest
{
    static public class ByteCfg
    {
        @Arg
        public byte   byteValue;
        @Arg
        public Byte   byteValueObject;
        @Arg
        public Byte[] byteArrayObject;
        @Arg
        public byte[] byteArray;
    }

    /**
     * <p>Constructor for ByteCLATest.</p>
     */
    public ByteCLATest()
    {
    }

    /**
     * <p>byteArrayObject.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void byteArrayObject() throws Exception
    {
        final ByteCfg target = new ByteCfg();
        CmdLine.load(target, "--byteArrayObject 00 01 0 1");
        Assert.assertEquals(4, target.byteArrayObject.length);
        Assert.assertEquals((byte) 0, target.byteArrayObject[0].byteValue());
        Assert.assertEquals((byte) 1, target.byteArrayObject[1].byteValue());
        Assert.assertEquals((byte) '0', target.byteArrayObject[2].byteValue());
        Assert.assertEquals((byte) '1', target.byteArrayObject[3].byteValue());
    }

    /**
     * <p>byteArrayPrimitive.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void byteArrayPrimitive() throws Exception
    {
        final ByteCfg target = new ByteCfg();
        CmdLine.load(target, "--byteArray 00 01 0 1 --byteArray 255");
        Assert.assertEquals(5, target.byteArray.length);
        Assert.assertEquals((byte) 0, target.byteArray[0]);
        Assert.assertEquals((byte) 1, target.byteArray[1]);
        Assert.assertEquals((byte) '0', target.byteArray[2]);
        Assert.assertEquals((byte) '1', target.byteArray[3]);
        Assert.assertEquals((byte) 255, target.byteArray[4]);
    }

    /**
     * <p>hexLiteral.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void hexLiteral() throws Exception
    {
        final ByteCfg target = new ByteCfg();
        CmdLine.load(target, "--byteArray null, SOH, cr lf");
        Assert.assertEquals(4, target.byteArray.length);
        Assert.assertEquals((byte) 0, target.byteArray[0]);
        Assert.assertEquals((byte) 1, target.byteArray[1]);
        Assert.assertEquals((byte) 13, target.byteArray[2]);
        Assert.assertEquals((byte) 10, target.byteArray[3]);
    }

    /**
     * <p>singleByteAlpha.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void singleByteAlpha() throws Exception
    {
        final ByteCfg target = new ByteCfg();
        CmdLine.load(target, "--byteValue a");
        Assert.assertEquals((byte) 97, target.byteValue);
    }

    /**
     * <p>singleByteBadData.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void singleByteBadData() throws Exception
    {
        try
        {
            final ByteCfg target = new ByteCfg();
            CmdLine.load(target, "--byteValue abc");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals(
                    "abc is not a valid byte code.  Try one of these codes or the corresponding number: NULL(0) SOH(1) STX(2) ETX(3) EOT(4) ENQ(5) ACK(6) BEL(7) BS(8) HT(9) LF(10) VT(11) FF(12) CR(13) SO(14) SI(15) DLE(16) DC1(17) DC2(18) DC3(19) DC4(20) NAK(21) SYN(22) ETB(23) CAN(24) EM(25) SUB(26) ESC(27) FS(28) GS(29) RS(30) US(31) SP(32) ",
                    e.getMessage());
        }
    }

    /**
     * <p>singleByteNegative.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void singleByteNegative() throws Exception
    {
        final ByteCfg target = new ByteCfg();
        CmdLine.load(target, "--byteValue -52");
        Assert.assertEquals((byte) -52, target.byteValue);
    }

    /**
     * <p>singleByteNumericMax.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void singleByteNumericMax() throws Exception
    {
        final ByteCfg target = new ByteCfg();
        CmdLine.load(target, "--byteValue 255");
        Assert.assertEquals((byte) 255, target.byteValue);
    }

    /**
     * <p>singleByteNumericMin.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void singleByteNumericMin() throws Exception
    {
        final ByteCfg target = new ByteCfg();
        CmdLine.load(target, "--byteValue 00");
        Assert.assertEquals((byte) 0, target.byteValue);
    }

    /**
     * <p>singleByteObject.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void singleByteObject() throws Exception
    {
        final ByteCfg target = new ByteCfg();
        CmdLine.load(target, "--byteValueObject a");
        Assert.assertEquals((byte) 97, target.byteValueObject.byteValue());
    }

    /**
     * <p>singleByteSingleDigit.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void singleByteSingleDigit() throws Exception
    {
        final ByteCfg target = new ByteCfg();
        CmdLine.load(target, "--byteValue 01");
        Assert.assertEquals((byte) 1, target.byteValue);
    }

    /**
     * <p>singleByteSingleDigitAlpha.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void singleByteSingleDigitAlpha() throws Exception
    {
        final ByteCfg target = new ByteCfg();
        CmdLine.load(target, "--byteValue 1");
        Assert.assertEquals((byte) '1', target.byteValue);
    }

    /**
     * <p>singleByteTooBig.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void singleByteTooBig() throws Exception
    {
        try
        {
            final ByteCfg target = new ByteCfg();
            CmdLine.load(target, "--byteValue 256");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("256 is too large, max = 255", e.getMessage());
        }

    }
}
