package com.obdobion.argument;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.obdobion.argument.input.CommandLineParserTest;
import com.obdobion.argument.input.NamespaceParserTest;
import com.obdobion.argument.input.NamespaceTest;
import com.obdobion.argument.input.XmlParserTest;
import com.obdobion.argument.input.XmlTest;

@RunWith(Suite.class)
@SuiteClasses({
        CommandLineParserTest.class,
        NamespaceParserTest.class,
        XmlParserTest.class,
        NamespaceTest.class,
        XmlTest.class,
        AbbreviationTest.class,
        BooleanTest.class,
        BracketTest.class,
        ByteCLATest.class,
        CaseSensitiveTest.class,
        CmdLineTest.class,
        CriteriaTest.class,
        DashingTest.class,
        DateTest.class,
        EnumTest.class,
        EquTest.class,
        EscapeTest.class,
        ExceptionTest.class,
        ExportTest.class,
        ExportWithVarianceTest.class,
        GroupTest.class,
        IncludeTest.class,
        InstantiatorTest.class,
        LinuxSortTest.class,
        ListTest.class,
        MultipleTest.class,
        NumbersTest.class,
        PositionalTest.class,
        QuotedLiteralsTest.class,
        RangeTest.class,
        RegexTest.class,
        RepeatParmTest.class,
        RequiredTest.class,
        SimpleTest.class,
        UsageTest.class,
        VariableTest.class,
        WildFileTest.class,
        WildPathTest.class,
        WindowsTest.class
})
public class MasterSuite
{

}
