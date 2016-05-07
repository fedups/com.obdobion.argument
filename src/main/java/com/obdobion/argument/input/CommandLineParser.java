package com.obdobion.argument.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.obdobion.argument.ICmdLineArg;
import com.obdobion.argument.Token;

/**
 * This parser takes a typical command line entry and creates tokens. A command
 * line is made up of key words and values. A key word is prefixed by a - or
 * two. Brackets or parenthesis may be used to create subparsers.
 * 
 * @author Chris DeGreef
 * 
 */
public class CommandLineParser extends AbstractInputParser implements IParserInput
{
    static private StringBuilder convertToString (
        final File commandFile)
        throws FileNotFoundException,
        IOException
    {
        final FileReader fis = new FileReader(commandFile);
        final BufferedReader buf = new BufferedReader(fis);
        final StringBuilder str = new StringBuilder();
        String input = buf.readLine();
        while (input != null)
        {
            /*
             * Allow comments if the first character on a line is #. It must be
             * column 1.
             */
            if (input.length() > 0 && input.charAt(0) != '#')
            {
                str.append(input);
                str.append(' ');
            }
            input = buf.readLine();
        }
        buf.close();
        return str;
    }

    static public IParserInput getInstance (
        final char commandPrefix,
        final boolean allowEmbeddedCommandPrefix,
        final File args)
        throws IOException
    {
        final CommandLineParser parser = new CommandLineParser();
        parser.commandPrefix = commandPrefix;
        parser.allowEmbeddedCommandPrefix = allowEmbeddedCommandPrefix;
        parser.commandLine = convertToString(args).toString();
        return parser;
    }

    static public IParserInput getInstance (
        final char commandPrefix,
        final boolean allowEmbeddedCommandPrefix,
        final String... args)
    {
        final CommandLineParser parser = new CommandLineParser();
        parser.commandPrefix = commandPrefix;
        parser.allowEmbeddedCommandPrefix = allowEmbeddedCommandPrefix;
        final StringBuilder str = new StringBuilder();
        for (int c = 0; c < args.length; c++)
        {
            str.append(args[c]);
            str.append(" ");
        }
        parser.commandLine = str.toString();
        return parser;
    }

    static public IParserInput getInstance (
        final char commandPrefix,
        final File args)
        throws IOException
    {
        return getInstance(commandPrefix, false, args);
    }

    static public IParserInput getInstance (
        final char commandPrefix,
        final String... args)
    {
        return getInstance(commandPrefix, false, args);
    }

    static public String unparseTokens (
        final List<ICmdLineArg<?>> args)
    {
        final StringBuilder out = new StringBuilder();
        unparseTokens(args, out);
        return out.toString();
    }

    static public void unparseTokens (
        final List<ICmdLineArg<?>> args,
        final StringBuilder out)
    {
        final Iterator<ICmdLineArg<?>> aIter = args.iterator();
        boolean first = true;
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg.isParsed())
            {
                if (!first)
                    out.append(" ");
                first = false;
                arg.exportCommandLine(out);
            }
        }
    }

    protected String commandLine;
    char             commandPrefix;

    boolean          allowEmbeddedCommandPrefix;

    private CommandLineParser()
    {
        super();
        allowEmbeddedCommandPrefix = true;
    }

    public Token[] parseTokens ()
    {
        char delim = ' ';
        final StringBuilder part = new StringBuilder();
        boolean inToken = false;
        int startX = 0;
        final List<Token> tokens = new ArrayList<>();
        char thisChar = ' ';
        char prevChar;

        for (int scanX = 0; scanX < commandLine.length(); scanX++)
        {
            prevChar = thisChar;
            thisChar = commandLine.charAt(scanX);
            if (inToken)
            {
                if (delim != ' ' && thisChar == delim)
                {
                    tokens.add(new Token(commandPrefix,
                        part.toString(),
                        startX,
                        scanX + 1,
                        true));
                    part.delete(0, part.length());
                    inToken = false;
                } else if (delim == ' '
                    && (Character.isWhitespace(thisChar)
                        || thisChar == '('
                        || thisChar == ')'
                        || thisChar == '['
                        || thisChar == ']'
                        || thisChar == ';'
                        || thisChar == ','
                        || thisChar == '='
                        || thisChar == '"'
                        // These two lines don't provide any known
                        // benefit.
                        // || (part.length() == 0 && thisChar == '@')
                        // || (part.length() == 0 && thisChar == '_')
                        || thisChar == '\''
                        || (!allowEmbeddedCommandPrefix && thisChar == commandPrefix && prevChar != commandPrefix)))
                {
                    boolean forceLiteral = false;
                    /*
                     * Single char commands can not be a number. This allows
                     * negative numbers to be entered without escaping them or
                     * surrounding them with delimiters.
                     */
                    if (part.length() > 1)
                    {
                        if (part.charAt(0) == commandPrefix
                            && Character.isDigit(part.charAt(1)))
                            forceLiteral = true;
                    }
                    tokens.add(new Token(commandPrefix,
                        part.toString(),
                        startX,
                        scanX,
                        forceLiteral));
                    part.delete(0, part.length());
                    /*
                     * rescan char
                     */
                    --scanX;
                    inToken = false;
                } else
                {
                    if (delim != ' ' && thisChar == '\\')
                        thisChar = commandLine.charAt(++scanX);
                    part.append(thisChar);
                }
            } else
            {
                if (Character.isWhitespace(thisChar)
                    || thisChar == ':'
                    || thisChar == ';'
                    || thisChar == ','
                    || thisChar == '=')
                    continue;
                if (thisChar == '(' || thisChar == ')' || thisChar == '[' || thisChar == ']')
                {
                    tokens.add(new Token(commandPrefix,
                        "" + thisChar,
                        scanX,
                        scanX + 1,
                        false));
                    continue;
                }
                if (thisChar == '"' || thisChar == '\'')
                {
                    delim = thisChar;
                } else
                {
                    delim = ' ';
                    part.append(thisChar);
                }
                inToken = true;
                startX = scanX;
            }
        }
        if (inToken)
            tokens.add(new Token(commandPrefix,
                part.toString(),
                startX,
                commandLine.length(),
                delim != ' '));

        return tokens.toArray(new Token[tokens.size()]);
    }

    public String substring (int inclusiveStart, int exclusiveEnd)
    {
        return commandLine.substring(inclusiveStart, exclusiveEnd);
    }
}
