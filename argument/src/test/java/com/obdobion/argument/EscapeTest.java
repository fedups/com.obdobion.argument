package com.obdobion.argument;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class EscapeTest
{

    public EscapeTest()
    {

    }

    @Test
    public void escape1 () throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t String -k a");

        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a \"c:\\\\program files\\\\\""));
        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1 esc", "c:\\program files\\", cl.arg("-a").getValue());
    }

}
