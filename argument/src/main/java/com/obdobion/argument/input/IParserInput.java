package com.obdobion.argument.input;

import com.obdobion.argument.Token;

/**
 * @author Chris DeGreef
 * 
 */
public interface IParserInput {

    public Token[] parseTokens ();

    public String substring (int inclusiveStart, int exclusiveEnd);
}
