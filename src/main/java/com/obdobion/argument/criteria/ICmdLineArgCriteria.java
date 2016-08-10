package com.obdobion.argument.criteria;

import com.obdobion.argument.usage.UsageBuilder;

/**
 * <p>ICmdLineArgCriteria interface.</p>
 *
 * @param <E>
 * @author Chris DeGreef fedupforone@gmail.com
 */
public interface ICmdLineArgCriteria<E> extends Cloneable
{
    /**
     * <p>clone.</p>
     *
     * @return a {@link com.obdobion.argument.criteria.ICmdLineArgCriteria} object.
     * @throws java.lang.CloneNotSupportedException if any.
     */
    ICmdLineArgCriteria<E> clone () throws CloneNotSupportedException;

    /**
     * <p>isSelected.</p>
     *
     * @param value a {@link java.lang.Comparable} object.
     * @param caseSensitive a boolean.
     * @return a boolean.
     */
    public boolean isSelected (Comparable<E> value, boolean caseSensitive);

    /**
     * <p>normalizeValue.</p>
     *
     * @param value a E object.
     * @param caseSensitive a boolean.
     * @return a E object.
     */
    E normalizeValue (E value, boolean caseSensitive);

    /**
     * <p>asDefinitionText.</p>
     *
     * @param sb a {@link java.lang.StringBuilder} object.
     */
    void asDefinitionText (StringBuilder sb);

    /**
     * <p>asSetter.</p>
     *
     * @param sb a {@link java.lang.StringBuilder} object.
     */
    void asSetter (StringBuilder sb);

    /**
     * <p>usage.</p>
     *
     * @param str a {@link com.obdobion.argument.usage.UsageBuilder} object.
     * @param indentLevel a int.
     */
    void usage (UsageBuilder str, int indentLevel);
}
