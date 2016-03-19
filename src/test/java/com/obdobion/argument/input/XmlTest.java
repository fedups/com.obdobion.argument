package com.obdobion.argument.input;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;

/**
 * @author Chris DeGreef
 * 
 */
public class XmlTest extends TestCase {

    InputStream is;

    public void testString () throws Exception {

        is = new ByteArrayInputStream("<cmdline><Hello>world</Hello><goodbye>for now</goodbye></cmdline>".getBytes());
        final ICmdLine cmd = new CmdLine();
        cmd.compile("-t string -k Hello", "-t string -k goodbye");
        cmd.parse(XmlParser.getInstance(is));
        final StringBuilder buf = new StringBuilder();
        cmd.exportCommandLine(buf);
        assertEquals("--Hello\"world\" --goodbye\"for now\"", buf.toString());
    }
}
