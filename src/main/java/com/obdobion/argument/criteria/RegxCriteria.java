package com.obdobion.argument.criteria;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.obdobion.argument.usage.UsageBuilder;

/**
 * <p>RegxCriteria class.</p>
 *
 * @param <E>
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class RegxCriteria<E> implements ICmdLineArgCriteria<E>
{
    Pattern pattern;
    String  patternParm;

    /**
     * <p>Constructor for RegxCriteria.</p>
     *
     * @param _pattern a {@link java.lang.String} object.
     */
    public RegxCriteria(final String _pattern)
    {
        this.patternParm = _pattern;
    }

    /** {@inheritDoc} */
    @Override
    public void asDefinitionText(final StringBuilder sb)
    {
        sb.append(" --matches '");
        sb.append(patternParm);
        sb.append("'");
    }

    /** {@inheritDoc} */
    @Override
    public void asSetter(final StringBuilder sb)
    {
        sb.append(".setRegxCriteria(\"");
        sb.append(patternParm);
        sb.append("\")");
    }

    /** {@inheritDoc} */
    @Override
    public RegxCriteria<E> clone() throws CloneNotSupportedException
    {
        @SuppressWarnings("unchecked")
        final RegxCriteria<E> clone = (RegxCriteria<E>) super.clone();
        return clone;
    }

    /**
     * <p>Getter for the field <code>patternParm</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPatternParm()
    {
        return patternParm;
    }

    /** {@inheritDoc} */
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
     * {@inheritDoc}
     *
     * Normalization is not possible for patterns.
     */
    @Override
    public E normalizeValue(final E value, final boolean caseSensitive)
    {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public void usage(final UsageBuilder str, final int indentLevel)
    {
        str.append("The value must match this regular expression: ").append(patternParm).append(".");
    }
}
