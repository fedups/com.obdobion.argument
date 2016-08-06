package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class AbbreviationTest
{
    @Arg(shortName = 'a', longName = "Item")
    String item;

    @Arg(shortName = 'b', longName = "Items")
    String items;

    @Test
    public void exactMatchOnShorterOtherwiseAmbiguous1() throws Exception
    {
        CmdLine.load(this, "--items longer");
        Assert.assertEquals("longer", items);
    }

    @Test
    public void exactMatchOnShorterOtherwiseAmbiguous2() throws Exception
    {
        CmdLine.load(this, "--item shorter");
        Assert.assertEquals("shorter", item);
    }

}
