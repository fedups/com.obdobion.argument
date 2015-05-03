package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class DateWeekOfMonthTest
{
    public DateWeekOfMonthTest()
    {

    }

    @Test
    public void addBeginningWOM_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "=9m =25d =btime", "+bweekofmonth");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void addEndingWOM_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "=9m =25d =btime", "+eweekofmonth");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: ADD", e.getMessage());
        }
    }

    @Test
    public void addWOM ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=2d =11m =btime", "+2weekofmonth");
    }

    @Test
    public void atBeginningWOM ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "-1m =25d =btime", "=bweekofmonth");
    }

    @Test
    public void atEndingWOM ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=23d =btime", "=eweekofmonth");
    }

    @Test
    public void atWOM ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=5d =btime", "=2weekofmonth");
    }

    @Test
    public void nextAbsoluteWOM_nextMonth ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =16d =0ms", "+1m =20d =btime", ">4weekofmonth");
    }

    @Test
    public void nextAbsoluteWOM_thisMonth ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =10d =0ms", "=17d =btime", ">4weekofmonth");
    }

    @Test
    public void nextBeginningWOM ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =30d", "=30d =btime =btime", ">bweekofmonth");
    }

    @Test
    public void nextEndingWOM_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =30d", "+1m =27d =btime", ">eweekofmonth");
    }

    @Test
    public void nextEndingWOM_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d", "=30d =btime", ">eweekofmonth");
    }

    @Test
    public void nextOrThisAbsoluteWOM_nextMonth ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =23d =0ms", "+1m =20d =btime", ">=4weekofmonth");
    }

    @Test
    public void nextOrThisAbsoluteWOM_thisMonth ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =10d =0ms", "=17d =btime", ">=4weekofmonth");
    }

    @Test
    public void nextOrThisBeginningWOM_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =10d =0ms", "=30d =btime", ">=bweekofmonth");
    }

    @Test
    public void nextOrThisBeginningWOM_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "=9M =25D =btime", ">=bweekofmonth");
    }

    @Test
    public void nextOrThisEndingWOM_next ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =10d", "=30d =btime", ">=eweekofmonth");
    }

    @Test
    public void nextOrThisEndingWOM_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =31d", "=30d =btime", ">=eweekofmonth");
    }

    @Test
    public void prevAbsoluteWOM_prevMonth ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =17d =0ms", "=9m =19d =btime", "<4weekofmonth");
    }

    @Test
    public void prevAbsoluteWOM_thisMonth ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =17d =0ms", "=10m =10d =btime", "<3weekofmonth");
    }

    @Test
    public void prevBeginningWOM_prev ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "=8m =28d =btime", "<bweekofmonth");
    }

    @Test
    public void prevBeginningWOM_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =23d =0ms", "=9m =25d =btime", "<bweekofmonth");
    }

    @Test
    public void prevEndingWOM_this ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =31d =0ms", "=9m =25d =btime", "<eweekofmonth");
    }

    @Test
    public void prevOrThisAbsoluteWOM_prevMonth ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =3d =0ms", "=9m =19d =btime", "<=4weekofmonth");
    }

    @Test
    public void prevOrThisAbsoluteWOM_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =16d =0ms", "=10m =16d =btime", "<=4weekofmonth");
    }

    @Test
    public void prevOrThisAbsoluteWOM_thisMonth ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =23d =0ms", "=10m =16d =btime", "<=4weekofmonth");
    }

    @Test
    public void prevOrThisBeginningWOM_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "=9m =25d =btime", "<=bweekofmonth");
    }

    @Test
    public void prevOrThisEndingWOM_prev ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =23d =0ms", "=9m =25d =btime", "<=eweekofmonth");
    }

    @Test
    public void prevOrThisEndingWOM_same ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =31d =0ms", "=10m =30d =btime", "<=eweekofmonth");
    }

    @Test
    public void subtractBeginningWOM_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "=9m =25d =btime", "-bweekofmonth");
        } catch (final ParseException e)
        {
            Assert.assertEquals("invalid direction in data adjustment: SUBTRACT", e.getMessage());
        }
    }

    @Test
    public void subtractEndingWOM_ERROR ()
        throws Exception
    {
        try
        {
            CalendarFactoryHelper.startExpectedComputed("=2011y =10M =1d =0ms", "=9m =25d =btime", "-eweekofmonth");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction in data adjustment: SUBTRACT", e.getMessage());
        }
    }

    @Test
    public void subtractWOM ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =19d =0ms", "=5d =btime", "-2weekofmonth");
    }
}
