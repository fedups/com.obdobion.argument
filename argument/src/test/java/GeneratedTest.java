import java.io.IOException;
import java.text.ParseException;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.CmdLineCLA;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.ICmdLineArg;
import com.obdobion.argument.input.CommandLineParser;
import com.obdobion.argument.input.IParserInput;

/**
 * @author Chris DeGreef
 * 
 */
public class GeneratedTest
{
    static final ICmdLine CommandLine;
    static
    {
        CommandLine = new CmdLine("GeneratedTest");
        try
        {
            ICmdLineArg<?> arg = null;
            CommandLine.add(arg = new com.obdobion.argument.BooleanCLA('a'));
            arg.setVariable("myBool");
            CommandLine.add(arg = new com.obdobion.argument.StringCLA('s'));
            arg.setDefaultValue("abc");
            arg.setDefaultValue("def");
            arg.setVariable("myString");
            arg.setMultiple(2);
            arg.setListCriteria(new String[]
            {
            "def", "abc", "xyz"
            });

            CmdLineCLA group0 = null;
            CommandLine.add(group0 = new com.obdobion.argument.CmdLineCLA('g'));
            {
                group0.setVariable("myGroup");
                group0.templateCmdLine = new CmdLine("");
                group0.templateCmdLine.add(arg = new com.obdobion.argument.ByteCLA('b'));
                arg.setRequired(true);
                arg.setCaseSensitive(true);
                arg.setVariable("myByteInAGroup");

                CmdLineCLA group1 = null;
                group0.templateCmdLine.add(group1 = new com.obdobion.argument.CmdLineCLA('z'));
                {
                    group1.setVariable("myGroup2");
                    group1.setMultiple(4);
                    group1.templateCmdLine = new CmdLine("");
                    group1.templateCmdLine.add(arg = new com.obdobion.argument.ByteCLA('y'));
                    arg.setVariable("myByteInAGroup2");
                }
            }
            CommandLine.add(arg = new com.obdobion.argument.PatternCLA('j'));
            arg.setVariable("myPattern");
            CommandLine.add(arg = new com.obdobion.argument.BooleanCLA('?', "help"));
        } catch (Exception e)
        {
            System.err.println("Static compilation of GeneratedTest - " + e.getMessage());
            Runtime.getRuntime().exit(1);
        }
    }

    public static class CLAGZConfiguration
    {
        public byte myByteInAGroup2;
    }

    public static class CLAGConfiguration
    {
        public byte                 myByteInAGroup;
        public CLAGZConfiguration[] myGroup2;
    }

    public static class CLAConfiguration
    {
        public boolean                 myBool;
        public String[]                myString;
        public CLAGConfiguration       myGroup;
        public java.util.regex.Pattern myPattern;
    }

    /**
     * Create an instance of the application and run it with the configuration.
     * 
     * @param args are supplied by the user on the command console.
     */
    public static void main (final String[] args)
    {
        try
        {
            new GeneratedTest().run(createConfiguration(args));

        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    private static CLAConfiguration createConfiguration (final String[] args)
            throws ParseException, IOException
    {
        /*
         * Wrap the command line parameters in a parser. This parser will be
         * used to read the command line.
         */
        IParserInput userInput = CommandLineParser.getInstance(CommandLine.getCommandPrefix(), args);
        /*
         * Create an instance of the configuration value object and parse the
         * user input into it. CommandLine will return the configuration object.
         */
        return (CLAConfiguration) CommandLine.parse(userInput, new CLAConfiguration());
    }

    /**
     * Application code inserted in this method.
     */
    private void run (CLAConfiguration configuration)
    {
        /*
         * Echo the command line to the command console. This is commented out
         * since it is not likely to be included in a finished application. But
         * it is extremely handy during initial development.
         */
        // StringBuilder parameterDump = new StringBuilder();
        // CommandLine.exportNamespace("", parameterDump);
        // System.out.print(parameterDump);
    }
}
