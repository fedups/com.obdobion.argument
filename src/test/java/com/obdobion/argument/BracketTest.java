package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class BracketTest
{

    public BracketTest()
    {

    }

    @Test
    public void testMultiGroup () throws Exception
    {

        final ICmdLine cl = new CmdLine();
        cl.compile("-tbegin-kg-m1", "-tboolean-ka", "-tboolean-kb", "-tend-kg");
        cl.parse("-g[-a][-b]");

        final CmdLineCLA parmG = (CmdLineCLA) cl.arg("-g");

        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1g cmd count", 2, cl.arg("-g").size());
        Assert.assertEquals("1g0a", Boolean.TRUE, parmG.getValue(0).arg("-a")
                .getValue());
        Assert.assertEquals("1g0b", Boolean.FALSE, parmG.getValue(0).arg("-b")
                .getValue());
        Assert.assertEquals("1g1a", Boolean.FALSE, parmG.getValue(1).arg("-a")
                .getValue());
        Assert.assertEquals("1g1b", Boolean.TRUE, parmG.getValue(1).arg("-b")
                .getValue());

    }

}
