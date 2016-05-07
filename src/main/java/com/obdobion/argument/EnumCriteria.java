package com.obdobion.argument;

import java.util.List;

public class EnumCriteria<E> extends ListCriteria<E>
{

    public EnumCriteria(final List<E> listOfValidValues)
    {
        super(listOfValidValues);
    }

    @Override
    public EnumCriteria<E> clone () throws CloneNotSupportedException
    {
        final EnumCriteria<E> clone = (EnumCriteria<E>) super.clone();
        return clone;
    }

    @Override
    public boolean isSelected (final Comparable<E> value, final boolean caseSensitive)
    {
        return list.contains(value);
    }

}
