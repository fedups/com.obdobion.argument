package com.obdobion.argument;

import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class GeneratedTest
{
    public static class CLAConfiguration
    {
        public boolean                 myBool;
        public CLAGConfiguration       myGroup;
        public java.util.regex.Pattern myPattern;
    }

    public static class CLAGConfiguration
    {
        public byte                 myByteInAGroup;
        public CLAGZConfiguration[] myGroup2;
    }

    public static class CLAGZConfiguration
    {
        public byte myByteInAGroup2;
    }

    final static String[] CLADefinition = new String[]
                                        {
        "--type boolean -k a -v myBool",
        "--type begin  -k g -v myGroup",
        "--type byte -k b -v myByteInAGroup",
        "--type begin  -k z -v myGroup2 -m 1 1",
        "--type byte -k y -v myByteInAGroup2",
        "--type end  -k z -v myGroup2 -m 1 1",
        "--type end  -k g -v myGroup",
        "--type pattern -k j -v myPattern"
                                        };

    public static void main (final String[] args)
    {
        try
        {
            final ICmdLine cmdline = new CmdLine("variableBooleans");
            cmdline.compile(CLADefinition);
            CLAConfiguration configuration = (CLAConfiguration) cmdline.parse(
                CommandLineParser.getInstance(cmdline.getCommandPrefix(), args),
                new CLAConfiguration());

            StringBuilder parameterDump = new StringBuilder();
            cmdline.exportNamespace("", parameterDump);
            System.out.print(parameterDump);

            GeneratedTest myApp = new GeneratedTest();
            myApp.myApplicationCode(configuration);

        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param configuration
     */
    private void myApplicationCode (CLAConfiguration configuration)
    {
        /*
         * Application code inserted here.
         */
    }
}
