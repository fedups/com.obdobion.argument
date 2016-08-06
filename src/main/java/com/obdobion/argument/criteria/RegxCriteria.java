package com.obdobion.argument.criteria;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.obdobion.argument.usage.UsageBuilder;

/**
 * @author Chris DeGreef
 *
 * @param <E>
 */
public class RegxCriteria<E> implements ICmdLineArgCriteria<E>
{
    Pattern pattern;
    String  patternParm;

    public RegxCriteria(final String _pattern)
    {
        this.patternParm = _pattern;
    }

    @Override
    public void asDefinitionText(final StringBuilder sb)
    {
        sb.append(" --matches '");
        sb.append(patternParm);
        sb.append("'");
    }

    @Override
    public void asSetter(final StringBuilder sb)
    {
        sb.append(".setRegxCriteria(\"");
        sb.append(patternParm);
        sb.append("\")");
    }

    @Override
    public RegxCriteria<E> clone() throws CloneNotSupportedException
    {
        @SuppressWarnings("unchecked")
        final RegxCriteria<E> clone = (RegxCriteria<E>) super.clone();
        return clone;
    }

    public String getPatternParm()
    {
        return patternParm;
    }

    @Override
    public boolean isSelected(final Comparable<E> value, final boolean caseSensitive)
    {
        if (pattern == null)
            if (caseSensitive)
                this.pattern = Pattern.compile(patternParm);
            else
                this.pattern = Pattern.compile(patternParm, Pattern.CASE_INSENSITIVE);
        final Matcher m = pattern.matcher(value.toString());
        return m.matches();
    }

    /**
     * Normalization is not possible for patterns.
     * 
     * @param caseSensitive
     */
    @Override
    public E normalizeValue(final E value, final boolean caseSensitive)
    {
        return value;
    }

    /**
     * @param indentLevel
     */
    @Override
    public void usage(final UsageBuilder str, final int indentLevel)
    {
        str.append("The value must match this regular expression: ").append(patternParm).append(".");
    }
}
