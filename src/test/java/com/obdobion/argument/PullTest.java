package com.obdobion.argument;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

/**
 * <p>
 * PullTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class PullTest
{
    static final public class InnerParm
    {
        @Arg
        private String innerParmString;
    }

    @Arg
    private String          updatedStr;
    @Arg
    private Integer         updatedInteger;
    @Arg
    private int             updatedInt;
    @Arg
    private String[]        updatedStrArray;
    @Arg
    private List<String>    updatedStrList;
    @Arg
    private InnerParm       innerParm;
    @Arg
    private InnerParm[]     innerParmArray;
    @Arg
    private List<InnerParm> innerParmList;

    /**
     * <p>
     * Constructor for PullTest.
     * </p>
     *
     * @since 4.3.1
     */
    public PullTest()
    {}

    /**
     * <p>
     * updateArray.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void updateArray() throws Exception
    {
        final ICmdLine cl = new CmdLine();
        cl.parse(this, "--updatedStrArray aaa, bbb");
        Assert.assertEquals("aaa", updatedStrArray[0]);
        Assert.assertEquals("bbb", updatedStrArray[1]);

        updatedStrArray = new String[] { "xxx", "yyy", "zzz" };

        cl.pull(this);
        Assert.assertEquals("xxx", cl.arg("--updatedStrArray").getValue(0));
        Assert.assertEquals("yyy", cl.arg("--updatedStrArray").getValue(1));
        Assert.assertEquals("zzz", cl.arg("--updatedStrArray").getValue(2));
    }

    /**
     * <p>
     * updateInnerParm.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void updateInnerParm() throws Exception
    {
        final ICmdLine cl = new CmdLine();
        cl.parse(this, "--innerParm(--innerParmString ccc)");
        Assert.assertEquals("ccc", innerParm.innerParmString);

        innerParm = new InnerParm();
        innerParm.innerParmString = "ddd";

        cl.pull(this);
        Assert.assertEquals("ddd", ((CmdLine) cl.arg("--innerParm").getValue()).arg("--innerParmString").getValue());
    }

    /**
     * <p>
     * updateInnerParmArray.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void updateInnerParmArray() throws Exception
    {
        final ICmdLine cl = new CmdLine();
        cl.parse(this, "--innerParmArray(--innerParmString ccc)(--innerParmString ddd)");
        Assert.assertEquals("ccc", innerParmArray[0].innerParmString);
        Assert.assertEquals("ddd", innerParmArray[1].innerParmString);

        innerParmArray = new InnerParm[3];
        innerParmArray[0] = new InnerParm();
        innerParmArray[0].innerParmString = "xxx";
        innerParmArray[1] = new InnerParm();
        innerParmArray[1].innerParmString = "yyy";
        innerParmArray[2] = new InnerParm();
        innerParmArray[2].innerParmString = "zzz";

        cl.pull(this);
        Assert.assertEquals("xxx",
                ((CmdLine) cl.arg("--innerParmArray").getValue(0)).arg("--innerParmString").getValue());
        Assert.assertEquals("yyy",
                ((CmdLine) cl.arg("--innerParmArray").getValue(1)).arg("--innerParmString").getValue());
        Assert.assertEquals("zzz",
                ((CmdLine) cl.arg("--innerParmArray").getValue(2)).arg("--innerParmString").getValue());
    }

    /**
     * <p>
     * updateInnerParmList.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void updateInnerParmList() throws Exception
    {
        final ICmdLine cl = new CmdLine();
        cl.parse(this, "--innerParmList(--innerParmString ccc)(--innerParmString ddd)");
        Assert.assertEquals("ccc", innerParmList.get(0).innerParmString);
        Assert.assertEquals("ddd", innerParmList.get(1).innerParmString);

        innerParmList.clear();
        InnerParm ip = null;
        innerParmList.add(ip = new InnerParm());
        ip.innerParmString = "xxx";
        innerParmList.add(ip = new InnerParm());
        ip.innerParmString = "yyy";
        innerParmList.add(ip = new InnerParm());
        ip.innerParmString = "zzz";

        cl.pull(this);
        Assert.assertEquals("xxx",
                ((CmdLine) cl.arg("--innerParmList").getValue(0)).arg("--innerParmString").getValue());
        Assert.assertEquals("yyy",
                ((CmdLine) cl.arg("--innerParmList").getValue(1)).arg("--innerParmString").getValue());
        Assert.assertEquals("zzz",
                ((CmdLine) cl.arg("--innerParmList").getValue(2)).arg("--innerParmString").getValue());
    }

    /**
     * <p>
     * updateList.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void updateList() throws Exception
    {
        final ICmdLine cl = new CmdLine();
        cl.parse(this, "--updatedStrList aaa, bbb");
        Assert.assertEquals("aaa", updatedStrList.get(0));
        Assert.assertEquals("bbb", updatedStrList.get(1));

        updatedStrList = new ArrayList<>();
        updatedStrList.add("xxx");
        updatedStrList.add("yyy");
        updatedStrList.add("zzz");

        cl.pull(this);
        Assert.assertEquals("xxx", cl.arg("--updatedStrList").getValue(0));
        Assert.assertEquals("yyy", cl.arg("--updatedStrList").getValue(1));
        Assert.assertEquals("zzz", cl.arg("--updatedStrList").getValue(2));
    }

    /**
     * <p>
     * updateSimple.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void updateSimple() throws Exception
    {
        final ICmdLine cl = new CmdLine();
        cl.parse(this,
                "--updatedStr was",
                "--updatedInteger 1",
                "--updatedInt 2");
        Assert.assertEquals("was", updatedStr);
        Assert.assertEquals(1, updatedInteger.intValue());
        Assert.assertEquals(2, updatedInt);

        updatedStr = "is";
        updatedInteger = 2;
        updatedInt = 3;

        cl.pull(this);
        Assert.assertEquals("is", cl.arg("--updatedStr").getValue());
        Assert.assertEquals(2, cl.arg("--updatedInteger").getValue());
        Assert.assertEquals(3, cl.arg("--updatedInt").getValue());
    }
}
