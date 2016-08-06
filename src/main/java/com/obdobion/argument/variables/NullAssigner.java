package com.obdobion.argument.variables;

import java.text.ParseException;

import com.obdobion.argument.type.CmdLineCLA;
import com.obdobion.argument.type.ICmdLineArg;

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
    /**
     * @param arg
     * @param target
     */
    @Override
    public void assign(final ICmdLineArg<?> arg, final Object target) throws ParseException
    {
        // intentionally left blank
    }

    /**
     * @param group
     * @param target
     * @param factoryValueArg
     */
    @Override
    public Object newGroupVariable(final CmdLineCLA group, final Object target, final ICmdLineArg<?> factoryValueArg)
            throws ParseException
    {
        return null;
    }
}
