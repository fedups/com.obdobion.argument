package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class DateParserErrorTest
{

    @Test
    public void directionIsRequired ()
        throws Exception
    {
        try
        {
            CalendarFactory.nowX("2010year");
            Assert.fail("should have been exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid direction: 2010year", e.getMessage());
        }

    }

    @Test
    public void invalidUOM ()
        throws Exception
    {
        try
        {
            CalendarFactory.nowX("+1xx");
            Assert.fail("should have been exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("invalid uom: xx", e.getMessage());
        }

    }

    @Test
    public void missingQTY ()
        throws Exception
    {
        try
        {
            CalendarFactory.nowX("+xx");
            Assert.fail("should have been exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("missing qty in +xx", e.getMessage());
        }

    }

    @Test
    public void spaceForQty ()
        throws Exception
    {
        try
        {
            CalendarFactory.nowX("+ d");
            Assert.fail("should have been exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("Premature end of formula, qty expected: +", e.getMessage());
        }

    }

    @Test
    public void uomIsRequired ()
        throws Exception
    {
        try
        {
            CalendarFactory.nowX("=2010");
            Assert.fail("should have been exception");
        } catch (final Exception e)
        {
            Assert.assertEquals("uom is required", e.getMessage());
        }

    }

}
