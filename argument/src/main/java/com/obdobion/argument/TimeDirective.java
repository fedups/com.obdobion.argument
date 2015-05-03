package com.obdobion.argument;

import java.text.SimpleDateFormat;

/**
 * @author Chris DeGreef
 * 
 */
public class TimeDirective extends DateDirective
{
    public TimeDirective(String _data)
    {
        super(_data);
    }

    @Override
    protected SimpleDateFormat replaceTokenDateFormat ()
    {
        return new SimpleDateFormat("HH:mm:ss.SSS");
    }

}
