package com.obdobion.argument;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Chris DeGreef
 * 
 */
public class VariableAssigner implements IVariableAssigner
{
    static private IVariableAssigner instance;

    static private void assign (
            final Field field,
            final ICmdLineArg<?> arg,
            final Object target)
            throws ParseException
    {
        if (arg.getVariable() == null)
            return;
        if (arg.getValue() == null)
            return;
        if (field == null)
            return;

        if (!(arg instanceof CmdLineCLA) && arg.getFactoryMethodName() != null)
            assignWithInstantiator(field, arg, target);
        else
            assignStandard(field, arg, target);
    }

    /**
     * This value may not be appropriate for the list. But since this is at
     * runtime we can't make use of the generics definition they may have
     * provided on the field. If the value is not what they expect then they
     * need to provide a --factory set of methods.
     */
    private static void assignList (final Field field, final ICmdLineArg<?> arg, final Object target)
            throws IllegalAccessException
    {
        Collection<Object> alist = (Collection<Object>) field.get(target);
        if (alist == null)
        {
            alist = new ArrayList<Object>();
            field.set(target, alist);
        }
        for (int v = 0; v < arg.size(); v++)
            alist.add(arg.getValue(v));
    }

    static private void assignStandard (
            final Field field,
            final ICmdLineArg<?> arg,
            final Object target)
            throws ParseException
    {
        final String errMsg = "expected: public "
                + arg.getValue().getClass().getName()
                + " "
                + arg.getVariable()
                + " on "
                + target.getClass().getName();
        try
        {
            if (isStringArray(field))
                field.set(target, arg.getValueAsStringArray());
            else if (isIntegerArray(field))
                field.set(target, arg.getValueAsIntegerArray());
            else if (isLongArray(field))
                field.set(target, arg.getValueAsLongArray());
            else if (isintArray(field))
                field.set(target, arg.getValueAsintArray());
            else if (isbyteArray(field))
                field.set(target, arg.getValueAsbyteArray());
            else if (isfloatArray(field))
                field.set(target, arg.getValueAsfloatArray());
            else if (isFloatArray(field))
                field.set(target, arg.getValueAsFloatArray());
            else if (isPatternArray(field))
                field.set(target, arg.getValueAsPatternArray());
            else if (isPattern(field))
                field.set(target, arg.getValueAsPattern());
            else if (isDateArray(field))
                field.set(target, arg.getValueAsDateArray());
            else if (isByteArray(field))
                field.set(target, arg.getValueAsByteArray());
            else if (isFileArray(field))
                field.set(target, arg.getValueAsFileArray());
            else if (isEnum(field))
                field.set(target, arg.asEnum(field.getName(), field.getType().getEnumConstants()));
            else if (isList(field))
                assignList(field, arg, target);
            else
                field.set(target, arg.getValue());
        } catch (final SecurityException e)
        {
            throw new ParseException("SecurityException: " + errMsg, -1);
        } catch (final IllegalArgumentException e)
        {
            throw new ParseException("IllegalArgumentException: " + errMsg, -1);
        } catch (final IllegalAccessException e)
        {
            throw new ParseException("IllegalAccessException: " + errMsg, -1);
        }

    }

    static private void assignWithInstantiator (
            final Field field,
            final ICmdLineArg<?> arg,
            final Object target)
            throws ParseException
    {
        Class<?> clazz = null;
        Method method = null;
        String baseClassName;
        if (field.getType().getName().charAt(0) == '[')
            baseClassName = field.getType().getName().substring(2, field.getType().getName().length() - 1);
        else
        {
            /*
             * Generics info is private. Lists can not determine the proper base
             * class. It is likely that errors will ensue.
             */
            baseClassName = field.getType().getName();
        }

        String errMsg = null;

        try
        {
            final int methodPvt = arg.getFactoryMethodName().lastIndexOf('.');

            final Class<?> parmClass = arg.getValue(0).getClass();

            if (methodPvt < 0)
            {
                if (field.getType().getPackage().getName().startsWith("java."))
                    /*
                     * It is too bad that the generic info on a field is not
                     * public. So here we can't determine the generic type of a
                     * list. To add multi value items to a list the --class
                     * parameter must be used.
                     */
                    throw new ParseException("Generics with a factory method must use --class",
                            0);

                errMsg = "expected: public static "
                        + baseClassName
                        + " "
                        + arg.getFactoryMethodName()
                        + "("
                        + parmClass.getName()
                        + ") on "
                        + baseClassName;
                clazz = ClassLoader.getSystemClassLoader().loadClass(baseClassName);
                method = clazz.getDeclaredMethod(arg.getFactoryMethodName(), parmClass);
            } else
            {
                errMsg = "expected: public static "
                        + arg.getFactoryMethodName().substring(0, methodPvt)
                        + " "
                        + arg.getFactoryMethodName().substring(methodPvt + 1)
                        + "("
                        + parmClass.getName()
                        + ") on "
                        + arg.getFactoryMethodName().substring(0, methodPvt);
                clazz = ClassLoader.getSystemClassLoader()
                        .loadClass(arg.getFactoryMethodName().substring(0, methodPvt));
                method = clazz.getDeclaredMethod(arg.getFactoryMethodName().substring(methodPvt + 1), parmClass);
            }

            if (arg.isMultiple())
            {
                if (field.getType().getName().charAt(0) == '[')
                {
                    // baseClassName = field.getType().getName().substring(2,
                    // field.getType().getName().length() - 1);
                    final Class<?> baseClazz = ClassLoader.getSystemClassLoader().loadClass(baseClassName);
                    for (int r = 0; r < arg.size(); r++)
                    {
                        final Object[] array = newArray(target, field, baseClazz);
                        array[array.length - 1] = method.invoke(null, arg.getValue(r));
                    }
                } else
                {
                    for (int r = 0; r < arg.size(); r++)
                    {
                        final ArrayList<Object> arrayList = newList(target, field);
                        arrayList.add(method.invoke(null, arg.getValue(r)));
                    }
                }
            } else
            {
                field.set(target, method.invoke(null, arg.getValue()));
            }

        } catch (final InstantiationException e)
        {
            throw new ParseException("ClassNotFoundException " + errMsg, -1);
        } catch (final ClassNotFoundException e)
        {
            throw new ParseException("ClassNotFoundException " + errMsg, -1);
        } catch (final InvocationTargetException e)
        {
            throw new ParseException("InvocationTargetException " + errMsg, -1);
        } catch (final IllegalAccessException e)
        {
            throw new ParseException("IllegalAccessException " + errMsg, -1);
        } catch (final IllegalArgumentException e)
        {
            e.printStackTrace();
            throw new ParseException("IllegalArgumentException " + errMsg, -1);
        } catch (final SecurityException e)
        {
            throw new ParseException("SecurityException " + errMsg, -1);
        } catch (final NoSuchMethodException e)
        {
            throw new ParseException("NoSuchMethodException " + errMsg, -1);
        }

    }

    static private String factoryArgValue (final ICmdLineArg<?> arg)
    {
        if (arg == null)
            return null;
        if (!arg.isParsed())
            return null;
        return (String) arg.getValue();
    }

    static public IVariableAssigner getInstance ()
    {
        if (instance == null)
            instance = new VariableAssigner();
        return instance;
    }

    private static boolean isbyteArray (final Field field)
    {
        return "[B".equals(field.getType().getName());
    }

    private static boolean isByteArray (final Field field)
    {
        return "[Ljava.lang.Byte;".equals(field.getType().getName());
    }

    private static boolean isDateArray (final Field field)
    {
        return "[Ljava.util.Date;".equals(field.getType().getName());
    }

    static private boolean isEnum (final Field field)
    {
        return field.getType().isEnum();
    }

    private static boolean isFileArray (final Field field)
    {
        return "[Ljava.io.File;".equals(field.getType().getName());
    }

    private static boolean isfloatArray (final Field field)
    {
        return "[F".equals(field.getType().getName());
    }

    private static boolean isFloatArray (final Field field)
    {
        return "[Ljava.lang.Float;".equals(field.getType().getName());
    }

    private static boolean isintArray (final Field field)
    {
        return "[I".equals(field.getType().getName());
    }

    private static boolean isIntegerArray (final Field field)
    {
        return "[Ljava.lang.Integer;".equals(field.getType().getName());
    }

    static private boolean isList (final Field field)
    {
        final Class<?>[] interfaces = field.getType().getInterfaces();
        if (interfaces.length == 0)
            return false;
        for (final Class<?> iface : interfaces)
        {
            if (Collection.class.getName().equals(iface.getName()))
                return true;
        }
        return false;
    }

    private static boolean isLongArray (final Field field)
    {
        return "[Ljava.lang.Long;".equals(field.getType().getName());
    }

    private static boolean isPattern (final Field field)
    {
        return "java.util.regex.Pattern".equals(field.getType().getName());
    }

    private static boolean isPatternArray (final Field field)
    {
        return "[Ljava.util.regex.Pattern;".equals(field.getType().getName());
    }

    private static boolean isStringArray (final Field field)
    {
        return "[Ljava.lang.String;".equals(field.getType().getName());
    }

    /**
     * @param target
     * @param field
     * @param baseClazz
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    static private Object[] newArray (
            final Object target,
            final Field field,
            final Class<?> baseClazz)
            throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException
    {
        final Object[] oldinstance = (Object[]) field.get(target);
        int oldsize = 0;
        if (oldinstance != null)
            oldsize = oldinstance.length;

        final Object[] arrayinstance = (Object[]) Array.newInstance(baseClazz, oldsize + 1);
        int i = 0;
        if (oldinstance != null)
            for (; i < oldsize; i++)
                arrayinstance[i] = oldinstance[i];
        field.set(target, arrayinstance);
        return arrayinstance;
    }

    /**
     * @param group
     * @param target
     * @param field
     * @param baseClassName
     * @param factoryValueArg
     * @param reusable
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ParseException
     */
    static private Object newInstanceForGroup (
            final CmdLineCLA group,
            final Object target,
            final Field field,
            String _baseClassName,
            final ICmdLineArg<?> factoryValueArg,
            final boolean reusable)
            throws ClassNotFoundException,
            InstantiationException,
            IllegalAccessException,
            SecurityException,
            NoSuchMethodException,
            IllegalArgumentException,
            InvocationTargetException,
            ParseException
    {
        String baseClassName = _baseClassName;
        /*
         * Allow an instantiated instance variable to used rather than replaced.
         */
        if (reusable && field.get(target) != null)
            return field.get(target);

        Object groupInstance;
        if (baseClassName == null)
            if (group.getInstanceClass() != null)
                baseClassName = group.getInstanceClass();
            else
                baseClassName = field.getType().getName();

        Class<?> clazz;
        Method method;

        /*
         * Get the proper constructor and possible argument to create the
         * instance
         */
        if (group.getFactoryMethodName() != null)
        {
            final int methodPvt = group.getFactoryMethodName().lastIndexOf('.');
            final String factoryValue = factoryArgValue(factoryValueArg);
            if (methodPvt < 0)
            {
                clazz = ClassLoader.getSystemClassLoader().loadClass(baseClassName);
                if (factoryValue == null)
                    method = clazz.getDeclaredMethod(group.getFactoryMethodName());
                else
                    method = clazz.getDeclaredMethod(group.getFactoryMethodName(), String.class);
            } else
            {
                clazz = ClassLoader.getSystemClassLoader().loadClass(
                        group.getFactoryMethodName().substring(0, methodPvt));
                if (factoryValue == null)
                    method = clazz.getDeclaredMethod(group.getFactoryMethodName().substring(methodPvt + 1));
                else
                    method = clazz.getDeclaredMethod(group.getFactoryMethodName().substring(methodPvt + 1),
                            String.class);
            }
            if (factoryValue == null)
                groupInstance = method.invoke(clazz, new Object[] {});
            else
                groupInstance = method.invoke(clazz, new Object[]
                {
                        factoryValue
                });

        } else
        {
            clazz = ClassLoader.getSystemClassLoader().loadClass(baseClassName);
            groupInstance = clazz.newInstance();
        }
        return groupInstance;
    }

    /**
     * @param target
     * @param field
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    static private ArrayList<Object> newList (
            final Object target,
            final Field field)
            throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException
    {
        ArrayList<Object> oldinstance = (ArrayList<Object>) field.get(target);
        if (oldinstance == null)
        {
            oldinstance = new ArrayList<Object>();
            field.set(target, oldinstance);
        }
        return oldinstance;
    }

    static public IVariableAssigner setInstance (IVariableAssigner newInstance)
    {
        IVariableAssigner previousAssigner = instance;
        instance = newInstance;
        return previousAssigner;
    }

    public void assign (final ICmdLineArg<?> arg, final Object target) throws ParseException
    {
        if (arg == null)
            return;
        if (target == null)
            return;
        Field field = null;

        final String errMsg = "expected: public "
                + arg.getValue().getClass().getName()
                + " "
                + arg.getVariable()
                + " on "
                + target.getClass().getName();

        try
        {
            field = target.getClass().getField(arg.getVariable());
        } catch (final SecurityException e)
        {
            throw new ParseException("SecurityException " + errMsg,
                    -1);
        } catch (final NoSuchFieldException e)
        {
            throw new ParseException("NoSuchFieldException " + errMsg,
                    -1);
        }
        assign(field, arg, target);
    }

    public Object newGroupVariable (
            final CmdLineCLA group,
            final Object target,
            final ICmdLineArg<?> factoryValueArg)
            throws ParseException
    {
        try
        {
            if (group.getVariable() == null)
                return null;
            if (target == null)
                return null;

            final Field field = target.getClass().getField(group.getVariable());
            Object groupInstance = null;

            if (group.isMultiple())
            {
                String baseClassName;
                if (group.getInstanceClass() != null)
                {
                    if (field.getType().getName().charAt(0) == '[')
                    {
                        baseClassName = group.getInstanceClass();
                        final Class<?> baseClazz = ClassLoader.getSystemClassLoader().loadClass(baseClassName);
                        final Object[] array = newArray(target, field, baseClazz);
                        array[array.length - 1] = newInstanceForGroup(group,
                                target,
                                field,
                                baseClassName,
                                factoryValueArg,
                                false);
                        groupInstance = array[array.length - 1];
                    } else
                    {
                        final ArrayList<Object> arrayList = newList(target, field);
                        groupInstance = newInstanceForGroup(group, target, field, null, factoryValueArg, false);
                        arrayList.add(groupInstance);
                    }
                } else if (field.getType().getName().charAt(0) == '[')
                {
                    baseClassName = field.getType().getName().substring(2, field.getType().getName().length() - 1);
                    final Class<?> baseClazz = ClassLoader.getSystemClassLoader().loadClass(baseClassName);
                    final Object[] array = newArray(target, field, baseClazz);
                    array[array.length - 1] = newInstanceForGroup(group,
                            target,
                            field,
                            baseClassName,
                            factoryValueArg,
                            false);
                    groupInstance = array[array.length - 1];
                } else
                {
                    final ArrayList<Object> arrayList = newList(target, field);
                    groupInstance = newInstanceForGroup(group, target, field, null, factoryValueArg, false);
                    arrayList.add(groupInstance);
                }
            } else
            {
                groupInstance = newInstanceForGroup(group, target, field, null, factoryValueArg, true);
                field.set(target, groupInstance);
            }
            return groupInstance;
        } catch (final ClassNotFoundException e)
        {
            throw new ParseException("ClassNotFoundException (" + group.getVariable() + ")", -1);
        } catch (final InstantiationException e)
        {
            e.printStackTrace();
            throw new ParseException("InstantiationException (" + group.getVariable() + ")", -1);
        } catch (final IllegalAccessException e)
        {
            throw new ParseException("IllegalAccessException (" + group.getVariable() + ")", -1);
        } catch (final SecurityException e)
        {
            throw new ParseException("SecurityException (" + group.getVariable() + ")", -1);
        } catch (final NoSuchFieldException e)
        {
            throw new ParseException("NoSuchFieldException ("
                    + target.getClass().getSimpleName()
                    + " "
                    + group.getVariable()
                    + ")",
                    -1);
        } catch (final IllegalArgumentException e)
        {
            throw new ParseException("IllegalArgumentException (" + group.getVariable() + ")", -1);
        } catch (final NoSuchMethodException e)
        {
            throw new ParseException("NoSuchMethodException ("
                    + target.getClass().getSimpleName()
                    + " "
                    + group.getVariable()
                    + ")", -1);
        } catch (final InvocationTargetException e)
        {
            throw new ParseException("InvocationTargetException (" + group.getVariable() + ")", -1);
        }
    }
}
