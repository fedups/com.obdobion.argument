package com.obdobion.argument.criteria;

import java.util.List;

/**
 * Comment from a testcase in InstantiatorTest.java.
 *
 * This is the only known time when --enumlist is actually needed. Otherwise the
 * list of possible enum names can be determined from either the instance
 * variable type of the instanceClass argument. In this case, neither of these
 * can be used to know that an enum is involved and the enumlist provide a set
 * of values that the input will be normalized, verified too.
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class EnumCriteria<E> extends ListCriteria<E>
{

    /**
     * <p>Constructor for EnumCriteria.</p>
     *
     * @param listOfValidValues a {@link java.util.List} object.
     */
    public EnumCriteria(final List<E> listOfValidValues)
    {
        super(listOfValidValues);
    }

    /** {@inheritDoc} */
    @Override
    public EnumCriteria<E> clone() throws CloneNotSupportedException
    {
        final EnumCriteria<E> clone = (EnumCriteria<E>) super.clone();
        return clone;
    }
}
