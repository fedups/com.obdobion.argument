package com.obdobion.argument.type;

import java.text.ParseException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Chris DeGreef
 *
 */
public class PatternCLA extends AbstractCLA<ComparablePattern>
{
    public PatternCLA(final char _keychar)
    {
        super(_keychar);
    }

    public PatternCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public PatternCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType(final StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_PATTERN);
    }

    /**
     * @param target
     */
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

    @Override
    public String defaultInstanceClass()
    {
        return "java.util.regex.Pattern";
    }

    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ).pattern());
    }

    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ).pattern());
        out.append("\n");
    }

    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ).pattern(), out);
    }

    @Override
    public String genericClassName()
    {
        return "java.util.regex.Pattern";
    }

    @Override
    public Object getDelegateOrValue()
    {
        return getValue().delegate;
    }

    @Override
    public Object getDelegateOrValue(final int occurrence)
    {
        return getValue(occurrence).delegate;
    }

    @Override
    public Pattern getValueAsPattern() throws ParseException
    {
        return getValue().delegate;
    }

    @Override
    public Pattern[] getValueAsPatternArray() throws ParseException
    {
        final Pattern[] result = new Pattern[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).delegate;

        return result;
    }

    @Override
    public boolean supportsCaseSensitive()
    {
        return true;
    }

    @Override
    public boolean supportsInList()
    {
        return false;
    }
}
