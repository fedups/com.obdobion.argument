package com.obdobion.argument;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 *
 */
public class VariableTest
{

    static public class MyGroup
    {

        public String  testString;
        public MyGroup innerGroup;
    }

    public boolean       testboolean;
    public Boolean       testBoolean;
    public int           testInt;
    public Integer       testInteger;
    public Integer[]     testIntegerArray;
    public int[]         testIntArray;
    public Float         testFloat;
    public Float[]       testFloatArray;
    public float[]       testfloatArray;
    public String        testString;

    public String[]      testStringArray;

    public MyGroup       testGroup;
    public Object        testObjectGroup;
    public MyGroup       testGroupArray[];
    public List<MyGroup> testGroupList;

    public VariableTest()
    {

    }

    @Test
    public void groupWithoutOwnVariable() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t begin -k g", "   -t String -k s -v testString -p", "-t end -k g");
        cl.parse(this, "-g('1string')");
        Assert.assertEquals("string", "1string", testString);
    }

    @Test
    public void nullAssigner() throws Exception
    {
        final IVariableAssigner previousAssigner = VariableAssigner.setInstance(new NullAssigner());
        final IVariableAssigner nullAssigner = VariableAssigner.setInstance(previousAssigner);
        try
        {
            final CmdLine cl = new CmdLine();
            cl.compile("-t begin -k g", "   -t String -k s -v testString -p", "-t end -k g");
            VariableAssigner.setInstance(nullAssigner);
            cl.parse(this, "-g('1string')");
            Assert.assertNull("nulls should always be assigned", testString);
        } finally
        {
            VariableAssigner.setInstance(previousAssigner);
        }
    }

    @Test
    public void variablebooleanAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k i -v testboolean");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i"), this);
        Assert.assertEquals("testboolean", true, testboolean);
    }

    @Test
    public void variableBooleanAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Boolean -k i -v testBoolean");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i"), this);
        Assert.assertEquals("testBoolean", Boolean.TRUE, testBoolean);
    }

    @Test
    public void variablefloatArrayAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Float -m1 -k i -v testfloatArray");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 2.1 1.2"), this);
        Assert.assertEquals("testfloatArray 0", 2.1F, testfloatArray[0], 0);
        Assert.assertEquals("testfloatArray 1", 1.2F, testfloatArray[1], 0);
    }

    @Test
    public void variableFloatArrayAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Float -m1 -k i -v testFloatArray");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 2.1 1.2"), this);
        Assert.assertEquals("testFloatArray 0", 2.1F, testFloatArray[0].floatValue(), 0);
        Assert.assertEquals("testFloatArray 1", 1.2F, testFloatArray[1].floatValue(), 0);
    }

    @Test
    public void variableFloatAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Float -k i -v testFloat");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 12.3"), this);
        Assert.assertEquals("testFloat", 12.3F, testFloat.floatValue(), 0);
    }

    @Test
    public void variableGroupArrayStringAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t begin -k g -v testGroupArray -m1", "-t String -k s -v testString -p", "-t end -k g");
        cl.parse(this, "-g('1string')('2string')('-s3')");
        Assert.assertEquals("group1.string", "1string", testGroupArray[0].testString);
        Assert.assertEquals("group2.string", "2string", testGroupArray[1].testString);
        Assert.assertEquals("group3.string", "-s3", testGroupArray[2].testString);
    }

    @Test
    public void variableGroupGroupStringAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
                "-t begin -k g -v testGroup",
                "-t begin -k h -v innerGroup",
                "-t String -k s -v testString",
                "-t end -k h",
                "-t end -k g");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-g(-h(-s 'a group string'))"), this);
        Assert.assertEquals("group.string", "a group string", testGroup.innerGroup.testString);
    }

    @Test
    public void variableGroupListStringAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
                "-t begin -k g -v testGroupList --class com.obdobion.argument.VariableTest$MyGroup -m1",
                "-t String -k s -v testString -p",
                "-t end -k g");
        cl.parse(this, "-g('1string')('2string')('-s3')");
        Assert.assertEquals("group1.string", "1string", testGroupList.get(0).testString);
        Assert.assertEquals("group2.string", "2string", testGroupList.get(1).testString);
        Assert.assertEquals("group3.string", "-s3", testGroupList.get(2).testString);
    }

    @Test
    public void variableGroupStringAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t begin -k g -v testGroup", "-t String -k s -v testString", "-t end -k g");
        cl.parse(this, "-g(-s 'a group string')");
        Assert.assertNotNull("testGroup should not be null", testGroup);
        Assert.assertEquals("group.string", "a group string", testGroup.testString);
    }

    @Test
    public void variableGroupStringAssignmentToObjectList() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile(
                "-t begin -k g -v testObjectGroup --class " + MyGroup.class.getName(),
                "-t String -k s -v testString",
                "-t end -k g");
        cl.parse(this, "-g(-s 'a group string')");
        Assert.assertNotNull("testObjectGroup should not be null", testObjectGroup);
        Assert.assertEquals("group.string", "a group string", ((MyGroup) testObjectGroup).testString);
    }

    @Test
    public void variableIntArrayAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Integer -m1 -k i -v testIntArray");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 2 1"), this);
        Assert.assertEquals("testIntArray 0", 2, testIntArray[0]);
        Assert.assertEquals("testIntArray 1", 1, testIntArray[1]);
    }

    @Test
    public void variableintAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Integer -k i -v testInt");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 123"), this);
        Assert.assertEquals("testInt", 123, testInt);
    }

    @Test
    public void variableIntegerArrayAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Integer -m1 -k i -v testIntegerArray");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 2 1"), this);
        Assert.assertEquals("testIntegerArray 0", 2, testIntegerArray[0].intValue());
        Assert.assertEquals("testIntegerArray 1", 1, testIntegerArray[1].intValue());
    }

    @Test
    public void variableIntegerAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t Integer -k i -v testInteger");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 123"), this);
        Assert.assertEquals("testInteger", 123, testInteger.intValue());
    }

    @Test
    public void variableStringArrayAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -m1 -k i -v testStringArray");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 'this is a test string' anotheronetoo"),
                this);
        Assert.assertEquals("testStringArray 0", "this is a test string", testStringArray[0]);
        Assert.assertEquals("testStringArray 1", "anotheronetoo", testStringArray[1]);
    }

    @Test
    public void variableStringAssignment() throws Exception
    {

        final CmdLine cl = new CmdLine();
        cl.compile("-t string -k i -v testString");
        cl.parse(CommandLineParser.getInstance(cl.getCommandPrefix(), "-i 'this is a test string'"), this);
        Assert.assertEquals("testString", "this is a test string", testString);
    }
}
