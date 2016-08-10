package com.obdobion.argument.directive;

import java.io.IOException;
import java.text.ParseException;

import com.obdobion.argument.input.Token;

/**
 * <p>Abstract DirectiveCommand class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public abstract class DirectiveCommand
{
    final protected String data;

    /**
     * <p>Constructor for DirectiveCommand.</p>
     *
     * @param _data a {@link java.lang.String} object.
     */
    public DirectiveCommand(String _data)
    {
        this.data = _data;
    }

    abstract Token replaceToken (Token[] tokens, int replacingFromTokenIndex, int replaceToTokenIndex)
            throws ParseException, IOException;
}
