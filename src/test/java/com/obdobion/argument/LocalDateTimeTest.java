package com.obdobion.argument;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
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
public class LocalDateTimeTest
{
    static private void assertValidReturnDate(
            final LocalDateTime toBeVerified,
            final int year,
            final Month month,
            final int _date,
            final int hour,
            final int minute,
            final int second,
            final int nano)
    {
        Assert.assertEquals(year, toBeVerified.getYear());
        Assert.assertEquals(month, toBeVerified.getMonth());
        Assert.assertEquals(_date, toBeVerified.getDayOfMonth());
        Assert.assertEquals(hour, toBeVerified.getHour());
        Assert.assertEquals(minute, toBeVerified.getMinute());
        Assert.assertEquals(second, toBeVerified.getSecond());
        Assert.assertEquals(nano, toBeVerified.getNano());
    }

    @Arg
    DateTimeFormatter   formatter;

    @Arg
    LocalDateTime       ldtArg;

    @Arg(caseSensitive = true, defaultValues = "now")
    LocalDateTime       ldtForHelp;

    @Arg
    LocalDateTime[]     ldtArray;

    @Arg
    List<LocalDateTime> ldtList;

    /**
     * <p>
     * arrays.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void arrays() throws Exception
    {
        CmdLine.load(this, "--ldtArray '2016/08/06' '2016/08/06@14:18'");
        assertValidReturnDate(ldtArray[0], 2016, Month.AUGUST, 6, 0, 0, 0, 0);
        assertValidReturnDate(ldtArray[1], 2016, Month.AUGUST, 6, 14, 18, 0, 0);
    }

    /**
     * <p>
     * format.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void format() throws Exception
    {
        CmdLine.load(this, "--formatter 'MMM-d-yy' --ldtArg '2016/08/06'");
        Assert.assertNotNull(formatter);
        Assert.assertEquals("Aug-6-16", formatter.format(ldtArg));
    }

    /**
     * <p>
     * helpDefaultNow.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void helpDefaultNow() throws Exception
    {
        CmdLine.load(this, "--help");
    }

    /**
     * <p>
     * json.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.4
     */
    @Test
    public void json() throws Exception
    {
        CmdLine.load(this, "--ldtArg '2016-03-24T11:19:52.000-05'");
        assertValidReturnDate(ldtArg, 2016, Month.MARCH, 24, 11, 19, 52, 0);
    }

    /**
     * <p>
     * lists.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void lists() throws Exception
    {
        CmdLine.load(this, "--ldtList '2016/08/06' '2016/08/06@14:18'");
        assertValidReturnDate(ldtList.get(0), 2016, Month.AUGUST, 6, 0, 0, 0, 0);
        assertValidReturnDate(ldtList.get(1), 2016, Month.AUGUST, 6, 14, 18, 0, 0);
    }

    /**
     * <p>
     * simpleUse.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void simpleUse() throws Exception
    {
        CmdLine.load(this, "--ldtArg '2016/08/06'");
        assertValidReturnDate(ldtArg, 2016, Month.AUGUST, 6, 0, 0, 0, 0);
    }
}
