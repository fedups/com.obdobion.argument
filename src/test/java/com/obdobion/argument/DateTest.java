package com.obdobion.argument;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>DateTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class DateTest
{

    @Arg(shortName = 'r')
    public Date date;

    @Arg(shortName = 'd', format = "yyyy-MM-dd")
    Date        date2;

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
        cal.setTime(date);
        Assert.assertEquals(year, cal.get(Calendar.YEAR));
        Assert.assertEquals(month, cal.get(Calendar.MONTH));
        Assert.assertEquals(_date, cal.get(Calendar.DATE));
        Assert.assertEquals(hour, cal.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(minute, cal.get(Calendar.MINUTE));
        Assert.assertEquals(second, cal.get(Calendar.SECOND));
        Assert.assertEquals(millisecond, cal.get(Calendar.MILLISECOND));
    }

    /**
     * <p>dateDirectiveAbsDay.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveAbsDay() throws Exception
    {
        CmdLine.load(this, "-r _date(04/09/2008@13:14:15.016 =3day)");
        assertValidReturnDate(2008, Calendar.APRIL, 3, 0, 0, 0, 0);
    }

    /**
     * <p>dateDirectiveAbsMin.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveAbsMin() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =5min)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 5, 15, 16);
    }

    /**
     * <p>dateDirectiveAbsMonth.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveAbsMonth() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =5MONTH)");
        assertValidReturnDate(2008, Calendar.MAY, 9, 13, 14, 15, 16);
    }

    /**
     * <p>dateDirectiveAbsMs.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveAbsMs() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =5ms)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 15, 5);
    }

    /**
     * <p>dateDirectiveAbsSec.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveAbsSec() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =5sec)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 5, 16);
    }

    /**
     * <p>dateDirectiveAbsYear.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveAbsYear() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =2011YEAR)");
        assertValidReturnDate(2011, Calendar.APRIL, 9, 13, 14, 15, 16);
    }

    /**
     * <p>dateDirectiveBeginOfYesterday.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveBeginOfYesterday() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 -1d =bd)");
        assertValidReturnDate(2008, Calendar.APRIL, 8, 0, 0, 0, 0);
    }

    /**
     * <p>dateDirectiveEndOfYesterday.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveEndOfYesterday() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 -1d =ed)");
        assertValidReturnDate(2008, Calendar.APRIL, 8, 23, 59, 59, 999);
    }

    /**
     * <p>dateDirectiveMinusDay.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveMinusDay() throws Exception
    {
        CmdLine.load(this, "-r _date(04/09/2008@13:14:15.016 -1day -1day)");
        assertValidReturnDate(2008, Calendar.APRIL, 7, 0, 0, 0, 0);
    }

    /**
     * <p>dateDirectiveMinusMin.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveMinusMin() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 -5minutes)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 9, 15, 16);
    }

    /**
     * <p>dateDirectiveMinusMonth.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveMinusMonth() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 -5Months)");
        assertValidReturnDate(2007, Calendar.NOVEMBER, 9, 13, 14, 15, 16);
    }

    /**
     * <p>dateDirectiveMinusMs.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveMinusMs() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 -5ms)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 15, 11);
    }

    /**
     * <p>dateDirectiveMinusSec.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveMinusSec() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 -5seconds)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 10, 16);
    }

    /**
     * <p>dateDirectiveMinusWeekOfYear.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveMinusWeekOfYear() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(12/30/2010@13:14:15.016 -5w)");
        assertValidReturnDate(2010, Calendar.NOVEMBER, 25, 13, 14, 15, 16);
    }

    /**
     * <p>dateDirectiveMinusYear.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveMinusYear() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 -5YEARS)");
        assertValidReturnDate(2003, Calendar.APRIL, 9, 13, 14, 15, 16);
    }

    /**
     * <p>dateDirectiveNowMinus5Min.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveNowMinus5Min() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(now -5min)");
    }

    /**
     * <p>dateDirectivePlusDay.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectivePlusDay() throws Exception
    {
        CmdLine.load(this, "-r _date(04/09/2008@13:14:15.016 +2day)");
        assertValidReturnDate(2008, Calendar.APRIL, 11, 0, 0, 0, 0);
    }

    /**
     * <p>dateDirectivePlusMin.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectivePlusMin() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 +5minutes)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 19, 15, 16);
    }

    /**
     * <p>dateDirectivePlusMonth.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectivePlusMonth() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 +5mon)");
        assertValidReturnDate(2008, Calendar.SEPTEMBER, 9, 13, 14, 15, 16);
    }

    /**
     * <p>dateDirectivePlusMs.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectivePlusMs() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 +34l)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 15, 50);
    }

    /**
     * <p>dateDirectivePlusSec.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectivePlusSec() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 +5seconds)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 20, 16);
    }

    /**
     * <p>dateDirectivePlusYear.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectivePlusYear() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 +5y)");
        assertValidReturnDate(2013, Calendar.APRIL, 9, 13, 14, 15, 16);
    }

    /**
     * <p>dateDirectiveWithoutAdj.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateDirectiveWithoutAdj() throws Exception
    {
        CmdLine.load(this, "-r _date(04/09/2008@13:14:15.016)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 0, 0, 0, 0);
    }

    /**
     * <p>dateTimeDirectiveBMin.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateTimeDirectiveBMin() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =BMin)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 0, 0);
    }

    /**
     * <p>dateTimeDirectiveBOD.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateTimeDirectiveBOD() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =BDay)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 0, 0, 0, 0);
    }

    /**
     * <p>dateTimeDirectiveBOM.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateTimeDirectiveBOM() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =BMON)");
        assertValidReturnDate(2008, Calendar.APRIL, 1, 0, 0, 0, 0);
    }

    /**
     * <p>dateTimeDirectiveBOY.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateTimeDirectiveBOY() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =By)");
        assertValidReturnDate(2008, Calendar.JANUARY, 1, 0, 0, 0, 0);
    }

    /**
     * <p>dateTimeDirectiveEHour.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateTimeDirectiveEHour() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =EHour)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 59, 59, 999);
    }

    /**
     * <p>dateTimeDirectiveEMin.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateTimeDirectiveEMin() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =EMin)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 59, 999);
    }

    /**
     * <p>dateTimeDirectiveEOD.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateTimeDirectiveEOD() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =EDay)");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 23, 59, 59, 999);
    }

    /**
     * <p>dateTimeDirectiveEOM.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateTimeDirectiveEOM() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =EMon)");
        assertValidReturnDate(2008, Calendar.APRIL, 30, 23, 59, 59, 999);
    }

    /**
     * <p>dateTimeDirectiveEOY.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dateTimeDirectiveEOY() throws Exception
    {
        CmdLine.load(this, "-r _dateTime(04/09/2008@13:14:15.016 =EY)");
        assertValidReturnDate(2008, Calendar.DECEMBER, 31, 23, 59, 59, 999);
    }

    /**
     * <p>error.</p>
     */
    @Test
    public void error()
    {
        try
        {
            CmdLine.load(this, "-d'2008/04/09'");
            Assert.fail("should have Assert.failed");

        } catch (final Exception e)
        {
            Assert.assertEquals("date --date2(-d) yyyy-MM-dd: Unparseable date: \"2008/04/09\"", e.getMessage());
        }
    }

    /**
     * <p>lastWeekSat.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void lastWeekSat() throws Exception
    {
        CmdLine.load(this, "-r _date(2010/12/31 -1week =7dayofweek)");
        assertValidReturnDate(2010, Calendar.DECEMBER, 25, 0, 0, 0, 0);
    }

    /**
     * <p>nextWeekSat.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void nextWeekSat() throws Exception
    {
        CmdLine.load(this, "-r _date(2010/12/31 +1week =7dayofweek)");
        assertValidReturnDate(2011, Calendar.JANUARY, 8, 0, 0, 0, 0);
    }

    /**
     * <p>predefined_fullTime.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_fullTime() throws Exception
    {
        CmdLine.load(this, "-r'04/09/2008 13:14:15.016'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 15, 16);
    }

    /**
     * <p>predefined_fullTime_underbar.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_fullTime_underbar() throws Exception
    {
        CmdLine.load(this, "-r04/09/2008@13:14:15.016");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 15, 16);
    }

    /**
     * <p>predefined_hh.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_hh() throws Exception
    {
        CmdLine.load(this, "-r'4/9/2008 13'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 0, 0, 0);
    }

    /**
     * <p>predefined_hhmm.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_hhmm() throws Exception
    {
        CmdLine.load(this, "-r'04/09/2008 13:14'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 0, 0);
    }

    /**
     * <p>predefined_hhmmss.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_hhmmss() throws Exception
    {
        CmdLine.load(this, "-r'04/09/2008 13:14:15'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 13, 14, 15, 0);
    }

    /**
     * <p>predefined_hhmmss_1digit.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_hhmmss_1digit() throws Exception
    {
        CmdLine.load(this, "-r'04/09/2008 1:4:5'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 1, 4, 5, 0);
    }

    /**
     * <p>predefined_HourOnly.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_HourOnly() throws Exception
    {
        CmdLine.load(this, "-r'17'");
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE),
                17,
                0,
                0,
                0);
    }

    /**
     * <p>predefined_mdy.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_mdy() throws Exception
    {
        CmdLine.load(this, "-r'04-09-2008'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 0, 0, 0, 0);
    }

    /**
     * <p>predefined_mdy2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_mdy2() throws Exception
    {
        CmdLine.load(this, "-r'04/09/2008'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 0, 0, 0, 0);
    }

    /**
     * <p>predefined_now.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_now() throws Exception
    {
        CmdLine.load(this, "-r'now'");
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                cal.get(Calendar.MILLISECOND));
    }

    /**
     * <p>predefined_TimeOnly.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_TimeOnly() throws Exception
    {
        CmdLine.load(this, "-r'23:59:59.999'");
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE),
                23,
                59,
                59,
                999);
    }

    /**
     * <p>predefined_today.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_today() throws Exception
    {
        CmdLine.load(this, "-r'today'");
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0, 0);
    }

    /**
     * <p>predefined_ymd.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_ymd() throws Exception
    {
        CmdLine.load(this, "-r'2008-04-09'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 0, 0, 0, 0);
    }

    /**
     * <p>predefined_ymd2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void predefined_ymd2() throws Exception
    {
        CmdLine.load(this, "-r'2008/04/09'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 0, 0, 0, 0);
    }

    /**
     * <p>predefinedNOT.</p>
     */
    @Test
    public void predefinedNOT()
    {
        try
        {
            CmdLine.load(this, "-r'208-04-09'");
            Assert.fail("should have Assert.failed");
        } catch (final Exception e)
        {
            Assert.assertEquals("date --date(-r) is not in a predefined date / time format (208-04-09)",
                    e.getMessage());
        }
    }

    /**
     * <p>thisWeekMon.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void thisWeekMon() throws Exception
    {
        CmdLine.load(this, "-r _date(2010/12/31 =2a)");
        assertValidReturnDate(2010, Calendar.DECEMBER, 27, 0, 0, 0, 0);
    }

    /**
     * <p>thisWeekSat.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void thisWeekSat() throws Exception
    {
        CmdLine.load(this, "-r _date(2010/12/31 =7a)");
        assertValidReturnDate(2011, Calendar.JANUARY, 1, 0, 0, 0, 0);
    }

    /**
     * <p>valid.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void valid() throws Exception
    {
        CmdLine.load(this, "-r'2008-04-09'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 0, 0, 0, 0);
    }

    /**
     * <p>validWithTime.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void validWithTime() throws Exception
    {
        CmdLine.load(this, "-r'2008-04-09_06:31:32'");
        assertValidReturnDate(2008, Calendar.APRIL, 9, 6, 31, 32, 0);
    }
}
