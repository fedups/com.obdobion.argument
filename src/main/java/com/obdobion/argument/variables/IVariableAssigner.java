package com.obdobion.argument.variables;

import java.text.ParseException;

import com.obdobion.argument.type.CmdLineCLA;
import com.obdobion.argument.type.ICmdLineArg;

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
