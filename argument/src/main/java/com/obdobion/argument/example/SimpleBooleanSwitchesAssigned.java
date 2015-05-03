package com.obdobion.argument.example;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class SimpleBooleanSwitchesAssigned {
    /**
     * This program shows how to accept simple boolean commands from the
     * command-line and display them on the console using the auto-assign
     * feature.
     * 
     * @param args
     */
    public static void main (final String[] args) {
        final SimpleBooleanSwitchesAssigned me = new SimpleBooleanSwitchesAssigned();
        me.run(args);

    }

    boolean aBool;
    boolean bBool;

    void run (final String[] args) {
        try {
            /*
             * Create a new instance of the command line processor.
             */
            final ICmdLine cmdline = new CmdLine();
            cmdline.compile("-t boolean -k a --variable aBool", "-t boolean -k b --variable bBool");
            /*
             * The user's command line is now parsed against the compiled
             * interpreter.
             */
            cmdline.parse(CommandLineParser.getInstance(cmdline.getCommandPrefix(), args), this);
            /*
             * Display the values for the boolean arguments. Booleans are by
             * default false. So if the are not specified they will be false,
             * merely specifying them causes them to be true.
             */
            System.out.println("-a = " + aBool);
            System.out.println("-b = " + bBool);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
