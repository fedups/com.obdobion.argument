package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class DateYearTest
{
    public DateYearTest()
    {

    }

    @Test
    public void absoluteBeginningYear_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2d =1M =2011y =btime", "=1d =1m =2011year", "=byear");
    }

    @Test
    public void absoluteEndYear_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2d =1M =2011y =btime", "=12m =31d =2011year =etime", "=eyear");
    }

    @Test
    public void addBeginningYear_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", "+byear");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void addEndingYear_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", "+eyear");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void addYear ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d", "=2013y", "+2year");
    }

    @Test
    public void nextBeginningYear ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=1d =1M =2011y =btime", "=1d =1m =2012y", ">byear");
    }

    @Test
    public void nextEndOfYear ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=1d =1M =2011y =btime", "=12m =31d =2012y =etime", ">eyear");
    }

    @Test
    public void nextOrThisBeginningYear_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2d =1M =2011y =btime", "=1d =1m =2012year", ">=byear");
    }

    @Test
    public void nextOrThisBeginningYear_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=1d =1M =2011y =btime", "", ">=byear");
    }

    @Test
    public void nextOrThisEndOfYear ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=12M =31d =2011y", "=etime", ">=eyear");
    }

    @Test
    public void nextOrThisYear_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", ">=1year");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: NEXTORTHIS", e.getMessage());
        }
    }

    @Test
    public void nextThisYear_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", ">1year");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: NEXT", e.getMessage());
        }
    }

    @Test
    public void prevBeginningYear ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2d =1M =2011y =btime", "=1d =2010y", "<byear");
    }

    @Test
    public void prevEndOfYear_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=12M =30d =2011y", "=31d -1year =etime", "<eyear");
    }

    @Test
    public void prevEndOfYear_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=12M =31d =2011y", "-1year =etime", "<eyear");
    }

    @Test
    public void prevOrThisBeginningYear_prev ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2d =1M =2011y =btime", "=1d =1m =2011year", "<=byear");
    }

    @Test
    public void prevOrThisEndOfYear_prev ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=12M =30d =2011y", "=31d -1year =etime", "<=eyear");
    }

    @Test
    public void prevOrThisEndOfYear_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=12M =31d =2011y", "=etime", "<=eyear");
    }

    @Test
    public void prevOrThisYear_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", "<=1year");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: PREVORTHIS", e.getMessage());
        }
    }

    @Test
    public void prevThisYear_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", "<1year");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: PREV", e.getMessage());
        }
    }

    @Test
    public void subtractBeginningYear_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", "-byear");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: SUBTRACT", e.getMessage());
        }
    }

    @Test
    public void subtractEndingYear_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", "-eyear");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: SUBTRACT", e.getMessage());
        }
    }

    @Test
    public void subtractYear ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d", "=2009y", "-2year");
    }
}
