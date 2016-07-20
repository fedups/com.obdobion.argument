package com.obdobion.argument;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class GroupTest
{

    static final class Embed
    {

        static public Embed create() throws Exception
        {

            return new Embed();
        }

        public boolean eVar;
        public Embed   child;
    }

    public boolean     noVar;
    public Embed[]     embedA;
    public List<Embed> embedL;

    public GroupTest()
    {

    }

    @Test
    public void arrayEmbeddedGroups() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-tbegin-kg-vembedA-m1", "-tbegin-kz-vchild", "-tboolean-ka-veVar", "-tend-kz", "-tend-kg");
        cl.parse(this, "-g(-z(-a))");

        Assert.assertTrue("eVar value", embedA[0].child.eVar);
    }

    @Test
    public void group() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-tbegin-kg", "-tboolean-ka", "-tend-kg");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g(-a)"));

        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1g cmd count", 1, cl.arg("-g").size());
        Assert.assertEquals("1g value", Boolean.TRUE, ((CmdLineCLA) cl.arg("-g")).getValue().arg("-a").getValue());
    }

    @Test
    public void groupWithNoVariable() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-tbegin-kg", "-tboolean-ka-vnoVar", "-tend-kg");
        cl.parse(this, "-g(-a)");

        Assert.assertTrue("noVar value", noVar);
    }

    @Test
    public void listEmbeddedGroups() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
                "-tbegin-kg-vembedL-m1--factoryMethod " + Embed.class.getName() + ".create",
                "-tbegin-kz-vchild",
                "-tboolean-ka-veVar",
                "-tend-kz",
                "-tend-kg");
        cl.parse(this, "-g(-z(-a))");

        Assert.assertTrue("eVar value", embedL.get(0).child.eVar);
    }

    @Test
    public void multiGroup() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-tbegin-kg-m1", "-tboolean-ka", "-tboolean-kb", "-tend-kg");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g(-a)(-b)"));

        final CmdLineCLA parmG = (CmdLineCLA) cl.arg("-g");

        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1g cmd count", 2, cl.arg("-g").size());
        Assert.assertEquals("1g0a", Boolean.TRUE, parmG.getValue(0).arg("-a").getValue());
        Assert.assertEquals("1g0b", Boolean.FALSE, parmG.getValue(0).arg("-b").getValue());
        Assert.assertEquals("1g1a", Boolean.FALSE, parmG.getValue(1).arg("-a").getValue());
        Assert.assertEquals("1g1b", Boolean.TRUE, parmG.getValue(1).arg("-b").getValue());
    }

    @Test
    public void stringSingleQuotedInGroup() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-tbegin-kg--pos", "-tString-ka-m1--pos", "-tend-kg");
        cl.parse(CommandLineParser.getInstance(
                cl.getCommandPrefix(),
                "('1this is single quoted' \"3this 'is double quoted\" '4this \"is single quoted')"));

        Assert.assertEquals("1 cmd count", 1, cl.size());
        Assert.assertEquals("1g cmd count", 1, cl.arg("-g").size());
        Assert.assertEquals("1 value", "1this is single quoted", ((CmdLineCLA) cl.arg("-g")).getValue().arg("-a")
                .getValue(0));
        Assert.assertEquals("3 value", "3this 'is double quoted", ((CmdLineCLA) cl.arg("-g")).getValue().arg("-a")
                .getValue(1));
        Assert.assertEquals("4 value", "4this \"is single quoted", ((CmdLineCLA) cl.arg("-g")).getValue().arg("-a")
                .getValue(2));
    }
}
