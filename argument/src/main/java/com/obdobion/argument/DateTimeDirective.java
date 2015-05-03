package com.obdobion.argument;

import java.text.SimpleDateFormat;

/**
 * @author Chris DeGreef
 * 
 */
public class DateTimeDirective extends DateDirective
{
    public DateTimeDirective(String _data)
    {
        super(_data);
    }

    @Override
    protected SimpleDateFormat replaceTokenDateFormat ()
    {
        return new SimpleDateFormat("yyyy/MM/dd@HH:mm:ss.SSS");
    }
}
