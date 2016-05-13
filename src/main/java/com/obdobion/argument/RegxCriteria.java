package com.obdobion.argument;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public RegxCriteria<E> clone () throws CloneNotSupportedException
    {
        @SuppressWarnings("unchecked")
        final RegxCriteria<E> clone = (RegxCriteria<E>) super.clone();
        return clone;
    }

    public boolean isSelected (final Comparable<E> value, final boolean caseSensitive)
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
     */
    public E normalizeValue (final E value, final boolean caseSensitive)
    {
        return value;
    }

    public void asDefinitionText (StringBuilder sb)
    {
        sb.append(" --matches '");
        sb.append(patternParm);
        sb.append("'");
    }

    public void asSetter (StringBuilder sb)
    {
        sb.append(".setRegxCriteria(\"");
        sb.append(patternParm);
        sb.append("\")");
    }

    public void usage (final UsageBuilder str, final int indentLevel)
    {
        str.append("The value must match this regular expression: ").append(patternParm).append(".");
    }
}
