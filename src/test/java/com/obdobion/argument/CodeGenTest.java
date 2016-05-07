package com.obdobion.argument;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.codegen.CodeGeneratorFactory;
import com.obdobion.argument.codegen.CodeGeneratorType;
import com.obdobion.argument.codegen.GeneratedElement;
import com.obdobion.argument.codegen.ICodeGenerator;

/**
 * @author Chris DeGreef
 * 
 */
public class CodeGenTest
{

    public CodeGenTest()
    {
    }

    @Test
    public void date () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();

        arguments.add(new DateCLA('v', "v1"));

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("date");
        {
            String expectedResults = "--type date -k v v1";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void groupVariablesAsArgs () throws Exception
    {
        ICmdLineArg<?> arg = null;
        CmdLineCLA group = null;
        CmdLineCLA group2 = null;
        List<ICmdLineArg<?>> arguments = new ArrayList<>();

        arguments.add(arg = new BooleanCLA('a'));
        arg.setVariable("myBool");

        arguments.add(arg = new StringCLA('s'));
        arg.setVariable("myString");
        arg.setMultiple(2);
        arg.setDefaultValue("abc");
        arg.setDefaultValue("def");
        arg.setListCriteria(new String[]
        {
            "def",
            "abc",
            "xyz"
        });

        arguments.add(group = new CmdLineCLA('g'));
        group.templateCmdLine = new CmdLine("G");
        group.setVariable("myGroup");
        {
            group.templateCmdLine.add(arg = new ByteCLA('b'));
            arg.setVariable("myByteInAGroup");
            arg.setRequired(true);
            arg.setCaseSensitive(true);

            group.templateCmdLine.add(group2 = new CmdLineCLA('z'));
            group2.templateCmdLine = new CmdLine("Z");
            group2.setVariable("myGroup2");
            group2.setMultiple(4);
            {
                group2.templateCmdLine.add(arg = new ByteCLA('y'));
                arg.setVariable("myByteInAGroup2");
            }
        }

        arguments.add(arg = new PatternCLA('j'));
        arg.setVariable("myPattern");

        arguments.add(arg = new BooleanCLA('?', "help"));

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15HardWire);
        gen.setArguments(arguments);
        gen.generateElements("GeneratedTest");
    }

    @Test
    public void groupVariablesAsString () throws Exception
    {
        ICmdLineArg<?> arg = null;
        CmdLineCLA group = null;
        CmdLineCLA group2 = null;
        List<ICmdLineArg<?>> arguments = new ArrayList<>();

        arguments.add(arg = new BooleanCLA('a'));
        arg.setVariable("myBool");

        arguments.add(group = new CmdLineCLA('g'));
        group.templateCmdLine = new CmdLine("G");
        group.setVariable("myGroup");
        {
            group.templateCmdLine.add(arg = new ByteCLA('b'));
            arg.setVariable("myByteInAGroup");

            group.templateCmdLine.add(group2 = new CmdLineCLA('z'));
            group2.templateCmdLine = new CmdLine("Z");
            group2.setVariable("myGroup2");
            group2.setMultiple(4);
            {
                group2.templateCmdLine.add(arg = new ByteCLA('y'));
                arg.setVariable("myByteInAGroup2");
            }
        }

        arguments.add(arg = new PatternCLA('j'));
        arg.setVariable("myPattern");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("GeneratedTest");
        {
            String expectedResults = "--type boolean -k a -v myBool";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type begin -k g -v myGroup";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type byte -k b -v myByteInAGroup";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type begin -k z -v myGroup2 -m 4";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type byte -k y -v myByteInAGroup2";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type end -k z -v myGroup2 -m 4";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type end -k g -v myGroup";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type pattern -k j -v myPattern";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }

    }

    @Test
    public void string () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();

        arguments.add(new StringCLA('v', "variable"));

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("string");
        {
            String expectedResults = "--type string -k v variable";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringCase () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v', "variable"));
        arg.setCaseSensitive(true);

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringCase");
        {
            String expectedResults = "--type string -k v variable -c";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringDefaultCase () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v', "variable"));
        /*
         * case sensitive must be set before default value otherwise it is
         * treated as lower case.
         */
        arg.setCaseSensitive(true);
        arg.setDefaultValue("IfNotSpecified");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringDefaultCase");
        {
            String expectedResults = "--type string -k v variable -c -d 'IfNotSpecified'";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringDefaultNotCase () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v', "variable"));
        arg.setDefaultValue("IfNotSpecified");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringDefaultNotCase");
        {
            String expectedResults = "--type string -k v variable -d 'ifnotspecified'";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringEnum () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setEnumCriteriaAllowError("my.enum.package.MyEnumClass");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringEnum");
        {
            String expectedResults = "--type string -k v --enumlist my.enum.package.MyEnumClass";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringFormat () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setFormat(".+");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringFormat");
        {
            String expectedResults = "--type string -k v -f '.+'";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringInstance () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setInstanceClass("my.instance.package.MyInstanceClass");
        arg.setFactoryMethodName("myFactoryMethod");
        arg.setFactoryArgName("myFactoryArgName");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringInstance");
        {
            String expectedResults = "--type string -k v --class my.instance.package.MyInstanceClass --factoryMethod myFactoryMethod --factoryArgName myFactoryArgName";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringListCase () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setCaseSensitive(true);
        arg.setListCriteria(new String[]
        {
            "abc",
            "X",
            "Y",
            "Z"
        });

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringListCase");
        {
            String expectedResults = "--type string -k v -c --list 'abc' 'X' 'Y' 'Z'";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringListNoCase () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setListCriteria(new String[]
        {
            "abc",
            "X",
            "Y",
            "Z"
        });

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringListNoCase");
        {
            String expectedResults = "--type string -k v --list 'abc' 'x' 'y' 'z'";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringMultiple1 () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setMultiple(true);
        arg.setMultiple(1);

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringMultiple1");
        {
            String expectedResults = "--type string -k v -m 1";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringMultiple2 () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setMultiple(true);
        arg.setMultiple(1, 5);

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringMultiple2");
        {
            String expectedResults = "--type string -k v -m 1 5";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringRangeCase () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setCaseSensitive(true);
        arg.setRangeCriteria("abc", "Z");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringRangeCase");
        {
            String expectedResults = "--type string -k v -c --range 'abc' 'Z'";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringRangeNoCase () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setRangeCriteria("abc", "Z");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringRangeNoCase");
        {
            String expectedResults = "--type string -k v --range 'abc' 'z'";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringRegex () throws Exception
    {
        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setCaseSensitive(true);
        arg.setRegxCriteria("[a-cZ]+");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringRegex");
        {
            String expectedResults = "--type string -k v -c --matches '[a-cZ]+'";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringRequired () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v', "variable"));
        arg.setRequired(true);

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringRequired");
        {
            String expectedResults = "--type string -k v variable -r";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void stringVariable () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();
        ICmdLineArg<?> arg = null;

        arguments.add(arg = new StringCLA('v'));
        arg.setVariable("myVariable");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("stringVariable");
        {
            String expectedResults = "--type string -k v -v myVariable";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void twoStrings () throws Exception
    {

        List<ICmdLineArg<?>> arguments = new ArrayList<>();

        arguments.add(new StringCLA('a', "s1"));
        arguments.add(new StringCLA('b', "s2"));

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("twoStrings");
        {
            String expectedResults = "--type string -k a s1";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type string -k b s2";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }

    @Test
    public void variables () throws Exception
    {
        ICmdLineArg<?> arg = null;
        List<ICmdLineArg<?>> arguments = new ArrayList<>();

        arguments.add(arg = new BooleanCLA('a'));
        arg.setVariable("myBool");
        arguments.add(arg = new ByteCLA('b'));
        arg.setVariable("myByte");
        arguments.add(arg = new DateCLA('c'));
        arg.setVariable("myDate");
        arguments.add(arg = new DoubleCLA('d'));
        arg.setVariable("myDouble");
        arguments.add(arg = new EnumCLA('e'));
        arg.setEnumCriteriaAllowError("com.test.MyEnumClass");
        arg.setVariable("myEnum");
        arguments.add(arg = new FileCLA('f'));
        arg.setVariable("myFile");
        arguments.add(arg = new FloatCLA('g'));
        arg.setVariable("myFloat");
        arguments.add(arg = new IntegerCLA('h'));
        arg.setVariable("myInteger");
        arguments.add(arg = new LongCLA('i'));
        arg.setVariable("myLong");
        arguments.add(arg = new PatternCLA('j'));
        arg.setVariable("myPattern");
        arguments.add(arg = new StringCLA('k'));
        arg.setVariable("myString");
        arguments.add(arg = new StringCLA('k'));
        arg.setInstanceClass("my.instance.classes.SpecialClass");
        arg.setVariable("mySpecialVariable");

        ICodeGenerator gen = CodeGeneratorFactory.create(CodeGeneratorType.Java15String);
        gen.setArguments(arguments);
        List<GeneratedElement> elements = gen.generateElements("variableBooleans");

        {
            String expectedResults = "--type boolean -k a -v myBool";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type byte -k b -v myByte";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type date -k c -v myDate";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type double -k d -v myDouble";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type enum -k e --enumlist com.test.MyEnumClass -v myEnum";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type file -k f -v myFile";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type float -k g -v myFloat";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type integer -k h -v myInteger";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type long -k i -v myLong";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type pattern -k j -v myPattern";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type string -k k -v myString";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
        {
            String expectedResults = "--type string -k k --class my.instance.classes.SpecialClass -v mySpecialVariable";
            Assert.assertTrue(expectedResults, elements.get(0).getContents().toString().contains(expectedResults));
        }
    }
}
