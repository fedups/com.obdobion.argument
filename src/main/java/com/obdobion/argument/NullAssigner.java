package com.obdobion.argument;

import java.text.ParseException;

/**
 * This class is intended to be distributed with the free android app. It will
 * be completely functional except for the assigned of the command line values
 * to variables. Nothing gets assigned. When the user pays for the app, the jar
 * with the real variable assigned will be in it.
 * 
 * @author Chris DeGreef
 * 
 */
public class NullAssigner implements IVariableAssigner
{
    public void assign (ICmdLineArg<?> arg, Object target) throws ParseException
    {
        // intentionally left blank
    }

    public Object newGroupVariable (CmdLineCLA group, Object target, ICmdLineArg<?> factoryValueArg)
            throws ParseException
    {
        return null;
    }
}
