package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class DateWeekOfYearTest
{
    public DateWeekOfYearTest()
    {

    }

    @Test
    public void addBeginningWOY_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", "+bweekofyear");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void addEndingWOY_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", "+eweekofyear");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void addWOY ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=11m =2d", "+2weekofyear");
    }

    @Test
    public void atBeginningWOY ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms",
                "=2010y =12month =26d =btime",
                "=bweekofyear");
    }

    @Test
    public void atEndingWOY ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms",
                "=2011y =12month =25d =btime",
                "=eweekofyear");
    }

    @Test
    public void atFirstWOY ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=12m =29d =2010yr", "=1weekofyear");
    }

    @Test
    public void atLastWOY ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=2011y =12month =28d", "=53weekofyear");
    }

    @Test
    public void atZeroWOY ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=12m =22d =2010yr", "=0weekofyear");
    }

    @Test
    public void nextAbsoluteWOY_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d", "=2012y =10M =3d", ">40weekofyear");
    }

    @Test
    public void nextAbsoluteWOY_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d", "=2011y =12M =7d", ">50weekofyear");
    }

    @Test
    public void nextBeginningWOY_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =1M =2d =0ms", "=2012y =1month =1d =btime", ">bweekofyear");
    }

    @Test
    public void nextEndingWOY_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =12M =27d =0ms", "=2012y =12M =30d =btime", ">eweekofyear");
    }

    @Test
    public void nextEndingWOY_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =12M =1d =0ms", "=2011y =12M =25d =btime", ">eweekofyear");
    }

    @Test
    public void nextOrThisAbsoluteWOY_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=2012y =1M =4d =0ms", ">=1weekofyear");
    }

    @Test
    public void nextOrThisAbsoluteWOY_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=2011y =10M =19d =0ms", ">=43weekofyear");
    }

    @Test
    public void nextOrThisAbsoluteWOY_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=2011y =10M =26d =0ms", ">=44weekofyear");
    }

    @Test
    public void nextOrThisBeginningWOY_next ()
        throws Exception
    {
        CalendarFactoryHelper
                .startExpectedComputed("=2011y =1M =2d =0ms", "=2012y =1month =1d =btime", ">=bweekofyear");
    }

    @Test
    public void nextOrThisBeginningWOY_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =1M =1d =0ms",
                "=2010y =12month =26d =btime",
            ">=bweekofyear");
    }

    @Test
    public void nextOrThisEndingWOY_same ()
        throws Exception
    {
        CalendarFactoryHelper
                .startExpectedComputed("=2011y =12M =27d =0ms", "=2011y =12M =25d =btime", ">=eweekofyear");
    }

    @Test
    public void prevAbsoluteWOY_prev ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d", "=20d =10m =2010yr", "<43weekofyear");
    }

    @Test
    public void prevAbsoluteWOY_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d", "=12d", "<42weekofyear");
    }

    @Test
    public void prevBeginningWOY_prev ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2012y =1M =1d =0ms",
                "=26day =12m =2010year =btime",
            "<bweekofyear");
    }

    @Test
    public void prevBeginningWOY_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2012y =11M =1d =0ms",
                "=1day =1m =2012year =btime",
            "<bweekofyear");
    }

    @Test
    public void prevEndingWOY_prev ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =12M =1d =0ms", "=2010y =12M =26d =btime", "<eweekofyear");
    }

    @Test
    public void prevOrThisAbsoluteWOY_prev ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d", "=27d =10m =2010yr", "<=44weekofyear");
    }

    @Test
    public void prevOrThisAbsoluteWOY_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d", "", "<=43weekofyear");
    }

    @Test
    public void prevOrThisAbsoluteWOY_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d", "=12d", "<=42weekofyear");
    }

    @Test
    public void prevOrThisBeginningWOY_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =1M =1d =0ms",
                "=26day =12m =2010year =btime",
            "<=bweekofyear");
    }

    @Test
    public void prevOrThisEndingWOY_prev ()
        throws Exception
    {
        CalendarFactoryHelper
                .startExpectedComputed("=2011y =12M =31d =0ms", "=2011y =12M =25d =btime", "<=eweekofyear");
    }

    @Test
    public void prevOrThisEndingWOY_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =12M =1d =0ms", "=2010y =12M =26d =btime", "<=eweekofyear");
    }

    @Test
    public void subtractEndingWOY_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "", "-eweekofyear");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: SUBTRACT", e.getMessage());
        }
    }

    @Test
    public void subtractWOY ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=10m =5d", "-2weekofyear");
    }
}
