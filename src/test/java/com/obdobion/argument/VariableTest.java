package com.obdobion.argument;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.variables.IVariableAssigner;
import com.obdobion.argument.variables.NullAssigner;
import com.obdobion.argument.variables.VariableAssigner;

/**
 * <p>
 * VariableTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class VariableTest
{
    static public class MyGroup
    {
        @Arg(shortName = 's', positional = true)
        public String  testString;

        @Arg(excludeArgs = "innerGroup")
        public MyGroup innerGroup;
    }

    @Arg(shortName = 'i')
    public boolean       testboolean;

    @Arg(longName = "BClass")
    public Boolean       testBoolean;

    @Arg
    public int           testInt;

    @Arg
    public Integer       testInteger;

    @Arg
    public Integer[]     testIntegerArray;

    @Arg
    public int[]         testIntArray;

    @Arg
    public Float         testFloat;

    @Arg(longName = "ClassFloatArray")
    public Float[]       testFloatArray;

    @Arg(longName = "PrimitiveFloatArray")
    public float[]       testfloatArray;

    @Arg
    public String        testString;

    @Arg
    public String[]      testStringArray;

    @Arg(shortName = 'g')
    public MyGroup       testGroup;

    @Arg(instanceClass = "com.obdobion.argument.VariableTest$MyGroup")
    public Object        testObjectGroup;

    @Arg
    public MyGroup       testGroupArray[];

    @Arg
    public List<MyGroup> testGroupList;

    /**
     * <p>
     * Constructor for VariableTest.
     * </p>
     *
     * @since 4.3.1
     */
    public VariableTest()
    {

    }

    /**
     * <p>
     * groupWithoutOwnVariable.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void groupWithoutOwnVariable() throws Exception
    {
        CmdLine.load(this, "-g('1string')");
        Assert.assertEquals("1string", testGroup.testString);
    }

    /**
     * <p>
     * nullAssigner.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void nullAssigner() throws Exception
    {
        final IVariableAssigner previousAssigner = VariableAssigner.setInstance(new NullAssigner());
        final IVariableAssigner nullAssigner = VariableAssigner.setInstance(previousAssigner);
        try
        {
            VariableAssigner.setInstance(nullAssigner);
            CmdLine.load(this, "-g('1string')");
            Assert.assertNull("nulls should always be assigned", testString);
        } finally
        {
            VariableAssigner.setInstance(previousAssigner);
        }
    }

    /**
     * <p>
     * variablebooleanAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variablebooleanAssignment() throws Exception
    {
        CmdLine.load(this, "-i");
        Assert.assertTrue(testboolean);
    }

    /**
     * <p>
     * variableBooleanAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableBooleanAssignment() throws Exception
    {
        CmdLine.load(this, "--bclass");
        Assert.assertTrue(testBoolean);
    }

    /**
     * <p>
     * variablefloatArrayAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variablefloatArrayAssignment() throws Exception
    {
        CmdLine.load(this, "--primitiveFloatArray 2.1 1.2");
        Assert.assertEquals(2.1F, testfloatArray[0], 0);
        Assert.assertEquals(1.2F, testfloatArray[1], 0);
    }

    /**
     * <p>
     * variableFloatArrayAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableFloatArrayAssignment() throws Exception
    {
        CmdLine.load(this, "--ClassFloatArray 2.1 1.2");
        Assert.assertEquals(2.1F, testFloatArray[0].floatValue(), 0);
        Assert.assertEquals(1.2F, testFloatArray[1].floatValue(), 0);
    }

    /**
     * <p>
     * variableFloatAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableFloatAssignment() throws Exception
    {
        CmdLine.load(this, "--testFloat 12.3");
        Assert.assertEquals(12.3F, testFloat.floatValue(), 0);
    }

    /**
     * <p>
     * variableGroupArrayStringAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableGroupArrayStringAssignment() throws Exception
    {
        CmdLine.load(this, "--testGroupArray('1string')('2string')('-s3')");
        Assert.assertEquals("group1.string", "1string", testGroupArray[0].testString);
        Assert.assertEquals("group2.string", "2string", testGroupArray[1].testString);
        Assert.assertEquals("group3.string", "-s3", testGroupArray[2].testString);
    }

    /**
     * <p>
     * variableGroupGroupStringAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableGroupGroupStringAssignment() throws Exception
    {
        CmdLine.load(this, "--testGroup(--innerGroup('a group string'))");
        Assert.assertEquals("a group string", testGroup.innerGroup.testString);
    }

    /**
     * <p>
     * variableGroupListStringAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableGroupListStringAssignment() throws Exception
    {
        CmdLine.load(this, "--testGroupList('1string')('2string')('-s3')");
        Assert.assertEquals("group1.string", "1string", testGroupList.get(0).testString);
        Assert.assertEquals("group2.string", "2string", testGroupList.get(1).testString);
        Assert.assertEquals("group3.string", "-s3", testGroupList.get(2).testString);
    }

    /**
     * <p>
     * variableGroupStringAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableGroupStringAssignment() throws Exception
    {
        CmdLine.load(this, "--testGroup('a group string')");
        Assert.assertEquals("group.string", "a group string", testGroup.testString);
    }

    /**
     * <p>
     * variableGroupStringAssignmentToObjectList.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableGroupStringAssignmentToObjectList() throws Exception
    {
        CmdLine.load(this, "--testObjectGroup('a group string')");
        Assert.assertNotNull("testObjectGroup should not be null", testObjectGroup);
        Assert.assertEquals("group.string", "a group string", ((MyGroup) testObjectGroup).testString);
    }

    /**
     * <p>
     * variableIntArrayAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableIntArrayAssignment() throws Exception
    {
        CmdLine.load(this, "--testIntArray 2 1");
        Assert.assertEquals("testIntArray 0", 2, testIntArray[0]);
        Assert.assertEquals("testIntArray 1", 1, testIntArray[1]);
    }

    /**
     * <p>
     * variableintAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableintAssignment() throws Exception
    {
        CmdLine.load(this, "--testInt 123");
        Assert.assertEquals("testInt", 123, testInt);
    }

    /**
     * <p>
     * variableIntegerArrayAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableIntegerArrayAssignment() throws Exception
    {
        CmdLine.load(this, "--testIntegerArray 2 1");
        Assert.assertEquals("testIntegerArray 0", 2, testIntegerArray[0].intValue());
        Assert.assertEquals("testIntegerArray 1", 1, testIntegerArray[1].intValue());
    }

    /**
     * <p>
     * variableIntegerAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableIntegerAssignment() throws Exception
    {
        CmdLine.load(this, "--testInteger 123");
        Assert.assertEquals("testInteger", 123, testInteger.intValue());
    }

    /**
     * <p>
     * variableStringArrayAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableStringArrayAssignment() throws Exception
    {
        CmdLine.load(this, "--testStringArray 'this is a test string' anotheronetoo");
        Assert.assertEquals("testStringArray 0", "this is a test string", testStringArray[0]);
        Assert.assertEquals("testStringArray 1", "anotheronetoo", testStringArray[1]);
    }

    /**
     * <p>
     * variableStringAssignment.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void variableStringAssignment() throws Exception
    {
        CmdLine.load(this, "--testString 'this is a test string'");
        Assert.assertEquals("testString", "this is a test string", testString);
    }
}
