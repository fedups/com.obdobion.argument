package com.obdobion.argument.criteria;

import com.obdobion.argument.usage.UsageBuilder;

/**
 * @author Chris DeGreef
 *
 * @param <E>
 */
public class RangedCriteria<E> implements ICmdLineArgCriteria<E>
{

    E min;
    E max;

    public RangedCriteria(final E inclusiveMin, final E inclusiveMax)
    {
        min = inclusiveMin;
        max = inclusiveMax;
    }

    @Override
    public void asDefinitionText(final StringBuilder sb)
    {
        if (min != null)
            if (max != null)
                sb.append(" --range '" + min.toString() + "' '" + max.toString() + "'");
            else
                sb.append(" --range '" + min.toString() + "'");
    }

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

    @Override
    public RangedCriteria<E> clone() throws CloneNotSupportedException
    {
        @SuppressWarnings("unchecked")
        final RangedCriteria<E> clone = (RangedCriteria<E>) super.clone();
        return clone;
    }

    public E getMax()
    {
        return max;
    }

    public E getMin()
    {
        return min;
    }

    /**
     * @param caseSensitive
     *            Not used in this method
     */
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
     * Normalization is not possible for ranges.
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
        if (min != null)
            if (max != null)
                str.append("The value must be from " + min.toString() + " to " + max.toString() + " inclusive");
            else
                str.append("The value must be at least " + min.toString() + ".");
        else
            str.append("The value must be less than or equal to " + max.toString() + ".");
    }
}
