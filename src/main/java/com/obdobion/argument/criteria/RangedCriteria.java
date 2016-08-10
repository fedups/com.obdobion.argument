package com.obdobion.argument.criteria;

import com.obdobion.argument.usage.UsageBuilder;

/**
 * <p>RangedCriteria class.</p>
 *
 * @param <E>
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class RangedCriteria<E> implements ICmdLineArgCriteria<E>
{

    E min;
    E max;

    /**
     * <p>Constructor for RangedCriteria.</p>
     *
     * @param inclusiveMin a E object.
     * @param inclusiveMax a E object.
     */
    public RangedCriteria(final E inclusiveMin, final E inclusiveMax)
    {
        min = inclusiveMin;
        max = inclusiveMax;
    }

    /** {@inheritDoc} */
    @Override
    public void asDefinitionText(final StringBuilder sb)
    {
        if (min != null)
            if (max != null)
                sb.append(" --range '" + min.toString() + "' '" + max.toString() + "'");
            else
                sb.append(" --range '" + min.toString() + "'");
    }

    /** {@inheritDoc} */
    @Override
    public void asSetter(final StringBuilder sb)
    {
        sb.append(".setRangeCriteria(new String[] {");
        if (min != null)
            if (max != null)
                sb.append("\"" + min.toString() + "\", \"" + max.toString() + "\"");
            else
                sb.append("\"" + min.toString() + "\"");
        else
            sb.append("\"" + max.toString() + "\"");
        sb.append("})");
    }

    /** {@inheritDoc} */
    @Override
    public RangedCriteria<E> clone() throws CloneNotSupportedException
    {
        @SuppressWarnings("unchecked")
        final RangedCriteria<E> clone = (RangedCriteria<E>) super.clone();
        return clone;
    }

    /**
     * <p>Getter for the field <code>max</code>.</p>
     *
     * @return a E object.
     */
    public E getMax()
    {
        return max;
    }

    /**
     * <p>Getter for the field <code>min</code>.</p>
     *
     * @return a E object.
     */
    public E getMin()
    {
        return min;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSelected(final Comparable<E> value, final boolean caseSensitive)
    {
        if (value instanceof String)
        {
            if (min != null)
                if (((String) value).toLowerCase().compareTo((String) min) < 0)
                    return false;
            if (max != null)
                if (((String) value).toLowerCase().compareTo((String) max) > 0)
                    return false;
        } else
        {
            if (min != null)
                if (value.compareTo(min) < 0)
                    return false;
            if (max != null)
                if (value.compareTo(max) > 0)
                    return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * Normalization is not possible for ranges.
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
        if (min != null)
            if (max != null)
                str.append("The value must be from " + min.toString() + " to " + max.toString() + " inclusive");
            else
                str.append("The value must be at least " + min.toString() + ".");
        else
            str.append("The value must be less than or equal to " + max.toString() + ".");
    }
}
