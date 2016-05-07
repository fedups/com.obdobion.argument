package com.obdobion.argument;

/**
 * @author Chris DeGreef
 * 
 * @param <E>
 */
public interface ICmdLineArgCriteria<E> extends Cloneable
{
    void asDefinitionText (StringBuilder sb);

    void asSetter (StringBuilder sb);

    ICmdLineArgCriteria<E> clone () throws CloneNotSupportedException;

    public boolean isSelected (Comparable<E> value, boolean caseSensitive);

    E normalizeValue (E value, boolean caseSensitive);

    void usage (UsageBuilder str, int indentLevel);
}
