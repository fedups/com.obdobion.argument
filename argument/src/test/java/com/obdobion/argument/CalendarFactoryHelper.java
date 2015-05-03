package com.obdobion.argument;

import java.util.Calendar;

import junit.framework.Assert;

/**
 * @author Chris DeGreef
 * 
 */
public class CalendarFactoryHelper
{

    static public void startExpectedComputed (
        final String startingDateCommand,
        final String expectedDateCommand,
        final String computedDateCommand)
        throws Exception
    {
        startExpectedComputed(startingDateCommand, expectedDateCommand, computedDateCommand, false);
    }

    static public void startExpectedComputed (
        final String startingDateCommand,
        final String expectedDateCommand,
        final String computedDateCommand,
        final boolean compareMillies)
        throws Exception
    {
        CalendarFactory.setBusinessDate(null);
        CalendarFactory.setBusinessDate(CalendarFactory.nowX(startingDateCommand));
        final Calendar expectedCalendar = CalendarFactory.nowX(expectedDateCommand);

        CalendarFactory.setBusinessDate(null);
        CalendarFactory.setBusinessDate(CalendarFactory.nowX(startingDateCommand));
        final Calendar computedCalendar = CalendarFactory.nowX(computedDateCommand);

        if (!compareMillies)
        {
            expectedCalendar.set(Calendar.MILLISECOND, 0);
            computedCalendar.set(Calendar.MILLISECOND, 0);
        }

        Assert.assertEquals(expectedCalendar.getTime(), computedCalendar.getTime());
    }

}
