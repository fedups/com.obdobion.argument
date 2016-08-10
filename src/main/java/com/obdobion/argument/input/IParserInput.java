package com.obdobion.argument.input;

/**
 * <p>IParserInput interface.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public interface IParserInput {

    /**
     * <p>parseTokens.</p>
     *
     * @return an array of {@link com.obdobion.argument.input.Token} objects.
     */
    public Token[] parseTokens ();

    /**
     * <p>substring.</p>
     *
     * @param inclusiveStart a int.
     * @param exclusiveEnd a int.
     * @return a {@link java.lang.String} object.
     */
    public String substring (int inclusiveStart, int exclusiveEnd);
}
