package com.obdobion.argument.type;

import java.util.regex.Pattern;

/**
 * <p>ComparablePattern class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class ComparablePattern implements Comparable<Pattern>
{
    /**
     * <p>compile.</p>
     *
     * @param valueStr a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ComparablePattern} object.
     */
    public static ComparablePattern compile(final String valueStr)
    {
        final ComparablePattern cp = new ComparablePattern();
        cp.delegate = Pattern.compile(valueStr);
        return cp;
    }

    /**
     * <p>compile.</p>
     *
     * @param valueStr a {@link java.lang.String} object.
     * @param caseInsensitive a int.
     * @return a {@link com.obdobion.argument.type.ComparablePattern} object.
     */
    public static ComparablePattern compile(final String valueStr, final int caseInsensitive)
    {
        final ComparablePattern cp = new ComparablePattern();
        cp.delegate = Pattern.compile(valueStr, caseInsensitive);
        return cp;
    }

    Pattern delegate;

    /** {@inheritDoc} */
    @Override
    public int compareTo(final Pattern o)
    {
        return delegate.pattern().compareTo(o.pattern());
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ComparablePattern other = (ComparablePattern) obj;
        if (delegate.pattern() == null)
        {
            if (other.delegate.pattern() != null)
                return false;
        } else if (!delegate.pattern().equals(other.delegate.pattern()))
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((delegate.pattern() == null)
                ? 0
                : delegate.pattern().hashCode());
        return result;
    }

    /**
     * <p>pattern.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String pattern()
    {
        return delegate.pattern();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return delegate.pattern();
    }
}
