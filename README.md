# argument
This is a full featured command line argument processor for Java. 
Essentially, it allows your program to get run-time direction from
the user as they execute it on the command line. 

Example: yourProgram --tail

This assumes that you have written a program called "yourProgram" and 
that it will need to do something special if the users also enters the 
--tail argument. "argument" is a library that makes handling this type 
of command line interaction very easy to implement.

Ask yourself the question: What if I had the ability to run my program with a command line option?  Maybe something as simple as adding a "confirm" option on the command line so that your wonderful, potentially destructive, program will not be run by accident, causing harm to the user's computer.

What if that can be added with an few lines of code.  Would you do it?  Would you be willing to add this to your class?  

    @Arg(required=true) 
    boolean confirm;

    static final public void main(String[] args) {
        CmdLine.load(this, args);
    }

Then the user of your program would be required to run it with "--confirm" on the command line or else get an exception.

Sounds simple to write for yourself; but, what if you also got these features? 

* Everything is done through the simple @Arg annotation.
* Many different data types that can be arguments, not just booleans.  This includes String, Date,  File, Enum, Integer, Long, Float, Double,  Byte, Characterâ?¦
* All primitive types too.
* Strongly typed Arrays and Lists (Generics).
* And advanced types like Pattern, Calendar, WildFiles, and Equations.
* An extensive calendar / date entry on the command-line that allows for date arithmetic.
* Extensive criteria checking like ranges, lists, enum values and regular expression.
* Control the number of entries allowed / required for arrays and lists.
* Single character (-) and word argument (--) specification on the command line.  
* CamelCaps and Metaphone
* Positional arguments
* Case-sensitive option for each argument.
* Factory creation possibilities for each argument.  
* Multiple arguments that update the same variable.
* Annotate classes you don't own
* Automatic help display for the command-line user.
* Include files (@) on the command line.
* Sub-parsers allowing for command-lines like "sort --orderBy(f1, asc)(f2, desc)"

I think these features almost beg you to write a program just to use them!
