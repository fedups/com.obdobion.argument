package com.obdobion.argument.type;

/**
 * This is one of the more interesting argument classes. It is the case that the
 * user is providing the factory method to create the instance but the instance
 * is not a subparser (no &#64;Arg annotations) with in it. So there is no
 * parameter that can be associated with the factory method. Instead the "this"
 * constant is used as the arg name. And this is a String (the value associated
 * with this argument). That string will be passed as an argument to the factory
 * method.
 *
 * <pre>
 *
 * &#64;Arg (factoryMethod="create", factoryArgName=CLAFactory.SELF_REFERENCING_ARGNAME)
 * private MyClassWithNoArgs myInstance;
 *
 * --myInstance NoParensHereBecauseNotASubparser
 *
 * (result)
 *
 * MyClassWithNoArgs.create("NoParensHereBecauseNotASubparser");
 *
 * </pre>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class PojoCLA extends StringCLA
{
    /** {@inheritDoc} */
    @Override
    public boolean supportsFactoryArgName()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsFactoryMethod()
    {
        return true;
    }
}
