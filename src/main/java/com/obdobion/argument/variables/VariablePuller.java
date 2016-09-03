package com.obdobion.argument.variables;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.argument.ICmdLine;
import com.obdobion.argument.type.CmdLineCLA;
import com.obdobion.argument.type.ICmdLineArg;

/**
 * <p>
 * VariablePuller class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class VariablePuller
{
    private final static Logger   logger = LoggerFactory.getLogger(VariablePuller.class.getName());

    static private VariablePuller instance;

    /**
     * <p>
     * Getter for the field <code>instance</code>.
     * </p>
     *
     * @return a {@link com.obdobion.argument.variables.VariablePuller} object.
     */
    public static VariablePuller getInstance()
    {
        if (instance == null)
            instance = new VariablePuller();
        return instance;
    }

    /**
     * <p>
     * pull.
     * </p>
     *
     * @param arg a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @param variableSource a {@link java.lang.Object} object.
     * @throws java.text.ParseException if any.
     * @throws java.lang.IllegalArgumentException if any.
     * @throws java.lang.IllegalAccessException if any.
     */
    @SuppressWarnings("unchecked")
    public void pull(final ICmdLineArg<?> arg, final Object variableSource)
            throws ParseException, IllegalArgumentException, IllegalAccessException
    {
        arg.reset();
        if (arg.isSystemGenerated())
            return;

        final String errMsg = "pulling "
                + arg.getVariable()
                + " from "
                + variableSource.getClass().getName();

        final Field field = VariableAssigner.findFieldInAnyParentOrMyself(arg, variableSource.getClass(), errMsg);
        if (field == null)
            return;

        final boolean wasAccessible = field.isAccessible();

        try
        {
            field.setAccessible(true);

            if (field.getType() == List.class)
            {
                final List<Object> values = (List<Object>) field.get(variableSource);
                if (values != null)
                    for (final Object value : values)
                        if (arg instanceof CmdLineCLA)
                        {
                            final ICmdLine cmdline = (((CmdLineCLA) arg).templateCmdLine).clone();
                            arg.setObject(cmdline);
                            for (final ICmdLineArg<?> innerArg : cmdline.allArgs())
                            {
                                pull(innerArg, value);
                            }
                        } else
                            arg.setObject(value);

            } else if (field.getType().isArray())
            {
                final Object[] values = (Object[]) field.get(variableSource);
                if (values != null)
                    for (final Object value : values)
                        if (arg instanceof CmdLineCLA)
                        {
                            final ICmdLine cmdline = (((CmdLineCLA) arg).templateCmdLine).clone();
                            arg.setObject(cmdline);
                            for (final ICmdLineArg<?> innerArg : cmdline.allArgs())
                            {
                                pull(innerArg, value);
                            }
                        } else
                            arg.setObject(value);
            } else
            {
                final Object value = field.get(variableSource);
                if (value != null)
                {
                    if (arg instanceof CmdLineCLA)
                    {
                        final ICmdLine cmdline = (((CmdLineCLA) arg).templateCmdLine).clone();
                        arg.setObject(cmdline);
                        for (final ICmdLineArg<?> innerArg : cmdline.allArgs())
                        {
                            pull(innerArg, value);
                        }
                    } else
                        /*
                         * Simple objects are handled here
                         */
                        arg.setObject(value);
                }
            }

        } catch (final CloneNotSupportedException e)
        {
            logger.error(errMsg, e);
        } finally
        {
            field.setAccessible(wasAccessible);
        }
    }
}
