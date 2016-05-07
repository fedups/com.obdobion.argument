package com.obdobion.argument.example;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.input.CommandLineParser;

/**
 * @author Chris DeGreef
 * 
 */
public class DefiningAParser
{

    static final private String FastSwitch    = "-tboolean -kfast";
    static final private String CheapSwitch   = "-tboolean -kcheap";
    static final private String CorrectSwitch = "-tboolean -kcorrect";

    /**
     * Example of one possible way to define a parser.
     * 
     * @param args
     */
    public static void main (final String[] args)
    {
        try
        {
            /*
             * Create a new instance of the command line processor.
             */
            final ICmdLine cmdline = new CmdLine();
            cmdline.compile(FastSwitch, CheapSwitch, CorrectSwitch);
            /*
             * The user's command line is now parsed against the compiled
             * interpreter.
             */
            cmdline.parse(CommandLineParser.getInstance(cmdline.getCommandPrefix(), args));
            /*
             * Display the values for the boolean arguments.
             */
            System.out.println("fast = " + cmdline.arg("--fast").getValue());
            System.out.println("cheap = " + cmdline.arg("--cheap").getValue());
            System.out.println("correct = " + cmdline.arg("--correct").getValue());

        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
}
