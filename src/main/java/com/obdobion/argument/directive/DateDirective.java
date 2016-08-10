package com.obdobion.argument.directive;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.obdobion.argument.input.Token;
import com.obdobion.argument.type.ClaType;
import com.obdobion.argument.type.DateCLA;
import com.obdobion.calendar.CalendarFactory;

/**
 * This class parses phrases that will be used to compute a date. That date is
 * returned as a literal token to be inserted into the command line at the same
 * position as this directive.
 * <p>
 * See the CalendarFactory for details on how to specify a date modification.
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class DateDirective extends DirectiveCommand
{
    int datePivot;

    /**
     * <p>Constructor for DateDirective.</p>
     *
     * @param _data a {@link java.lang.String} object.
     */
    public DateDirective(
            final String _data)
    {
        super(_data);
    }

    private Calendar acquireStartDate()
            throws ParseException,
            IOException
    {
        final Calendar cal = Calendar.getInstance();

        datePivot = data.indexOf(' ');

        /*
         * today or now
         */
        if (data.charAt(0) == 'n' || data.charAt(0) == 't')
            return cal;
        /*
         * Stop before the adjustments, if any, otherwise take the whole data
         * area.
         */
        final DateCLA dateHelper = (DateCLA) ClaType.DATE.argumentInstance('-', '-', "HELPER");
        if (datePivot > 0)
            cal.setTime(dateHelper.convert(data.substring(0, datePivot)));
        else
            cal.setTime(dateHelper.convert(data));
        return cal;
    }

    /** {@inheritDoc} */
    @Override
    public Token replaceToken(final Token[] tokens, final int replacingFromTokenIndex, final int replaceToTokenIndex)
            throws ParseException,
            IOException
    {
        final Calendar startingDate = acquireStartDate();

        CalendarFactory.modify(startingDate, datePivot > 0
                ? data.substring(datePivot + 1)
                : "");

        return new Token(tokens[replacingFromTokenIndex].charCommand(),
                replaceTokenDateFormat().format(startingDate.getTime()),
                tokens[replacingFromTokenIndex].getInputStartX(),
                tokens[replaceToTokenIndex].getInputEndX(),
                true);
    }

    /**
     * <p>replaceTokenDateFormat.</p>
     *
     * @return a {@link java.text.SimpleDateFormat} object.
     */
    protected SimpleDateFormat replaceTokenDateFormat()
    {
        return new SimpleDateFormat("yyyy/MM/dd");
    }
}
