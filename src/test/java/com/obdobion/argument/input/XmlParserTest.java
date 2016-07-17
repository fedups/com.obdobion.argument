package com.obdobion.argument.input;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;

import junit.framework.TestCase;

/**
 * @author Chris DeGreef
 *
 */
public class XmlParserTest extends TestCase
{

    InputStream is;

    public void testArrayComma () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><hello delim=';'>0;1</hello></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tInteger-khello-m1");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--hello0 1", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><hello>0</hello><hello>1</hello></cmdline>", XmlParser.unparseTokens(cmd.allArgs()));
    }

    public void testArrayDupTags () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><hello>0</hello><hello>1</hello></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tInteger-khello-m1");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--hello0 1", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><hello>0</hello><hello>1</hello></cmdline>", XmlParser.unparseTokens(cmd.allArgs()));
    }

    public void testboolean () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><boolean></boolean></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tboolean-k boolean");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--boolean", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><boolean></boolean></cmdline>", XmlParser.unparseTokens(cmd.allArgs()));

    }

    public void testEmbeddedLevelArray () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><my><hello><moon>1</moon><world>0</world></hello><hello><moon>2</moon></hello></my></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-kmy", "-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello", "-tend-kmy");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--my[--hello[--moon1 --world0] [--moon2]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><my><hello><moon>1</moon><world>0</world></hello><hello><moon>2</moon></hello></my></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespace () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><b><c><d>alpha</d></c></b></a></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb-m1", "-tbegin-kc", "-tString-kd-m1", "-tend-kc", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[-b[-c[-d'alpha']]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><b><c><d>alpha</d></c></b></a></cmdline>", XmlParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespace2 () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><b z='WHAT' x='WHEN'><c><d>alpha</d></c></b></a></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb-m1", "-tString-kz", "-tString-kx", "-tbegin-kc", "-tString-kd-m1", "-tend-kc", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[-b[-z'WHAT' -x'WHEN' -c[-d'alpha']]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><b><z>WHAT</z><x>WHEN</x><c><d>alpha</d></c></b></a></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos2b () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><noname><c>alpha</c></noname></a></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb--pos", "-tString-kc", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[ [-c'alpha']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><noname><c>alpha</c></noname></a></cmdline>", XmlParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos2Changes () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><b c='alpha'/><d z='omega'/></a></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tString-kc", "-tend-kb", "-tbegin-kd", "-tString-kz", "-tend-kd", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[-b[-c'alpha'] -d[-z'omega']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><b><c>alpha</c></b><d><z>omega</z></d></a></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos2twicea () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><noname><c>alpha</c><c>omega</c></noname></a></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb--pos", "-tString-kc-m1", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[ [-c'alpha' 'omega']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><noname><c>alpha</c><c>omega</c></noname></a></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos2twiceaWithOrder () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><noname><c>omega</c></noname><noname><c>alpha</c></noname></a></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb--pos-m1", "-tString-kc-m1", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[ [-c'omega'] [-c'alpha']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><noname><c>omega</c></noname><noname><c>alpha</c></noname></a></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos2twiceb () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><noname><c><noname>alpha</noname><noname>omega</noname></c></noname></a></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb--pos-m1", "-tbegin-kc", "-tString-kd-m1--pos", "-tend-kc", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[ [-c[ 'alpha' 'omega']]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><noname><c><noname>alpha</noname><noname>omega</noname></c></noname></a></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4a () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><aa><b><c>alpha</c></b></aa></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-kaa", "-tbegin-kb", "-tString-kc", "-tend-kb", "-tend-kaa");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--aa[-b[-c'alpha']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><aa><b><c>alpha</c></b></aa></cmdline>", XmlParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4aAttr () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><b c='alpha'/></a></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tString-kc", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[-b[-c'alpha']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><b><c>alpha</c></b></a></cmdline>", XmlParser.unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4b () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><b><c><noname>alpha</noname></c></b></a></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tbegin-kc", "-tString-kz--pos", "-tend-kc", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[-b[-c[ 'alpha']]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><b><c><noname>alpha</noname></c></b></a></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4bAttr () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><b><c noname='alpha'></c></b></a></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tbegin-kc", "-tString-kz--pos", "-tend-kc", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[-b[-c[ 'alpha']]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><b><c><noname>alpha</noname></c></b></a></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4twicea () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><b><c>alpha</c><c>omega</c></b></a></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tString-kc-m1", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[-b[-c'alpha' 'omega']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><b><c>alpha</c><c>omega</c></b></a></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespacePos4twiceb () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><b><c><noname>alpha</noname></c><c><noname>omega</noname></c></b></a></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd
                .compile("-tbegin-ka", "-tbegin-kb", "-tbegin-kc-m1", "-tString-kd--pos", "-tend-kc", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[-b[-c[ 'alpha'] [ 'omega']]]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><b><c><noname>alpha</noname></c><c><noname>omega</noname></c></b></a></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testNamespaceRepeatingGroup () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><noname><c>alpha</c></noname></a></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb --pos", "-tstring-kc", "-tend-kb", "-tend-ka");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[ [-c'alpha']]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><noname><c>alpha</c></noname></a></cmdline>", XmlParser.unparseTokens(cmd.allArgs()));
    }

    public void testPositionalArrayDelim () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><noname delim=','>world,for now</noname></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tString-ks--pos-m1");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals(" 'world' 'for now'", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><noname>world</noname><noname><![CDATA[for now]]></noname></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));

    }

    public void testPositionalArrayDelim2 () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><noname delim=','>world,<![CDATA[for now]]></noname></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tString-ks--pos-m1");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals(" 'world' 'for now'", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><noname>world</noname><noname><![CDATA[for now]]></noname></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));

    }

    public void testPositionalArrayDupTags () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><noname>world</noname><noname>for now</noname></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tString-ks--pos-m1");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals(" 'world' 'for now'", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><noname>world</noname><noname><![CDATA[for now]]></noname></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));

    }

    public void testPositionalArrayDupTags2 () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><noname>world</noname><noname><![CDATA[for now]]></noname></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tString-ks--pos-m1");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals(" 'world' 'for now'", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><noname>world</noname><noname><![CDATA[for now]]></noname></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));

    }

    public void testQuoted () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><Hello>world</Hello><goodbye>'for now'</goodbye></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tString-k Hello", "-tString-k goodbye");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--Hello'world' --goodbye'\\'for now\\''", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><Hello>world</Hello><goodbye><![CDATA['for now']]></goodbye></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testQuotedEsc () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><Hello>world</Hello><goodbye>&quot;for now&quot;</goodbye></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tString-k Hello", "-tString-k goodbye");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--Hello'world' --goodbye'\\\"for now\\\"'", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><Hello>world</Hello><goodbye><![CDATA[\"for now\"]]></goodbye></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testSimple () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><Hello>world</Hello><goodbye>for now</goodbye></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tString-k Hello", "-tString-k goodbye");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--Hello'world' --goodbye'for now'", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><Hello>world</Hello><goodbye><![CDATA[for now]]></goodbye></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testTopLevelArray1 () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><hello moon='1'/><hello world='0'/></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--hello[--moon1] [--world0]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><hello><moon>1</moon></hello><hello><world>0</world></hello></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testTopLevelArray1a () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><hello><moon>1</moon><world>0</world></hello></cmdline>".getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--hello[--moon1 --world0]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><hello><moon>1</moon><world>0</world></hello></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testTopLevelArray2 () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><hello><moon>1</moon><world>0</world></hello><hello><moon>2</moon></hello></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--hello[--moon1 --world0] [--moon2]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><hello><moon>1</moon><world>0</world></hello><hello><moon>2</moon></hello></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testTwoLevelArray () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><my><hello><moon>1</moon><world>0</world></hello></my><my><hello><moon>2</moon></hello></my></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-kmy-m1", "-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello", "-tend-kmy");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--my[--hello[--moon1 --world0]] [--hello[--moon2]]", CommandLineParser
                .unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><my><hello><moon>1</moon><world>0</world></hello></my><my><hello><moon>2</moon></hello></my></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testTwoNamespaces () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><a><b><c><d>alpha</d></c></b></a><hello moon='2' world='1'></hello></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-ka", "-tbegin-kb", "-tbegin-kc", "-tString-kd-m1", "-tend-kc", "-tend-kb", "-tend-ka", "-tbegin-khello", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("-a[-b[-c[-d'alpha']]] --hello[--moon2 --world1]", CommandLineParser.unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><a><b><c><d>alpha</d></c></b></a><hello><moon>2</moon><world>1</world></hello></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

    public void testTwoTwoLevelArray () throws Exception
    {

        is = new ByteArrayInputStream("<cmdline><my><hello><moon>1</moon><world>0</world></hello><hello><moon>3</moon></hello></my><my><hello><moon>2</moon></hello></my></cmdline>"
                .getBytes());

        final ICmdLine cmd = new CmdLine("");
        cmd.compile("-tbegin-kmy-m1", "-tbegin-khello-m1", "-tInteger-kmoon", "-tInteger-kworld", "-tend-khello", "-tend-kmy");
        cmd.parse(XmlParser.getInstance(is));

        assertEquals("--my[--hello[--moon1 --world0] [--moon3]] [--hello[--moon2]]", CommandLineParser
                .unparseTokens(cmd.allArgs()));
        assertEquals("<cmdline><my><hello><moon>1</moon><world>0</world></hello><hello><moon>3</moon></hello></my><my><hello><moon>2</moon></hello></my></cmdline>", XmlParser
                .unparseTokens(cmd.allArgs()));
    }

}
