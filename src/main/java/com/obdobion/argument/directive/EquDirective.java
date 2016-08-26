package com.obdobion.argument.directive;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obdobion.algebrain.Equ;
import com.obdobion.argument.input.Token;

/**
 * This class parses phrases that will be used to run an equation. That result
 * is returned as a literal token to be inserted into the command line at the
 * same position as this directive.
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class EquDirective extends DirectiveCommand
{
    private final static Logger logger = LoggerFactory.getLogger(EquDirective.class.getName());

    /**
     * <p>
     * Constructor for DateDirective.
     * </p>
     *
     * @param _data
     *            a {@link java.lang.String} object.
     */
    public EquDirective(final String _data)
    {
        super(_data);
    }

    /** {@inheritDoc} */
    @Override
    public Token replaceToken(final Token[] tokens, final int replacingFromTokenIndex, final int replaceToTokenIndex)
            throws ParseException, IOException
    {
        final Equ equ = Equ.getInstance(true);
        String resultAsATokenValue = null;
        try
        {
            Object equResult;
            equResult = equ.evaluate(data);
            if (equResult instanceof Calendar)
                resultAsATokenValue = new SimpleDateFormat("yyyy/MM/dd@HH:mm:ss.SSS")
                        .format(((Calendar) equResult).getTime());
            else
                resultAsATokenValue = equResult.toString();

        } catch (final Exception e)
        {
            logger.error("{}", e.getMessage(), e);
            throw new ParseException(e.getMessage(), 0);
        }

        return new Token(tokens[replacingFromTokenIndex].charCommand(),
                resultAsATokenValue,
                tokens[replacingFromTokenIndex].getInputStartX(),
                tokens[replaceToTokenIndex].getInputEndX(),
                true);
    }
}
