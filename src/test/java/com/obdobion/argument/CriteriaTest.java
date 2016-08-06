package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLineTest.DoubleParm;
import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class CriteriaTest
{
    static public class CritError
    {
        @Arg(range = { "4" }, inList = { "1", "2" })
        float var;
    }

    static public class CritError2
    {
        @Arg(range = { "4" }, matches = "abc")
        float var;
    }

    @Test
    public void rangeAndList() throws Exception
    {
        try
        {
            final DoubleParm target = new DoubleParm();
            CmdLine.load(target, "--help");

        } catch (final ParseException e)
        {
            Assert.assertEquals(
                    "Only one criteria is allowed for \"float -i\", found \" --range '4.0'\" and \" --list '1.0' '2.0'\"",
                    e.getMessage());
        }
    }

    @Test
    public void rangeAndMatches() throws Exception
    {
        try
        {
            final DoubleParm target = new DoubleParm();
            CmdLine.load(target, "--help");

        } catch (final ParseException e)
        {
            Assert.assertEquals(
                    "Only one criteria is allowed for \"float -i\", found \" --range '4.0'\" and \" --matches 'abc'\"",
                    e.getMessage());
        }
    }
}
