package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class BracketTest
{

    static public class Group
    {
        @Arg(shortName = 'a')
        boolean b1;

        @Arg(shortName = 'b')
        boolean b2;
    }

    @Arg(shortName = 'g')
    Group[] group;

    @Test
    public void testMultiGroup() throws Exception
    {
        CmdLine.load(this, "-g[-a][-b]");
        Assert.assertTrue(group[0].b1);
        Assert.assertFalse(group[0].b2);
        Assert.assertFalse(group[1].b1);
        Assert.assertTrue(group[1].b2);
    }
}
