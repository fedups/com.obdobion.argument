package com.obdobion.argument.example;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class SimpleBooleanSwitches {
    /**
     * This program shows how to accept simple boolean commands from the
     * command-line and display them on the console.
     * 
     * @param args
     */
    public static void main (final String[] args) {
        try {
            /*
             * Create a new instance of the command line processor.
             */
            final ICmdLine cmdline = new CmdLine();
            cmdline.compile("-t boolean -k a", "-t boolean -k b");
            /*
             * The user's command line is now parsed against the compiled
             * interpreter.
             */
            cmdline.parse(CommandLineParser.getInstance(cmdline.getCommandPrefix(), args));
            /*
             * Display the values for the boolean arguments. Booleans are by
             * default false. So if the are not specified they will be false,
             * merely specifying them causes them to be true.
             */
            System.out.println("-a = " + cmdline.arg("-a").getValue());
            System.out.println("-b = " + cmdline.arg("-b").getValue());

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
