package com.obdobion.argument.input;

/**
 * @author Chris DeGreef
 * 
 */
public interface IParserInput {

    public Token[] parseTokens ();

    public String substring (int inclusiveStart, int exclusiveEnd);
}
