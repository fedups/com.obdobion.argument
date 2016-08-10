package com.obdobion.argument.directive;

import java.text.SimpleDateFormat;

/**
 * <p>DateTimeDirective class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class DateTimeDirective extends DateDirective
{
    /**
     * <p>Constructor for DateTimeDirective.</p>
     *
     * @param _data a {@link java.lang.String} object.
     */
    public DateTimeDirective(String _data)
    {
        super(_data);
    }

    /** {@inheritDoc} */
    @Override
    protected SimpleDateFormat replaceTokenDateFormat ()
    {
        return new SimpleDateFormat("yyyy/MM/dd@HH:mm:ss.SSS");
    }
}
