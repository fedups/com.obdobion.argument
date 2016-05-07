package com.obdobion.argument;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class DateTest
{

    static private void assertValidReturnDate (
        final Calendar returnCal,
        final int year,
        final int month,
        final int _date,
        final int hour,
        final int minute,
        final int second,
        final int millisecond)
    {
        Assert.assertEquals(year, returnCal.get(Calendar.YEAR));
        Assert.assertEquals(month, returnCal.get(Calendar.MONTH));
        Assert.assertEquals(_date, returnCal.get(Calendar.DATE));
        Assert.assertEquals(hour, returnCal.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(minute, returnCal.get(Calendar.MINUTE));
        Assert.assertEquals(second, returnCal.get(Calendar.SECOND));
        Assert.assertEquals(millisecond, returnCal.get(Calendar.MILLISECOND));
    }

    public Date date;

    public DateTest()
    {

    }

    @Test
    public void dateDirectiveAbsDay ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _date(04/09/2008@13:14:15.016 =3day)"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 3, 0, 0, 0, 0);

    }

    @Test
    public void dateDirectiveAbsMin ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =5min)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 5, 15, 16);

    }

    @Test
    public void dateDirectiveAbsMonth ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =5MONTH)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.MAY, 9, 13, 14, 15, 16);

    }

    @Test
    public void dateDirectiveAbsMs ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =5ms)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 15, 5);

    }

    @Test
    public void dateDirectiveAbsSec ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =5sec)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 5, 16);

    }

    @Test
    public void dateDirectiveAbsYear ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(
            CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =2011YEAR)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2011, Calendar.APRIL, 9, 13, 14, 15, 16);

    }

    @Test
    public void dateDirectiveBeginOfYesterday ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 -1d =bd)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 8, 0, 0, 0, 0);

    }

    @Test
    public void dateDirectiveEndOfYesterday ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 -1d =ed)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 8, 23, 59, 59, 999);

    }

    @Test
    public void dateDirectiveMinusDay ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _date(04/09/2008@13:14:15.016 -1day -1day)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 7, 0, 0, 0, 0);

    }

    @Test
    public void dateDirectiveMinusMin ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(
            CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 -5minutes)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 9, 15, 16);

    }

    @Test
    public void dateDirectiveMinusMonth ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(
            CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 -5Months)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2007, Calendar.NOVEMBER, 9, 13, 14, 15, 16);

    }

    @Test
    public void dateDirectiveMinusMs ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 -5ms)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 15, 11);

    }

    @Test
    public void dateDirectiveMinusSec ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(
            CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 -5seconds)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 10, 16);

    }

    @Test
    public void dateDirectiveMinusWeekOfYear ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(12/30/2010@13:14:15.016 -5w)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2010, Calendar.NOVEMBER, 25, 13, 14, 15, 16);

    }

    @Test
    public void dateDirectiveMinusYear ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 -5YEARS)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2003, Calendar.APRIL, 9, 13, 14, 15, 16);

    }

    @Test
    public void dateDirectiveNowMinus5Min ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(now -5min)"), this);

    }

    @Test
    public void dateDirectivePlusDay ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _date(04/09/2008@13:14:15.016 +2day)"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 11, 0, 0, 0, 0);

    }

    @Test
    public void dateDirectivePlusMin ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(
            CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 +5minutes)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 19, 15, 16);

    }

    @Test
    public void dateDirectivePlusMonth ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 +5mon)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.SEPTEMBER, 9, 13, 14, 15, 16);

    }

    @Test
    public void dateDirectivePlusMs ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 +34l)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 15, 50);

    }

    @Test
    public void dateDirectivePlusSec ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(
            CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 +5seconds)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 20, 16);

    }

    @Test
    public void dateDirectivePlusYear ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 +5y)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2013, Calendar.APRIL, 9, 13, 14, 15, 16);

    }

    @Test
    public void dateDirectiveWithoutAdj ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _date(04/09/2008@13:14:15.016)"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 0, 0, 0, 0);

    }

    @Test
    public void dateTimeDirectiveBMin ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =BMin)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 0, 0);

    }

    @Test
    public void dateTimeDirectiveBOD ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =BDay)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 0, 0, 0, 0);

    }

    @Test
    public void dateTimeDirectiveBOM ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =BMON)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 1, 0, 0, 0, 0);

    }

    @Test
    public void dateTimeDirectiveBOY ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =By)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.JANUARY, 1, 0, 0, 0, 0);

    }

    @Test
    public void dateTimeDirectiveEHour ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =EHour)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 59, 59, 999);

    }

    @Test
    public void dateTimeDirectiveEMin ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =EMin)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 59, 999);

    }

    @Test
    public void dateTimeDirectiveEOD ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =EDay)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 23, 59, 59, 999);

    }

    @Test
    public void dateTimeDirectiveEOM ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =EMon)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 30, 23, 59, 59, 999);

    }

    @Test
    public void dateTimeDirectiveEOY ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _dateTime(04/09/2008@13:14:15.016 =EY)"),
            this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.DECEMBER, 31, 23, 59, 59, 999);

    }

    @Test
    public void error ()
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t date -k r --var date --format 'yyyy-MM-dd'");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'2008/04/09'"), this);

            Assert.fail("should have Assert.failed");

        } catch (final Exception e)
        {
            Assert.assertEquals("-r yyyy-MM-dd: Unparseable date: \"2008/04/09\"", e.getMessage());
        }
    }

    @Test
    public void lastWeekSat ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _date(2010/12/31 -1week =7dayofweek)"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2010, Calendar.DECEMBER, 25, 0, 0, 0, 0);

    }

    @Test
    public void nextWeekSat ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _date(2010/12/31 +1week =7dayofweek)"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2011, Calendar.JANUARY, 8, 0, 0, 0, 0);

    }

    @Test
    public void predefined_fullTime ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'04/09/2008 13:14:15.016'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 15, 16);

    }

    @Test
    public void predefined_fullTime_underbar ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r04/09/2008@13:14:15.016"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 15, 16);

    }

    @Test
    public void predefined_hh ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'4/9/2008 13'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 0, 0, 0);

    }

    @Test
    public void predefined_hhmm ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'04/09/2008 13:14'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 0, 0);

    }

    @Test
    public void predefined_hhmmss ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'04/09/2008 13:14:15'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 13, 14, 15, 0);

    }

    @Test
    public void predefined_hhmmss_1digit ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'04/09/2008 1:4:5'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 1, 4, 5, 0);

    }

    @Test
    public void predefined_HourOnly ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'17'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 17, 0, 0, 0);

    }

    @Test
    public void predefined_mdy ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'04-09-2008'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 0, 0, 0, 0);

    }

    @Test
    public void predefined_mdy2 ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'04/09/2008'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 0, 0, 0, 0);

    }

    @Test
    public void predefined_now ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'now'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DATE),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND),
            cal.get(Calendar.MILLISECOND));

    }

    @Test
    public void predefined_TimeOnly ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'23:59:59.999'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DATE),
            23,
            59,
            59,
            999);

    }

    @Test
    public void predefined_today ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'today'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0, 0);

    }

    @Test
    public void predefined_ymd ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'2008-04-09'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 0, 0, 0, 0);

    }

    @Test
    public void predefined_ymd2 ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'2008/04/09'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 0, 0, 0, 0);

    }

    @Test
    public void predefinedNOT ()
    {

        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t date -k r --var date");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'208-04-09'"), this);

            Assert.fail("should have Assert.failed");

        } catch (final Exception e)
        {
            Assert.assertEquals("-r is not in a predefined date / time format (208-04-09)", e.getMessage());
        }

    }

    @Test
    public void thisWeekMon ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _date(2010/12/31 =2a)"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2010, Calendar.DECEMBER, 27, 0, 0, 0, 0);

    }

    @Test
    public void thisWeekSat ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r _date(2010/12/31 =7a)"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertValidReturnDate(cal, 2011, Calendar.JANUARY, 1, 0, 0, 0, 0);

    }

    @Test
    public void valid ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date --format 'yyyy-MM-dd'");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'2008-04-09'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 0, 0, 0, 0);

    }

    @Test
    public void validWithTime ()
        throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t date -k r --var date --format 'yyyy-MM-dd_HH:mm:ss'");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-r'2008-04-09_06:31:32'"), this);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        assertValidReturnDate(cal, 2008, Calendar.APRIL, 9, 6, 31, 32, 0);
    }
}
