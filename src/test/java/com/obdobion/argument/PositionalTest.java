package com.obdobion.argument;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>
 * PositionalTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class PositionalTest
{

    static public class Master01
    {
        @Arg(shortName = 'a', positional = true)
        boolean  item1;

        @Arg(shortName = 'd')
        Master02 master02;
    }

    static public class Master02
    {
        @Arg(shortName = 'b', positional = true)
        boolean item2;
    }

    static public class Master03
    {
        @Arg(shortName = 'a')
        boolean  item1;

        @Arg(shortName = 'd', positional = true)
        Master04 master04;
    }

    static public class Master04
    {
        @Arg(shortName = 'b')
        boolean item2;
    }

    static public class Master05
    {
        @Arg(shortName = 'a', positional = true)
        boolean  item1;

        @Arg(shortName = 'd', positional = true)
        Master06 master06;
    }

    static public class Master06
    {
        @Arg(shortName = 'b', positional = true)
        boolean item2;
    }

    static public class Master07
    {
        @Arg(positional = true)
        String[] str;
    }

    static public class Master08
    {
        @Arg(positional = true)
        String[] str1;

        @Arg(positional = true)
        String[] str2;
    }

    static public class Master09
    {
        @Arg(positional = true)
        String[] str;
    }

    static public class Master10
    {
        @Arg(positional = true)
        String str;
    }

    static public class Master11
    {
        @Arg(positional = true)
        String str1;

        @Arg(positional = true)
        String str2;
    }

    @Arg(shortName = 'g')
    Master01   master01;

    @Arg(shortName = 'h')
    Master03   master03;

    @Arg(shortName = 'j')
    Master05   master05;

    @Arg(positional = true)
    Master07[] master07;

    /**
     * <p>
     * groupingGroupsPositionalBooleans.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void groupingGroupsPositionalBooleans() throws Exception
    {
        CmdLine.load(this, "-g( true -d(true) ) ");
        Assert.assertTrue(master01.item1);
        Assert.assertTrue(master01.master02.item2);
    }

    /**
     * <p>
     * groupingGroupsPositionalGroups.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void groupingGroupsPositionalGroups() throws Exception
    {
        CmdLine.load(this, "-h( -a (-b) ) ");
        Assert.assertTrue(master03.item1);
        Assert.assertTrue(master03.master04.item2);
    }

    /**
     * <p>
     * groupingGroupsPositionalGroupsAndBooleans.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void groupingGroupsPositionalGroupsAndBooleans() throws Exception
    {
        CmdLine.load(this, "-j( true (true) ) ");
        Assert.assertTrue(master05.item1);
        Assert.assertTrue(master05.master06.item2);
    }

    /**
     * <p>
     * groupMultiple.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void groupMultiple() throws Exception
    {
        CmdLine.load(this, "(input1 input3),(input2)");
        Assert.assertEquals("input1", master07[0].str[0]);
        Assert.assertEquals("input3", master07[0].str[1]);
        Assert.assertEquals("input2", master07[1].str[0]);
    }

    /**
     * <p>
     * multipleValuesWithMultiplePositionals.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void multipleValuesWithMultiplePositionals() throws Exception
    {
        try
        {
            CmdLine.load(new Master08(), "(input1 input3),(input2)");
            Assert.fail("expected a parse exception");
        } catch (final ParseException e)
        {
            Assert.assertEquals(
                    "a multi-value positional argument must be the only positional argument, found \"--str1\"' and \"--str2\"",
                    e.getMessage());
        }
    }

    /**
     * <p>
     * stringMultiple.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void stringMultiple() throws Exception
    {
        final Master09 target = new Master09();
        CmdLine.load(target, "input1,input2");
        Assert.assertEquals("input1", target.str[0]);
        Assert.assertEquals("input2", target.str[1]);
    }

    /**
     * <p>
     * testString.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void testString() throws Exception
    {
        final Master10 target = new Master10();
        CmdLine.load(target, "input1");
        Assert.assertEquals("input1", target.str);
    }

    /**
     * <p>
     * twoStrings.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void twoStrings() throws Exception
    {
        final Master11 target = new Master11();
        CmdLine.load(target, "input1,input2");
        Assert.assertEquals("input1", target.str1);
        Assert.assertEquals("input2", target.str2);
    }

    /**
     * <p>
     * twoStringsOnlyUsed1.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     * @since 4.3.1
     */
    @Test
    public void twoStringsOnlyUsed1() throws Exception
    {
        final Master11 target = new Master11();
        CmdLine.load(target, "input1");
        Assert.assertEquals("input1", target.str1);
        Assert.assertNull(target.str2);
    }
}
