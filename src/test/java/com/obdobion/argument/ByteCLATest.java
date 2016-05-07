package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class ByteCLATest
{

    public byte   byteValue;

    public Byte   byteValueObject;
    public Byte[] byteArrayObject;
    public byte[] byteArray;

    public ByteCLATest()
    {

    }

    @Test
    public void byteArrayObject () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t byte -k b --var byteArrayObject -m1");
        cl.parse(this, "-b 00 01 0 1");
        Assert.assertEquals(4, byteArrayObject.length);
        Assert.assertEquals((byte) 0, byteArrayObject[0].byteValue());
        Assert.assertEquals((byte) 1, byteArrayObject[1].byteValue());
        Assert.assertEquals((byte) '0', byteArrayObject[2].byteValue());
        Assert.assertEquals((byte) '1', byteArrayObject[3].byteValue());
    }

    @Test
    public void byteArrayPrimitive () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t byte -k b --var byteArray -m1");
        cl.parse(this, "-b 00 01 0 1 -b 255");
        Assert.assertEquals(5, byteArray.length);
        Assert.assertEquals((byte) 0, byteArray[0]);
        Assert.assertEquals((byte) 1, byteArray[1]);
        Assert.assertEquals((byte) '0', byteArray[2]);
        Assert.assertEquals((byte) '1', byteArray[3]);
        Assert.assertEquals((byte) 255, byteArray[4]);
    }

    @Test
    public void hexLiteral () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t byte -k b --var byteArray -m1 4");
        cl.parse(this, "-b null, SOH, cr lf");
        Assert.assertEquals(4, byteArray.length);
        Assert.assertEquals((byte) 0, byteArray[0]);
        Assert.assertEquals((byte) 1, byteArray[1]);
        Assert.assertEquals((byte) 13, byteArray[2]);
        Assert.assertEquals((byte) 10, byteArray[3]);
    }

    @Test
    public void singleByteAlpha () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t byte -k b --var byteValue");
        cl.parse(this, "-b a");
        Assert.assertEquals((byte) 97, byteValue);
    }

    @Test
    public void singleByteBadData () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t byte -k b --var byteValue");
            cl.parse(this, "-b abc");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals(
                "abc is not a valid byte code.  Try one of these codes or the corresponding number: NULL(0) SOH(1) STX(2) ETX(3) EOT(4) ENQ(5) ACK(6) BEL(7) BS(8) HT(9) LF(10) VT(11) FF(12) CR(13) SO(14) SI(15) DLE(16) DC1(17) DC2(18) DC3(19) DC4(20) NAK(21) SYN(22) ETB(23) CAN(24) EM(25) SUB(26) ESC(27) FS(28) GS(29) RS(30) US(31) SP(32) ",
                e.getMessage());
        }

    }

    @Test
    public void singleByteNegative () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t byte -k b --var byteValue");
        cl.parse(this, "-b '-52'");
        Assert.assertEquals((byte) -52, byteValue);
    }

    @Test
    public void singleByteNumericMax () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t byte -k b --var byteValue");
        cl.parse(this, "-b 255");
        Assert.assertEquals((byte) 255, byteValue);
    }

    @Test
    public void singleByteNumericMin () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t byte -k b --var byteValue");
        cl.parse(this, "-b 00");
        Assert.assertEquals((byte) 0, byteValue);
    }

    @Test
    public void singleByteObject () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t byte -k b --var byteValueObject");
        cl.parse(this, "-b a");
        Assert.assertEquals((byte) 97, byteValueObject.byteValue());
    }

    @Test
    public void singleByteSingleDigit () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t byte -k b --var byteValue");
        cl.parse(this, "-b 01");
        Assert.assertEquals((byte) 1, byteValue);
    }

    @Test
    public void singleByteSingleDigitAlpha () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t byte -k b --var byteValue");
        cl.parse(this, "-b 1");
        Assert.assertEquals((byte) '1', byteValue);
    }

    @Test
    public void singleByteTooBig () throws Exception
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t byte -k b --var byteValue");
            cl.parse(this, "-b 256");
            Assert.fail("expected exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("256 is too large, max = 255", e.getMessage());
        }

    }
}
