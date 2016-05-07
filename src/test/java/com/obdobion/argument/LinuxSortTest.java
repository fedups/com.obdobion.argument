package com.obdobion.argument;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class LinuxSortTest
{

    CmdLine sorter;

    public LinuxSortTest()
    {
    }

    @Before
    public void createParser ()
    {
        sorter = new CmdLine("LinuxSortTest", "Write sorted concatenation of all FILE(s) to standard output.");

        try
        {
            sorter.compile(
                "-t boolean -k b 'ignore-leading-blanks' -h 'ignore leading blanks'",
                "-t boolean -k d 'dictionary-order' -h 'consider only blanks and alphanumeric characters'",
                "-t boolean -k f 'ignore-case' -h 'fold lower case to upper case characters'",
                "-t boolean -k g 'general-numeric-sort' -h 'compare according to general numerical value'",
                "-t boolean -k i 'ignore-nonprinting' -h 'consider only printable characters'",
                "-t boolean -k M 'month-sort' -h 'compare (unknown) < \"JAN\" < ... < \"DEC\"'",
                "-t boolean -k n 'numeric-sort' -h 'compare according to string numerical value'",
                "-t boolean -k r 'reverse' -h 'reverse the result of comparisons'",
                "-t boolean -k c 'check' -h 'check whether input is sorted; do not sort'",
                "-t begin   -k k 'key' -m1 -h 'start a key at POS1, end it at POS 2 (origin 1)\nPOS is F[.C][OPTS], where F is the field number and C the character position in the field. OPTS is one or more single-letter ordering options, which override global ordering options for that key. If no key is given, use the entire line as the key.'",
                "-t   string -k   'pos1' -r -p",
                "-t   string -k   'pos2' -p",
                "-t end     -k k 'key'",
                "-t boolean -k m 'merge' -h 'merge already sorted files; do not sort'",
                "-t file -k o 'output' -h 'write result to FILE instead of standard output'",
                "-t boolean -k s 'stable' -h 'stabilize sort by disabling last-resort comparison'",
                "-t integer -k S 'buffer-size' -h 'use SIZE for main memory buffer'",
                "-t string -k t 'field-separator' -h 'use SEP instead of non- to whitespace transition'",
                "-t string -k T 'temporary-directory' -m1 -h 'use DIR for temporaries, not $TMPDIR or /tmp multiple options specify multiple directories'",
                "-t boolean -k u 'unique' -h 'with -c: check for strict ordering\notherwise: output only the first of an equal run'",
                "-t boolean -k z 'zero-terminated' -h 'ends line with 0 byte, not newline'",
                "-t boolean -k   'version' -h 'output version information and exit'");

        } catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void keytest () throws Exception
    {
        sorter.parse(CommandLineParser.getInstance(sorter.getCommandPrefix(), "-k(1,4)(5,2)"), this);
        // Assert.assertEquals(TestEnum.KEY2, enum1);

        Assert.assertNotNull(sorter.arg("-b"));
    }

    @Test
    public void usage () throws Exception
    {
        final ByteArrayOutputStream sw = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sw));

        sorter.parse(CommandLineParser.getInstance(sorter.getCommandPrefix(), "--" + CmdLine.MaxHelpCommandName));
        sorter.parse(CommandLineParser.getInstance(sorter.getCommandPrefix(), "-" + CmdLine.MinHelpCommandName));

        System.err.println(sw.toString());
    }
}
