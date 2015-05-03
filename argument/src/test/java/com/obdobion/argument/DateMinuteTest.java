package com.obdobion.argument;

import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class DateMinuteTest
{
    public DateMinuteTest()
    {

    }

    @Test
    public void add_invalidBeginning ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=bt =0hours =10minutes", "", "+bminute");
            Assert.fail("expected an exception");
        } catch (final ParseException e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void add_minute ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =0hours =10minutes", "=11min", "+1minute");
    }

    @Test
    public void atBeforeCurrentTimeResultsInPastTime ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours =30min", "-15minutes", "=15minutes");
    }

    @Test
    public void atEndWhenAtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =13hours", "", "=eminute");
    }

    @Test
    public void atEndWhenNotAtEnd ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =13hours =4min", "=et =13hour =4minutes", "=eminutes");
    }

    @Test
    public void beginningAt ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min =1sec =2mil",
                "=bt =1hours =10min =0sec =0mil",
                "=bminute");
    }

    @Test
    public void beginningNext ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min =1sec =1mil", "=bt =1hours =11min", ">bmin");
    }

    @Test
    public void beginningNextOrThis_beginning ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "=bt =1hours =10min", ">=bmin");
    }

    @Test
    public void beginningNextOrThis_notBeginning ()
        throws Exception
    {
        CalendarFactoryHelper
                .startExpectedComputed("=bt =1hours =10min =15sec =30mill", "=bt =1hours =11min", ">=bmin");
    }

    @Test
    public void beginningPrev_after ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min =1sec", "=bt =1hours =10min", "<bmin");
    }

    @Test
    public void beginningPrev_beginning ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "=bt =1hours =9min", "<bmin");
    }

    @Test
    public void beginningPrevOrThis_after ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min =1sec =2mill", "=bt =1hours =10min", "<=bmin");
    }

    @Test
    public void beginningPrevOrThis_beginning ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "=bt =1hours =10min", "<=bmin");
    }

    @Test
    public void endingNext_ending ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =1hours =10min", "=et =1hours =11min", ">emin");
    }

    @Test
    public void endingNext_notEnding ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "=et =1hours =10min", ">emin");
    }

    @Test
    public void endingNextOrThis_ending ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =1hours =10min", "", ">=emin");
    }

    @Test
    public void endingNextOrThis_notEnding ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =1hours =10min =998mil", "=et =1hours =9min", "<=emin");
    }

    @Test
    public void endingPrev_ending ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =1hours =10min", "=et =1hours =9min", "<emin");
    }

    @Test
    public void endingPrev_notEnding ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "=et =1hours =9min", "<emin");
    }

    @Test
    public void endingPrevOrThis_ending ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =1hours =10min", "", "<=emin");
    }

    @Test
    public void endingPrevOrThis_notEnding ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=et =1hours =10min =998mil", "=et =1hours =9min", "<=emin");
    }

    @Test
    public void greaterBeforeCurrentTimeResultsInFutureTime ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =12hours =30min", "=13hours =4min", ">4min");
    }

    @Test
    public void nextOrThisMinute_after ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10minutes", "=11min", ">=11minute");
    }

    @Test
    public void nextOrThisMinute_before ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10minutes", "=2hour =9min", ">=9minute");
    }

    @Test
    public void nextOrThisMinute_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10minutes", "", ">=10minute");
    }

    @Test
    public void prev_after ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "=0hour =11min", "<11minute");
    }

    @Test
    public void prev_before ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "=9minute", "<9minute");
    }

    @Test
    public void prev_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "=0hour", "<10minute");
    }

    @Test
    public void prevOrThis_after ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "=0hours =11min", "<=11minute");
    }

    @Test
    public void prevOrThis_before ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "=9min", "<=9minute");
    }

    @Test
    public void prevOrThis_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=bt =1hours =10min", "", "<=10minute");
    }

}
