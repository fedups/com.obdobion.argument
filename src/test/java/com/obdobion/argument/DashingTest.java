package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * @author Chris DeGreef
 *
 */
public class DashingTest
{
    static public class CamelCaps
    {
        @Arg(allowCamelCaps = true)
        boolean camelCaps;
    }

    static public class CamelCapsError
    {
        @Arg(allowCamelCaps = true)
        boolean camelCaps;

        @Arg
        String  cc;
    }

    static public class CamelCapsPartial
    {
        @Arg(allowCamelCaps = true)
        boolean camelCaps;

        @Arg
        String  ccx;
    }

    static public class EmbeddedDash
    {
        @Arg(shortName = 'a')
        boolean noDash;

        @Arg(shortName = 'n')
        boolean name;

        @Arg(shortName = 'b', longName = "bee-name")
        boolean withDoubleDash;
    }

    static public class MPhoneIssues
    {
        @Arg(shortName = 'a', allowMetaphone = true)
        boolean inputFileName;

        @Arg(shortName = 'a', allowMetaphone = true)
        boolean inputFileNum;
    }

    static public class MPhoneVersion
    {
        @Arg(allowMetaphone = true)
        boolean version;
    }

    @Test
    public void camelCaps() throws Exception
    {
        final CamelCaps target = new CamelCaps();
        CmdLine.load(target, "--cc");
        Assert.assertTrue(target.camelCaps);
    }

    @Test
    public void camelCapsCompetesWithExactKeyword() throws Exception
    {
        try
        {
            final CamelCapsError target = new CamelCapsError();
            CmdLine.load(target, "--usage");
            Assert.fail("expected exception");

        } catch (final ParseException e)
        {
            Assert.assertEquals("camelcaps equals long name, found \"boolean --camelCaps\"' and \"string --cc\"",
                    e.getMessage());
        }
    }

    @Test
    public void camelCapsCompetesWithPartialKeyword() throws Exception
    {
        final CamelCapsPartial target = new CamelCapsPartial();
        CmdLine.load(target, "--cc");
        Assert.assertTrue(target.camelCaps);
    }

    @Test
    public void embeddedDash1() throws Exception
    {
        final EmbeddedDash target = new EmbeddedDash();
        CmdLine.load(target, "-a-b");
        Assert.assertTrue(target.noDash);
        Assert.assertTrue(target.withDoubleDash);
    }

    @Test
    public void embeddedDash2() throws Exception
    {
        final EmbeddedDash target = new EmbeddedDash();
        CmdLine.load(target, "-a --bee-name");
        Assert.assertTrue(target.noDash);
        Assert.assertTrue(target.withDoubleDash);
    }

    @Test
    public void embeddedDash3() throws Exception
    {
        final EmbeddedDash target = new EmbeddedDash();
        CmdLine.load(target, "--bee-name -a");
        Assert.assertTrue(target.noDash);
        Assert.assertTrue(target.withDoubleDash);
    }

    @Test
    public void embeddedDash4() throws Exception
    {
        final EmbeddedDash target = new EmbeddedDash();
        CmdLine.load(target, "-a--bee-name");
        Assert.assertTrue(target.noDash);
        Assert.assertTrue(target.withDoubleDash);
    }

    @Test
    public void embeddedDash5() throws Exception
    {
        try
        {
            final EmbeddedDash target = new EmbeddedDash();
            CmdLine.load(target, "--bee-name-a");
            Assert.fail("expected exception");
        } catch (final ParseException e)
        {
            Assert.assertEquals("unexpected input: --bee-name-a ", e.getMessage());
        }
    }

    @Test
    public void embeddedDash6() throws Exception
    {
        final EmbeddedDash target = new EmbeddedDash();
        CmdLine.load(target, "--bee-name--noDash");
        Assert.assertTrue(target.noDash);
        Assert.assertTrue(target.withDoubleDash);
    }

    @Test
    public void embeddedDash7() throws Exception
    {
        final EmbeddedDash target = new EmbeddedDash();
        CmdLine.load(target, "-a --b -n");
        Assert.assertTrue(target.noDash);
        Assert.assertTrue(target.name);
        Assert.assertTrue(target.withDoubleDash);
    }

    @Test
    public void embeddedDash8() throws Exception
    {
        final EmbeddedDash target = new EmbeddedDash();
        CmdLine.load(target, "-a --bee-n -n");
        Assert.assertTrue(target.noDash);
        Assert.assertTrue(target.name);
        Assert.assertTrue(target.withDoubleDash);
    }

    @Test
    public void embeddedDash9() throws Exception
    {
        final EmbeddedDash target = new EmbeddedDash();
        CmdLine.load(target, "-a --bee-n");
        Assert.assertTrue(target.noDash);
        Assert.assertFalse(target.name);
        Assert.assertTrue(target.withDoubleDash);
    }

    @Test
    public void metaphoneIssues() throws Exception
    {
        final ICmdLine cl = new CmdLine();
        try
        {
            final MPhoneIssues target = new MPhoneIssues();
            CmdLine.load(cl, target, "--help");
            Assert.fail("expected an exception");
        } catch (final ParseException e)
        {
            Assert.assertEquals("multiple parse exceptions", e.getMessage());
            Assert.assertEquals(2, cl.getParseExceptions().size());
            Assert.assertEquals(
                    "duplicate short name, found \"boolean --inputFileName(-a)\"' and \"boolean --inputFileNum(-a)\"",
                    cl.getParseExceptions().get(0).getMessage());
            Assert.assertEquals(
                    "duplicate values for metaphone, found \"boolean --inputFileName(-a)\"' and \"boolean --inputFileNum(-a)\"",
                    cl.getParseExceptions().get(1).getMessage());
        }
    }

    @Test
    public void metaphoneVersion() throws Exception
    {
        final MPhoneVersion target = new MPhoneVersion();
        CmdLine.load(target, "--vrshn");
        Assert.assertTrue(target.version);
    }
}
