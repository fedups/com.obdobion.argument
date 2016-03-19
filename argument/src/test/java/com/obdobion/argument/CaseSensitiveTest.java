package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class CaseSensitiveTest
{

    public CaseSensitiveTest()
    {

    }

    @Test
    public void caseDoesNotMatter () throws Exception
    {
        final ICmdLine cl = new CmdLine();
        cl.compile("-t String -k a item1 --list lower UPPER");
        cl.parse("-a LOWER");
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1a", "LOWER", cl.arg("-a").getValue());
        cl.parse("-a UPPER");
        Assert.assertEquals("2 cmd count", 1, cl.size());
        Assert.assertEquals("2a", "UPPER", cl.arg("-a").getValue());
        cl.parse("-a Lower");
        Assert.assertEquals("3 cmd count", 1, cl.size());
        Assert.assertEquals("3a", "Lower", cl.arg("-a").getValue());
        cl.parse("-a upper");
        Assert.assertEquals("4 cmd count", 1, cl.size());
        Assert.assertEquals("4a", "upper", cl.arg("-a").getValue());
    }

    @Test
    public void caseMattersValid () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t String -ka item1 --list lower UPPER -c");
        cl.parse("-a lower");
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1a", "lower", cl.arg("-a").getValue());
        cl.parse("-a UPPER");
        Assert.assertEquals("2 cmd count", 1, cl.size());
        Assert.assertEquals("2a", "UPPER", cl.arg("-a").getValue());
    }

    @Test
    public void caseMattersErrorLower () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t String -ka item1 --list lowerx UPPER");
        cl.parse("-a LOWER");
        Assert.assertEquals("1a", "lowerx", cl.arg("-a").getValue());
    }

    @Test
    public void caseMattersErrorUpper () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t String -ka item1 --list lower UPPER -c");
        cl.parse("-a upper");
        Assert.assertEquals("1a", "UPPER", cl.arg("-a").getValue());
    }
}
