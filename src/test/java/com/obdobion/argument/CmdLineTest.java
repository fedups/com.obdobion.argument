package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.CmdLineTest.GroupMaster.Grouping;
import com.obdobion.argument.annotation.Arg;

/**
 * <p>CmdLineTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class CmdLineTest
{
    static public class DoubleDash
    {
        @Arg(shortName = 'a')
        boolean avar;

        @Arg(shortName = 'b')
        boolean bee;
    }

    static public class DoubleParm
    {
        @Arg(required = true)
        double   var1;

        @Arg(multimax = 3, range = { "-15", "3000" })
        double[] var2;
    }

    static public class FloatParm
    {
        @Arg(required = true)
        float   var1;

        @Arg(multimax = 3, range = { "-15", "3000" })
        float[] var2;
    }

    static public class GroupMaster
    {
        static public class Grouping
        {
            static public class GroupA
            {
                @Arg(shortName = 'a')
                boolean item1;
                @Arg(shortName = 'b')
                boolean item2;
            }

            @Arg(shortName = 'g')
            GroupA  groupa;

            @Arg(shortName = 's')
            boolean itemS;
        }

        @Arg(shortName = 'm')
        Grouping master;
    }

    static final class IntCfg
    {
        @Arg(shortName = 'i', required = true)
        int   int1;

        @Arg(shortName = 'm', required = true, multimax = 2, range = { "1", "3000" })
        int[] int2;
    }

    static final class LongCfg
    {
        @Arg(shortName = 'i', required = true)
        long   long1;

        @Arg(shortName = 'm', required = true, multimax = 2, range = { "1", "3000" })
        long[] long2;
    }

    static public class NameValidation
    {
        @Arg(shortName = '1')
        boolean invalid;
    }

    static public class NameValidation1
    {
        @Arg(longName = "1word")
        boolean invalid;
    }

    static public class Parmed
    {
        @Arg(shortName = 'i', required = true)
        String   inputfile;

        @Arg(shortName = 'e')
        boolean  ebcdic;

        @Arg(shortName = 'x')
        boolean  dup1;

        @Arg(shortName = 'd')
        String[] dup2;
    }

    static public class PatternMatching
    {
        @Arg(shortName = 'p', matches = "one|two")
        String   stringVar;

        @Arg(shortName = 'q', matches = "one|two")
        String[] stringArray;
    }

    static public class PositionBool
    {

        @Arg(shortName = 'i')
        String   infile;

        @Arg(shortName = 'a')
        boolean  p1;

        @Arg(shortName = 's', positional = true)
        String[] outfile;

        @Arg(shortName = 'b')
        boolean  p2;
    }

    static public class StringCfg
    {
        @Arg(shortName = 'i')
        String[] stringArray;

        @Arg(shortName = 'x')
        String   stringVar;

        @Arg
        boolean  word1;
    }

    static public class TestBoolean
    {
        @Arg(shortName = 'i')
        boolean inputfile;

        @Arg(shortName = 'e')
        boolean ebcdic;

        @Arg(shortName = 'x')
        boolean dup1;

        @Arg(shortName = 'd')
        boolean dup2;

        @Arg(defaultValues = "true")
        boolean dup3;

        @Arg(longName = "typedefs-and-c++")
        boolean varForCPlusPlus;
    }

    /**
     * <p>booleanConcats1.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void booleanConcats1() throws Exception
    {
        final TestBoolean target = new TestBoolean();
        CmdLine.load(target, "-i -e");
        Assert.assertTrue(target.inputfile);
        Assert.assertTrue(target.ebcdic);
        Assert.assertFalse(target.dup1);
        Assert.assertFalse(target.dup2);
    }

    /**
     * <p>booleanConcats2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void booleanConcats2() throws Exception
    {
        final TestBoolean target = new TestBoolean();
        CmdLine.load(target, "--input --eb");
        Assert.assertTrue(target.inputfile);
        Assert.assertTrue(target.ebcdic);
        Assert.assertFalse(target.dup1);
        Assert.assertFalse(target.dup2);
    }

    /**
     * <p>booleanConcats3.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void booleanConcats3() throws Exception
    {
        final TestBoolean target = new TestBoolean();
        CmdLine.load(target, "--in -xe");
        Assert.assertTrue(target.inputfile);
        Assert.assertTrue(target.ebcdic);
        Assert.assertTrue(target.dup1);
        Assert.assertFalse(target.dup2);
    }

    /**
     * <p>booleanConcats4.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void booleanConcats4() throws Exception
    {
        final TestBoolean target = new TestBoolean();
        CmdLine.load(target, "-d");
        Assert.assertFalse(target.inputfile);
        Assert.assertFalse(target.ebcdic);
        Assert.assertFalse(target.dup1);
        Assert.assertTrue(target.dup2);
    }

    /**
     * <p>booleanConcats5.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void booleanConcats5() throws Exception
    {
        final TestBoolean target = new TestBoolean();
        CmdLine.load(target, "-dxie");
        Assert.assertTrue(target.inputfile);
        Assert.assertTrue(target.ebcdic);
        Assert.assertTrue(target.dup1);
        Assert.assertTrue(target.dup2);
    }

    /**
     * <p>cPlusPlusName.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void cPlusPlusName() throws Exception
    {
        final TestBoolean target = new TestBoolean();
        CmdLine.load(target, "--typedefs-and-c++");
        Assert.assertTrue(target.varForCPlusPlus);
    }

    /**
     * <p>dashDoubleDash1.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dashDoubleDash1() throws Exception
    {
        final DoubleDash target = new DoubleDash();
        CmdLine.load(target, "-ab");
        Assert.assertTrue(target.avar);
        Assert.assertTrue(target.bee);
    }

    /**
     * <p>dashDoubleDash2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dashDoubleDash2() throws Exception
    {
        final DoubleDash target = new DoubleDash();
        CmdLine.load(target, "-a--bee");
        Assert.assertTrue(target.avar);
        Assert.assertTrue(target.bee);
    }

    /**
     * <p>dashDoubleDash3.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void dashDoubleDash3() throws Exception
    {
        final DoubleDash target = new DoubleDash();
        CmdLine.load(target, "--bee -a");
        Assert.assertTrue(target.avar);
        Assert.assertTrue(target.bee);
    }

    /**
     * <p>equalsOnCharCommand.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void equalsOnCharCommand() throws Exception
    {
        final StringCfg target = new StringCfg();
        CmdLine.load(target, "-x=abc -i=def,xyz");
        Assert.assertEquals("abc", target.stringVar);
        Assert.assertEquals("def", target.stringArray[0]);
        Assert.assertEquals("xyz", target.stringArray[1]);
    }

    /**
     * <p>equalsOnWordCommands1.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void equalsOnWordCommands1() throws Exception
    {
        final StringCfg target = new StringCfg();
        CmdLine.load(target, "--stringVar=abc");
        Assert.assertEquals("abc", target.stringVar);
    }

    /**
     * <p>equalsOnWordCommands2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void equalsOnWordCommands2() throws Exception
    {
        final StringCfg target = new StringCfg();
        CmdLine.load(target, "--stringVar=abc--stringArray=def,xyz");
        Assert.assertEquals("abc", target.stringVar);
        Assert.assertEquals("def", target.stringArray[0]);
        Assert.assertEquals("xyz", target.stringArray[1]);
    }

    /**
     * <p>grouping.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void grouping() throws Exception
    {
        final Grouping target = new Grouping();
        CmdLine.load(target, "-g ( -a -b )");
        Assert.assertTrue(target.groupa.item1);
        Assert.assertTrue(target.groupa.item2);
    }

    /**
     * <p>groupingGroups.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void groupingGroups() throws Exception
    {
        final GroupMaster target = new GroupMaster();
        CmdLine.load(target, "-m(-s-g(-ab))");
        Assert.assertTrue(target.master.itemS);
        Assert.assertTrue(target.master.groupa.item1);
        Assert.assertTrue(target.master.groupa.item2);
    }

    /**
     * <p>integers.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void integers() throws Exception
    {
        final IntCfg target = new IntCfg();
        CmdLine.load(target, "-i 1 -m1 '2026'");
        Assert.assertEquals(1, target.int1);
        Assert.assertEquals(2, target.int2.length);
        Assert.assertEquals(1, target.int2[0]);
        Assert.assertEquals(2026, target.int2[1]);
    }

    /**
     * <p>longs.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void longs() throws Exception
    {
        final LongCfg target = new LongCfg();
        CmdLine.load(target, "-i 1 -m1 '2026'");
        Assert.assertEquals(1, target.long1);
        Assert.assertEquals(2, target.long2.length);
        Assert.assertEquals(1, target.long2[0]);
        Assert.assertEquals(2026, target.long2[1]);
    }

    /**
     * <p>multiValuedString1.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void multiValuedString1() throws Exception
    {
        final StringCfg target = new StringCfg();
        CmdLine.load(target, "-i i1");
        Assert.assertEquals("i1", target.stringArray[0]);
    }

    /**
     * <p>multiValuedString2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void multiValuedString2() throws Exception
    {
        final StringCfg target = new StringCfg();
        CmdLine.load(target, "-i i1 i2");
        Assert.assertEquals("i1", target.stringArray[0]);
        Assert.assertEquals("i2", target.stringArray[1]);
    }

    /**
     * <p>multiValuedString3.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void multiValuedString3() throws Exception
    {
        final StringCfg target = new StringCfg();
        CmdLine.load(target, "-i i1 i2 -x x1");
        Assert.assertEquals("i1", target.stringArray[0]);
        Assert.assertEquals("i2", target.stringArray[1]);
        Assert.assertEquals("x1", target.stringVar);
    }

    /**
     * <p>numberInAKeyWordIsOK.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void numberInAKeyWordIsOK() throws Exception
    {
        try
        {
            final StringCfg target = new StringCfg();
            CmdLine.load(target, "--word1");
        } catch (final ParseException p)
        {
            Assert.fail("should have allowed a digit after the 1st char in a key word: " + p.getMessage());
        }
    }

    /**
     * <p>numericKeyCharIsBad.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void numericKeyCharIsBad() throws Exception
    {
        try
        {
            final NameValidation target = new NameValidation();
            CmdLine.load(target, "--usage");
            Assert.fail("expected exception");
        } catch (final ParseException p)
        {
            Assert.assertEquals("The Key Character can not be a digit '1'", p.getMessage());
        }
    }

    /**
     * <p>numericKeyCharIsBad2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void numericKeyCharIsBad2() throws Exception
    {
        try
        {
            final NameValidation1 target = new NameValidation1();
            CmdLine.load(target, "--usage");
            Assert.fail("expected exception");
        } catch (final ParseException p)
        {
            Assert.assertEquals("The first character of a Key Word can not be a digit \"1word\"", p.getMessage());
        }
    }

    /**
     * <p>parmed1.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void parmed1() throws Exception
    {
        final Parmed target = new Parmed();
        CmdLine.load(target, "-i infile");
        Assert.assertEquals("infile", target.inputfile);
        Assert.assertFalse(target.ebcdic);
        Assert.assertFalse(target.dup1);
        Assert.assertNull(target.dup2);
    }

    /**
     * <p>parmed2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void parmed2() throws Exception
    {
        final Parmed target = new Parmed();
        CmdLine.load(target, "-i infile -x");
        Assert.assertEquals("infile", target.inputfile);
        Assert.assertFalse(target.ebcdic);
        Assert.assertTrue(target.dup1);
        Assert.assertNull(target.dup2);
    }

    /**
     * <p>parmed3.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void parmed3() throws Exception
    {
        final Parmed target = new Parmed();
        CmdLine.load(target, "-xi infile");
        Assert.assertEquals("infile", target.inputfile);
        Assert.assertFalse(target.ebcdic);
        Assert.assertTrue(target.dup1);
        Assert.assertNull(target.dup2);
    }

    /**
     * <p>parmed4.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void parmed4() throws Exception
    {
        final Parmed target = new Parmed();
        CmdLine.load(target, "-xiinfile");
        Assert.assertEquals("infile", target.inputfile);
        Assert.assertFalse(target.ebcdic);
        Assert.assertTrue(target.dup1);
        Assert.assertNull(target.dup2);
    }

    /**
     * <p>patternMatching1.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void patternMatching1() throws Exception
    {
        final PatternMatching target = new PatternMatching();
        CmdLine.load(target, "-ptwo");
        Assert.assertEquals("two", target.stringVar);
        Assert.assertNull(target.stringArray);
    }

    /**
     * <p>patternMatching2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void patternMatching2() throws Exception
    {
        final PatternMatching target = new PatternMatching();
        CmdLine.load(target, "-q two one");
        Assert.assertNull(target.stringVar);
        Assert.assertEquals("two", target.stringArray[0]);
        Assert.assertEquals("one", target.stringArray[1]);
    }

    /**
     * <p>positionalBooleans.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void positionalBooleans() throws Exception
    {
        final PositionBool target = new PositionBool();
        CmdLine.load(target, "-a what -b -i when where");
        Assert.assertTrue(target.p1);
        Assert.assertTrue(target.p2);
        Assert.assertEquals("what", target.outfile[0]);
        Assert.assertEquals("where", target.outfile[1]);
        Assert.assertEquals("when", target.infile);
    }

    /**
     * <p>testBoolean1.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testBoolean1() throws Exception
    {
        final TestBoolean target = new TestBoolean();
        CmdLine.load(target, "-i");
        Assert.assertTrue(target.inputfile);
        Assert.assertFalse(target.ebcdic);
        Assert.assertFalse(target.dup1);
        Assert.assertFalse(target.dup2);
        Assert.assertTrue(target.dup3);
    }

    /**
     * <p>testBoolean2.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testBoolean2() throws Exception
    {
        final TestBoolean target = new TestBoolean();
        CmdLine.load(target, "-exi--dup3");
        Assert.assertTrue(target.inputfile);
        Assert.assertTrue(target.ebcdic);
        Assert.assertTrue(target.dup1);
        Assert.assertFalse(target.dup2);
        Assert.assertFalse(target.dup3);
    }

    /**
     * <p>testDouble.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testDouble() throws Exception
    {
        final DoubleParm target = new DoubleParm();
        CmdLine.load(target, "--var1 1.0 --var2 1 2026.25 -14.355");
        Assert.assertEquals(1.0, target.var1, 0.01);
        Assert.assertEquals(1.0, target.var2[0], 0.01);
        Assert.assertEquals(2026.25, target.var2[1], 0.01);
        Assert.assertEquals(-14.355, target.var2[2], 0.01);
    }

    /**
     * <p>testFloat.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testFloat() throws Exception
    {
        final FloatParm target = new FloatParm();
        CmdLine.load(target, "--var1 1.0 --var2 1 2026.25 -14.355");
        Assert.assertEquals(1.0, target.var1, 0.01);
        Assert.assertEquals(1.0, target.var2[0], 0.01);
        Assert.assertEquals(2026.25, target.var2[1], 0.01);
        Assert.assertEquals(-14.355, target.var2[2], 0.01);
    }
}
