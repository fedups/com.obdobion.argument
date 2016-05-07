package com.obdobion.argument.input;

import junit.framework.TestCase;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;

/**
 * @author Chris DeGreef
 * 
 */
public class NamespaceTest extends TestCase
{

    public void testString () throws Exception
    {

        final IParserInput p = NamespaceParser.getInstance("Hello=world", "goodbye=for now");
        final ICmdLine cmd = new CmdLine();
        cmd.compile("-t string -k Hello", "-t string -k goodbye");
        cmd.parse(p);
        final StringBuilder buf = new StringBuilder();
        cmd.exportCommandLine(buf);
        assertEquals("--Hello\"world\" --goodbye\"for now\"", buf.toString());
    }
}
