package com.obdobion.argument.codegen;

import java.util.ArrayList;
import java.util.List;

import com.obdobion.argument.CmdLineCLA;
import com.obdobion.argument.ICmdLineArg;

/**
 * @author Chris DeGreef
 * 
 */
abstract public class Java15Generator implements ICodeGenerator
{
    String               generatorName;
    List<ICmdLineArg<?>> arguments;

    public List<GeneratedElement> generateElements (String _generatorName)
    {
        this.generatorName = _generatorName;

        List<GeneratedElement> elements = new ArrayList<>();
        elements.add(genDefinitionAsString(_generatorName + "_Definition"));
        elements.addAll(genConfigurationAsString(_generatorName + "_Configuration"));
        elements.add(genMainAsString(_generatorName + "_Main"));

        replaceElementsWithSingleWorkingClassExample(_generatorName, elements);

        for (GeneratedElement element : elements)
            System.out.println(element.getContents().toString());

        return elements;
    }

    private void replaceElementsWithSingleWorkingClassExample (
            String configClassNamePrefix,
            List<GeneratedElement> generatedElements)
    {
        GeneratedElement fullExample = new GeneratedElement(configClassNamePrefix);
        int defIdx = 0;
        int mainIdx = generatedElements.size() - 1;
        int lastConfIdx = generatedElements.size() - 2;

        // fullExample.getContents().append("package your.package;\n\n");

        writeImports(fullExample);

        fullExample.getContents().append("public class " + configClassNamePrefix + "\n");
        fullExample.getContents().append("{\n");

        fullExample.getContents().append(generatedElements.get(defIdx).getContents().toString());
        fullExample.getContents().append("\n");

        for (int ic = 1; ic <= lastConfIdx; ic++)
        {
            GeneratedElement innerClass = generatedElements.get(ic);
            fullExample.getContents().append(innerClass.getContents().toString());
            fullExample.getContents().append("\n");
        }

        fullExample.getContents().append(generatedElements.get(mainIdx).getContents().toString());
        fullExample.getContents().append("\n}\n");

        generatedElements.clear();
        generatedElements.add(fullExample);
    }

    void writeImports (GeneratedElement fullExample)
    {
        fullExample.getContents().append("import java.io.IOException;\n");
        fullExample.getContents().append("import java.text.ParseException;\n");
    }

    /**
     * @param elementName
     * @return
     */
    private List<GeneratedElement> genConfigurationAsString (String elementName)
    {
        List<GeneratedElement> elements = new ArrayList<>();
        writeConfigurationClasses(elements, "CLA", arguments);
        return elements;
    }

    @SuppressWarnings("null")
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
                else if (arg.getInstanceClass() != null)
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

    private GeneratedElement genMainAsString (String elementName)
    {
        GeneratedElement element = new GeneratedElement(elementName);

        element.getContents().append("    /**\n");
        element.getContents().append(
                "     * Create an instance of the application and run it with the configuration.\n");
        element.getContents().append("     * \n");
        element.getContents().append("     * @param args are supplied by the user on the command console.\n");
        element.getContents().append("     */\n");
        element.getContents().append("    public static void main (final String[] args)\n");
        element.getContents().append("    {\n");
        element.getContents().append("        try\n");
        element.getContents().append("        {\n");
        element.getContents().append("            new " + generatorName + "().run(createConfiguration(args));\n\n");

        element.getContents().append("       } catch (final Exception e)\n");
        element.getContents().append("       {\n");
        element.getContents().append("           e.printStackTrace();\n");
        element.getContents().append("       }\n");
        element.getContents().append("    }\n\n");

        element.getContents().append("    private static CLAConfiguration createConfiguration (final String[] args)\n");
        element.getContents().append("        throws ParseException, IOException\n");
        element.getContents().append("    {\n");
        element.getContents().append("        /*\n");
        element.getContents().append("         * Wrap the command line parameters in a parser. This parser will be\n");
        element.getContents().append("         * used to read the command line.\n");
        element.getContents().append("         */\n");
        element
                .getContents()
                .append(
                        "        IParserInput userInput = CommandLineParser.getInstance(CommandLine.getCommandPrefix(), args);\n");

        element.getContents().append("        /*\n");
        element.getContents().append("         * Create an instance of the configuration value object and parse the\n");
        element.getContents().append(
                "         * user input into it. CommandLine will return the configuration object.\n");
        element.getContents().append("         */\n");
        element.getContents().append(
                "        return (CLAConfiguration) CommandLine.parse(userInput, new CLAConfiguration());\n");
        element.getContents().append("    }\n\n");

        element.getContents().append("    /**\n");
        element.getContents().append("     * Application code inserted in this method.\n");
        element.getContents().append("     */\n");
        element.getContents().append("    private void run (CLAConfiguration configuration)\n");
        element.getContents().append("    {\n");
        element.getContents()
                .append("        /* Echo the command line to the command console. This is commented out\n");
        element.getContents().append(
                "         * since it is not likely to be included in a finished application. But\n");
        element.getContents().append("         * it is extremely handy during initial development.\n");
        element.getContents().append("         */\n");
        element.getContents().append("        // StringBuilder parameterDump = new StringBuilder();\n");
        element.getContents().append("        // CommandLine.exportNamespace(\"\", parameterDump);\n");
        element.getContents().append("        // System.out.print(parameterDump);\n");
        element.getContents().append("    }");

        return element;
    }

    private GeneratedElement genDefinitionAsString (String elementName)
    {
        GeneratedElement element = new GeneratedElement(elementName);

        element.getContents().append("    static final ICmdLine CommandLine;\n");
        element.getContents().append("    static\n");
        element.getContents().append("    {\n");
        element.getContents().append("        CommandLine = new CmdLine(\"" + generatorName + "\");\n");
        element.getContents().append("        try\n");
        element.getContents().append("        {\n");

        writeDefinitionStrings(element, arguments, 0, "arg");

        element.getContents().append("        } catch (Exception e)\n");
        element.getContents().append("        {\n");
        element.getContents().append(
                "            System.err.println(\"Static compilation of " + generatorName
                        + " - \" + e.getMessage());\n");
        element.getContents().append("            Runtime.getRuntime().exit(1);\n");
        element.getContents().append("        }\n");
        element.getContents().append("    }\n");

        return element;
    }

    abstract void writeDefinitionStrings (
            GeneratedElement element,
            List<ICmdLineArg<?>> localArguments,
            int recursionLevel, String prefix);

    public void setArguments (List<ICmdLineArg<?>> _arguments)
    {
        this.arguments = _arguments;
    }
}
