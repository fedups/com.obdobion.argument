package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 *
 */
public class CriteriaTest
{

    public CriteriaTest()
    {

    }

    @Test
    public void rangeAndList () throws Exception
    {
        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t Float -k i --ra 4 -m1 --list 1 2");
            Assert.fail("should have Assert.failed");
        } catch (final ParseException e)
        {
            Assert.assertEquals(
                    "Only one criteria is allowed for \"float -i\", found \" --range '4.0'\" and \" --list '1.0' '2.0'\"",
                    e.getMessage());
        }
    }

    @Test
    public void rangeAndMatches () throws Exception
    {
        final CmdLine cl = new CmdLine();
        try
        {
            cl.compile("-t Float -k i --ra 4 -m1 --matches abc");
            Assert.fail("should have Assert.failed");
        } catch (final ParseException e)
        {
            Assert.assertEquals(
                    "Only one criteria is allowed for \"float -i\", found \" --range '4.0'\" and \" --matches 'abc'\"",
                    e.getMessage());
        }
    }
}
