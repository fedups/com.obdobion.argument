package com.obdobion.argument.criteria;

import com.obdobion.argument.usage.UsageBuilder;

/**
 * @author Chris DeGreef
 * 
 * @param <E>
 */
public interface ICmdLineArgCriteria<E> extends Cloneable
{
    ICmdLineArgCriteria<E> clone () throws CloneNotSupportedException;

    public boolean isSelected (Comparable<E> value, boolean caseSensitive);

    E normalizeValue (E value, boolean caseSensitive);

    void asDefinitionText (StringBuilder sb);

    void asSetter (StringBuilder sb);

    void usage (UsageBuilder str, int indentLevel);
}
