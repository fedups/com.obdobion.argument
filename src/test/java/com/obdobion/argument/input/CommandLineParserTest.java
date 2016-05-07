package com.obdobion.argument.input;

import junit.framework.TestCase;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;

/**
 * @author Chris DeGreef
 * 
 */
public class CommandLineParserTest extends TestCase
{

    public void testPackedCharCommandWithValue () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tboolean-ka", "-tboolean-kb");
        cmd.parse(CommandLineParser.getInstance(cmd.getCommandPrefix(), "-a"));

        assertEquals("-a", CommandLineParser.unparseTokens(cmd.allArgs()));
        // assertEquals("a=", NamespaceParser.unparseTokens(args));
        assertEquals("<cmdline><a></a></cmdline>", XmlParser.unparseTokens(cmd.allArgs()));
    }
}
