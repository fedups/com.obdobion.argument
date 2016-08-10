package com.obdobion.argument.type;

import com.obdobion.algebrain.Equ;

/**
 * <p>ComparableEqu class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class ComparableEqu implements Comparable<Equ>
{
    /**
     * <p>compile.</p>
     *
     * @param valueStr a {@link java.lang.String} object.
     * @return a {@link com.obdobion.argument.type.ComparableEqu} object.
     * @throws java.lang.Exception if any.
     */
    public static ComparableEqu compile(final String valueStr) throws Exception
    {
        final ComparableEqu cp = new ComparableEqu();
        cp.delegate = Equ.getInstance(true);
        cp.delegate.compile(valueStr);
        return cp;
    }

    Equ delegate;

    /** {@inheritDoc} */
    @Override
    public int compareTo(final Equ o)
    {
        return delegate.toString().compareTo(o.toString());
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
        final ComparableEqu other = (ComparableEqu) obj;
        if (delegate.toString() == null)
        {
            if (other.delegate.toString() != null)
                return false;
        } else if (!delegate.toString().equals(other.delegate.toString()))
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((delegate.toString() == null)
                ? 0
                : delegate.toString().hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return delegate.toString();
    }
}
