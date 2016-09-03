package com.obdobion.argument.type;

import java.text.SimpleDateFormat;

/**
 * <p>
 * ComparablePattern class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 * @since 4.3.1
 */
public class ComparableSimpleDateFormat implements Comparable<String>
{
    /**
     * <p>
     * compile.
     * </p>
     *
     * @param valueStr a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ComparableSimpleDateFormat}
     *         object.
     */
    public static ComparableSimpleDateFormat compile(final String valueStr)
    {
        final ComparableSimpleDateFormat cp = new ComparableSimpleDateFormat();
        cp.delegate = new SimpleDateFormat(valueStr);
        cp.pattern = valueStr;
        return cp;
    }

    /**
     * <p>
     * compile.
     * </p>
     *
     * @param valueStr a {@link java.lang.String} object.
     * @param caseInsensitive a int.
     * @return a {@link com.obdobion.argument.type.ComparableSimpleDateFormat}
     *         object.
     */
    public static ComparableSimpleDateFormat compile(final String valueStr, final int caseInsensitive)
    {
        final ComparableSimpleDateFormat cp = new ComparableSimpleDateFormat();
        cp.delegate = new SimpleDateFormat(valueStr);
        cp.pattern = valueStr;
        return cp;
    }

    String           pattern;
    SimpleDateFormat delegate;

    /** {@inheritDoc} */
    @Override
    public int compareTo(final String o)
    {
        return pattern.compareTo(o);
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
        final ComparableSimpleDateFormat other = (ComparableSimpleDateFormat) obj;
        if (pattern == null)
        {
            if (other.pattern != null)
                return false;
        } else if (!pattern.equals(other.pattern))
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pattern == null)
                ? 0
                : pattern.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return pattern;
    }
}
