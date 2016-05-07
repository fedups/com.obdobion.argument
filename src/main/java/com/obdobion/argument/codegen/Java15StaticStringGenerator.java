package com.obdobion.argument.codegen;

import java.util.List;

import com.obdobion.argument.BooleanCLA;
import com.obdobion.argument.CmdLineCLA;
import com.obdobion.argument.ICmdLineArg;

/**
 * @author Chris DeGreef
 * 
 */
public class Java15StaticStringGenerator extends Java15Generator
{

    static private void asDefinedText (ICmdLineArg<?> arg, StringBuilder sb)
    {
        sb.append(" ");
        sb.append("-k ");
        if (arg.getKeychar() != null && arg.getKeychar() != ' ')
            sb.append(arg.getKeychar());
        if (arg.getKeyword() != null && arg.getKeyword().trim().length() > 0)
        {
            if (arg.getKeychar() != null && arg.getKeychar() != ' ')
                sb.append(" ");
            sb.append(arg.getKeyword());
        }
        if (arg.isRequired())
            sb.append(" -r");
        if (arg.isCaseSensitive())
            sb.append(" -c");
        List<?> defaults = arg.getDefaultValues();
        if (defaults.size() > 0 && !(arg instanceof BooleanCLA))
        {
            sb.append(" -d");
            for (Object oneDefault : defaults)
            {
                sb.append(" '");
                sb.append(oneDefault.toString());
                sb.append("'");
            }
        }
        if (arg.getEnumClassName() != null)
        {
            sb.append(" --enumlist ");
            sb.append(arg.getEnumClassName());
        }
        if (arg.getInstanceClass() != null)
        {
            sb.append(" --class ");
            sb.append(arg.getInstanceClass());
        }
        if (arg.getFactoryMethodName() != null)
        {
            sb.append(" --factoryMethod ");
            sb.append(arg.getFactoryMethodName());
        }
        if (arg.getFactoryArgName() != null)
        {
            sb.append(" --factoryArgName ");
            sb.append(arg.getFactoryArgName());
        }
        if (arg.getVariable() != null)
        {
            sb.append(" -v ");
            sb.append(arg.getVariable());
        }
        if (arg.getFormat() != null)
        {
            sb.append(" -f '");
            sb.append(arg.getFormat());
            sb.append("'");
        }
        if (arg.isMultiple())
        {
            sb.append(" -m ");
            sb.append(arg.getMultipleMin());
            if (arg.getMultipleMax() != Integer.MAX_VALUE)
            {
                sb.append(" ");
                sb.append(arg.getMultipleMax());
            }
        }
        if (!(arg instanceof BooleanCLA) && arg.getCriteria() != null)
        {
            arg.getCriteria().asDefinitionText(sb);
        }
    }

    @Override
    void writeDefinitionStrings (
        GeneratedElement element,
        List<ICmdLineArg<?>> localArguments,
        int recursionLevel, String prefix)
    {
        boolean firstTime = true;

        if (recursionLevel == 0)
        {
            element.getContents().append("            CommandLine.compile(new String[]\n");
            element.getContents().append("            {\n");
        }

        for (ICmdLineArg<?> arg : localArguments)
        {
            if (!firstTime)
                element.getContents().append(",\n");

            if (arg instanceof CmdLineCLA)
            {
                element.getContents().append("    \"--type begin");
                asDefinedText(arg, element.getContents());
                element.getContents().append("\",\n");

                writeDefinitionStrings(
                    element,
                    ((CmdLineCLA) arg).templateCmdLine.allArgs(),
                    recursionLevel + 1,
                    prefix);
                element.getContents().append(",\n");

                element.getContents().append("    \"--type end");
                asDefinedText(arg, element.getContents());
                element.getContents().append("\"");
            } else
            {
                element.getContents().append("    \"--type ");
                arg.asDefinedType(element.getContents());
                asDefinedText(arg, element.getContents());
                element.getContents().append("\"");
            }

            firstTime = false;
        }
        if (recursionLevel == 0)
        {
            element.getContents().append("\n            });\n");
        }
    }

    @Override
    void writeImports (GeneratedElement fullExample)
    {
        fullExample.getContents().append("import com.obdobion.argument.CmdLine;\n");
        fullExample.getContents().append("import com.obdobion.argument.ICmdLine;\n");
        fullExample.getContents().append("import com.obdobion.argument.input.IParserInput;\n");
        fullExample.getContents().append("import com.obdobion.argument.input.CommandLineParser;\n\n");
    }
}
