package com.obdobion.argument.variables;

import java.text.ParseException;

import com.obdobion.argument.type.CmdLineCLA;
import com.obdobion.argument.type.ICmdLineArg;

/**
 * <p>
 * IVariableAssigner interface.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public interface IVariableAssigner
{
    /**
     * <p>
     * assign.
     * </p>
     *
     * @param arg a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @param target a {@link java.lang.Object} object.
     * @throws java.text.ParseException if any.
     */
    void assign(final ICmdLineArg<?> arg, final Object target) throws ParseException;

    /**
     * <p>
     * newGroupVariable.
     * </p>
     *
     * @param group a {@link com.obdobion.argument.type.CmdLineCLA} object.
     * @param target a {@link java.lang.Object} object.
     * @param factoryValueArg a {@link com.obdobion.argument.type.ICmdLineArg}
     *            object.
     * @return a {@link java.lang.Object} object.
     * @throws java.text.ParseException if any.
     */
    Object newGroupVariable(final CmdLineCLA group, final Object target, final ICmdLineArg<?> factoryValueArg)
            throws ParseException;
}
