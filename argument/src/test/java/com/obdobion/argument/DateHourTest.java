package com.obdobion.argument;

import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class DateHourTest
{
    public DateHourTest()
    {

    }

    @Test
    public void add_hour ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =0hours =10minutes", "=1h", "+1hour");
    }

    @Test
    public void add_invalidBeginning ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=bt =0hours =10minutes", "", "+bhour");
            Assert.fail("expected an exception");
        } catch (final ParseException e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void atBeforeCurrentTimeResultsInPastTime ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours", "=9hours", "=9hours");
    }

    @Test
    public void atEndOfHourWhenAtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =13hours", "", "=ehour");
    }

    @Test
    public void atEndOfHourWhenNotAtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =13hours", "=et =13hour", "=ehours");
    }

    @Test
    public void bug_nextOrThisAfterTheTimeInTheCurrentDay ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =20hours =45minutes", "=12h =00i", "+0d =btime >11h >0min");
    }

    @Test
    public void bug_nextOrThisAfterTheTimeInTheCurrentDayAtBegHour ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =20hours =45minutes", "+1d =11h =00i", "+0d >11h =bhour");
    }

    @Test
    public void bug_nextOrThisAfterTheTimeInTheCurrentDayNoBTIME ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =20hours =45minutes", "+1d =12h =00i", "+0d >11h >0min");
    }

    @Test
    public void greaterBeforeCurrentTimeResultsInFutureTime ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours", "+1d =9hours", ">9hours");
    }

    @Test
    public void itsBeginningOfHour_prevOrThis ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours", "", "<=bhour");
    }

    @Test
    public void itsFirstHour_nextOrThisMidnight ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10minutes", "=bt =2h", ">=bhour");
    }

    @Test
    public void itsFirstHour_prevOrThisMidnight ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10minutes", "=bt =1h", "<=bhour");
    }

    @Test
    public void itsJustAfterMidnightHour_thisMidnight ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =0hours =10minutes", "=bt", "<bhour");
    }

    @Test
    public void itsMidnight_prevHour ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt", "-1day =bt =23h", "<bhour");
    }

    @Test
    public void itsMidnightHour_nextMidnight ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =0hours =0minutes", "=bt =1h", ">bhour");
    }

    @Test
    public void itsMidnightHour_nextOrThisMidnight ()
        throws Exception
    {
        // CalendarFactory.setInDebug(true);
        CalendarFactoryHelper.startExpectedComputed("=bt", "", ">=bhour");
        // CalendarFactory.setInDebug(false);
    }

    @Test
    public void itsNoonHour_at5pm ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours =10minutes",
                "=bt +17hours =10minutes",
                "=17hours =10minutes");
    }

    @Test
    public void itsNoonHour_atMidnight ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours =10minutes", "=bt =12h", "=bh");
    }

    @Test
    public void lessAfterTimeResultsInPastTime ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours", "-1d =13hours", "<13hours");
    }

    @Test
    public void lessBeforeTimeResultsInExactTime ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours", "=9hours", "<9hours");
    }

    @Test
    public void lessOrEquals_equals ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours =10minutes", "=0min", "<=12hours");
    }

    @Test
    public void lessOrEquals_greater ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours =10minutes", "-1d =13h =0min", "<=13hours");
    }

    @Test
    public void nextEnd_AtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =12hours", "=13h", ">eh");
    }

    @Test
    public void nextEnd_NotAtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours =10minutes", "=et =12h", ">eh");
    }

    @Test
    public void nextOrThisEnd_AtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =12hours", "", ">=eh");
    }

    @Test
    public void nextOrThisEnd_NotAtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours =10minutes", "=et =12h", ">=eh");
    }

    @Test
    public void nextOrThisHour_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =2hours =10minutes", "+1d =1h =0i", ">=1hour");
    }

    @Test
    public void nextOrThisHour_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10minutes", "=1h =0i", ">=1hour");
    }

    @Test
    public void prevEnd_AtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =12hours", "=et =11h", "<eh");
    }

    @Test
    public void prevEnd_NotAtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours", "=et =11h", "<eh");
    }

    @Test
    public void prevOrThisEnd_AtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =12hours", "", "<=eh");
    }

    @Test
    public void prevOrThisEnd_NotAtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours", "=et =11h", "<=eh");
    }

    @Test
    public void subtract_hour ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =0hours =10minutes", "-1d =23h", "-1hour");
    }

    @Test
    public void subtract_invalidBeginning ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=bt =0hours =10minutes", "", "-bhour");
            Assert.fail("expected an exception");
        } catch (final ParseException e)
        {
            Assert.assertEquals("invalid direction in data adjustment: SUBTRACT", e.getMessage());
        }
    }
}
