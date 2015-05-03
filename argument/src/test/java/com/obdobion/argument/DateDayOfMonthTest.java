package com.obdobion.argument;

import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class DateDayOfMonthTest
{
    public DateDayOfMonthTest()
    {

    }

    @Test
    public void back12Months ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =12M =31d =0ms", "=31d =12m =2010y", "-12month");
    }

    @Test
    public void forward12Months ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2010y =12M =31d =0ms", "=31d =12m =2011y", "+12month");
    }

    @Test
    public void from30to31backward ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =11M =30d =0ms", "=10m =30d", "-1month");
    }

    @Test
    public void from30to31forward ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =11M =30d =0ms", "=12m =30d", "+1month");
    }

    @Test
    public void from31to30backward ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =12M =31d =0ms", "=11m =30d", "-1month");
    }

    @Test
    public void from31to30forward ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011y =10M =31d =0ms", "=11m =30d", "+1month");
    }

}
