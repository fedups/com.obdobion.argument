package com.obdobion.argument;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * This only verifies what is different than the Date tests. And that is just
 * variable assignment.
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class CalendarTest
{
    static private void assertValidReturnDate(
            final Calendar toBeVerified,
            final int year,
            final int month,
            final int _date,
            final int hour,
            final int minute,
            final int second,
            final int millisecond)
    {
        Assert.assertEquals(year, toBeVerified.get(Calendar.YEAR));
        Assert.assertEquals(month, toBeVerified.get(Calendar.MONTH));
        Assert.assertEquals(_date, toBeVerified.get(Calendar.DATE));
        Assert.assertEquals(hour, toBeVerified.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(minute, toBeVerified.get(Calendar.MINUTE));
        Assert.assertEquals(second, toBeVerified.get(Calendar.SECOND));
        Assert.assertEquals(millisecond, toBeVerified.get(Calendar.MILLISECOND));
    }

    @Arg
    Calendar       calArg;

    @Arg(caseSensitive = true, defaultValues = "now")
    Calendar       calForHelp;

    @Arg
    Calendar[]     calArray;

    @Arg
    List<Calendar> calList;

    /**
     * <p>
     * arrays.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     */
    @Test
    public void arrays() throws Exception
    {
        CmdLine.load(this, "--calArray '2016/08/06' '2016/08/06@14:18'");
        assertValidReturnDate(calArray[0], 2016, Calendar.AUGUST, 6, 0, 0, 0, 0);
        assertValidReturnDate(calArray[1], 2016, Calendar.AUGUST, 6, 14, 18, 0, 0);
    }

    /**
     * <p>
     * helpDefaultNow.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     */
    @Test
    public void helpDefaultNow() throws Exception
    {
        CmdLine.load(this, "--help");
    }

    @Test
    public void json() throws Exception
    {
        CmdLine.load(this, "--calarg '2016-03-24T11:19:52.000-05'");
        assertValidReturnDate(calArg, 2016, Calendar.MARCH, 24, 11, 19, 52, 0);
    }

    /**
     * <p>
     * lists.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     */
    @Test
    public void lists() throws Exception
    {
        CmdLine.load(this, "--calList '2016/08/06' '2016/08/06@14:18'");
        assertValidReturnDate(calList.get(0), 2016, Calendar.AUGUST, 6, 0, 0, 0, 0);
        assertValidReturnDate(calList.get(1), 2016, Calendar.AUGUST, 6, 14, 18, 0, 0);
    }

    /**
     * <p>
     * simpleUse.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     */
    @Test
    public void simpleUse() throws Exception
    {
        CmdLine.load(this, "--calArg '2016/08/06'");
        assertValidReturnDate(calArg, 2016, Calendar.AUGUST, 6, 0, 0, 0, 0);
    }
}
