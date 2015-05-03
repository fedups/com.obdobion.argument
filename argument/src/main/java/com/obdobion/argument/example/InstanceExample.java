package com.obdobion.argument.example;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.StringCLA;
import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class InstanceExample {

    static private void displayArgValue (final StringCLA arg) {
        System.out.println("s = " + arg.getValue());
    }

    /**
     * This program shows how to get hold of an arugment instance and pass it as
     * a parameter.
     * 
     * @param args
     */
    public static void main (final String[] args) {
        try {
            /*
             * Create a new instance of the command line processor.
             */
            final ICmdLine cmdline = new CmdLine();
            cmdline.compile("-t String -k s");
            /*
             * The user's command line is now parsed against the compiled
             * interpreter.
             */
            cmdline.parse(CommandLineParser.getInstance(cmdline.getCommandPrefix(), args));
            /*
             * Grab the String argument and pass it to another method.
             */
            final StringCLA myStringArg = (StringCLA) cmdline.arg("-s");
            displayArgValue(myStringArg);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
