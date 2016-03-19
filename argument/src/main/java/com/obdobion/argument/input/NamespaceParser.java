package com.obdobion.argument.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.obdobion.argument.ICmdLineArg;
import com.obdobion.argument.Token;

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
 * @author Chris DeGreef
 * 
 */
public class NamespaceParser extends AbstractInputParser implements IParserInput
{

    static public IParserInput getInstance (final String... args)
    {

        final NamespaceParser parser = new NamespaceParser();
        parser.args = args;
        return parser;
    }

    static protected String quote (final String value)
    {
        return value;
    }

    static public String unparseTokens (final List<ICmdLineArg<?>> args)
    {
        final StringBuilder out = new StringBuilder();
        unparseTokens("", args, out);
        return out.toString();
    }

    static public void unparseTokens (final String prefix, final List<ICmdLineArg<?>> args, final StringBuilder out)
    {
        final Iterator<ICmdLineArg<?>> aIter = args.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg.isParsed())
            {
                arg.exportNamespace(prefix, out);
            }
        }
    }

    protected String[] args;
    final char         commandPrefix = '-';

    private NamespaceParser()
    {
        super();
    }

    public String substring (int inclusiveStart, int exclusiveEnd)
    {
        return "";
    }

    static private String parseNamespaceLine (final String arg, final List<NodeOc> line)
    {
        final String[] keyValue = arg.split("=");
        final String[] nodes = keyValue[0].split("\\.");

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

    public Token[] parseTokens ()
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
}
