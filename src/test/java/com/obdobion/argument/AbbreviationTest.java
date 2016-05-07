package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class AbbreviationTest
{

    public AbbreviationTest()
    {

    }

    @Test
    public void exactMatchOnShorterOtherwiseAmbiguous () throws Exception
    {

        final ICmdLine cl = new CmdLine();
        cl.compile("-t String -k a Item", "-t String -k b Items");

        cl.parse("--items longer");
        Assert.assertEquals("longer", cl.arg("-b").getValue());

        cl.parse("--item shorter");
        Assert.assertEquals("shorter", cl.arg("-a").getValue());
    }

}
