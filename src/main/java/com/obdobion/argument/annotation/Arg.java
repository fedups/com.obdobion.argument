package com.obdobion.argument.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * "argument" is a full featured command line argument processor for Java.
 * Essentially, it allows your program to get run-time direction from the user
 * as they execute it on the command line.
 *
 * (Example: <code>yourProgram --tail</code>)
 *
 * This assumes that you have written a program called <code>yourProgram</code>
 * and that it will need to do something special if the user also enters the
 * <code>--tail</code> argument. "argument" is a library that makes handling
 * this type of command line interaction very easy to implement. All parsers are
 * equipped with a few basic arguments; <code>--usage</code>, <code>-?</code>,
 * <code>-!</code>, and <code>@(filename)</code>.
 * <p>
 * In the <code>main</code> method in your program, or somewhere similar,
 *
 * <pre>
 public static void main (final String[] args) {
 * </pre>
 *
 * you would need to create an instance of the argument processor
 *
 * <pre>
 * ICmdLine cmdline = new CmdLine();
 * </pre>
 *
 * and an instance of the specific parser (in this case CommandLineParser)
 *
 * <pre>
 * IParserInput userInput = CommandLineParser.getInstance(commandLineParser.getCommandPrefix(), args);
 * </pre>
 *
 * and then process the command line arguments into the instance variables of
 * your program.
 *
 * <pre>
 * MyProgram instance = new MyProgram();
 * cmdline.parse(userInput, instance));
 * </pre>
 *
 * Decorate your instance variables with this &#64;Arg annotation to tell the
 * parser what to do. Maybe something like this (to follow on the
 * <code>--tail</code> example) in the <code>MyProgram</code> class.
 *
 * <pre>
 * &#64;Arg
 * private boolean tail;
 * </pre>
 *
 * You can decorate instance variables of many types (including all primitives)
 * and also those of any POJO as long as the POJO is decorated with the @Arg
 * annotation. Arrays and java.util.List of these same types are also valid. For
 * instance, you may want to allow the user of your program to specify multiple
 * files for processing
 *
 * <pre>
 * &#64;Arg
 * private File[] inputFiles;
 * </pre>
 *
 * Then the user could enter this on the command line and the
 * <code>inputFiles</code> instance variable would contain the File objects.
 *
 * <pre>
 * &gt;myProgram --inputFiles oneFile.txt anotherFile.txt
 * </pre>
 *
 * @see Boolean
 * @see Byte
 * @see Character
 * @see String
 * @see Integer
 * @see Long
 * @see Float
 * @see Double
 * @see java.io.File File
 * @see java.util.Date Date
 * @see java.util.regex.Pattern Pattern
 * @see com.obdobion.argument.type.WildFiles WildFiles
 * @see com.obdobion.algebrain.Equ Equ
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(Args.class)
public @interface Arg {

    /**
     * Indicates that the user can specify the argument on the command-line
     * using the
     * {@link com.obdobion.argument.type.AbstractCLA#createCamelCapVersionOfKeyword
     * Camel Caps} version of the long name.
     */
    boolean allowCamelCaps() default false;

    /**
     * Indicates that the user can specify the argument on the command-line with
     * misspellings as long as it evaluates to the
     * {@link com.obdobion.argument.type.AbstractCLA#createMetaphoneVersionOfKeyword
     * Metaphone} equivalent of the long name.
     */
    boolean allowMetaphone() default false;

    /**
     * Indicates that all values associated with this argument are case
     * sensitive. The default for this is false meaning that all alpha
     * characters are converted to lowercase. If upper case is needed then make
     * this true.
     */
    boolean caseSensitive() default false;

    /**
     * Indicates the default value(s) for this argument. All entries are of the
     * String type and will be internally converted to the proper type when
     * needed.
     */
    String[] defaultValues() default {};

    /**
     * Indicates the arguments from a pojo that should be excluded from the
     * parser.
     */
    String[] excludeArgs() default {};

    /**
     * Indicates the argument from an embedded parser that is to be used as a
     * String parameter to the factory method. If this is not specified then the
     * factory method will be called without an argument. Use the argument name
     * (like <code>"--type"</code>) for this value.
     */
    String factoryArgName() default "";

    /**
     * Indicates the factory method to be used when creating an instance of this
     * embedded parser. It should be specified as
     * <code>package.class.method</code>. If an inner class is involved then use
     * <code>package.class$innerClass.method</code>
     */
    String factoryMethod() default "";

    /**
     * Indicates the date format for types of Date. User entry for this argument
     * will be parsed with this date. Use the {@link java.text.SimpleDateFormat
     * SimpleDateFormat} parameters.
     */
    String format() default "";

    /**
     * Indicates the message that will be included in the usage for this
     * argument.
     */
    String help() default "";

    /**
     * Indicates an enum, implying a list of values that will be allowed for
     * this argument. They will be internally converted to a list of Strings for
     * a criteria.
     */
    String inEnum() default "";

    /**
     * Indicates a list of values that will be allowed for this argument. They
     * are to be entered as an array of Strings that will be internally
     * converted to the proper type when needed.
     */
    String[] inList() default {};

    /**
     * Indicates the class that will be used to create an object for this
     * argument. This is only needed when Argument can not figure it out by
     * itself. That means, hardly ever.
     */
    String instanceClass() default "";

    /**
     * Indicates the single word, long name of this argument. The command-line
     * use of this is a double dash (--) followed immediately by this word, or
     * more precisely, the unique beginning of this word relative to all other
     * arguments at this level of the parser.
     * <p>
     * The default for the long name is the actual name of the variable. In the
     * example below it would not be necessary to specify the long name for the
     * confirm variable since that is the default.
     * <p>
     * If you prefer not to have a long name at all then set this value to be an
     * empty string.
     * <p>
     * Examples:
     *
     * <pre>
     * &#64;Arg(longName = 'confirm')
     * private boolean confirm;
     *
     * &#64;Arg(longName = 'file')
     * private File inputFile;
     * </pre>
     *
     * With the user then running the program as...
     *
     * <pre>
     * &gt;myProgram --file aFile.txt --confirm
     * </pre>
     */
    String longName() default "";

    /**
     * Indicates a verification pattern to be run against the user input before
     * it will be accepted. It must match the entire input. Use
     * {@link java.util.regex.Pattern Pattern} to write the value for this
     * parameter. This pattern will be applied to the <code>toString()</code>
     * result on the user input. This typically only makes sense on a type of
     * String.
     */
    String matches() default "";

    /**
     * Indicates the maximum allowed number of values that will be accepted for
     * this argument. Useful when the type of the variable is an array or a
     * List. The default for the parameter is the max integer.
     */
    int multimax() default 0;

    /**
     * Indicates the minimum allowed number of values that will be accepted for
     * this argument. Useful when the type of the variable is an array or a
     * List. The default for the parameter is 0.
     */
    int multimin() default 0;

    /**
     * Indicates that a name is not allowed for this argument. The user input is
     * for this parameter is determined based on the order of the input. A way
     * to think about this is that all of the named arguments are extracted from
     * the user's input, and what is left is then assigned to the positional
     * parameters. A common use for this is file name input to your program.
     */
    boolean positional() default false;

    /**
     * Indicates a criteria to be applied to the user's input. This is an array
     * of two values; the first one being the minimum and the second the
     * maximum, they are inclusive. The second value is optional meaning that
     * there is no maximum associated. A typical use might to allow only
     * positive values for an integer; range = {"1"}.
     */
    String[] range() default {};

    /**
     * Indicates that this argument is required - the user must provide the
     * argument. A not-so-obvious use for this is the create a boolean called
     * confirm and apply the annotation <code>@Arg(required=true}</code>. This
     * will automatically require the user to enter <code>--confirm</code> or
     * the parser will throw an exception.
     */
    boolean required() default false;

    /**
     * Indicates the single character, short name of this argument. The
     * command-line use of this is a single dash (-) followed immediately by
     * this character. This needs to be unique for all arguments at the same
     * level of the parser.
     * <p>
     * For a boolean type of argument there would be no supplied value after the
     * <code>-c</code>. For any other type of instance variable the value would
     * follow.
     * <p>
     * Examples:
     *
     * <pre>
     * &#64;Arg(shortName = 'c')
     * private boolean confirm;
     *
     * &#64;Arg(shortName = 'f')
     * private File inputFile;
     * </pre>
     *
     * With the user then running the program as...
     *
     * <pre>
     * &gt;myProgram -f aFile.txt -c
     * </pre>
     */
    char shortName() default ' ';

    /**
     * Indicates a non-used integer value that will be kept with the argument in
     * internal memory. There is currently no use for this value except the GUI.
     */
    int uniqueId() default 0;

    /**
     * Indicates that a variable in the complex class of the previous @Arg
     * annotation (without the variable parameter) is to be annotated. This is
     * needed when the embedded complex class source is not to be directly
     * modified by annotations.
     */
    String variable() default "";
}
