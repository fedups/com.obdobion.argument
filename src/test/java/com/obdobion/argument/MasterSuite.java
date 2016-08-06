package com.obdobion.argument;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.obdobion.argument.annotation.AnnotateAllTypesTest;
import com.obdobion.argument.annotation.AnnotateDefaultsTest;
import com.obdobion.argument.annotation.AnnotateFactoryCreationTests;
import com.obdobion.argument.annotation.AnnotateFactoryCreationWithEnumArgTest;
import com.obdobion.argument.annotation.AnnotateInListCriteriaTest;
import com.obdobion.argument.annotation.AnnotateInstanceOverrideTest;
import com.obdobion.argument.annotation.AnnotateRangeCriteriaTest;
import com.obdobion.argument.annotation.AnnotateRegexCriteriaTest;
import com.obdobion.argument.annotation.AnnotateSimpleModifiersTest;
import com.obdobion.argument.annotation.AnnotateStandardClassesTest;
import com.obdobion.argument.annotation.AnnotateSubparserTest;

@RunWith(Suite.class)
@SuiteClasses({

        AnnotateAllTypesTest.class,
        AnnotateDefaultsTest.class,
        AnnotateSimpleModifiersTest.class,
        AnnotateSubparserTest.class,
        AnnotateStandardClassesTest.class,
        AnnotateFactoryCreationTests.class,
        AnnotateFactoryCreationWithEnumArgTest.class,
        AnnotateInstanceOverrideTest.class,
        AnnotateRangeCriteriaTest.class,
        AnnotateRegexCriteriaTest.class,
        AnnotateInListCriteriaTest.class,

        PullTest.class,

        ExportImportTest.class,

        AbbreviationTest.class,
        BooleanTest.class,
        BracketTest.class,
        ByteCLATest.class,
        CaseSensitiveTest.class,
        CmdLineTest.class,
        CriteriaTest.class,
        DashingTest.class,
        DateTest.class,
        CalendarTest.class,
        EnumTest.class,
        EquTest.class,
        ExceptionTest.class,
        IncludeTest.class,
        InstantiatorTest.class,
        ListTest.class,
        MultipleTest.class,
        NumbersTest.class,
        PositionalTest.class,
        QuotedLiteralsTest.class,
        RangeTest.class,
        RegexTest.class,
        RepeatParmTest.class,
        RequiredTest.class,
        VariableTest.class,
        WildFileTest.class,
        WildPathTest.class,
        WindowsTest.class
})
public class MasterSuite
{

}
