package com.obdobion.argument;

import junit.framework.Assert;

import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class RequiredTest {

    public int maxUpdates = 0;

    public RequiredTest() {

    }

    @Test
    public void requireInteger () throws Exception {

        final CmdLine cl = new CmdLine();
        try {
            cl.compile("-t Integer --key m maxUpdates --req --var maxUpdates");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), ""));
            Assert.fail("should have failed");
        } catch (final Exception e) {
            Assert.assertEquals("requires int", "missing required parameters: --maxUpdates(-m) ", e.getMessage());
        }

    }
}
