package com.obdobion.argument.codegen;

import java.util.ArrayList;
import java.util.List;

import com.obdobion.argument.BooleanCLA;
import com.obdobion.argument.CmdLineCLA;
import com.obdobion.argument.ICmdLineArg;

/**
 * @author Chris DeGreef
 * 
 */
public class Java15ImplementationCodeGenerator implements ICodeGenerator
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
    String               generatorName;

    List<ICmdLineArg<?>> arguments;

    /**
     * @param elementName
     * @return
     */
    private List<GeneratedElement> genConfigurationAsString (String elementName)
    {
        List<GeneratedElement> elements = new ArrayList<GeneratedElement>();
        writeConfigurationClasses(elements, "CLA", arguments);
        for (GeneratedElement element : elements)
            System.out.println(element.getContents().toString());
        return elements;
    }

    private GeneratedElement genDefinitionAsString (String elementName)
    {
        GeneratedElement element = new GeneratedElement(elementName);
        element.getContents().append("final static String[] CLADefinition = new String[] {\n");
        writeDefinitionStrings(element, arguments);
        element.getContents().append("};");
        System.out.println(element.getContents().toString());
        return element;
    }

    public List<GeneratedElement> generateElements (String _generatorName)
    {
        this.generatorName = _generatorName;

        List<GeneratedElement> elements = new ArrayList<GeneratedElement>();
        elements.add(genDefinitionAsString(_generatorName + "_Definition"));
        elements.addAll(genConfigurationAsString(_generatorName + "_Configuration"));
        elements.add(genMainAsString(_generatorName + "_Main"));
        return elements;
    }

    private GeneratedElement genMainAsString (String elementName)
    {
        GeneratedElement element = new GeneratedElement(elementName);

        element.getContents().append("public static void main (final String[] args) {\n");
        element.getContents().append("    try {\n");
        element.getContents().append("        final ICmdLine cmdline = new CmdLine(\"" + generatorName + "\");\n");
        element.getContents().append("        cmdline.compile(CLADefinition);\n");

        element.getContents().append("        CLAConfiguration configuration = (CLAConfiguration) cmdline.parse(\n");
        element.getContents().append(
                "                CommandLineParser.getInstance(cmdline.getCommandPrefix(), args),\n");
        element.getContents().append("                new CLAConfiguration());\n\n");

        element.getContents().append("       StringBuilder parameterDump = new StringBuilder();\n");
        element.getContents().append("       cmdline.exportNamespace(\"\", parameterDump);\n");
        element.getContents().append("       System.out.print(parameterDump);\n\n");

        element.getContents().append("       GeneratedTest myApp = new GeneratedTest();\n");
        element.getContents().append("       myApp.myApplicationCode(configuration);\n\n");

        element.getContents().append("    } catch (final Exception e) {\n");
        element.getContents().append("        e.printStackTrace();\n");
        element.getContents().append("    }\n");
        element.getContents().append("}\n\n");
        

        element.getContents().append("private void myApplicationCode (CLAConfiguration configuration)\n");
        element.getContents().append("{\n");
        element.getContents().append("    /*\n");
        element.getContents().append("     * Application code inserted here.\n");
        element.getContents().append("     */\n");
        element.getContents().append("}");

        System.out.println(element.getContents().toString());

        return element;
    }

    public void setArguments (List<ICmdLineArg<?>> _arguments)
    {
        this.arguments = _arguments;
    }

    private void writeConfigurationClasses (
            List<GeneratedElement> elements,
            String configClassNamePrefix,
            List<ICmdLineArg<?>> localArguments)
    {
        GeneratedElement element = null;
        boolean firstTime = true;
        for (ICmdLineArg<?> arg : localArguments)
        {
            if (arg.getVariable() != null)
            {
                if (firstTime)
                {
                    element = new GeneratedElement(configClassNamePrefix);
                    element.getContents().append("public static class " + configClassNamePrefix + "Configuration {");
                }

                String groupName = null;
                if (arg instanceof CmdLineCLA)
                {
                    groupName = configClassNamePrefix + ((CmdLineCLA) arg).templateCmdLine.getName();
                    writeConfigurationClasses(elements, groupName, ((CmdLineCLA) arg).templateCmdLine.allArgs());
                }

                element.getContents().append("\n    public ");
                if (groupName != null)
                    element.getContents().append(groupName + "Configuration");
                else
                if (arg.getInstanceClass() != null)
                    element.getContents().append(arg.getInstanceClass());
                else
                    element.getContents().append(arg.defaultInstanceClass());
                if (arg.isMultiple())
                    element.getContents().append("[]");
                element.getContents().append(" ");
                element.getContents().append(arg.getVariable());
                element.getContents().append(";");

                firstTime = false;

            }
        }
        if (!firstTime)
        {
            element.getContents().append("\n}");
            elements.add(element);
        }
    }

    private void writeDefinitionStrings (GeneratedElement element, List<ICmdLineArg<?>> localArguments)
    {
        boolean firstTime = true;
        for (ICmdLineArg<?> arg : localArguments)
        {
            if (!firstTime)
                element.getContents().append(",\n");

            if (arg instanceof CmdLineCLA)
            {
                element.getContents().append("    \"--type begin ");
                asDefinedText(arg, element.getContents());
                element.getContents().append("\",\n");

                writeDefinitionStrings(element, ((CmdLineCLA) arg).templateCmdLine.allArgs());
                element.getContents().append(",\n");

                element.getContents().append("    \"--type end ");
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
    }
}
