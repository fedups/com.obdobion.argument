package com.obdobion.argument.input;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;

import junit.framework.TestCase;

/**
 * @author Chris DeGreef
 *
 */
public class NamespaceParserTest extends TestCase
{

    public void testArray () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tInteger-khello-m1");
        cmd.parse(NamespaceParser.getInstance("hello[0]=0", "hello[1]=1"));

        assertEquals("--hello0 1", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("hello[0]=0\nhello[1]=1\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testboolean () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tboolean-k boolean");
        cmd.parse(NamespaceParser.getInstance("boolean="));

        assertEquals("--boolean", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("boolean=\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testEmbeddedLevelArray () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-kmy", "-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello", "-tend-kmy");
        cmd.parse(NamespaceParser.getInstance("my.hello[0].world=0", "my.hello[0].moon=1", "my.hello[1].moon=2"));

        assertEquals("--my[--hello[--moon1 --world0] [--moon2]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("my.hello[0].moon=1\nmy.hello[0].world=0\nmy.hello[1].moon=2\n", NamespaceParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespace () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb-m1", "-tbegin-kc", "-tString-kd-m1", "-tend-kc", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a.b.c.d=alpha"));

        assertEquals("-a[-b[-c[-d'alpha']]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a.b[0].c.d[0]=alpha\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos2b () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb--pos", "-tString-kc-m1", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a.[0].c=alpha"));

        assertEquals("-a[ [-c'alpha']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a..c[0]=alpha\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos2Changes () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tString-kc", "-tend-kb", "-tbegin-kd", "-tString-kz", "-tend-kd", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a.b.c=alpha", "a.d.z=omega"));

        assertEquals("-a[-b[-c'alpha'] -d[-z'omega']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a.b.c=alpha\na.d.z=omega\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos2twicea () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb--pos", "-tString-kc-m1", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a..c=alpha", "a..c=omega"));

        assertEquals("-a[ [-c'alpha' 'omega']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a..c[0]=alpha\na..c[1]=omega\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos2twiceaWithOrder () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb--pos-m1", "-tString-kc", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a.[1].c=alpha", "a.[0].c=omega"));

        assertEquals("-a[ [-c'omega'] [-c'alpha']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a.[0].c=omega\na.[1].c=alpha\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos2twiceb () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb--pos-m1", "-tbegin-kc", "-tString-kd-m1--pos", "-tend-kc", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a..c.[0]=alpha", "a..c.[1]=omega"));

        assertEquals("-a[ [-c[ 'alpha' 'omega']]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a.[0].c.[0]=alpha\na.[0].c.[1]=omega\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4a () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tString-kc", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a.b.c=alpha"));

        assertEquals("-a[-b[-c'alpha']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a.b.c=alpha\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4b () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tbegin-kc", "-tString-kz--pos", "-tend-kc", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a.b.c.[0]=alpha"));

        assertEquals("-a[-b[-c[ 'alpha']]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a.b.c.=alpha\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4twicea () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tString-kc-m1", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a.b.c=alpha", "a.b.c=omega"));

        assertEquals("-a[-b[-c'alpha' 'omega']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a.b.c[0]=alpha\na.b.c[1]=omega\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4twiceaWithOrder () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tString-kc-m1", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a.b.c[1]=alpha", "a.b.c[0]=omega"));

        assertEquals("-a[-b[-c'omega' 'alpha']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a.b.c[0]=omega\na.b.c[1]=alpha\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4twiceb () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd
                .compile("-tbegin-ka", "-tbegin-kb", "-tbegin-kc", "-tString-kd--pos-m1", "-tend-kc", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a.b.c.[0]=alpha", "a.b.c.[1]=omega"));

        assertEquals("-a[-b[-c[ 'alpha' 'omega']]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a.b.c.[0]=alpha\na.b.c.[1]=omega\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePosGroupNotMultiple () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb--pos", "-tString-kc", "-tend-kb", "-tend-ka");
        cmd.parse(NamespaceParser.getInstance("a..c=alpha"));

        assertEquals("-a[ [-c'alpha']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a..c=alpha\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testPositionalArray () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tstring-ka--pos-m1");
        cmd.parse(NamespaceParser.getInstance("[0]=world", "[1]=for now"));

        assertEquals(" 'world' 'for now'", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("[0]=world\n[1]=for now\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testPositionalNotMultiple () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tstring-ka--pos");
        cmd.parse(NamespaceParser.getInstance("=world"));

        assertEquals(" 'world'", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("=world\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testQuoted () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tstring-k Hello", "-tstring-k goodbye");
        cmd.parse(NamespaceParser.getInstance("Hello=world", "goodbye='for now'"));

        assertEquals("--Hello'world' --goodbye'\\'for now\\''", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("Hello=world\ngoodbye='for now'\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testString () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tstring-k Hello", "-tstring-k goodbye");
        cmd.parse(NamespaceParser.getInstance("Hello=world", "goodbye=for now"));

        assertEquals("--Hello'world' --goodbye'for now'", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("Hello=world\ngoodbye=for now\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testTopLevelArray1 () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello");
        cmd.parse(NamespaceParser.getInstance("hello[0].world=0", "hello[0].moon=1"));

        assertEquals("--hello[--moon1 --world0]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("hello[0].moon=1\nhello[0].world=0\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testTopLevelArray2 () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello");
        cmd.parse(NamespaceParser.getInstance("hello[0].world=0", "hello[0].moon=1", "hello[1].moon=2"));

        assertEquals("--hello[--moon1 --world0] [--moon2]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("hello[0].moon=1\nhello[0].world=0\nhello[1].moon=2\n", NamespaceParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testTwoLevelArray () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-kmy-m1", "-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello", "-tend-kmy");
        cmd.parse(NamespaceParser
                .getInstance("my[0].hello[0].world=0", "my[0].hello[0].moon=1", "my[1].hello[0].moon=2"));

        assertEquals("--my[--hello[--moon1 --world0]] [--hello[--moon2]]", CommandLineParser
                .unparseTokens(cmd.allArgs()));
        assertEquals("my[0].hello[0].moon=1\nmy[0].hello[0].world=0\nmy[1].hello[0].moon=2\n", NamespaceParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testTwoNamespaces () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tbegin-kc", "-tString-kd-m1", "-tend-kc", "-tend-kb", "-tend-ka", "-tbegin-khello", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello");
        cmd.parse(NamespaceParser.getInstance("hello.world=1", "a.b.c.d=alpha", "hello.moon=2"));

        assertEquals("-a[-b[-c[-d'alpha']]] --hello[--moon2 --world1]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("a.b.c.d[0]=alpha\nhello.moon=2\nhello.world=1\n", NamespaceParser.unparseTokens(cmd.allArgs()));
    }

    public void testTwoTwoLevelArray () throws Exception
    {

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-kmy-m1", "-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello", "-tend-kmy");
        cmd.parse(NamespaceParser
                .getInstance("my[0].hello[0].world=0", "my[0].hello[0].moon=1", "my[0].hello[1].moon=3", "my[1].hello[0].moon=2"));

        assertEquals("--my[--hello[--moon1 --world0] [--moon3]] [--hello[--moon2]]", CommandLineParser
                .unparseTokens(cmd.allArgs()));
        assertEquals("my[0].hello[0].moon=1\nmy[0].hello[0].world=0\nmy[0].hello[1].moon=3\nmy[1].hello[0].moon=2\n", NamespaceParser
                .unparseTokens(cmd.allArgs()));
    }
}
