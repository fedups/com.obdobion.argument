package com.obdobion.argument;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class DateDayOfWeekTest
{
    public DateDayOfWeekTest()
    {

    }

    @Test
    public void at_BOW ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2dayofweek =20ms", "-1d =20ms", "=bdayofweek");
    }

    @Test
    public void at_EOW ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=7dayofweek =20ms", "-6d =20ms", "=edayofweek");
    }

    @Test
    public void next_BOW_NextDayInNextWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=6dayofweek =btime", "+1w =1dayofweek", ">bdayofweek");
    }

    @Test
    public void next_DAY_NextDayInNextWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=6dayofweek =btime", "+1w", ">6dayofweek");
    }

    @Test
    public void next_DAY_NextDayInSameWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=5dayofweek =btime", "+1d", ">6dayofweek");
    }

    @Test
    public void next_EOW_NextDayInNextWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=7dayofweek", "+1w =7dayofweek =etime", ">edayofweek");
    }

    @Test
    public void nextEq_BOW_NextDayInNextWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2dayofweek =btime", "+1w =1dayofweek", ">=bdayofweek");
    }

    @Test
    public void nextEq_BOW_NextDayInSameWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=1dayofweek =btime", "", ">=bdayofweek");
    }

    @Test
    public void nextEq_DAY_NextDayInNextWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=5dayofweek =btime", "+1w -1d", ">=4dayofweek");
    }

    @Test
    public void nextEq_DAY_NextDayInSameWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=5dayofweek =btime", "+1d", ">=6dayofweek");
    }

    @Test
    public void nextEq_DAY_ThisDayIsOk ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=6dayofweek =btime", "", ">=6dayofweek");
    }

    @Test
    public void nextEq_EOW_NextDayInSameWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=3dayofweek ", "=7dayofweek =etime", ">=edayofweek");
    }

    @Test
    public void nextEq_EOW_NextDayIsOK ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=7dayofweek ", "=etime", ">=edayofweek");
    }

    @Test
    public void plus_BOW_error ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("", "", "+bdayofweek");
            Assert.fail("expected invalid direction in data adjustment: ADD");

        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void plus_DayOfWeek ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=btime", "+24Hours", "+1dayofweek");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void plus_EOW_error ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("", "", "+edayofweek");
            Assert.fail("expected invalid direction in data adjustment: ADD");

        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void prev_BOW_PrevDayInPrevWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=1dayofweek =btime", "-1w", "<bdayofweek");
    }

    @Test
    public void prev_BOW_PrevDayInSameWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2dayofweek =btime", "-1d", "<bdayofweek");
    }

    @Test
    public void prev_DAY_PrevDayInPrevWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=6dayofweek =btime", "-1w", "<6dayofweek");
    }

    @Test
    public void prev_DAY_PrevDayInSameWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=6dayofweek =btime", "-1d", "<5dayofweek");
    }

    @Test
    public void prev_EOW_PrevDayInPrevWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=7dayofweek", "-1w =etime", "<edayofweek");
    }

    @Test
    public void prevEq_BOW_PrevDayInSameWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2dayofweek =btime", "-1d", "<=bdayofweek");
    }

    @Test
    public void prevEq_BOW_ThisDayIsOk ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=1dayofweek =btime", "", "<=bdayofweek");
    }

    @Test
    public void prevEq_DAY_PrevDayInPrevWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=4dayofweek =btime", "-1w +1d", "<=5dayofweek");
    }

    @Test
    public void prevEq_DAY_PrevDayInSameWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=6dayofweek =btime", "-1d", "<=5dayofweek");
    }

    @Test
    public void prevEq_DAY_ThisDayIsOk ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=6dayofweek =btime", "", "<=6dayofweek");
    }

    @Test
    public void prevEq_EOW_PrevDayInSameWeek ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=7dayofweek", "=etime", "<=edayofweek");
    }

    @Test
    public void prevEq_EOW_ThisDayIsOk ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=7dayofweek", "=etime", "<=edayofweek");
    }

    @Test
    public void subtract_BOW_error ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("", "", "-bdayofweek");
            Assert.fail("expected invalid direction in data adjustment: SUBTRACT");

        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: SUBTRACT", e.getMessage());
        }
    }

    @Test
    public void subtract_DayOfWeek ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=btime", "-1w =1dayofweek", "-1dayofweek");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: SUBTRACT", e.getMessage());
        }
    }

    @Test
    public void subtract_EOW_error ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("", "", "-edayofweek");
            Assert.fail("expected invalid direction in data adjustment: SUBTRACT");

        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: SUBTRACT", e.getMessage());
        }
    }
}
