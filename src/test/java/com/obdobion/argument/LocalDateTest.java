package com.obdobion.argument;

import java.time.LocalDate;
import java.time.Month;
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
public class LocalDateTest
{
    static private void assertValidReturnDate(
            final LocalDate toBeVerified,
            final int year,
            final Month month,
            final int _date)
    {
        Assert.assertEquals(year, toBeVerified.getYear());
        Assert.assertEquals(month, toBeVerified.getMonth());
        Assert.assertEquals(_date, toBeVerified.getDayOfMonth());
    }

    @Arg
    LocalDate       ldtArg;

    @Arg(caseSensitive = true, defaultValues = "now")
    LocalDate       ldtForHelp;

    @Arg
    LocalDate[]     ldtArray;

    @Arg
    List<LocalDate> ldtList;

    /**
     * <p>
     * arrays.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void arrays() throws Exception
    {
        CmdLine.load(this, "--ldtArray '2016/08/06' '2016/08/06@14:18'");
        assertValidReturnDate(ldtArray[0], 2016, Month.AUGUST, 6);
        assertValidReturnDate(ldtArray[1], 2016, Month.AUGUST, 6);
    }

    /**
     * <p>
     * helpDefaultNow.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void helpDefaultNow() throws Exception
    {
        CmdLine.load(this, "--help");
    }

    /**
     * <p>json.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void json() throws Exception
    {
        CmdLine.load(this, "--ldtArg '2016-03-24T11:19:52.000-05'");
        assertValidReturnDate(ldtArg, 2016, Month.MARCH, 24);
    }

    /**
     * <p>
     * lists.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void lists() throws Exception
    {
        CmdLine.load(this, "--ldtList '2016/08/06' '2016/08/06@14:18'");
        assertValidReturnDate(ldtList.get(0), 2016, Month.AUGUST, 6);
        assertValidReturnDate(ldtList.get(1), 2016, Month.AUGUST, 6);
    }

    /**
     * <p>
     * simpleUse.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void simpleUse() throws Exception
    {
        CmdLine.load(this, "--ldtArg '2016/08/06'");
        assertValidReturnDate(ldtArg, 2016, Month.AUGUST, 6);
    }
}
