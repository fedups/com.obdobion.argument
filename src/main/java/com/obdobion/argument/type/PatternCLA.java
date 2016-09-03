package com.obdobion.argument.type;

import java.text.ParseException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <p>
 * PatternCLA class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class PatternCLA extends AbstractCLA<ComparablePattern>
{
    /** {@inheritDoc} */
    @Override
    public ComparablePattern convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        try
        {
            if (_caseSensitive && isCaseSensitive())
                return ComparablePattern.compile(valueStr);
            return ComparablePattern.compile(valueStr, Pattern.CASE_INSENSITIVE);
        } catch (final PatternSyntaxException pse)
        {
            throw new ParseException(pse.getMessage(), 0);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "java.util.regex.Pattern";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ).pattern());
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ).pattern());
        out.append("\n");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ).pattern(), out);
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.util.regex.Pattern";
    }

    /** {@inheritDoc} */
    @Override
    public Object getDelegateOrValue()
    {
        return getValue().delegate;
    }

    /** {@inheritDoc} */
    @Override
    public Object getDelegateOrValue(final int occurrence)
    {
        return getValue(occurrence).delegate;
    }

    /** {@inheritDoc} */
    @Override
    public Pattern getValueAsPattern() throws ParseException
    {
        return getValue().delegate;
    }

    /** {@inheritDoc} */
    @Override
    public Pattern[] getValueAsPatternArray() throws ParseException
    {
        final Pattern[] result = new Pattern[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).delegate;

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsCaseSensitive()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsInList()
    {
        return false;
    }
}
