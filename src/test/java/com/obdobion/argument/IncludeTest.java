package com.obdobion.argument;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class IncludeTest
{
    static final File workDir = new File("\\tmp");

    static public File createSpecFile(final String specs) throws IOException
    {
        final File file = File.createTempFile("IncludeTest.", ".fun", workDir);
        try (final BufferedWriter out = new BufferedWriter(new FileWriter(file)))
        {
            out.write(specs);
            out.newLine();
        }
        return file;
    }

    @Arg(shortName = 'a')
    boolean boola;

    @Arg(shortName = 'b')
    boolean boolb;

    @Arg(caseSensitive = true)
    String  workDirectory;

    @Test
    public void includeExactPath() throws Exception
    {
        final File spec = createSpecFile("--workDirectory /temp");
        try
        {
            CmdLine.load(this, "-a @'" + spec.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\") + "' -b");
            Assert.assertTrue(boola);
            Assert.assertTrue(boolb);
            Assert.assertEquals("/temp", workDirectory);

        } finally
        {
            Assert.assertTrue(spec.delete());
        }
    }

    @Test
    public void includeNotFoundNotQuoted() throws Exception
    {
        try
        {
            CmdLine.load(this, "@BadFileName.fun");
            Assert.fail("expected a file not found message");
        } catch (final Exception e)
        {
            Assert.assertEquals("@BadFileName.fun could not be found", e.getMessage());
        }
    }

    @Test
    public void includeNotFoundQuoted() throws Exception
    {
        try
        {
            CmdLine.load(this, "@'BadFileName.fun' -b");
            Assert.fail("expected a file not found message");
        } catch (final Exception e)
        {
            Assert.assertEquals("@BadFileName.fun could not be found", e.getMessage());
        }
    }

    @Test
    public void includeNotFoundWithSpace()
            throws Exception
    {
        try
        {
            CmdLine.load(this, "-a @ BadFileName.fun -b");
            Assert.fail("expected a file not found message");
        } catch (final Exception e)
        {
            Assert.assertEquals("@BadFileName.fun could not be found", e.getMessage());
        }
    }

    @Test
    public void includeUsingDefaultPath() throws Exception
    {
        final File spec = createSpecFile("--workDirectory /temp");
        try
        {
            final ICmdLine cLine = new CmdLine();
            cLine.addDefaultIncludeDirectory(spec.getParentFile());

            CmdLine.load(cLine, this, "-a @" + spec.getName() + " -b");
            Assert.assertTrue(boola);
            Assert.assertTrue(boolb);
            Assert.assertEquals("/temp", workDirectory);

        } finally
        {
            Assert.assertTrue(spec.delete());
        }
    }
}
