package com.obdobion.argument.criteria;

import java.util.List;

import com.obdobion.argument.usage.UsageBuilder;

/**
 * <p>
 * ListCriteria class.
 * </p>
 *
 * @param <E>
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class ListCriteria<E> implements ICmdLineArgCriteria<E>
{
    List<E> list;

    /**
     * <p>
     * Constructor for ListCriteria.
     * </p>
     *
     * @param listOfValidValues a {@link java.util.List} object.
     */
    public ListCriteria(final List<E> listOfValidValues)
    {
        list = listOfValidValues;
    }

    /** {@inheritDoc} */
    @Override
    public void asDefinitionText(final StringBuilder sb)
    {
        sb.append(" --list");
        for (final E item : list)
        {
            sb.append(" '");
            sb.append(item.toString());
            sb.append("'");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void asSetter(final StringBuilder sb)
    {
        sb.append(".setListCriteria(new String[] {");
        boolean firstTime = true;
        for (final E item : list)
        {
            if (!firstTime)
                sb.append(",");
            sb.append("\"");
            sb.append(item.toString());
            sb.append("\"");
            firstTime = false;
        }
        sb.append("})");
    }

    /** {@inheritDoc} */
    @Override
    public ListCriteria<E> clone() throws CloneNotSupportedException
    {
        @SuppressWarnings("unchecked")
        final ListCriteria<E> clone = (ListCriteria<E>) super.clone();
        return clone;
    }

    /**
     * <p>
     * Getter for the field <code>list</code>.
     * </p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<E> getList()
    {
        return list;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSelected(final Comparable<E> value, final boolean caseSensitive)
    {
        if (!list.isEmpty() && list.get(0) instanceof String
                && !caseSensitive) { return list.contains(((String) value).toLowerCase()); }
        return list.contains(value);
    }

    /** {@inheritDoc} */
    @Override
    public E normalizeValue(final E value, final boolean caseSensitive)
    {
        E bestSoFar = null;

        /*
         * Only string lists make sense to normalize.
         */
        if (!(value instanceof String)) { return value; }

        String stringValue = (String) value;
        for (final E listItem : list)
        {
            String stringListItem = (String) listItem;

            stringValue = stringValue.toLowerCase();
            stringListItem = stringListItem.toLowerCase();

            if (stringListItem.startsWith(stringValue))
            {
                if (stringListItem.length() == stringValue.length())
                {
                    /*
                     * exactly matches something in the list, take the list item
                     * so that true normalization occurs
                     */
                    if (caseSensitive)
                        return listItem;
                    return value;
                }
                if (bestSoFar != null)
                {
                    /*
                     * Ambiguous list value. This will cause an error later.
                     */
                    return value;
                }
                /*
                 * We must take what is in the list in cases where the user
                 * enters in a shortened version of the listitem.
                 */
                bestSoFar = listItem;
            }
        }
        if (bestSoFar != null)
            /*
             * Only one thing matched so lets return the normalized value
             * instead. This will cause the validation to succeed.
             */
            return bestSoFar;
        /*
         * This will most likely cause a validation error later.
         */
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        if (list == null)
            return "null list";
        return list.toString();
    }

    /** {@inheritDoc} */
    @Override
    public void usage(final UsageBuilder str, final int indentLevel)
    {
        str.append("Possible choices are: ");
        for (final E item : list)
        {
            str.append(item.toString()).append(" ");
        }
    }

}
