package com.obdobion.argument;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.obdobion.argument.input.CommandLineParserTest;
import com.obdobion.argument.input.NamespaceParserTest;
import com.obdobion.argument.input.NamespaceTest;
import com.obdobion.argument.input.XmlParserTest;
import com.obdobion.argument.input.XmlTest;

/**
 * @author Chris DeGreef
 * 
 */
@RunWith(Suite.class)
@SuiteClasses(
{
        AbbreviationTest.class, BooleanTest.class, BracketTest.class, BusinessDateTest.class, ByteCLATest.class,
        CaseSensitiveTest.class, CmdLineTest.class, CodeGenTest.class, DashingTest.class, DateDayOfMonthTest.class,
        DateDayOfWeekTest.class, DateHourTest.class, DateMinuteTest.class, DateParserErrorTest.class, DateTest.class,
        DateWeekOfMonthTest.class, DateWeekOfYearTest.class, DateYearTest.class, EnumTest.class, EscapeTest.class,
        ExceptionTest.class, ExportTest.class, GroupTest.class, IncludeTest.class, InstantiatorTest.class,
        LinuxSortTest.class, ListTest.class, MultipleTest.class, NumbersTest.class, PositionalTest.class,
        QuotedLiteralsTest.class, RangeTest.class, RegexTest.class, RepeatParmTest.class, RequiredTest.class,
        SimpleTest.class, UsageTest.class, VariableTest.class, WindowsTest.class, WildFileTest.class,

        CommandLineParserTest.class, NamespaceParserTest.class, NamespaceTest.class, XmlParserTest.class,
        XmlTest.class
})
public class AllTests
{

}
