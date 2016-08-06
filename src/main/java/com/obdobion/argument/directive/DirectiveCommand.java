package com.obdobion.argument.directive;

import java.io.IOException;
import java.text.ParseException;

import com.obdobion.argument.input.Token;

/**
 * @author Chris DeGreef
 * 
 */
public abstract class DirectiveCommand
{
    final protected String data;

    public DirectiveCommand(String _data)
    {
        this.data = _data;
    }

    abstract Token replaceToken (Token[] tokens, int replacingFromTokenIndex, int replaceToTokenIndex)
            throws ParseException, IOException;
}
