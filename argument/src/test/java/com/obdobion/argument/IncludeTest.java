package com.obdobion.argument;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class IncludeTest
{

    static final File workDir = new File("\\tmp");

    static public File createSpecFile (
            final String specs)
            throws IOException
    {

        final File file = File.createTempFile("IncludeTest.", ".fun", workDir);
        final BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(specs);
        out.newLine();
        out.close();
        return file;
    }

    public IncludeTest()
    {

    }

    @Test
    public void includeExactPath ()
            throws Exception
    {

        final File spec = createSpecFile("--workDirectory /temp");

        final ICmdLine cl = new CmdLine();
        try
        {
            cl.compile("-tboolean-ka", "-tboolean-kb", "-tString-kworkDirectory");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(),
                    "-a @'" + spec.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\") + "' -b"));
        } finally
        {
            Assert.assertTrue("delete of spec file", spec.delete());
        }

    }

    @Test
    public void includeNotFoundNotQuoted ()
            throws Exception
    {

        final ICmdLine cl = new CmdLine();
        try
        {
            cl.compile("-tboolean-ka", "-tboolean-kb", "-tString-kworkDirectory");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "@BadFileName.fun"));
            Assert.fail("expected a file not found message");
        } catch (final Exception e)
        {
            Assert.assertEquals("@BadFileName.fun could not be found", e.getMessage());
        }

    }

    @Test
    public void includeNotFoundQuoted ()
            throws Exception
    {

        final ICmdLine cl = new CmdLine();
        try
        {
            cl.compile("-tboolean-ka", "-tboolean-kb", "-tString-kworkDirectory");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "@'BadFileName.fun' -b"));
            Assert.fail("expected a file not found message");
        } catch (final Exception e)
        {
            Assert.assertEquals("@BadFileName.fun could not be found", e.getMessage());
        }

    }

    @Test
    public void includeNotFoundWithSpace ()
            throws Exception
    {

        final ICmdLine cl = new CmdLine();
        try
        {
            cl.compile("-tboolean-ka", "-tboolean-kb", "-tString-kworkDirectory");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a @ BadFileName.fun -b"));
            Assert.fail("expected a file not found message");
        } catch (final Exception e)
        {
            Assert.assertEquals("@BadFileName.fun could not be found", e.getMessage());
        }

    }

    @Test
    public void includeUsingDefaultPath ()
            throws Exception
    {

        final File spec = createSpecFile("--workDirectory /temp");

        final ICmdLine cl = new CmdLine();
        cl.addDefaultIncludeDirectory(new File("/tmp"));

        try
        {
            cl.compile("-tboolean-ka", "-tboolean-kb", "-tString-kworkDirectory");
            cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-a @" + spec.getName() + " -b"));

        } finally
        {
            Assert.assertTrue("delete of spec file", spec.delete());
        }

    }

}
