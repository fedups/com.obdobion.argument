@Arg annotation Syntax
======================

<ul id="ProjectSubmenu">
    <li><a href="/projects/markdown/" title="Markdown Project Page">Main</a></li>
    <li><a href="/projects/markdown/basics" title="Markdown Basics">Basics</a></li>
    <li><a class="selected" title="Markdown Syntax Documentation">Syntax</a></li>
    <li><a href="/projects/markdown/license" title="Pricing and License Information">License</a></li>
    <li><a href="/projects/markdown/dingus" title="Online Markdown Web Form">Dingus</a></li>
</ul>


The @Arg annotation is used to annotate class instance variables (non-static).  A variable that is annotated in this
way will be made available on the command-line as a run-time argument.  The simplest @Arg annotation is with no
parameters and this provides for a command line argument that is the same as the variable name.  See the [basics] page 
for an example of this.

Parameters to the @Arg annotation.

*   [Overview](#overview)
    *   [Philosophy](#philosophy)
*   [User Interface](#ui)
    *   [allowCamelCaps](#allowCamelCaps)
    *   [allowMetaphone](#allowMetaphone)
    *   [defaultValues](#defaultValues)
    *   [help](#help)
    *   [longName](#longName)
    *   [shortName](#shortName)
    *   [positional](#positional)
*   [Validation](#validation)
    *   [caseSensitive](#caseSensitive)
    *   [format](#format)
    *   [inList](#inList)
    *   [inEnum](#inEnum)
    *   [matches](#matches)
    *   [multiMax](#multiMax)
    *   [multiMin](#multiMin)
    *   [range](#range)
    *   [required](#required)
*   [Complex Object Creation](#subparsers)
    *   [factoryArgName](#factoryArgName)
    *   [factoryMethod](#factoryMethod)
    *   [instanceClass](#instanceClass)
    *   [excludeArgs](#excludeArgs)
    *   [variable](#variable)

<h2 id="overview">Overview</h2>

<h3 id="philosophy">Philosophy</h3>

<h2 id="ui">User Interface</h2>

<h3 id="allowCamelCaps">allowCamelCaps</h3>

Indicates that the user can specify the argument on the command-line using the 
Camel Caps version of the long name.

<h3 id="allowMetaphone">allowMetaphone</h3>

Indicates that the user can specify the argument on the command-line with misspellings as 
long as it evaluates to the Metaphone equivalent of the long name.

<h3 id="defaultValues">defaultValues</h3>

Indicates the default value(s) for this argument. All entries are of the String 
type and will be internally converted to the proper type when needed.

<h3 id="help">help</h3>

Indicates the message that will be included in the usage for this argument.

<h3 id="longName">longName</h3>

Indicates the single word, long name of this argument. The command-line use of this is a 
double dash (--) followed immediately by this word, or more precisely, the unique beginning 
of this word relative to all other arguments at this level of the parser. 

The default for the long name is the actual name of the variable. In the example below it 
would not be necessary to specify the long name for the confirm variable since that is the default. 

If you prefer not to have a long name at all then set this value to be an empty string. 

Examples: 
    @Arg(longName = 'confirm')
    private boolean confirm;
    
    @Arg(longName = 'file')
    private File inputFile;
 
With the user then running the program as...  >myProgram --file aFile.txt --confirm
 
<h3 id="shortName">shortName</h3>

Indicates the single character, short name of this argument. The command-line use of 
this is a single dash (-) followed immediately by this character. This needs to be unique 
for all arguments at the same level of the parser. 

For a boolean type of argument there would be no supplied value after the -c. For any 
other type of instance variable the value would follow. 

Examples: 
    @Arg(shortName = 'c')
    private boolean confirm;
    
    @Arg(shortName = 'f')
    private File inputFile;
 
With the user then running the program as...  >myProgram -f aFile.txt -c
 
<h3 id="positional">positional</h3>

Indicates that a name is not allowed for this argument. The user input is for this parameter 
is determined based on the order of the input. A way to think about this is that all of the 
named arguments are extracted from the user's input, and what is left is then assigned to 
the positional parameters. A common use for this is file name input to your program.

<h2 id="validation">Validation</h2>

<h3 id="caseSensitive">caseSensitive</h3>

Indicates that all values associated with this argument are case sensitive. The default 
for this is false meaning that all alpha characters are converted to lowercase. 
If upper case is needed then make this true.

<h3 id="format">format</h3>

Indicates the date format for types of Date. User entry for this argument will be parsed 
with this date. Use the SimpleDateFormat parameters.

<h3 id="inList">inList</h3>

Indicates a list of values that will be allowed for this argument. They are to be entered 
as an array of Strings that will be internally converted to the proper type when needed.

<h3 id="inEnum">inEnum</h3>

Indicates an enum, implying a list of values that will be allowed for this argument. 
They will be internally converted to a list of Strings for a criteria.

<h3 id="matches">matches</h3>

Indicates a verification pattern to be run against the user input before it will be accepted. 
It must match the entire input. Use Pattern to write the value for this parameter. 
This pattern will be applied to the toString() result on the user input. This typically only 
makes sense on a type of String.

<h3 id="multiMax">multiMax</h3>

Indicates the maximum allowed number of values that will be accepted for this argument. 
Useful when the type of the variable is an array or a List. The default for the parameter 
is the max integer.

<h3 id="multiMin">multiMin</h3>

Indicates the minimum allowed number of values that will be accepted for this argument. 
Useful when the type of the variable is an array or a List. The default for the parameter 
is 0.

<h3 id="range">range</h3>

Indicates a criteria to be applied to the user's input. This is an array of two values; 
the first one being the minimum and the second the maximum, they are inclusive. The second 
value is optional meaning that there is no maximum associated. A typical use might to allow 
only positive values for an integer; range = "1".

<h3 id="required">required</h3>

Indicates that this argument is required - the user must provide the argument. A not-so-obvious 
use for this is the create a boolean called confirm and apply the annotation @Arg(required=true}. 
This will automatically require the user to enter --confirm or the parser will throw an exception.

<h2 id="subparsers">Complex Object Creation</h2>

<h3 id="factoryArgName">factoryArgName</h3>

Indicates the argument from an embedded parser that is to be used as a String parameter 
to the factory method. If this is not specified then the factory method will be called 
without an argument. Use the argument variable name (like "type") for this value.

<h3 id="factoryMethod">factoryMethod</h3>

Indicates the factory method to be used when creating an instance of this embedded parser. 
It should be specified as package.class.method. If an inner class is involved then use 
package.class$innerClass.method

<h3 id="instanceClass">instanceClass</h3>

Indicates the class that will be used to create an object for this argument. This is needed 
when an abstract class, maybe even Object is used as the variable type. This parameter 
allows the specification of the actual instance class that should be used to create the 
instance. An example: 

    @Arg(instanceClass = "com.obdobion.argument.VariableTest$MyGroup")
    private Object testObjectGroup;

<h3 id="excludeArgs">excludeArgs</h3>

Indicates the arguments from a pojo that should be excluded from the parser.

<h3 id="variable">variable</h3>

Indicates that a variable in the complex class of the previous @Arg annotation
(without the variable parameter) is to be annotated. This is needed when the embedded 
complex class source is not to be directly modified by annotations.

