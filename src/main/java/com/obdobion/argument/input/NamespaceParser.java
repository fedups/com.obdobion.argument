package com.obdobion.argument.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.obdobion.argument.type.ICmdLineArg;

/**
 * Properties can be used to represent command line args using a modified
 * namespace encoding. "."s break the name up into group levels and the final
 * node is the specific arg name. On the RHS of the = is the value associated
 * with the arg.
 * <p>
 * Each of the nodes in the arg name can be suffixed with an index in order to
 * handle multi valued args. These are specified with brackets. [0] for
 * instance.
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class NamespaceParser extends AbstractInputParser implements IParserInput
{
    /**
     * <p>getInstance.</p>
     *
     * @param file a {@link java.io.File} object.
     * @return a {@link com.obdobion.argument.input.IParserInput} object.
     * @throws java.io.IOException if any.
     */
    static public IParserInput getInstance(final File file) throws IOException
    {
        final List<String> args = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String aConfigLine = null;
            while ((aConfigLine = reader.readLine()) != null)
                args.add(aConfigLine);
        }

        final NamespaceParser parser = new NamespaceParser();
        parser.args = args.toArray(new String[args.size()]);
        return parser;
    }

    /**
     * <p>getInstance.</p>
     *
     * @param args a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.input.IParserInput} object.
     */
    static public IParserInput getInstance(final String... args)
    {
        final NamespaceParser parser = new NamespaceParser();
        parser.args = args;
        return parser;
    }

    static private String parseNamespaceLine(final String arg, final List<NodeOc> line)
    {
        final String[] keyValue = arg.split("=");

        /*
         * "a.b.c." represents a positional (unnamed) final level. If the split
         * does not see a space after the last node then it will only count 3.
         * It needs to count 4. The [0] seems to indicate a multiple item But
         * the 0 element is the same as a direct reference in argument.
         */
        String value = keyValue[0];
        if (value.length() > 0)
            if (value.charAt(value.length() - 1) == '.')
                value = keyValue[0] + "[0]";
        final String[] nodes = value.split("\\.");

        line.clear();
        for (int n = 0; n < nodes.length; n++)
        {
            final int start = nodes[n].indexOf("[");
            final NodeOc newNode = new NodeOc();
            if (start > -1)
            {
                final int end = nodes[n].indexOf("]");
                newNode.key = nodes[n].substring(0, start);
                newNode.occurrance = Integer.parseInt(nodes[n].substring(start + 1, end));
            } else
            {
                newNode.key = nodes[n];
                newNode.occurrance = -1;
            }
            line.add(newNode);
        }
        if (keyValue.length > 1)
            return keyValue[1];
        return "";
    }

    /** {@inheritDoc} */
    static protected String quote(final String value)
    {
        return value;
    }

    /**
     * <p>unparseTokens.</p>
     *
     * @param args a {@link java.util.List} object.
     * @return a {@link java.lang.String} object.
     */
    static public String unparseTokens(final List<ICmdLineArg<?>> args)
    {
        final StringBuilder out = new StringBuilder();
        unparseTokens("", args, out);
        return out.toString();
    }

    /**
     * <p>unparseTokens.</p>
     *
     * @param prefix a {@link java.lang.String} object.
     * @param args a {@link java.util.List} object.
     * @param out a {@link java.lang.StringBuilder} object.
     */
    static public void unparseTokens(final String prefix, final List<ICmdLineArg<?>> args, final StringBuilder out)
    {
        final Iterator<ICmdLineArg<?>> aIter = args.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg.isParsed())
                arg.exportNamespace(prefix, out);
        }
    }

    protected String[] args;

    final char         commandPrefix = '-';

    NamespaceParser()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public Token[] parseTokens()
    {
        final List<Token> out = new ArrayList<>();
        final List<NodeOc> depth = new ArrayList<>();
        final List<NodeOc> line = new ArrayList<>();
        Arrays.sort(args);
        for (int a = 0; a < args.length; a++)
        {
            final String value = parseNamespaceLine(args[a], line);
            buildTokens(commandPrefix, out, depth, line, value);
        }

        for (int d = 1; d < depth.size(); d++)
            out.add(new Token(commandPrefix, CLOSE_GROUP, 0, 0, false));

        return out.toArray(new Token[out.size()]);
    }

    /** {@inheritDoc} */
    @Override
    public String substring(final int inclusiveStart, final int exclusiveEnd)
    {
        return "";
    }
}
