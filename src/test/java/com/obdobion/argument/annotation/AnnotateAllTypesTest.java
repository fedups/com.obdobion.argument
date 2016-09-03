package com.obdobion.argument.annotation;

import java.io.File;
import java.io.IOException;
import java.lang.management.MemoryType;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;
import com.obdobion.argument.type.WildFiles;

/**
 * <p>AnnotateAllTypesTest class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class AnnotateAllTypesTest
{
    @Arg
    public boolean         pBooleanVar;
    @Arg()
    public int             pIntVar;
    @Arg()
    public long            pLongVar;

    @Arg()
    public float           pFloatVar;
    @Arg()
    public double          pDoubleVar;
    @Arg()
    public char            pCharVar;
    @Arg()
    public byte            pByteVar;
    @Arg()
    public Boolean         booleanVar;
    @Arg()
    public Integer         intVar;
    @Arg()
    public Long            longVar;
    @Arg()
    public Float           floatVar;

    @Arg()
    public Double          doubleVar;
    @Arg()
    public Character       charVar;
    @Arg()
    public Byte            byteVar;
    @Arg()
    public String          stringVar;

    @Arg()
    public int[]           pIntVars;
    @Arg()
    public long[]          pLongVars;
    @Arg()
    public float[]         pFloatVars;
    @Arg()
    public double[]        pDoubleVars;
    @Arg()
    public char[]          pCharVars;
    @Arg()
    public byte[]          pByteVars;

    @Arg()
    public Integer[]       intVars;
    @Arg()
    public Long[]          longVars;
    @Arg()
    public Float[]         floatVars;
    @Arg()
    public Double[]        doubleVars;
    @Arg()
    public Character[]     charVars;
    @Arg()
    public Byte[]          byteVars;
    @Arg()
    public String[]        stringVars;

    @Arg()
    public List<Integer>   intVarList;
    @Arg()
    public List<Long>      longVarList;
    @Arg()
    public List<Float>     floatVarList;
    @Arg()
    public List<Double>    doubleVarList;
    @Arg()
    public List<Character> charVarList;
    @Arg()
    public List<Byte>      byteVarList;
    @Arg()
    public List<String>    stringVarList;

    @Arg()
    public File            fileVar;
    @Arg()
    public Pattern         patternVar;
    @Arg()
    public WildFiles       wildFilesVar;
    @Arg()
    public Date            dateVar;

    @Arg()
    public File[]          fileVars;
    @Arg()
    public Pattern[]       patternVars;
    @Arg()
    public Date[]          dateVars;

    /*-
    @Arg()
    public Calendar     calendarVar;
    @Arg()
    public Calendar[]   calendarVars;
     */

    @Arg()
    public MemoryType      enumVar;
    /*-
    @Arg()
    public MemoryType[] enumVars;
    */

    @Arg()
    public Equ             equVar;
    @Arg()
    public Equ[]           equVars;

    @Arg()
    public List<File>      fileVarList;
    @Arg()
    public List<Pattern>   patternVarList;
    @Arg()
    public List<Date>      dateVarList;
    @Arg()
    public List<Equ>       equVarList;
    /*-
    @Arg()
    public List<MemoryType> enumVarList;
    */

    /**
     * <p>testAllDefaultValues.</p>
     *
     * @since 4.3.1
     */
    @Test
    public void testAllDefaultValues()
    {
        final ICmdLine cmdParser = new CmdLine("testAllDefaultValues", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] {});
        try
        {
            cmdParser.parse(userInput, this);
            Assert.assertFalse(pBooleanVar);
            Assert.assertEquals(0, pIntVar);
            Assert.assertEquals(0, pFloatVar, 0.001);
            Assert.assertEquals(0, pDoubleVar, 0.001);
            Assert.assertEquals(0, pCharVar);
            Assert.assertEquals(0, pByteVar);

            Assert.assertFalse(booleanVar);
            Assert.assertNull(intVar);
            Assert.assertNull(floatVar);
            Assert.assertNull(doubleVar);
            Assert.assertNull(stringVar);
            Assert.assertNull(charVar);
            Assert.assertNull(byteVar);

            Assert.assertNull(pIntVars);
            Assert.assertNull(pFloatVars);
            Assert.assertNull(pDoubleVars);
            Assert.assertNull(pCharVars);
            Assert.assertNull(pByteVars);

            Assert.assertNull(intVars);
            Assert.assertNull(floatVars);
            Assert.assertNull(doubleVars);
            Assert.assertNull(stringVars);
            Assert.assertNull(charVars);
            Assert.assertNull(byteVars);

            Assert.assertNull(dateVar);
            Assert.assertNull(enumVar);
            Assert.assertNull(equVar);
            Assert.assertNull(fileVar);
            Assert.assertNull(patternVar);
            Assert.assertNull(wildFilesVar);

            Assert.assertNull(dateVars);
            Assert.assertNull(equVars);
            Assert.assertNull(fileVars);
            Assert.assertNull(patternVars);

            Assert.assertNull(intVarList);
            Assert.assertNull(longVarList);
            Assert.assertNull(floatVarList);
            Assert.assertNull(doubleVarList);
            Assert.assertNull(charVarList);
            Assert.assertNull(byteVarList);
            Assert.assertNull(stringVarList);
            Assert.assertNull(fileVarList);
            Assert.assertNull(patternVarList);
            Assert.assertNull(dateVarList);
            Assert.assertNull(equVarList);

        } catch (IOException | ParseException e)
        {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * <p>testAllSpecifiedValues.</p>
     *
     * @since 4.3.1
     */
    @Test
    public void testAllSpecifiedValues()
    {
        final ICmdLine cmdParser = new CmdLine("testAllSpecifiedValues", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] {
                " --pbooleanVar",
                " --pintVar 12",
                " --plongVar 67",
                " --pfloatVar 12.34",
                " --pdoubleVar 45",
                " --pcharVar a",
                " --pbyteVar b",

                " --booleanVar",
                " --intVar 12",
                " --longVar 67",
                " --floatVar 12.34",
                " --doubleVar 45",
                " --stringVar 'WHAT!'",
                " --charVar a",
                " --byteVar b",

                " --pintVars 12 21",
                " --plongVars 67 76",
                " --pfloatVars 12.34 43.21",
                " --pdoubleVars 45 54",
                " --pcharVars a b",
                " --pbyteVars b a",

                " --intVars 12 21",
                " --longVars 67 76",
                " --floatVars 12.34 43.21",
                " --doubleVars 45 54",
                " --stringVars 'WHAT!' 'WHEN!'",
                " --charVars a b",
                " --byteVars b a",

                " --dateVar 2016/07/27",
                " --enumVar HEAP",
                " --equVar a",
                " --fileVar b",
                " --patternVar c",
                " --wildFilesVar d",

                " --dateVars  2016/07/27  2016/07/28",
                " --equVars b a",
                " --fileVars b a",
                " --patternVars b a",

                " --intVarList 1 2 3",
                " --longVarList 4 5 6",
                " --floatVarList 7.1 7.2 7.3",
                " --doubleVarList 8.1 8.2 8.3",
                " --charVarList A B C",
                " --byteVarList D E F",
                " --stringVarList 'Hello world' 'good bye'",
                " --fileVarList file1 file2 file3",
                " --patternVarList '.*' '$[0-1]*^'",
                " --dateVarList 2016/07/29  2016/07/30",
                " --equVarList g h i"
        });
        try
        {
            cmdParser.parse(userInput, this);

            Assert.assertTrue("booleanVar", pBooleanVar);
            Assert.assertEquals(12, pIntVar);
            Assert.assertEquals(67, pLongVar);
            Assert.assertEquals(12.34, pFloatVar, 0.001);
            Assert.assertEquals(45, pDoubleVar, 0.001);
            Assert.assertEquals('a', pCharVar);
            Assert.assertEquals('b', pByteVar);

            Assert.assertTrue(booleanVar);
            Assert.assertEquals(12, intVar.intValue());
            Assert.assertEquals(67, longVar.longValue());
            Assert.assertEquals(12.34, floatVar, 0.001);
            Assert.assertEquals(45, doubleVar, 0.001);
            Assert.assertEquals("what!", stringVar);
            Assert.assertEquals('a', charVar.charValue());
            Assert.assertEquals('b', byteVar.byteValue());

            Assert.assertEquals(12, pIntVars[0]);
            Assert.assertEquals(67, pLongVars[0]);
            Assert.assertEquals(12.34, pFloatVars[0], 0.001);
            Assert.assertEquals(45, pDoubleVars[0], 0.001);
            Assert.assertEquals('a', pCharVars[0]);
            Assert.assertEquals('b', pByteVars[0]);
            Assert.assertEquals(21, pIntVars[1]);
            Assert.assertEquals(76, pLongVars[1]);
            Assert.assertEquals(43.21, pFloatVars[1], 0.001);
            Assert.assertEquals(54, pDoubleVars[1], 0.001);
            Assert.assertEquals('b', pCharVars[1]);
            Assert.assertEquals('a', pByteVars[1]);

            Assert.assertEquals(12, intVars[0].intValue());
            Assert.assertEquals(67, longVars[0].longValue());
            Assert.assertEquals(12.34, floatVars[0], 0.001);
            Assert.assertEquals(45, doubleVars[0], 0.001);
            Assert.assertEquals("what!", stringVars[0]);
            Assert.assertEquals('a', charVars[0].charValue());
            Assert.assertEquals('b', byteVars[0].byteValue());
            Assert.assertEquals(21, intVars[1].intValue());
            Assert.assertEquals(76, longVars[1].longValue());
            Assert.assertEquals(43.21, floatVars[1], 0.001);
            Assert.assertEquals(54, doubleVars[1], 0.001);
            Assert.assertEquals("when!", stringVars[1]);
            Assert.assertEquals('b', charVars[1].charValue());
            Assert.assertEquals('a', byteVars[1].byteValue());

            Assert.assertEquals(1469595600000L, dateVar.getTime());
            Assert.assertEquals(MemoryType.HEAP, enumVar);
            Assert.assertEquals("a", equVar.toString());
            Assert.assertEquals("b", fileVar.getName());
            Assert.assertEquals("c", patternVar.pattern());
            Assert.assertEquals("d", wildFilesVar.toString());

            Assert.assertEquals(1469595600000L, dateVars[0].getTime());
            Assert.assertEquals(1469682000000L, dateVars[1].getTime());

            Assert.assertEquals("b", equVars[0].toString());
            Assert.assertEquals("a", equVars[1].toString());

            Assert.assertEquals("b", fileVars[0].getName());
            Assert.assertEquals("a", fileVars[1].getName());

            Assert.assertEquals("b", patternVars[0].pattern());
            Assert.assertEquals("a", patternVars[1].pattern());

            Assert.assertEquals(1, intVarList.get(0).intValue());
            Assert.assertEquals(2, intVarList.get(1).intValue());
            Assert.assertEquals(3, intVarList.get(2).intValue());

            Assert.assertEquals(4, longVarList.get(0).longValue());
            Assert.assertEquals(5, longVarList.get(1).longValue());
            Assert.assertEquals(6, longVarList.get(2).longValue());

            Assert.assertEquals(7.1, floatVarList.get(0).floatValue(), 0.01);
            Assert.assertEquals(7.2, floatVarList.get(1).floatValue(), 0.01);
            Assert.assertEquals(7.3, floatVarList.get(2).floatValue(), 0.01);

            Assert.assertEquals(8.1, doubleVarList.get(0).doubleValue(), 0.01);
            Assert.assertEquals(8.2, doubleVarList.get(1).doubleValue(), 0.01);
            Assert.assertEquals(8.3, doubleVarList.get(2).doubleValue(), 0.01);

            Assert.assertEquals('a', charVarList.get(0).charValue());
            Assert.assertEquals('b', charVarList.get(1).charValue());
            Assert.assertEquals('c', charVarList.get(2).charValue());

            Assert.assertEquals('d', byteVarList.get(0).byteValue());
            Assert.assertEquals('e', byteVarList.get(1).byteValue());
            Assert.assertEquals('f', byteVarList.get(2).byteValue());

            Assert.assertEquals("hello world", stringVarList.get(0));
            Assert.assertEquals("good bye", stringVarList.get(1));

            Assert.assertEquals("file1", fileVarList.get(0).getName());
            Assert.assertEquals("file2", fileVarList.get(1).getName());
            Assert.assertEquals("file3", fileVarList.get(2).getName());

            Assert.assertEquals(".*", patternVarList.get(0).pattern());
            Assert.assertEquals("$[0-1]*^", patternVarList.get(1).pattern());

            Assert.assertEquals(1469768400000L, dateVarList.get(0).getTime());
            Assert.assertEquals(1469854800000L, dateVarList.get(1).getTime());

            Assert.assertEquals("g", equVarList.get(0).toString());
            Assert.assertEquals("h", equVarList.get(1).toString());
            Assert.assertEquals("i", equVarList.get(2).toString());

        } catch (IOException | ParseException e)
        {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * <p>testUsage.</p>
     *
     * @since 4.3.1
     */
    @Test
    public void testUsage()
    {
        final ICmdLine cmdParser = new CmdLine("testAllSpecifiedValues", "", '-', '!');
        final IParserInput userInput = CommandLineParser.getInstance('-', new String[] {
                "--help"
        });
        try
        {
            cmdParser.parse(userInput, this);

        } catch (IOException | ParseException e)
        {
            Assert.fail(e.getMessage());
        }
    }
}
