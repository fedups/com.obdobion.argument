package com.obdobion.argument;

import java.util.regex.Pattern;

/**
 * @author Chris DeGreef
 * 
 */
public class ComparablePattern implements Comparable<Pattern>
{
    public static ComparablePattern compile (
        final String valueStr)
    {
        final ComparablePattern cp = new ComparablePattern();
        cp.delegate = Pattern.compile(valueStr);
        return cp;
    }

    public static ComparablePattern compile (
        final String valueStr,
        final int caseInsensitive)
    {
        final ComparablePattern cp = new ComparablePattern();
        cp.delegate = Pattern.compile(valueStr, caseInsensitive);
        return cp;
    }

    Pattern delegate;

    public int compareTo (
        final Pattern o)
    {
        return delegate.pattern().compareTo(o.pattern());
    }

    @Override
    public boolean equals (Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ComparablePattern other = (ComparablePattern) obj;
        if (delegate.pattern() == null)
        {
            if (other.delegate.pattern() != null)
                return false;
        } else if (!delegate.pattern().equals(other.delegate.pattern()))
            return false;
        return true;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((delegate.pattern() == null)
                ? 0
                : delegate.pattern().hashCode());
        return result;
    }

    public String pattern ()
    {
        return delegate.pattern();
    }

    @Override
    public String toString ()
    {
        return delegate.pattern();
    }
}
