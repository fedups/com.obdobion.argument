package com.obdobion.argument;

import java.text.ParseException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.obdobion.argument.annotation.Arg;

public class PolymorphismTest
{
    static abstract public class Animal2 implements IAnimal2
    {
        static public IAnimal2 create(final String sound)
        {
            if ("meow".equals(sound))
                return new Cat2();
            if ("woof".equals(sound))
                return new Dog2();
            return new CatDog2();
        }

        @Arg(required = true)
        private String sound;
    }

    static public class Cat1 implements IAnimal
    {}

    static public class Cat2 extends Animal2
    {}

    static public class CatDog1 implements IAnimal
    {}

    static public class CatDog2 extends Animal2
    {}

    public static class Context1
    {
        @Arg
        private List<Object>      onlyObjects;

        @Arg(instanceClass = "com.obdobion.argument.PolymorphismTest$SpecificInstance")
        private List<Object>      specificInstances;

        @Arg(longName = "type2", instanceClass = "com.obdobion.argument.PolymorphismTest$SpecificInstance2")
        @Arg(longName = "type1", instanceClass = "com.obdobion.argument.PolymorphismTest$SpecificInstance")
        private List<Object>      twoTypes;

        @Arg(longName = "si2", instanceClass = "com.obdobion.argument.PolymorphismTest$SpecificInstance2")
        @Arg(longName = "si1", instanceClass = "com.obdobion.argument.PolymorphismTest$SpecificInstance")
        private SpecificInstance2 singleInstance1;

        @Arg(longName = "si4", instanceClass = "com.obdobion.argument.PolymorphismTest$SpecificInstance")
        @Arg(longName = "si3", instanceClass = "com.obdobion.argument.PolymorphismTest$SpecificInstance2")
        private SpecificInstance2 singleInstance2;

        @Arg(longName = "si6", instanceClass = "com.obdobion.argument.PolymorphismTest$SpecificInstance2")
        @Arg(longName = "si5", instanceClass = "com.obdobion.argument.PolymorphismTest$SpecificInstance2")
        private SpecificInstance2 singleInstance3;

        @Arg(
                factoryMethod = "create",
                factoryArgName = Arg.SELF_REFERENCING_ARGNAME)
        private List<IAnimal>     animals1;

        @Arg(
                factoryMethod = "create",
                factoryArgName = "sound",
                instanceClass = "com.obdobion.argument.PolymorphismTest$Animal2")
        private List<IAnimal2>    animals2;

        @Arg(
                factoryMethod = "create",
                factoryArgName = "sound")
        private List<Animal2>     animals3;
    }

    static public class Dog1 implements IAnimal
    {}

    static public class Dog2 extends Animal2
    {}

    static public interface IAnimal
    {
        static public IAnimal create(final String sound)
        {
            if ("meow".equals(sound))
                return new Cat1();
            if ("woof".equals(sound))
                return new Dog1();
            return new CatDog1();
        }
    }

    static public interface IAnimal2
    {
        static public IAnimal2 create(final String sound)
        {
            if ("meow".equals(sound))
                return new Cat2();
            if ("woof".equals(sound))
                return new Dog2();
            return new CatDog2();
        }
    }

    public static class SpecificInstance
    {
        @Arg
        private int typeCode;
    }

    public static class SpecificInstance2
    {
        @Arg
        private int typeCode;
    }

    /**
     * <p>
     * animalFactory1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void animalFactory1() throws Exception
    {
        final Context1 context1 = new Context1();
        CmdLine.load(context1, " --animals1 woof, meow WHAT! ");
        Assert.assertEquals(3, context1.animals1.size());
        Assert.assertTrue(context1.animals1.get(0) instanceof Dog1);
        Assert.assertTrue(context1.animals1.get(1) instanceof Cat1);
        Assert.assertTrue(context1.animals1.get(2) instanceof CatDog1);
    }

    /**
     * <p>
     * animalFactory2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void animalFactory2() throws Exception
    {
        final Context1 context1 = new Context1();
        CmdLine.load(context1, " --animals2 (--sound woof) (--sound meow) (--sound WHAT!) ");
        Assert.assertEquals(3, context1.animals2.size());
        Assert.assertTrue(context1.animals2.get(0) instanceof Dog2);
        Assert.assertTrue(context1.animals2.get(1) instanceof Cat2);
        Assert.assertTrue(context1.animals2.get(2) instanceof CatDog2);
    }

    /**
     * <p>
     * animalFactory3.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void animalFactory3() throws Exception
    {
        final Context1 context1 = new Context1();
        CmdLine.load(context1, " --animals3 (--sound woof) (--sound meow) (--sound WHAT!) ");
        Assert.assertEquals(3, context1.animals3.size());
        Assert.assertTrue(context1.animals3.get(0) instanceof Dog2);
        Assert.assertTrue(context1.animals3.get(1) instanceof Cat2);
        Assert.assertTrue(context1.animals3.get(2) instanceof CatDog2);
    }

    /**
     * <p>
     * errorForImproperAsssignment1.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void errorForImproperAsssignment1() throws Exception
    {
        final Context1 context1 = new Context1();
        try
        {
            CmdLine.load(context1, " --si2(--typeCode 2)  --si1(--typeCode 1)");
            Assert.fail("expected exception");
        } catch (final ParseException e)
        {
            /*
             * This error occurs if a subsequent @Arg after the first has a type
             * that can not be stored in the variable, regardless of which order
             * the user specifies them on the command line.
             */
            Assert.assertEquals(
                    "Error in instance creation for \"--si1\", com.obdobion.argument.PolymorphismTest$SpecificInstance2 can not be reassigned to com.obdobion.argument.PolymorphismTest$SpecificInstance",
                    e.getMessage());
        }
    }

    /**
     * <p>
     * errorForImproperAsssignment2.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void errorForImproperAsssignment2() throws Exception
    {
        final Context1 context1 = new Context1();
        try
        {
            CmdLine.load(context1, " --si4(--typeCode 2)  --si3(--typeCode 1)");
            Assert.fail("expected exception");
        } catch (final ParseException e)
        {
            /*
             * This error occurs if the first @Arg has a type that can not be
             * stored in the variable, regardless of which order the user
             * specifies them on the command line.
             */
            Assert.assertEquals(
                    "IllegalArgumentException (singleInstance2)",
                    e.getMessage());
        }
    }

    /**
     * <p>
     * onlyObjects.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void onlyObjects() throws Exception
    {
        final Context1 context1 = new Context1();
        CmdLine.load(context1, "--onlyObjects ()()");
        Assert.assertEquals(2, context1.onlyObjects.size());
        Assert.assertTrue(context1.onlyObjects.get(0) instanceof Object);
        Assert.assertTrue(context1.onlyObjects.get(1) instanceof Object);
    }

    /**
     * <p>
     * onlySpecificInstances.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void onlySpecificInstances() throws Exception
    {
        final Context1 context1 = new Context1();
        CmdLine.load(context1, "--specificInstances(--typeCode 1)(--typeCode 2)");
        Assert.assertEquals(2, context1.specificInstances.size());
        Assert.assertTrue(context1.specificInstances.get(0) instanceof SpecificInstance);
        Assert.assertTrue(context1.specificInstances.get(1) instanceof SpecificInstance);
    }

    /**
     * <p>
     * twoTypesInAList.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void twoTypesInAList() throws Exception
    {
        final Context1 context1 = new Context1();
        CmdLine.load(context1, "--type1(--typeCode 1) --type2(--typeCode 2)");
        Assert.assertEquals(2, context1.twoTypes.size());
        Assert.assertTrue(context1.twoTypes.get(1) instanceof SpecificInstance);
        Assert.assertTrue(context1.twoTypes.get(0) instanceof SpecificInstance2);
    }

    /**
     * <p>
     * errorForImproperAsssignment3.
     * </p>
     *
     * @throws java.lang.Exception if any.
     * @since 4.3.1
     */
    @Test
    public void weirdImproperAsssignment3() throws Exception
    {
        final Context1 context1 = new Context1();
        CmdLine.load(context1, " --si6(--typeCode 2)  --si5(--typeCode 1)");
    }

}
