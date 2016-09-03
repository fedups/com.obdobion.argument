package com.obdobion.argument.help.test1;

import org.junit.Test;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.annotation.Arg;
import com.obdobion.argument.help.HelpHelp;

/**
 * <p>
 * AbbreviationTest class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.1.2
 */
public class HelpTest
{
    static public class Test1
    {
        @Arg(shortName = 'a', help = "This is developer supplied narrative.", defaultValues = "abc")
        String   item;

        @Arg(shortName = 'b', help = "This is developer supplied narrative.")
        String[] items;

        @Arg(shortName = 'c', help = "This is developer supplied narrative.")
        Test2    test2;

        @Arg(shortName = 'd', help = "This is developer supplied narrative.")
        Test2[]  test2multi;
    }

    static public class Test2
    {
        @Arg(positional = true, help = "This is developer supplied narrative.")
        String item;

        @Arg(shortName = 'b', help = "This is developer supplied narrative.")
        String items;
    }

    /**
     * <p>
     * testLevelHelp3.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     */
    @Test
    public void testLevelHelp() throws Exception
    {
        final HelpHelp help = new HelpHelp();
        help.startCapture();
        final ICmdLine cmdline = new CmdLine("The name of the Program",
                "A longer version of the top level documentation help");
        CmdLine.load(cmdline, new Test1(), "--help");
        help.stopCapture();
        help.compare("help3.txt", false);
    }

    /**
     * <p>
     * testLevelHelp1.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     */
    @Test
    public void testLevelHelp1() throws Exception
    {
        final HelpHelp help = new HelpHelp();
        help.startCapture();
        final ICmdLine cmdline = new CmdLine("The name of the Program",
                "A longer version of the top level documentation help");
        CmdLine.load(cmdline, new Test1(), "--help:1");
        help.stopCapture();
        help.compare("help1.txt", false);
    }

    /**
     * <p>
     * testLevelHelp2.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     */
    @Test
    public void testLevelHelp2() throws Exception
    {
        final HelpHelp help = new HelpHelp();
        help.startCapture();
        final ICmdLine cmdline = new CmdLine("The name of the Program",
                "A longer version of the top level documentation help");
        CmdLine.load(cmdline, new Test1(), "--help:2");
        help.stopCapture();
        help.compare("help2.txt", false);
    }

    /**
     * <p>
     * testQuestion.
     * </p>
     *
     * @throws java.lang.Exception
     *             if any.
     */
    @Test
    public void testQuestion() throws Exception
    {
        final HelpHelp help = new HelpHelp();
        help.startCapture();
        final ICmdLine cmdline = new CmdLine("The name of the Program",
                "A longer version of the top level documentation help");
        CmdLine.load(cmdline, new Test1(), "-?");
        help.stopCapture();
        help.compare("help1.txt", false);
    }
}
