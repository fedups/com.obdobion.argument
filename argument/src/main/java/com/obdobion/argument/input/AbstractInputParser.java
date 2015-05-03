package com.obdobion.argument.input;

import java.util.List;

import com.obdobion.argument.Token;

/**
 * @author Chris DeGreef
 * 
 */
abstract public class AbstractInputParser
{
    protected static final String OPEN_GROUP  = "[";
    protected static final String CLOSE_GROUP = "]";
    protected static final char   QUOTER      = '"';

    static private String dashed (char commandPrefix, final String key)
    {
        if (key.length() == 1)
            return commandPrefix + key;
        return "" + commandPrefix + commandPrefix + key;
    }

    static protected String quote (final String value)
    {
        final StringBuilder out = new StringBuilder();
        out.append("\"").append(value.replaceAll("\"", "\\\\\\\"")).append("\"");
        return out.toString();
    }

    public AbstractInputParser()
    {
        super();
    }

    protected void buildCommandLine (final StringBuilder out, final List<NodeOc> depth, final List<NodeOc> line,
            final String value)
    {
        for (int n = 0; n < line.size(); n++)
        {
            if (depth.size() <= n)
            {
                /*
                 * This is a new command
                 */
                depth.add(line.get(n));
                /*
                 * an empty keyword means a positional parameter
                 */
                if (line.get(n).key.length() > 0)
                    out.append("--" + line.get(n).key);
                /*
                 * if the depth so far is shorter than the namespace then it is
                 * necessary to start a group
                 */
                if (n < line.size() - 1)
                    out.append(OPEN_GROUP);
            } else if (depth.get(n).key.equalsIgnoreCase(line.get(n).key))
            {
                /*
                 * This is a subsequent command for the same namespace at this
                 * level. Check the occurrence to see if a new multiple group )(
                 * should be started. If the occurrence is the same then the
                 * subsequent namespace will make it unique and this specific
                 * node in the namespace can be safely ignored. In essence we
                 * are within a sub-structure and only change instance
                 * variables.
                 */
                if (depth.get(n).occurrance == line.get(n).occurrance)
                    continue;
                /*
                 * A new occurrence of the same namespace node. So we close off
                 * deeper groups.
                 */
                for (int d = depth.size() - 1; d > n; d--)
                {
                    depth.remove(d);
                    out.append(CLOSE_GROUP);
                }
                depth.get(n).occurrance = line.get(n).occurrance;
                if (n < line.size() - 1)
                    out.append(OPEN_GROUP);
                continue;
            } else
            {
                for (int d = depth.size() - 1; d > n; d--)
                {
                    depth.remove(d);
                    out.append(CLOSE_GROUP);
                }
                depth.get(n).key = line.get(n).key;
                depth.get(n).occurrance = line.get(n).occurrance;
                /*
                 * an empty keyword means a positional parameter
                 */
                if (line.get(n).key.length() > 0)
                    out.append("--" + line.get(n).key);
                if (n < line.size() - 1)
                    out.append(OPEN_GROUP);
                continue;
            }
        }
        /*
         * booleans have no value
         */
        if (value.length() > 0)
            out.append(quote(value));
    }

    protected void buildTokens (char commandPrefix, final List<Token> tokens, final List<NodeOc> depth,
            final List<NodeOc> line, final String value)
    {
        for (int n = 0; n < line.size(); n++)
        {
            if (depth.size() <= n)
            {
                /*
                 * This is a new command
                 */
                depth.add(line.get(n));
                /*
                 * an empty keyword means a positional parameter
                 */
                if (line.get(n).key.length() > 0)
                {
                    tokens.add(new Token(commandPrefix, dashed(commandPrefix, line.get(n).key), 0, 0, false));
                }
                /*
                 * if the depth so far is shorter than the namespace then it is
                 * necessary to start a group
                 */
                if (n < line.size() - 1)
                    tokens.add(new Token(commandPrefix, OPEN_GROUP, 0, 0, false));
            } else if (depth.get(n).key.equalsIgnoreCase(line.get(n).key))
            {
                /*
                 * This is a subsequent command for the same namespace at this
                 * level. Check the occurrence to see if a new multiple group )(
                 * should be started. If the occurrence is the same then the
                 * subsequent namespace will make it unique and this specific
                 * node in the namespace can be safely ignored. In essence we
                 * are within a sub-structure and only change instance
                 * variables.
                 */
                if (depth.get(n).occurrance == line.get(n).occurrance)
                    continue;
                /*
                 * A new occurrence of the same namespace node. So we close off
                 * deeper groups.
                 */
                for (int d = depth.size() - 1; d > n; d--)
                {
                    depth.remove(d);
                    tokens.add(new Token(commandPrefix, CLOSE_GROUP, 0, 0, false));
                }
                depth.get(n).occurrance = line.get(n).occurrance;
                if (n < line.size() - 1)
                    tokens.add(new Token(commandPrefix, OPEN_GROUP, 0, 0, false));
                continue;
            } else
            {
                for (int d = depth.size() - 1; d > n; d--)
                {
                    depth.remove(d);
                    tokens.add(new Token(commandPrefix, CLOSE_GROUP, 0, 0, false));
                }
                depth.get(n).key = line.get(n).key;
                depth.get(n).occurrance = line.get(n).occurrance;
                /*
                 * an empty keyword means a positional parameter
                 */
                if (line.get(n).key.length() > 0)
                    tokens.add(new Token(commandPrefix, dashed(commandPrefix, line.get(n).key), 0, 0, false));
                if (n < line.size() - 1)
                    tokens.add(new Token(commandPrefix, OPEN_GROUP, 0, 0, false));
                continue;
            }
        }
        /*
         * booleans have no value
         */
        if (value != null && value.length() > 0)
        {
            tokens.add(new Token(commandPrefix, value, 0, 0, true));
        }
    }
}
