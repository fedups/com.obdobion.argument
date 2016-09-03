package com.obdobion.argument;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>
 * DirectiveTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.3.1
 */
public class DirectiveTest
{
    @Arg
    Date   dateVar;

    @Arg
    long   longVar;

    @Arg
    int    intVar;

    @Arg
    float  floatVar;

    @Arg
    String strVar;

    /**
     * <p>
     * asDate.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void asDate() throws Exception
    {
        CmdLine.load(this, "--dateVar _(date('04/09/2008', '=3day'))");
        assertValidReturnDate(2008, Calendar.APRIL, 3, 0, 0, 0, 0);
    }

    /**
     * <p>
     * asFloat.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void asFloat() throws Exception
    {
        CmdLine.load(this, "--floatVar _( 200.02 * 12)");
        Assert.assertEquals(2400.24F, floatVar, 0);
    }

    /**
     * <p>
     * asInt.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void asInt() throws Exception
    {
        CmdLine.load(this, "--intVar _( 200 * 12)");
        Assert.assertEquals(2400, intVar);
    }

    /**
     * <p>
     * asLong.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void asLong() throws Exception
    {
        CmdLine.load(this, "--longVar _( 200 * 12)");
        Assert.assertEquals(2400, longVar);
    }

    private void assertValidReturnDate(
            final int year,
            final int month,
            final int _date,
            final int hour,
            final int minute,
            final int second,
            final int millisecond)
    {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(dateVar);
        Assert.assertEquals(year, cal.get(Calendar.YEAR));
        Assert.assertEquals(month, cal.get(Calendar.MONTH));
        Assert.assertEquals(_date, cal.get(Calendar.DATE));
        Assert.assertEquals(hour, cal.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(minute, cal.get(Calendar.MINUTE));
        Assert.assertEquals(second, cal.get(Calendar.SECOND));
        Assert.assertEquals(millisecond, cal.get(Calendar.MILLISECOND));
    }

    /**
     * <p>
     * asString.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void asString() throws Exception
    {
        CmdLine.load(this, "--strVar _(cat('ab','yz'))");
        Assert.assertEquals("abyz", strVar);
    }

    /**
     * <p>
     * equalSignAsASpecialEquDirective.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void equalSignAsASpecialEquDirective() throws Exception
    {
        CmdLine.load(this, "--dateVar =(date('04/09/2008', '=3day'))");
        assertValidReturnDate(2008, Calendar.APRIL, 3, 0, 0, 0, 0);
    }
}
