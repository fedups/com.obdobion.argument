


*   [Overview](#overview)
    *   [Philosophy](#philosophy)
*   [Anatomy of a Command Line interface](#)
*   [Full example of a Java App](#example)
	Supports advanced types 
		date/time, formatters, and modification
		equation processing
*   [How is argument packaged into your application](#)
*   [Automated multi-level help feature](#)
*   [Embedded arguments](#)
*   [](#)

<h2 id="example">Java Example</h2>

Transform your code to use command line arguments with an annotation (@Arg).  
Lets create the proverbial "hello world" program.

    public class MyClass
    {
        private String message = "hello world";
        
        public static void main(final String[] args) {
            System.out.println(message);
        }
    }
    
Run this program from the command line

    > java MyClass

		hello world
		
Modify the program to allow the "hello world" message to be anything you want at run time.  
This is the same class with the necessary changes.

    public class MyClass
    {
        @Arg (defaultValues = "hello world")
        private String message;
        
        public static void main(final String[] args) {
            CmdLine.load(this, args);
            System.out.println(message);
        }
    }

And now it can be run like this with an argument on the command line.

    > java MyClass --message "It is a beautiful day!"
    
    It is a beautiful day!

