package com.obdobion.argument.type;

import java.util.ArrayList;
import java.util.List;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.input.Token;

/**
 * @author Chris DeGreef
 *
 */
public class DefaultCLA extends AbstractCLA<String>
{
    /**
     * not called. A special case in the caller will redirect this call to the
     * one with the args as a parameter.
     */
    @Override
    public void applyDefaults()
    {
        // intentionally left blank
    }

    public void applyDefaults(final char commandPrefix, final List<ICmdLineArg<?>> allKnownArgs)
    {
        for (final String argNameToReset : getValues())
        {
            if (argNameToReset == null)
                return;

            Token token = null;
            if (argNameToReset.trim().length() == 1)
                token = new Token(commandPrefix, commandPrefix + argNameToReset);
            else
                token = new Token(commandPrefix, "" + commandPrefix + commandPrefix + argNameToReset);

            final List<ICmdLineArg<?>> bestArgs = new ArrayList<>();
            CmdLine.matchingArgs(bestArgs, allKnownArgs, token, true);
            if (bestArgs.isEmpty())
                return;

            final ICmdLineArg<?> target = bestArgs.get(0);
            target.useDefaults();
        }
    }

    /**
     * @param target
     */
    @Override
    public String convert(final String valueStr, final boolean _caseSensitive, final Object target)
    {
        if (_caseSensitive)
            return valueStr;
        return valueStr.toLowerCase();
    }

    @Override
    public String defaultInstanceClass()
    {
        return "Object";
    }

    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ));
    }

    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ));
        out.append("\n");
    }

    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ), out);
    }

    @Override
    public String genericClassName()
    {
        return "java.lang.String";
    }
}
