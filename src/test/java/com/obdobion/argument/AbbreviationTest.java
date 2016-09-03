package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>AbbreviationTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class AbbreviationTest
{
    @Arg(shortName = 'a', longName = "Item")
    String item;

    @Arg(shortName = 'b', longName = "Items")
    String items;

    /**
     * <p>exactMatchOnShorterOtherwiseAmbiguous1.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void exactMatchOnShorterOtherwiseAmbiguous1() throws Exception
    {
        CmdLine.load(this, "--items longer");
        Assert.assertEquals("longer", items);
    }

    /**
     * <p>exactMatchOnShorterOtherwiseAmbiguous2.</p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void exactMatchOnShorterOtherwiseAmbiguous2() throws Exception
    {
        CmdLine.load(this, "--item shorter");
        Assert.assertEquals("shorter", item);
    }

}
