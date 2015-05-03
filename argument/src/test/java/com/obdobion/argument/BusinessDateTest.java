package com.obdobion.argument;

import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class BusinessDateTest
{
    public BusinessDateTest()
    {
    }

    @Test
    public void largeNegative ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011year =1month =1day =btime",
                "=2011year =1month =1day =btime",
                "=btime");
    }

    @Test
    public void largePositive ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2111year =1month =1day =btime",
                "=2111year =1month =1day =btime",
                "=btime");
    }

    @Test
    public void smallNegative ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011year =10month =20day =btime",
                "=2011year =10month =20day =btime",
                "=btime");
    }

    @Test
    public void smallPositive ()
        throws Exception
    {
        CalendarFactoryHelper.startExpectedComputed("=2011year =12month =31day =btime",
                "=2011year =12month =31day =btime",
                "=btime");
    }

}
