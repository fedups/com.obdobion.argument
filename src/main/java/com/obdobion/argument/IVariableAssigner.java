package com.obdobion.argument;

import java.text.ParseException;

/**
 * @author Chris DeGreef
 * 
 */
public interface IVariableAssigner
{
    void assign (final ICmdLineArg<?> arg, final Object target) throws ParseException;

    Object newGroupVariable (final CmdLineCLA group, final Object target, final ICmdLineArg<?> factoryValueArg)
            throws ParseException;
}
