package com.obdobion.argument.directive;

import java.text.SimpleDateFormat;

/**
 * <p>TimeDirective class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class TimeDirective extends DateDirective
{
    /**
     * <p>Constructor for TimeDirective.</p>
     *
     * @param _data a {@link java.lang.String} object.
     */
    public TimeDirective(String _data)
    {
        super(_data);
    }

    /** {@inheritDoc} */
    @Override
    protected SimpleDateFormat replaceTokenDateFormat ()
    {
        return new SimpleDateFormat("HH:mm:ss.SSS");
    }

}
