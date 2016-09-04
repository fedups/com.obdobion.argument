package com.obdobion.argument.type;

import java.util.ArrayList;
import java.util.List;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.input.Token;

/**
 * <p>
 * DefaultCLA class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class DefaultCLA extends AbstractCLA<String>
{
    /**
     * {@inheritDoc}
     *
     * not called. A special case in the caller will redirect this call to the
     * one with the args as a parameter.
     */
    @Override
    public void applyDefaults()
    {
        // intentionally left blank
    }

    /**
     * <p>
     * applyDefaults.
     * </p>
     *
     * @param commandPrefix a char.
     * @param allKnownArgs a {@link java.util.List} object.
     */
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

    /** {@inheritDoc} */
    @Override
    public String convert(final String valueStr, final boolean _caseSensitive, final Object target)
    {
        if (_caseSensitive)
            return valueStr;
        return valueStr.toLowerCase();
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "Object";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ));
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ));
        out.append("\n");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ), out);
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.lang.String";
    }
}
