package com.obdobion.argument;

import java.lang.management.MemoryType;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.NamespaceParser;
import com.obdobion.argument.input.XmlParser;

/**
 * <p>
 * ExportImportTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class ExportImportTest
{
    static public class BooleanALong
    {
        @Arg(shortName = 'a', longName = "long")
        boolean along;

        @Arg(shortName = 'b', longName = "short")
        boolean bshort;
    }

    static public class Export1
    {
        @Arg(longName = "boolean")
        private boolean b1;

        @Arg
        private int[]   hello;
    }

    static public class ExportEmums
    {
        @Arg(shortName = 'a', defaultValues = "heap")
        MemoryType type1;

        @Arg(shortName = 'b', defaultValues = "HEAP", caseSensitive = true)
        MemoryType type2;
    }

    static public class ExportNumbers
    {
        @Arg(shortName = 'f')
        float[] floater;

        @Arg(shortName = 'i')
        int[]   integers;
    }

    static public class HelloGoodbye
    {
        @Arg(longName = "Hello")
        private String hello;

        @Arg
        private String goodbye;
    }

    static public class Master01
    {
        @Arg
        Master02 my;
    }

    static public class Master02
    {
        @Arg
        Master03[] hello;
    }

    static public class Master03
    {
        @Arg
        int moon;

        @Arg
        int world;
    }

    static public class Master04
    {
        @Arg(shortName = 'a')
        Master05 a;
    }

    static public class Master05
    {
        @Arg(shortName = 'b')
        Master06[] a;
    }

    static public class Master06
    {
        @Arg(shortName = 'c')
        Master07 a;
    }

    static public class Master07
    {
        @Arg(shortName = 'd')
        String[] a;
    }

    static public class Master08
    {
        @Arg(shortName = 'a')
        Master09 a;
    }

    static public class Master09
    {
        @Arg(shortName = 'b', positional = true)
        Master10 a;
    }

    static public class Master10
    {
        @Arg(shortName = 'c')
        String[] a;
    }

    static public class Master11
    {
        @Arg(shortName = 'a')
        Master12 a;
    }

    static public class Master12
    {
        @Arg(shortName = 'b')
        Master13 a;
        @Arg(shortName = 'd')
        Master14 d;
    }

    static public class Master13
    {
        @Arg(shortName = 'c')
        String a;
    }

    static public class Master14
    {
        @Arg(shortName = 'z')
        String a;
    }

    static public class Master15
    {
        @Arg(shortName = 'a')
        Master16 a;
    }

    static public class Master16
    {
        @Arg(shortName = 'b', positional = true)
        Master17[] a;
    }

    static public class Master17
    {
        @Arg(shortName = 'c')
        String a;
    }

    static public class Master18
    {
        @Arg(shortName = 'a')
        Master19 a;
    }

    static public class Master19
    {
        @Arg(shortName = 'b', positional = true)
        Master20[] a;
    }

    static public class Master20
    {
        @Arg(shortName = 'c')
        Master21 a;
    }

    static public class Master21
    {
        @Arg(shortName = 'd', positional = true)
        String[] a;
    }

    static public class Master22
    {
        @Arg(shortName = 'a')
        Master23 a;
    }

    static public class Master23
    {
        @Arg(shortName = 'b')
        Master24 a;
    }

    static public class Master24
    {
        @Arg(shortName = 'c')
        String a;
    }

    static public class Master25
    {
        @Arg(shortName = 'a')
        Master26 a;
    }

    static public class Master26
    {
        @Arg(shortName = 'b')
        Master27 b;
    }

    static public class Master27
    {
        @Arg(shortName = 'c')
        Master28 c;
    }

    static public class Master28
    {
        @Arg(shortName = 'z', positional = true)
        String z;
    }

    static public class Master29
    {
        @Arg(shortName = 'a')
        Master30 a;
    }

    static public class Master30
    {
        @Arg(shortName = 'b')
        Master31 a;
    }

    static public class Master31
    {
        @Arg(shortName = 'c')
        String[] a;
    }

    static public class Master32
    {
        @Arg(shortName = 'a')
        Master33 a;
    }

    static public class Master33
    {
        @Arg(shortName = 'b')
        Master34 a;
    }

    static public class Master34
    {
        @Arg(shortName = 'c')
        Master35 a;
    }

    static public class Master35
    {
        @Arg(shortName = 'd', positional = true)
        String[] a;
    }

    static public class Master36
    {
        @Arg(shortName = 'a')
        Master37 a;
    }

    static public class Master37
    {
        @Arg(shortName = 'b', positional = true)
        Master38 a;
    }

    static public class Master38
    {
        @Arg(shortName = 'c')
        String a;
    }

    static public class Master39
    {
        @Arg
        Master40[] hello;
    }

    static public class Master40
    {
        @Arg
        private int moon;

        @Arg
        private int world;
    }

    static public class Master41
    {
        @Arg
        Master39[] my;
    }

    static public class Master42
    {
        @Arg(shortName = 'a')
        Master43 a;
        @Arg
        Master40 hello;
    }

    static public class Master43
    {
        @Arg(shortName = 'b')
        Master44 a;
    }

    static public class Master44
    {
        @Arg(shortName = 'c')
        Master45 a;
    }

    static public class Master45
    {
        @Arg(shortName = 'd')
        String[] a;
    }

    static private void verify(
            final Object target,
            final ICmdLine originalCmdLine,
            final String expectedNamespace,
            final String expectedXML,
            final String expectedCommandLine)
                    throws Exception
    {
        if (expectedNamespace != null)
            try
            {
                final String exported = NamespaceParser.unparseTokens(originalCmdLine.allArgs());
                final String[] exportedAsLines = exported.split("\n");
                Assert.assertEquals(expectedNamespace, exported);
                final ICmdLine cmd = CmdLine.loadProperties(target, exportedAsLines);
                Assert.assertEquals(exported, NamespaceParser.unparseTokens(cmd.allArgs()));
            } catch (final ParseException e)
            {
                Assert.fail(e.getMessage());
            }

        if (expectedXML != null)
            try
            {
                final String exported = XmlParser.unparseTokens(originalCmdLine.allArgs());
                final String[] exportedAsLines = exported.split("\n");
                Assert.assertEquals(expectedXML, exported);
                final ICmdLine cmd = CmdLine.loadXml(target, exportedAsLines);
                Assert.assertEquals(exported, XmlParser.unparseTokens(cmd.allArgs()));
            } catch (final ParseException e)
            {
                Assert.fail(e.getMessage());
            }

        if (expectedCommandLine != null)
            try
            {
                final String exported = CommandLineParser.unparseTokens(originalCmdLine.allArgs());
                final String[] exportedAsLines = exported.split("\n");
                Assert.assertEquals(expectedCommandLine, exported);
                final ICmdLine cmd = CmdLine.load(target, exportedAsLines);
                Assert.assertEquals(exported, CommandLineParser.unparseTokens(cmd.allArgs()));
            } catch (final ParseException e)
            {
                Assert.fail(e.getMessage());
            }
    }

    /**
     * <p>
     * exportBoolean1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportBoolean1() throws Exception
    {
        final Object target = new BooleanALong();
        verify(target, CmdLine.load(target,
                "-a"),
                "a=\n",
                "<cmdline><a/></cmdline>",
                "-a");
    }

    /**
     * <p>
     * exportBoolean2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportBoolean2() throws Exception
    {
        final Object target = new BooleanALong();
        verify(target, CmdLine.load(target,
                "--long"),
                "a=\n",
                "<cmdline><a/></cmdline>",
                "-a");
    }

    /**
     * <p>
     * exportBoolean3.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportBoolean3() throws Exception
    {
        final Object target = new BooleanALong();
        verify(target, CmdLine.load(target,
                "--long -b"),
                "a=\nb=\n",
                "<cmdline><a/><b/></cmdline>",
                "-a -b");
    }

    /**
     * <p>
     * exportBoolean4.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportBoolean4() throws Exception
    {
        final Object target = new BooleanALong();
        verify(target, CmdLine.load(target,
                "-b--long"),
                "a=\nb=\n",
                "<cmdline><a/><b/></cmdline>",
                "-a -b");
    }

    /**
     * <p>
     * exportBoolean5.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportBoolean5() throws Exception
    {
        final Object target = new BooleanALong();
        verify(target, CmdLine.load(target,
                "-b"),
                "b=\n",
                "<cmdline><b/></cmdline>",
                "-b");
    }

    /**
     * <p>
     * exportEnumDefault1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportEnumDefault1() throws Exception
    {
        final Object target = new ExportEmums();
        verify(target, CmdLine.load(target,
                ""),
                "",
                "<cmdline></cmdline>",
                "");
    }

    /**
     * <p>
     * exportEnumDefault2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportEnumDefault2() throws Exception
    {
        final Object target = new ExportEmums();
        verify(target, CmdLine.load(target,
                "-aHEAP"),
                "",
                "<cmdline></cmdline>",
                "");
    }

    /**
     * <p>
     * exportEnumDefault3.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportEnumDefault3() throws Exception
    {
        final Object target = new ExportEmums();
        verify(target, CmdLine.load(target,
                "-aheap"),
                "",
                "<cmdline></cmdline>",
                "");
    }

    /**
     * <p>
     * exportEnumDefaultWithCaseMattering1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportEnumDefaultWithCaseMattering1() throws Exception
    {
        final Object target = new ExportEmums();
        verify(target, CmdLine.load(target,
                "-bHEAP"),
                "",
                "<cmdline></cmdline>",
                "");
    }

    /**
     * <p>
     * exportFloat1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportFloat1() throws Exception
    {
        final Object target = new ExportNumbers();
        verify(target, CmdLine.load(target,
                "-f 1500.25"),
                "f[0]=1500.25\n",
                "<cmdline><f>1500.25</f></cmdline>",
                "-f1500.25");
    }

    /**
     * <p>
     * exportFloat2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportFloat2() throws Exception
    {
        final Object target = new ExportNumbers();
        verify(target, CmdLine.load(target,
                "-f 1 2.001"),
                "f[0]=1\nf[1]=2.001\n",
                "<cmdline><f>1</f><f>2.001</f></cmdline>",
                "-f1 2.001");
    }

    /**
     * <p>
     * exportFloat3.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportFloat3() throws Exception
    {
        final Object target = new ExportNumbers();
        verify(target, CmdLine.load(target,
                "-f -1500.25"),
                "f[0]=-1500.25\n",
                "<cmdline><f>-1500.25</f></cmdline>",
                "-f'-1500.25'");
    }

    /**
     * <p>
     * exportInt1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportInt1() throws Exception
    {
        final Object target = new ExportNumbers();
        verify(target, CmdLine.load(target,
                "-i 1500"),
                "i[0]=1500\n",
                "<cmdline><i>1500</i></cmdline>",
                "-i1500");
    }

    /**
     * <p>
     * exportInt2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void exportInt2() throws Exception
    {
        final Object target = new ExportNumbers();
        verify(target, CmdLine.load(target,
                "-i 1 2"),
                "i[0]=1\ni[1]=2\n",
                "<cmdline><i>1</i><i>2</i></cmdline>",
                "-i1 2");
    }

    /**
     * <p>
     * testArray.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testArray() throws Exception
    {
        final Export1 target = new Export1();
        verify(target, CmdLine.loadProperties(target,
                "hello[0]=0", "hello[1]=1"),
                "hello[0]=0\nhello[1]=1\n",
                "<cmdline><hello>0</hello><hello>1</hello></cmdline>",
                "--hello 0 1");
    }

    /**
     * <p>
     * testboolean.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testboolean() throws Exception
    {
        final Export1 target = new Export1();
        verify(target, CmdLine.loadProperties(target,
                "boolean="),
                "boolean=\n",
                "<cmdline><boolean/></cmdline>",
                "--boolean");
    }

    /**
     * <p>
     * testEmbeddedLevelArray.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testEmbeddedLevelArray() throws Exception
    {
        final Master01 target = new Master01();
        verify(target, CmdLine.loadProperties(target,
                "my.hello[0].world=0", "my.hello[0].moon=1", "my.hello[1].moon=2"),
                "my.hello[0].moon=1\nmy.hello[0].world=0\nmy.hello[1].moon=2\n",
                "<cmdline><my><hello><moon>1</moon><world>0</world></hello><hello><moon>2</moon></hello></my></cmdline>",
                "--my [--hello [--moon 1 --world 0] [--moon 2]]");
    }

    /**
     * <p>
     * testNamespace.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespace() throws Exception
    {
        final Master04 target = new Master04();
        verify(target, CmdLine.loadProperties(target,
                "a.b.c.d=alpha"),
                "a.b[0].c.d[0]=alpha\n",
                "<cmdline><a><b><c><d>alpha</d></c></b></a></cmdline>",
                "-a[-b[-c[-d'alpha']]]");
    }

    /**
     * <p>
     * testNamespacePos2b.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePos2b() throws Exception
    {
        final Master08 target = new Master08();
        verify(target, CmdLine.loadProperties(target,
                "a..c=alpha"),
                "a..c[0]=alpha\n",
                "<cmdline><a><noname><c>alpha</c></noname></a></cmdline>",
                "-a[ [-c'alpha']]");
    }

    /**
     * <p>
     * testNamespacePos2Changes.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePos2Changes() throws Exception
    {
        final Object target = new Master11();
        verify(target, CmdLine.loadProperties(target,
                "a.b.c=alpha", "a.d.z=omega"),
                "a.b.c=alpha\na.d.z=omega\n",
                "<cmdline><a><b><c>alpha</c></b><d><z>omega</z></d></a></cmdline>",
                "-a[-b[-c'alpha'] -d[-z'omega']]");
    }

    /**
     * <p>
     * testNamespacePos2twicea.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePos2twicea() throws Exception
    {
        final Object target = new Master08();
        verify(target, CmdLine.loadProperties(target,
                "a..c=alpha", "a..c=omega"),
                "a..c[0]=alpha\na..c[1]=omega\n",
                "<cmdline><a><noname><c>alpha</c><c>omega</c></noname></a></cmdline>",
                "-a[ [-c'alpha' 'omega']]");
    }

    /**
     * <p>
     * testNamespacePos2twiceaWithOrder.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePos2twiceaWithOrder() throws Exception
    {
        final Object target = new Master15();
        verify(target, CmdLine.loadProperties(target,
                "a.[1].c=alpha", "a.[0].c=omega"),
                "a.[0].c=omega\na.[1].c=alpha\n",
                "<cmdline><a><noname><c>omega</c></noname><noname><c>alpha</c></noname></a></cmdline>",
                "-a[ [-c'omega'] [-c'alpha']]");
    }

    /**
     * <p>
     * testNamespacePos2twiceb.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePos2twiceb() throws Exception
    {
        final Object target = new Master18();
        verify(target, CmdLine.loadProperties(target,
                "a..c.[0]=alpha", "a..c.[1]=omega"),
                "a.[0].c.[0]=alpha\na.[0].c.[1]=omega\n",
                "<cmdline><a><noname><c><noname>alpha</noname><noname>omega</noname></c></noname></a></cmdline>",
                "-a[ [-c[ 'alpha' 'omega']]]");
    }

    /**
     * <p>
     * testNamespacePos4a.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePos4a() throws Exception
    {
        final Object target = new Master22();
        verify(target, CmdLine.loadProperties(target,
                "a.b.c=alpha"),
                "a.b.c=alpha\n",
                "<cmdline><a><b><c>alpha</c></b></a></cmdline>",
                "-a[-b[-c'alpha']]");
    }

    /**
     * <p>
     * testNamespacePos4b.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePos4b() throws Exception
    {
        final Object target = new Master25();
        verify(target, CmdLine.loadProperties(target,
                "a.b.c.[0]=alpha"),
                "a.b.c.=alpha\n",
                "<cmdline><a><b><c><noname>alpha</noname></c></b></a></cmdline>",
                "-a[-b[-c[ 'alpha']]]");
    }

    /**
     * <p>
     * testNamespacePos4twicea.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePos4twicea() throws Exception
    {
        final Object target = new Master29();
        verify(target, CmdLine.loadProperties(target,
                "a.b.c=alpha", "a.b.c=omega"),
                "a.b.c[0]=alpha\na.b.c[1]=omega\n",
                "<cmdline><a><b><c>alpha</c><c>omega</c></b></a></cmdline>",
                "-a[-b[-c'alpha' 'omega']]");
    }

    /**
     * <p>
     * testNamespacePos4twiceaWithOrder.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePos4twiceaWithOrder() throws Exception
    {
        final Object target = new Master29();
        verify(target, CmdLine.loadProperties(target,
                "a.b.c[1]=alpha", "a.b.c[0]=omega"),
                "a.b.c[0]=omega\na.b.c[1]=alpha\n",
                "<cmdline><a><b><c>omega</c><c>alpha</c></b></a></cmdline>",
                "-a[-b[-c'omega' 'alpha']]");
    }

    /**
     * <p>
     * testNamespacePos4twiceb.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePos4twiceb() throws Exception
    {
        final Object target = new Master32();
        verify(target, CmdLine.loadProperties(target,
                "a.b.c.[0]=alpha", "a.b.c.[1]=omega"),
                "a.b.c.[0]=alpha\na.b.c.[1]=omega\n",
                "<cmdline><a><b><c><noname>alpha</noname><noname>omega</noname></c></b></a></cmdline>",
                "-a[-b[-c[ 'alpha' 'omega']]]");
    }

    /**
     * <p>
     * testNamespacePosGroupNotMultiple.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testNamespacePosGroupNotMultiple() throws Exception
    {
        final Object target = new Master36();
        verify(target, CmdLine.loadProperties(target,
                "a..c=alpha"),
                "a..c=alpha\n",
                "<cmdline><a><noname><c>alpha</c></noname></a></cmdline>",
                "-a[ [-c'alpha']]");
    }

    /**
     * <p>
     * testPositionalArray.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testPositionalArray() throws Exception
    {
        final Object target = new Master21();
        verify(target, CmdLine.loadProperties(target,
                "[0]=world", "[1]=for now"),
                "[0]=world\n[1]=for now\n",
                "<cmdline><noname>world</noname><noname><![CDATA[for now]]></noname></cmdline>",
                " 'world' 'for now'");
    }

    /**
     * <p>
     * testPositionalNotMultiple.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testPositionalNotMultiple() throws Exception
    {
        final Object target = new Master28();
        verify(target, CmdLine.loadProperties(target,
                "=world"),
                "=world\n",
                "<cmdline><noname>world</noname></cmdline>",
                " 'world'");
    }

    /**
     * <p>
     * testQuoted.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testQuoted() throws Exception
    {
        final Object target = new HelloGoodbye();
        verify(target, CmdLine.loadProperties(target,
                "Hello=world", "goodbye='for now'"),
                "Hello=world\ngoodbye='for now'\n",
                "<cmdline><Hello>world</Hello><goodbye><![CDATA['for now']]></goodbye></cmdline>",
                null);
        /*
         * This kind of quoting is ridiculous on the command line within a test
         * case.
         */
    }

    /**
     * <p>
     * testString.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testString() throws Exception
    {
        final Object target = new HelloGoodbye();
        verify(target, CmdLine.loadProperties(target,
                "Hello=world", "goodbye=for now"),
                "Hello=world\ngoodbye=for now\n",
                "<cmdline><Hello>world</Hello><goodbye><![CDATA[for now]]></goodbye></cmdline>",
                "--Hello 'world' --goodbye 'for now'");
    }

    /**
     * <p>
     * testTopLevelArray1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testTopLevelArray1() throws Exception
    {
        final Object target = new Master39();
        verify(target, CmdLine.loadProperties(target,
                "hello[0].world=0", "hello[0].moon=1"),
                "hello[0].moon=1\nhello[0].world=0\n",
                "<cmdline><hello><moon>1</moon><world>0</world></hello></cmdline>",
                "--hello [--moon 1 --world 0]");
    }

    /**
     * <p>
     * testTopLevelArray2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testTopLevelArray2() throws Exception
    {
        final Object target = new Master39();
        verify(target, CmdLine.loadProperties(target,
                "hello[0].world=0", "hello[0].moon=1", "hello[1].moon=2"),
                "hello[0].moon=1\nhello[0].world=0\nhello[1].moon=2\n",
                "<cmdline><hello><moon>1</moon><world>0</world></hello><hello><moon>2</moon></hello></cmdline>",
                "--hello [--moon 1 --world 0] [--moon 2]");
    }

    /**
     * <p>
     * testTwoLevelArray.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testTwoLevelArray() throws Exception
    {
        final Object target = new Master41();
        verify(target, CmdLine.loadProperties(target,
                "my[0].hello[0].world=0", "my[0].hello[0].moon=1", "my[1].hello[0].moon=2"),
                "my[0].hello[0].moon=1\nmy[0].hello[0].world=0\nmy[1].hello[0].moon=2\n",
                "<cmdline><my><hello><moon>1</moon><world>0</world></hello></my><my><hello><moon>2</moon></hello></my></cmdline>",
                "--my [--hello [--moon 1 --world 0]] [--hello [--moon 2]]");
    }

    /**
     * <p>
     * testTwoNamespaces.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testTwoNamespaces() throws Exception
    {
        final Object target = new Master42();
        verify(target, CmdLine.loadProperties(target,
                "hello.world=1", "a.b.c.d=alpha", "hello.moon=2"),
                "a.b.c.d[0]=alpha\nhello.moon=2\nhello.world=1\n",
                "<cmdline><a><b><c><d>alpha</d></c></b></a><hello><moon>2</moon><world>1</world></hello></cmdline>",
                "-a[-b[-c[-d'alpha']]] --hello [--moon 2 --world 1]");
    }

    /**
     * <p>
     * testTwoTwoLevelArray.
     * </p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testTwoTwoLevelArray() throws Exception
    {
        final Object target = new Master41();
        verify(target, CmdLine.loadProperties(target,
                "my[0].hello[0].world=0", "my[0].hello[0].moon=1", "my[0].hello[1].moon=3", "my[1].hello[0].moon=2"),
                "my[0].hello[0].moon=1\nmy[0].hello[0].world=0\nmy[0].hello[1].moon=3\nmy[1].hello[0].moon=2\n",
                "<cmdline><my><hello><moon>1</moon><world>0</world></hello><hello><moon>3</moon></hello></my><my><hello><moon>2</moon></hello></my></cmdline>",
                "--my [--hello [--moon 1 --world 0] [--moon 3]] [--hello [--moon 2]]");
    }

}
