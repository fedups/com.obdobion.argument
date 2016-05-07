package com.obdobion.argument.codegen;

/**
 * @author Chris DeGreef
 * 
 */
public class GeneratedElement
{
    private String              name;
    final private StringBuilder contents;

    public GeneratedElement(String _name)
    {
        this.name = _name;
        contents = new StringBuilder();
    }

    public StringBuilder getContents ()
    {
        return contents;
    }

    public String getName ()
    {
        return name;
    }
}
