package com.obdobion.argument;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Chris DeGreef
 * 
 */
public class AbbreviationTest {

    public AbbreviationTest() {

    }

    @Test
    public void exactMatchOnShorterOtherwiseAmbiguous () throws Exception {

        final ICmdLine cl = new CmdLine();
        cl.compile("-t String -k a Item", "-t String -k b Items");

        cl.parse("--items longer");
        Assert.assertEquals("longer", (String) cl.arg("-b").getValue());

        cl.parse("--item shorter");
        Assert.assertEquals("shorter", (String) cl.arg("-a").getValue());
    }

}
