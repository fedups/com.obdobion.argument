package com.obdobion.argument.codegen;

import java.util.List;

import com.obdobion.argument.BooleanCLA;
import com.obdobion.argument.CmdLineCLA;
import com.obdobion.argument.ICmdLineArg;

/**
 * @author Chris DeGreef
 * 
 */
public class Java15HardWireGenerator extends Java15Generator
{
    static private void asSetter (String argVar, ICmdLineArg<?> arg, StringBuilder sb)
    {
        if (arg.isRequired())
            sb.append(argVar + ".setRequired(true);\n");
        if (arg.isCaseSensitive())
            sb.append(argVar + ".setCaseSensitive(true);\n");

        List<?> defaults = arg.getDefaultValues();
        if (defaults.size() > 0 && !(arg instanceof BooleanCLA))
        {
            for (Object oneDefault : defaults)
            {
                sb.append(argVar);
                sb.append(".setDefaultValue(\"");
                sb.append(oneDefault.toString());
                sb.append("\");\n");
            }
        }
        if (arg.getEnumClassName() != null)
        {
            sb.append(argVar);
            sb.append(".setEnumClassName(\"");
            sb.append(arg.getEnumClassName());
            sb.append("\");\n");
        }
        if (arg.getInstanceClass() != null)
        {
            sb.append(argVar);
            sb.append(".setInstanceClass(\"");
            sb.append(arg.getInstanceClass());
            sb.append("\");\n");
        }
        if (arg.getFactoryMethodName() != null)
        {
            sb.append(argVar);
            sb.append(".setFactoryMethodName(\"");
            sb.append(arg.getFactoryMethodName());
            sb.append("\");\n");
        }
        if (arg.getFactoryArgName() != null)
        {
            sb.append(argVar);
            sb.append(".setFactoryArgName(\"");
            sb.append(arg.getFactoryArgName());
            sb.append("\");\n");
        }
        if (arg.getVariable() != null)
        {
            sb.append(argVar);
            sb.append(".setVariable(\"");
            sb.append(arg.getVariable());
            sb.append("\");\n");
        }
        if (arg.getFormat() != null)
        {
            sb.append(argVar);
            sb.append(".setFormat(\"");
            sb.append(arg.getFormat());
            sb.append("\");\n");
        }
        if (arg.isMultiple())
        {
            sb.append(argVar);
            sb.append(".setMultiple(");
            sb.append(arg.getMultipleMin());
            if (arg.getMultipleMax() != Integer.MAX_VALUE)
            {
                sb.append(", ");
                sb.append(arg.getMultipleMax());
            }
            sb.append(");\n");
        }
        if (!(arg instanceof BooleanCLA) && arg.getCriteria() != null)
        {
            sb.append(argVar);
            arg.getCriteria().asSetter(sb);
            sb.append(";\n");
        }
    }

    static private String constructor (ICmdLineArg<?> arg)
    {
        if (arg.getKeychar() != ' ' && arg.getKeyword() != null)
            return (arg.getClass().getName() + "('" + arg.getKeychar() + "', \"" + arg.getKeyword() + "\")");
        if (arg.getKeychar() != ' ')
            return (arg.getClass().getName() + "('" + arg.getKeychar() + "')");
        if (arg.getKeyword() != null)
            return (arg.getClass().getName() + "(\"" + arg.getKeyword() + "\")");
        return (arg.getClass().getName() + "()");
    }

    @Override
    void writeDefinitionStrings (
        GeneratedElement element,
        List<ICmdLineArg<?>> localArguments,
        int recursionLevel, String prefix)
    {
        if (recursionLevel == 0)
        {
            element.getContents().append("            ICmdLineArg<?> arg = null;\n");
        }

        for (ICmdLineArg<?> arg : localArguments)
        {

            if (arg instanceof CmdLineCLA)
            {
                element.getContents().append("\n           CmdLineCLA group" + recursionLevel + " = null;\n");
                if (recursionLevel == 0)
                {
                    element.getContents().append("            CommandLine");
                } else
                {
                    element.getContents().append("            group" + (recursionLevel - 1) + ".templateCmdLine");
                }
                element.getContents().append(
                    ".add(group" + recursionLevel + " = new " + constructor(arg) + ");\n");
                element.getContents().append("           {\n");

                asSetter("group" + recursionLevel, arg, element.getContents());

                element.getContents().append(
                    "            group" + recursionLevel + ".templateCmdLine = new CmdLine(\"\");");

                writeDefinitionStrings(
                    element,
                    ((CmdLineCLA) arg).templateCmdLine.allArgs(),
                    recursionLevel + 1,
                    "group" + recursionLevel);

                element.getContents().append("           }\n");
            } else
            {
                if (recursionLevel == 0)
                {
                    element.getContents().append("            CommandLine");
                } else
                {
                    element.getContents().append("            group" + (recursionLevel - 1) + ".templateCmdLine");
                }
                element.getContents().append(".add(arg = new " + constructor(arg) + ");\n");
                asSetter("arg", arg, element.getContents());
            }
        }
    }

    @Override
    void writeImports (GeneratedElement fullExample)
    {
        super.writeImports(fullExample);

        fullExample.getContents().append("import java.util.ArrayList;\n");
        fullExample.getContents().append("import java.util.List;\n");

        fullExample.getContents().append("import com.obdobion.argument.CmdLine;\n");
        fullExample.getContents().append("import com.obdobion.argument.CmdLineCLA;\n");
        fullExample.getContents().append("import com.obdobion.argument.ICmdLine;\n");
        fullExample.getContents().append("import com.obdobion.argument.ICmdLineArg;\n");
        fullExample.getContents().append("import com.obdobion.argument.input.CommandLineParser;\n");
        fullExample.getContents().append("import com.obdobion.argument.input.IParserInput;\n");

    }
}
