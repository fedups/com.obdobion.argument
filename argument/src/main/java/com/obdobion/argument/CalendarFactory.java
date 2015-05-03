package com.obdobion.argument;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This class parses phrases that will be used to compute a date. That date will
 * be returned as a Calendar.
 * <p>
 * The first token can be a special date function that is relative to the
 * current date.
 * <p>
 * <ul>
 * <li>Today or Now
 * <li>specific date in a format that is supported by the Date argument.
 * </ul>
 * <p>
 * The following parameters are applied in order to that date. Each one has this
 * structure. No spaces are allowed within a parameter.
 * <ol>
 * <li>+ or - or @: the direction of effect on the date (@ means absolute or at)
 * <li>>or < or >= or <=: next, prev, next or this, prev or this.
 * <li>### : the quantity of the effect; E and B can be used in conjunction with
 * the direction
 * <li>unit : the unit of the effect (case is not important)
 * <ul>
 * <li>(T)ime (@BTime and @ETime are valid giving 0:0:0.0 and 23:59:59.999
 * respectively)
 * <li>(Y)ear
 * <li>(M)onth
 * <li>(W)eekOfYear (B and E work on the current week)
 * <li>Week(O)fMonth (B and E work on the current week)
 * <li>(D)ay
 * <li>D(a)yOfWeek (B and E work on the current week, @ is current week, - is
 * previous week, and + is next week. Sunday is the first day of the week.)
 * <li>(H)our
 * <li>M(i)nute
 * <li>(S)econd
 * <li>Mi(l)lisecond or ms
 * </ul>
 * </ol>
 * <h4>examples</h4>
 * <h5>The beginning of today</h5>
 * _dateTime(now @bday) <br>
 * <h5>The beginning of yesterday</h5>
 * _dateTime(now -1day @bday) <br>
 * <h5>The end of yesterday</h5>
 * _dateTime(now -1day @eday) <br>
 * <h5>Monday of this week</h5>
 * _dateTime(now @2dayOfWeek) <br>
 * <h5>Monday of the week that contained 2010/04/09</h5>
 * _dateTime(2010/04/09 @2dayOfWeek) <br>
 * <h5>Same day and time last week</h5>
 * _dateTime(now -1week) <br>
 * <h5>Same day last week but at the end of that day.</h5>
 * _dateTime(now -1week @eday) <br>
 * <h5>The first day of this month</h5>
 * _dateTime(now @1d) <br>
 * <h5>The last day of last month</h5>
 * _dateTime(now -1month @emonth) <br>
 * This could be done in different ways (like all of the others too), <br>
 * _dateTime(now @1d -1d @ed) <br>
 * 
 * @author Chris DeGreef
 * 
 */
public class CalendarFactory
{
    static private enum AdjustmentDirection
    {
            ADD,
            SUBTRACT,
            AT,
            NEXT,
            PREV,
            NEXTORTHIS,
            PREVORTHIS
    }

    static private class DateAdjustment
    {
        static private enum QuantityType
        {
                BEGINNING,
                ENDING,
                ABSOLUTE
        }
        static private enum UnitOfMeasure
        {
                TIME,
                YEAR,
                MONTH,
                WEEKOFYEAR,
                WEEKOFMONTH,
                DAY,
                DAYOFWEEK,
                HOUR,
                MINUTE,
                SECOND,
                MILLISECOND
        }

        AdjustmentDirection         direction;
        int                         quantity;

        /*
         * Due to the direction being * and the qty being B or E. This will be
         * the B or E.
         */
        QuantityType                quantityType;

        UnitOfMeasure               unitOfMeasure;

        static final private Format DebugTimeFmt = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss.SSS");

        public DateAdjustment(
                final String tokenValue)
                throws ParseException
        {
            final int qtyStart = parseDirection(tokenValue);
            final int uomStart = parseQuantity(tokenValue, qtyStart);
            parseUnitOfMeasure(tokenValue, uomStart);
        }

        public void adjust (
            final Calendar cal)
            throws ParseException
        {
            StringBuilder debugStr = null;
            if (isInDebug())
            {
                debugStr = new StringBuilder();
                debugStr.append(DebugTimeFmt.format(cal.getTime()));
                debugStr.append(" ");
                debugStr.append(direction);
                debugStr.append(" ");
                debugStr.append(quantityType == QuantityType.ABSOLUTE
                        ? quantity
                        : quantityType);
                debugStr.append(" ");
                debugStr.append(unitOfMeasure);
            }

            int qty = quantity;
            if (direction == AdjustmentDirection.SUBTRACT)
                qty = 0 - qty;

            switch (unitOfMeasure)
            {
                case TIME:
                    adjustTime(cal, qty);
                    break;
                case MILLISECOND:
                    adjustMillisecond(cal, qty);
                    break;
                case SECOND:
                    adjustSecond(cal, qty);
                    break;
                case MINUTE:
                    adjustMinute(cal, qty);
                    break;
                case HOUR:
                    adjustHour(cal, qty);
                    break;
                case DAY:
                    adjustDay(cal, qty);
                    break;
                case DAYOFWEEK:
                    adjustDayOfWeek(cal, qty);
                    break;
                case WEEKOFYEAR:
                    adjustWeekOfYear(cal, qty);
                    break;
                case WEEKOFMONTH:
                    adjustWeekOfMonth(cal, qty);
                    break;
                case MONTH:
                    adjustMonth(cal, qty);
                    break;
                case YEAR:
                    adjustYear(cal, qty);
                    break;
                default:
                    throw new ParseException("invalid unit of measure in data adjustment: " + unitOfMeasure,
                        0);
            }

            if (isInDebug())
            {
                debugStr.append(" = ");
                debugStr.append(DebugTimeFmt.format(cal.getTime()));
                System.out.println(debugStr.toString());
            }
        }

        void adjustDay (
            final Calendar cal,
            final int qty)
        {
            switch (quantityType)
            {
                case BEGINNING:
                    adjustToBeginningOfTime(cal);
                    break;
                case ENDING:
                    adjustToEndOfTime(cal);
                    break;
                default:
                    if (direction == AdjustmentDirection.AT)
                        cal.set(Calendar.DATE, qty);
                    else
                        cal.add(Calendar.DATE, qty);
            }
        }

        void adjustDayOfWeek (
            final Calendar cal,
            final int qty)
            throws ParseException
        {
            switch (quantityType)
            {
                case BEGINNING: // same as @bw or @bo
                    switch (direction)
                    {
                        case AT:
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            break;
                        case NEXTORTHIS:
                            adjustToBeginningOfTime(cal);
                            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                                break;
                            cal.add(Calendar.WEEK_OF_MONTH, 1);
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            break;
                        case NEXT:
                            cal.add(Calendar.WEEK_OF_MONTH, 1);
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            adjustToBeginningOfTime(cal);
                            break;
                        case PREVORTHIS:
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            adjustToBeginningOfTime(cal);
                            break;
                        case PREV:
                            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                                cal.add(Calendar.WEEK_OF_MONTH, -1);
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            adjustToBeginningOfTime(cal);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    break;
                case ENDING: // same as @ew or @eo
                    switch (direction)
                    {
                        case AT:
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            break;
                        case NEXTORTHIS:
                            adjustToEndOfTime(cal);
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            break;
                        case NEXT:
                            cal.add(Calendar.WEEK_OF_MONTH, 1);
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            adjustToEndOfTime(cal);
                            break;
                        case PREVORTHIS:
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            adjustToEndOfTime(cal);
                            break;
                        case PREV:
                            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                                cal.add(Calendar.WEEK_OF_MONTH, -1);
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            adjustToEndOfTime(cal);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    break;
                default:
                    switch (direction)
                    {
                        case AT:
                            cal.set(Calendar.DAY_OF_WEEK, qty);
                            break;
                        case NEXTORTHIS:
                            if (cal.get(Calendar.DAY_OF_WEEK) == qty)
                                break;
                            if (cal.get(Calendar.DAY_OF_WEEK) > qty)
                                cal.add(Calendar.WEEK_OF_MONTH, 1);
                            break;
                        case NEXT:
                            if (cal.get(Calendar.DAY_OF_WEEK) >= qty)
                                cal.add(Calendar.WEEK_OF_MONTH, 1);
                            break;
                        case PREVORTHIS:
                            if (cal.get(Calendar.DAY_OF_WEEK) == qty)
                                break;
                            if (cal.get(Calendar.DAY_OF_WEEK) < qty)
                                cal.add(Calendar.WEEK_OF_MONTH, -1);
                            break;
                        case PREV:
                            if (cal.get(Calendar.DAY_OF_WEEK) <= qty)
                                cal.add(Calendar.WEEK_OF_MONTH, -1);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    cal.set(Calendar.DAY_OF_WEEK, qty);
            }
        }

        void adjustHour (
            final Calendar cal,
            final int qty)
            throws ParseException
        {
            switch (quantityType)
            {
                case BEGINNING:

                    switch (direction)
                    {
                        case AT:
                        {
                            final int hour = cal.get(Calendar.HOUR_OF_DAY);
                            adjustToBeginningOfTime(cal);
                            cal.set(Calendar.HOUR_OF_DAY, hour);
                            break;
                        }
                        case NEXTORTHIS:
                        {
                            final int hour = cal.get(Calendar.HOUR_OF_DAY);
                            if (cal.get(Calendar.MINUTE) != 0
                                || cal.get(Calendar.SECOND) != 0
                                || cal.get(Calendar.MILLISECOND) != 0)
                            {
                                adjustToBeginningOfTime(cal);
                                cal.set(Calendar.HOUR_OF_DAY, hour + 1);
                            }
                            break;
                        }
                        case NEXT:
                        {
                            final int hour = cal.get(Calendar.HOUR_OF_DAY);
                            adjustToBeginningOfTime(cal);
                            cal.set(Calendar.HOUR_OF_DAY, hour + 1);
                            break;
                        }
                        case PREVORTHIS:
                        {
                            final int hour = cal.get(Calendar.HOUR_OF_DAY);
                            adjustToBeginningOfTime(cal);
                            cal.set(Calendar.HOUR_OF_DAY, hour);
                            break;
                        }
                        case PREV:
                        {
                            final int hour = cal.get(Calendar.HOUR_OF_DAY);

                            if (cal.get(Calendar.MINUTE) != 0
                                || cal.get(Calendar.SECOND) != 0
                                || cal.get(Calendar.MILLISECOND) != 0)
                            {
                                adjustToBeginningOfTime(cal);
                                cal.set(Calendar.HOUR_OF_DAY, hour);
                            } else
                            {
                                adjustToBeginningOfTime(cal);
                                cal.set(Calendar.HOUR_OF_DAY, hour - 1);
                            }
                            break;
                        }
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }

                    break;
                case ENDING:

                    switch (direction)
                    {
                        case AT:
                        {
                            final int hour = cal.get(Calendar.HOUR_OF_DAY);
                            adjustToEndOfTime(cal);
                            cal.set(Calendar.HOUR_OF_DAY, hour);
                            break;
                        }
                        case NEXTORTHIS:
                        {
                            final int hour = cal.get(Calendar.HOUR_OF_DAY);
                            adjustToEndOfTime(cal);
                            cal.set(Calendar.HOUR_OF_DAY, hour);
                            break;
                        }
                        case NEXT:
                        {
                            final int hour = cal.get(Calendar.HOUR_OF_DAY);
                            if (cal.get(Calendar.MINUTE) != 59
                                || cal.get(Calendar.SECOND) != 59
                                || cal.get(Calendar.MILLISECOND) != 999)
                            {
                                adjustToEndOfTime(cal);
                                cal.set(Calendar.HOUR_OF_DAY, hour);
                            } else
                            {
                                adjustToEndOfTime(cal);
                                cal.set(Calendar.HOUR_OF_DAY, hour + 1);
                            }
                            break;
                        }
                        case PREVORTHIS:
                        {
                            final int hour = cal.get(Calendar.HOUR_OF_DAY);

                            if (cal.get(Calendar.MINUTE) != 0
                                || cal.get(Calendar.SECOND) != 0
                                || cal.get(Calendar.MILLISECOND) != 0)
                            {
                                adjustToEndOfTime(cal);
                                cal.set(Calendar.HOUR_OF_DAY, hour);
                            } else
                            {
                                adjustToEndOfTime(cal);
                                cal.set(Calendar.HOUR_OF_DAY, hour - 1);
                            }
                            break;
                        }
                        case PREV:
                        {
                            final int hour = cal.get(Calendar.HOUR_OF_DAY);
                            adjustToEndOfTime(cal);
                            cal.set(Calendar.HOUR_OF_DAY, hour - 1);
                            break;
                        }
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    break;

                default:
                    switch (direction)
                    {
                        case AT:
                            cal.set(Calendar.HOUR_OF_DAY, qty);
                            break;
                        case ADD:
                            cal.add(Calendar.HOUR_OF_DAY, qty);
                            break;
                        case SUBTRACT:
                            cal.add(Calendar.HOUR_OF_DAY, qty);
                            break;
                        case NEXTORTHIS:
                            if (cal.get(Calendar.HOUR_OF_DAY) > qty)
                                cal.add(Calendar.DATE, 1);
                            cal.set(Calendar.HOUR_OF_DAY, qty);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        case NEXT:
                            if (cal.get(Calendar.HOUR_OF_DAY) >= qty)
                                cal.add(Calendar.DATE, 1);
                            cal.set(Calendar.HOUR_OF_DAY, qty);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        case PREVORTHIS:
                            if (cal.get(Calendar.HOUR_OF_DAY) < qty)
                                cal.add(Calendar.DATE, -1);
                            cal.set(Calendar.HOUR_OF_DAY, qty);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        case PREV:
                            if (cal.get(Calendar.HOUR_OF_DAY) <= qty)
                                cal.add(Calendar.DATE, -1);
                            cal.set(Calendar.HOUR_OF_DAY, qty);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
            }
        }

        void adjustMillisecond (
            final Calendar cal,
            final int qty)
        {
            if (direction == AdjustmentDirection.AT)
                cal.set(Calendar.MILLISECOND, qty);
            else
                cal.add(Calendar.MILLISECOND, qty);
        }

        void adjustMinute (
            final Calendar cal,
            final int qty)
            throws ParseException
        {
            switch (quantityType)
            {
                case BEGINNING:

                    switch (direction)
                    {
                        case AT:
                        {
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        }
                        case NEXTORTHIS:
                        {
                            if (cal.get(Calendar.SECOND) != 0 || cal.get(Calendar.MILLISECOND) != 0)
                            {
                                cal.add(Calendar.MINUTE, 1);
                                cal.set(Calendar.SECOND, 0);
                                cal.set(Calendar.MILLISECOND, 0);
                            }
                            break;
                        }
                        case NEXT:
                        {
                            cal.add(Calendar.MINUTE, 1);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        }
                        case PREVORTHIS:
                        {
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        }
                        case PREV:
                        {
                            if (cal.get(Calendar.SECOND) != 0 || cal.get(Calendar.MILLISECOND) != 0)
                            {
                                cal.set(Calendar.SECOND, 0);
                                cal.set(Calendar.MILLISECOND, 0);
                            } else
                            {
                                cal.add(Calendar.MINUTE, -1);
                                cal.set(Calendar.SECOND, 0);
                                cal.set(Calendar.MILLISECOND, 0);
                            }
                            break;
                        }
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }

                    break;
                case ENDING:

                    switch (direction)
                    {
                        case AT:
                        {
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);
                            break;
                        }
                        case NEXTORTHIS:
                        {
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);
                            break;
                        }
                        case NEXT:
                        {
                            if (cal.get(Calendar.SECOND) != 59 || cal.get(Calendar.MILLISECOND) != 999)
                            {
                                cal.set(Calendar.SECOND, 59);
                                cal.set(Calendar.MILLISECOND, 999);
                            } else
                            {
                                cal.add(Calendar.MINUTE, 1);
                                cal.set(Calendar.SECOND, 59);
                                cal.set(Calendar.MILLISECOND, 999);
                            }
                            break;
                        }
                        case PREVORTHIS:
                        {
                            if (cal.get(Calendar.SECOND) != 59 || cal.get(Calendar.MILLISECOND) != 999)
                            {
                                cal.add(Calendar.MINUTE, -1);
                                cal.set(Calendar.SECOND, 59);
                                cal.set(Calendar.MILLISECOND, 999);
                            }
                            break;
                        }
                        case PREV:
                        {
                            cal.add(Calendar.MINUTE, -1);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);
                            break;
                        }
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    break;

                default:
                    switch (direction)
                    {
                        case AT:
                            cal.set(Calendar.MINUTE, qty);
                            break;
                        case ADD:
                            cal.add(Calendar.MINUTE, qty);
                            break;
                        case SUBTRACT:
                            cal.add(Calendar.MINUTE, qty);
                            break;
                        case NEXTORTHIS:
                            if (cal.get(Calendar.MINUTE) > qty)
                                cal.add(Calendar.HOUR_OF_DAY, 1);
                            cal.set(Calendar.MINUTE, qty);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        case NEXT:
                            if (cal.get(Calendar.MINUTE) >= qty)
                                cal.add(Calendar.HOUR_OF_DAY, 1);
                            cal.set(Calendar.MINUTE, qty);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        case PREVORTHIS:
                            if (cal.get(Calendar.MINUTE) < qty)
                                cal.add(Calendar.HOUR_OF_DAY, -1);
                            cal.set(Calendar.MINUTE, qty);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        case PREV:
                            if (cal.get(Calendar.MINUTE) <= qty)
                                cal.add(Calendar.HOUR_OF_DAY, -1);
                            cal.set(Calendar.MINUTE, qty);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
            }
        }

        void adjustMonth (
            final Calendar cal,
            final int qty)
        {
            switch (quantityType)
            {
                case BEGINNING:
                    cal.set(Calendar.DATE, 1);
                    adjustToBeginningOfTime(cal);
                    break;
                case ENDING:
                    cal.add(Calendar.MONTH, 1);
                    cal.set(Calendar.DATE, 1);
                    cal.add(Calendar.DATE, -1);
                    adjustToEndOfTime(cal);
                    break;
                default:
                    if (direction == AdjustmentDirection.AT)
                    {
                        cal.set(Calendar.MONTH, qty - 1);
                        /*
                         * It might be the case that we are moving from a month
                         * with more days and the day of the current date is
                         * greater than the number of days in the target month.
                         */
                        if (cal.get(Calendar.MONTH) != (qty - 1))
                        {
                            /*
                             * Move back to the end of the selected month.
                             */
                            cal.set(Calendar.DAY_OF_MONTH, 1);
                            cal.add(Calendar.DAY_OF_MONTH, -1);
                        }
                    } else
                        cal.add(Calendar.MONTH, qty);
            }
        }

        void adjustSecond (
            final Calendar cal,
            final int qty)
        {

            switch (quantityType)
            {
                case BEGINNING:
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case ENDING:
                    cal.set(Calendar.MILLISECOND, 999);
                    break;
                default:
                    if (direction == AdjustmentDirection.AT)
                    {
                        cal.set(Calendar.SECOND, qty);
                    } else
                        cal.add(Calendar.SECOND, qty);
            }
        }

        /**
         * @param cal
         * @param qty
         * @throws ParseException
         */
        void adjustTime (
            final Calendar cal,
            final int qty)
            throws ParseException
        {
            if (direction == AdjustmentDirection.AT)

                switch (quantityType)
                {
                    case BEGINNING:
                        adjustToBeginningOfTime(cal);
                        break;
                    case ENDING:
                        adjustToEndOfTime(cal);
                        break;
                    default:
                        throw new ParseException("invalid qty in data adjustment: " + direction,
                            0);
                }
            else
            {
                throw new ParseException("invalid direction in data adjustment: " + direction,
                    0);
            }
        }

        void adjustToBeginningOfTime (
            final Calendar cal)
        {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }

        void adjustToEndOfTime (
            final Calendar cal)
        {
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
        }

        void adjustWeekOfMonth (
            final Calendar cal,
            final int qty)
            throws ParseException
        {
            switch (quantityType)
            {
                case BEGINNING:
                    switch (direction)
                    {
                        case AT:
                            cal.set(Calendar.WEEK_OF_MONTH, 1);
                            break;
                        case NEXTORTHIS:
                            if (cal.get(Calendar.WEEK_OF_MONTH) != 1)
                            {
                                cal.add(Calendar.MONTH, 1);
                                cal.set(Calendar.WEEK_OF_MONTH, 1);
                            }
                            break;
                        case NEXT:
                            cal.add(Calendar.MONTH, 1);
                            cal.set(Calendar.WEEK_OF_MONTH, 1);
                            break;
                        case PREVORTHIS:
                            cal.set(Calendar.WEEK_OF_MONTH, 1);
                            break;
                        case PREV:
                            if (cal.get(Calendar.WEEK_OF_MONTH) == 1)
                                cal.add(Calendar.MONTH, -1);
                            cal.set(Calendar.WEEK_OF_MONTH, 1);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    break;
                case ENDING: // same as @ew or @eo
                    switch (direction)
                    {
                        case AT:
                            cal.add(Calendar.MONTH, 1);
                            cal.set(Calendar.WEEK_OF_MONTH, 1);
                            cal.add(Calendar.WEEK_OF_MONTH, -1);
                            break;
                        case NEXTORTHIS:
                            cal.set(Calendar.WEEK_OF_MONTH, endWeekOfThisMonth(cal));
                            break;
                        case NEXT:
                            if (cal.get(Calendar.WEEK_OF_MONTH) == endWeekOfThisMonth(cal))
                            {
                                cal.add(Calendar.MONTH, 1);
                            }
                            cal.set(Calendar.WEEK_OF_MONTH, endWeekOfThisMonth(cal));
                            break;
                        case PREVORTHIS:
                            if (cal.get(Calendar.WEEK_OF_MONTH) != endWeekOfThisMonth(cal))
                            {
                                cal.add(Calendar.MONTH, -1);
                            }
                            cal.set(Calendar.WEEK_OF_MONTH, endWeekOfThisMonth(cal));
                            break;
                        case PREV:
                            cal.add(Calendar.MONTH, -1);
                            cal.set(Calendar.WEEK_OF_MONTH, endWeekOfThisMonth(cal));
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    break;
                default:
                    switch (direction)
                    {
                        case AT:
                            cal.set(Calendar.WEEK_OF_MONTH, qty);
                            break;
                        case NEXTORTHIS:
                            if (cal.get(Calendar.WEEK_OF_MONTH) > qty)
                            {
                                cal.add(Calendar.MONTH, 1);
                            }
                            cal.set(Calendar.WEEK_OF_MONTH, qty);
                            break;
                        case NEXT:
                            if (cal.get(Calendar.WEEK_OF_MONTH) >= qty)
                            {
                                cal.add(Calendar.MONTH, 1);
                            }
                            cal.set(Calendar.WEEK_OF_MONTH, qty);
                            break;
                        case PREVORTHIS:
                            if (cal.get(Calendar.WEEK_OF_MONTH) < qty)
                            {
                                cal.add(Calendar.MONTH, -1);
                            }
                            cal.set(Calendar.WEEK_OF_MONTH, qty);
                            break;
                        case PREV:
                            if (cal.get(Calendar.WEEK_OF_MONTH) <= qty)
                            {
                                cal.add(Calendar.MONTH, -1);
                            }
                            cal.set(Calendar.WEEK_OF_MONTH, qty);
                            break;
                        case SUBTRACT:
                            cal.add(Calendar.WEEK_OF_MONTH, qty);
                            break;
                        case ADD:
                            cal.add(Calendar.WEEK_OF_MONTH, qty);
                            break;
                    }
            }
            adjustToBeginningOfTime(cal);
        }

        void adjustWeekOfYear (
            final Calendar cal,
            final int qty)
            throws ParseException
        {
            switch (quantityType)
            {
                case BEGINNING:
                    switch (direction)
                    {
                        case AT:
                            break;
                        case NEXTORTHIS:
                            if (cal.get(Calendar.WEEK_OF_YEAR) != 1)
                            {
                                cal.add(Calendar.YEAR, 1);
                            }
                            break;
                        case NEXT:
                            cal.add(Calendar.YEAR, 1);
                            break;
                        case PREVORTHIS:
                            break;
                        case PREV:
                            if (cal.get(Calendar.WEEK_OF_YEAR) == 1)
                                cal.add(Calendar.YEAR, -1);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    cal.set(Calendar.WEEK_OF_YEAR, 1);
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    adjustToBeginningOfTime(cal);
                    break;

                case ENDING: // same as @ew or @eo
                    switch (direction)
                    {
                        case AT:
                            break;
                        case NEXTORTHIS:
                            break;
                        case NEXT:
                            if (cal.get(Calendar.WEEK_OF_YEAR) == 53)
                            {
                                cal.add(Calendar.YEAR, 1);
                            }
                            break;
                        case PREVORTHIS:
                            if (cal.get(Calendar.WEEK_OF_YEAR) != 53)
                            {
                                cal.add(Calendar.YEAR, -1);
                            }
                            break;
                        case PREV:
                            cal.add(Calendar.YEAR, -1);
                            cal.set(Calendar.WEEK_OF_YEAR, 53);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    cal.set(Calendar.WEEK_OF_YEAR, 53);
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    adjustToBeginningOfTime(cal);
                    break;
                default:
                    switch (direction)
                    {
                        case AT:
                            cal.set(Calendar.WEEK_OF_YEAR, qty);
                            break;
                        case NEXTORTHIS:
                            if (cal.get(Calendar.WEEK_OF_YEAR) > qty)
                            {
                                cal.add(Calendar.YEAR, 1);
                            }
                            cal.set(Calendar.WEEK_OF_YEAR, qty);
                            break;
                        case NEXT:
                            if (cal.get(Calendar.WEEK_OF_YEAR) >= qty)
                            {
                                cal.add(Calendar.YEAR, 1);
                            }
                            cal.set(Calendar.WEEK_OF_YEAR, qty);
                            break;
                        case PREVORTHIS:
                            if (cal.get(Calendar.WEEK_OF_YEAR) < qty)
                            {
                                cal.add(Calendar.YEAR, -1);
                            }
                            cal.set(Calendar.WEEK_OF_YEAR, qty);
                            break;
                        case PREV:
                            if (cal.get(Calendar.WEEK_OF_YEAR) <= qty)
                            {
                                cal.add(Calendar.YEAR, -1);
                            }
                            cal.set(Calendar.WEEK_OF_YEAR, qty);
                            break;
                        case SUBTRACT:
                            cal.add(Calendar.WEEK_OF_YEAR, qty);
                            break;
                        case ADD:
                            cal.add(Calendar.WEEK_OF_YEAR, qty);
                            break;
                    }
            }
        }

        void adjustYear (
            final Calendar cal,
            final int qty)
            throws ParseException
        {
            switch (quantityType)
            {
                case BEGINNING:
                    switch (direction)
                    {
                        case AT:
                            break;
                        case NEXTORTHIS:
                            if (cal.get(Calendar.DAY_OF_YEAR) != 1)
                            {
                                cal.add(Calendar.YEAR, 1);
                            }
                            break;
                        case NEXT:
                            cal.add(Calendar.YEAR, 1);
                            break;
                        case PREVORTHIS:
                            break;
                        case PREV:
                            cal.add(Calendar.YEAR, -1);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    cal.set(Calendar.DAY_OF_YEAR, 1);
                    adjustToBeginningOfTime(cal);
                    break;

                case ENDING: // same as @ew or @eo
                    switch (direction)
                    {
                        case AT:
                            break;
                        case NEXTORTHIS:
                            break;
                        case NEXT:
                            cal.add(Calendar.YEAR, 1);
                            break;
                        case PREVORTHIS:
                            if (!isEndOfThisYear(cal))
                                cal.add(Calendar.YEAR, -1);
                            break;
                        case PREV:
                            cal.add(Calendar.YEAR, -1);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
                    cal.set(Calendar.DATE, 1);
                    cal.add(Calendar.YEAR, 1);
                    cal.set(Calendar.MONTH, Calendar.JANUARY);
                    cal.add(Calendar.DATE, -1);
                    adjustToEndOfTime(cal);
                    break;
                default:
                    switch (direction)
                    {
                        case AT:
                            cal.set(Calendar.YEAR, qty);
                            break;
                        case SUBTRACT:
                            cal.add(Calendar.YEAR, qty);
                            break;
                        case ADD:
                            cal.add(Calendar.YEAR, qty);
                            break;
                        default:
                            throw new ParseException("invalid direction in data adjustment: " + direction,
                                0);
                    }
            }
        }

        int endWeekOfThisMonth (
            final Calendar cal)
        {
            final Calendar work = Calendar.getInstance();
            work.setTime(cal.getTime());
            work.set(Calendar.DAY_OF_MONTH, 1);
            work.add(Calendar.MONTH, 1);
            work.add(Calendar.DAY_OF_MONTH, -1);
            return work.get(Calendar.WEEK_OF_MONTH);
        }

        boolean isEndOfThisYear (
            final Calendar cal)
        {
            return cal.get(Calendar.DATE) == 31 && cal.get(Calendar.MONTH) == Calendar.DECEMBER;
        }

        public boolean isInDebug ()
        {
            return CalendarFactory.isInDebug();
        }

        int parseDirection (
            final String tokenValue)
            throws ParseException
        {
            int qtyStart;

            switch (tokenValue.charAt(0))
            {
                case '+':
                    direction = AdjustmentDirection.ADD;
                    qtyStart = 1;
                    break;
                case '-':
                    direction = AdjustmentDirection.SUBTRACT;
                    qtyStart = 1;
                    break;
                case '=':
                    direction = AdjustmentDirection.AT;
                    qtyStart = 1;
                    break;
                case '>':
                    if (tokenValue.charAt(1) == '=')
                    {
                        direction = AdjustmentDirection.NEXTORTHIS;
                        qtyStart = 2;
                    } else
                    {
                        direction = AdjustmentDirection.NEXT;
                        qtyStart = 1;
                    }
                    break;
                case '<':
                    if (tokenValue.charAt(1) == '=')
                    {
                        direction = AdjustmentDirection.PREVORTHIS;
                        qtyStart = 2;
                    } else
                    {
                        direction = AdjustmentDirection.PREV;
                        qtyStart = 1;
                    }
                    break;
                default:
                    throw new ParseException("invalid direction: " + tokenValue,
                        0);
            }
            return qtyStart;
        }

        int parseEndOfNumber (
                final String tokenValue,
                int startOfNumber)
        {
            for (int son = startOfNumber; son < tokenValue.length(); son++)
            {
                if (tokenValue.charAt(son) < '0')
                    return son;
                if (tokenValue.charAt(son) > '9')
                    return son;
            }
            return tokenValue.length();
        }

        int parseQuantity (
            final String tokenValue,
            final int qtyStart)
            throws ParseException
        {
            int uomStart = 0;
            quantityType = QuantityType.ABSOLUTE;

            if (qtyStart >= tokenValue.length())
                throw new ParseException("Premature end of formula, qty expected: " + tokenValue,
                    qtyStart);

            uomStart = parseRelativeQuantity(tokenValue, qtyStart);
            if (uomStart == 0)
            {
                uomStart = parseEndOfNumber(tokenValue, qtyStart);
                if (uomStart == qtyStart)
                    throw new ParseException("missing qty in " + tokenValue,
                        uomStart);
                quantity = Integer.parseInt(tokenValue.substring(qtyStart, uomStart));
            }
            return uomStart;
        }

        int parseRelativeQuantity (
            final String tokenValue,
            final int qtyStart)
        {
            if (tokenValue.charAt(qtyStart) == 'B' || tokenValue.charAt(qtyStart) == 'b')
            {
                quantityType = QuantityType.BEGINNING;
                return qtyStart + 1;
            } else if (tokenValue.charAt(qtyStart) == 'E' || tokenValue.charAt(qtyStart) == 'e')
            {
                quantityType = QuantityType.ENDING;
                return qtyStart + 1;
            }
            return 0;
        }

        void parseUnitOfMeasure (
            final String tokenValue,
            final int uomStart)
            throws ParseException
        {
            final String uom = tokenValue.substring(uomStart).toLowerCase();
            if (uom == null || uom.trim().length() == 0)
                throw new ParseException("uom is required",
                    0);
            switch (uom.charAt(0))
            {
                case 't':
                    unitOfMeasure = UnitOfMeasure.TIME;
                    break;
                case 'y':
                    unitOfMeasure = UnitOfMeasure.YEAR;
                    break;
                case 'h':
                    unitOfMeasure = UnitOfMeasure.HOUR;
                    break;
                case 's':
                    unitOfMeasure = UnitOfMeasure.SECOND;
                    break;
                case 'o':
                    unitOfMeasure = UnitOfMeasure.WEEKOFMONTH;
                    break;
                case 'l':
                    unitOfMeasure = UnitOfMeasure.MILLISECOND;
                    break;
                case 'i':
                    unitOfMeasure = UnitOfMeasure.MINUTE;
                    break;
                case 'a':
                    unitOfMeasure = UnitOfMeasure.DAYOFWEEK;
                    break;
                case 'm':
                    if (uom.startsWith("min"))
                        unitOfMeasure = UnitOfMeasure.MINUTE;
                    else if (uom.startsWith("mil") || uom.equals("ms"))
                        unitOfMeasure = UnitOfMeasure.MILLISECOND;
                    else
                        unitOfMeasure = UnitOfMeasure.MONTH;
                    break;
                case 'w':
                    if (uom.startsWith("weekofm"))
                        unitOfMeasure = UnitOfMeasure.WEEKOFMONTH;
                    else
                        unitOfMeasure = UnitOfMeasure.WEEKOFYEAR;
                    break;
                case 'd':
                    if (uom.startsWith("dayofw"))
                        unitOfMeasure = UnitOfMeasure.DAYOFWEEK;
                    else
                        unitOfMeasure = UnitOfMeasure.DAY;
                    break;
                default:
                    throw new ParseException("invalid uom: " + uom,
                        0);
            }
        }
    }

    static private CalendarFactory instance;

    static boolean                 inDebug = false;

    static public String asFormula (
        final Calendar calendar)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("@").append(calendar.get(Calendar.YEAR)).append("year");
        sb.append(" @").append(calendar.get(Calendar.MONTH) + 1).append("month");
        sb.append(" @").append(calendar.get(Calendar.DAY_OF_MONTH)).append("day");
        sb.append(" @").append(calendar.get(Calendar.HOUR_OF_DAY)).append("hour");
        sb.append(" @").append(calendar.get(Calendar.MINUTE)).append("minute");
        sb.append(" @").append(calendar.get(Calendar.SECOND)).append("second");
        sb.append(" @").append(calendar.get(Calendar.MILLISECOND)).append("millisecond");
        return sb.toString();
    }

    static public String asFormula (
        final Date date)
    {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return asFormula(cal);
    }

    static public Calendar at (
        final long milliseconds)
    {
        return getInstance().atImpl(milliseconds);
    }

    static public CalendarFactory getInstance ()
    {
        if (instance == null)
            instance = new CalendarFactory();
        return instance;
    }

    static public boolean isInDebug ()
    {
        return inDebug;
    }

    /**
     * The adjustments are separated by a space. Multiple elements are
     * acceptable and are assumed to be more adjusts. This just provides
     * flexibility in how this method can be called.
     * 
     * @param startingDate
     * @param adjustments
     * @return
     */
    static public Calendar modify (
        final Calendar startingDate,
        final String... adjustmentsArray)
    {
        try
        {
            return getInstance().modifyImpl(startingDate, adjustmentsArray);

        } catch (final ParseException e)
        {
            e.printStackTrace();
            return Calendar.getInstance();
        }
    }

    /**
     * The adjustments are separated by a space. Multiple elements are
     * acceptable and are assumed to be more adjusts. This just provides
     * flexibility in how this method can be called.
     * 
     * @param startingDate
     * @param adjustments
     * @return
     */
    static public Calendar modify (
        final Date startingDate,
        final String... adjustmentsArray)
    {
        try
        {
            return getInstance().modifyImpl(startingDate, adjustmentsArray);

        } catch (final ParseException e)
        {
            e.printStackTrace();
            return Calendar.getInstance();
        }
    }

    static public Calendar modify (
        final long startingMilliseconds,
        final String... adjustmentsArray)
    {
        try
        {
            return getInstance().modifyImpl(startingMilliseconds, adjustmentsArray);

        } catch (final ParseException e)
        {
            e.printStackTrace();
            return Calendar.getInstance();
        }
    }

    static public Calendar noTime (
        final Calendar startingDate)
    {
        return getInstance().noTimeImpl(startingDate);
    }

    static public Date noTime (
        final Date startingDate)
    {
        return getInstance().noTimeImpl(startingDate);
    }

    /**
     * The adjustments are separated by a space. Multiple elements are
     * acceptable and are assumed to be more adjusts. This just provides
     * flexibility in how this method can be called. The computation starts at
     * the exact millisecond this method is called
     * 
     * @param startingDate
     * @param adjustments
     * @return
     */
    static public Calendar now (
        final String... adjustmentsArray)
    {
        try
        {
            return getInstance().nowImpl(adjustmentsArray);

        } catch (final ParseException e)
        {
            e.printStackTrace();
            return Calendar.getInstance();
        }
    }

    static public Calendar nowX (
        final String... adjustmentsArray)
        throws ParseException
    {
        return getInstance().nowImpl(adjustmentsArray);
    }

    static public CalendarFactory reset (
        final CalendarFactory newFactory)
    {
        final CalendarFactory oldFactory = instance;
        instance = newFactory;
        return oldFactory;
    }

    static public void setBusinessDate (
        final Calendar businessDate)
    {
        getInstance().setBusinessDateImpl(businessDate);
    }

    static public void setInDebug (
        final boolean inDebug_parm)
    {
        inDebug = inDebug_parm;
    }

    /**
     * The adjustments are separated by a space. Multiple elements are
     * acceptable and are assumed to be more adjusts. This just provides
     * flexibility in how this method can be called. The computation starts at
     * the beginning of the current date.
     * 
     * @param startingDate
     * @param adjustments
     * @return
     */
    static public Calendar today (
        final String... adjustmentsArray)
    {
        try
        {
            return getInstance().todayImpl(adjustmentsArray);

        } catch (final ParseException e)
        {
            e.printStackTrace();
            return Calendar.getInstance();
        }
    }

    long businessDateAdjustment;

    void adjustForBusinessDate (
        final Calendar startingDate)
    {
        long bMs = businessDateAdjustment;
        if (bMs > 0)
            while (bMs > 0)
            {
                int bMsPart;
                if (bMs > Integer.MAX_VALUE)
                    bMsPart = Integer.MAX_VALUE;
                else
                    bMsPart = (int) bMs;
                startingDate.add(Calendar.MILLISECOND, bMsPart);
                bMs -= bMsPart;
            }
        else
            while (bMs < 0)
            {
                int bMsPart;
                if (bMs < Integer.MIN_VALUE)
                    bMsPart = Integer.MIN_VALUE;
                else
                    bMsPart = (int) bMs;
                startingDate.add(Calendar.MILLISECOND, bMsPart);
                bMs -= bMsPart;
            }
    }

    Calendar applyAdjustments (
        final Calendar startingDate,
        final String... adjustmentsArray)
        throws ParseException
    {
        if (adjustmentsArray != null)
        {
            final List<DateAdjustment> adjustments = new ArrayList<DateAdjustment>();
            for (final String adjs : adjustmentsArray)
            {
                if (adjs.trim().length() == 0)
                    break;
                final String[] singleAdjs = adjs.split(" +");
                for (final String adj : singleAdjs)
                    adjustments.add(new DateAdjustment(adj));
            }

            for (final DateAdjustment adj : adjustments)
            {
                adj.adjust(startingDate);
            }
        }
        return startingDate;
    }

    Calendar atImpl (
        final long milliseconds)
    {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        return cal;
    }

    Calendar modifyImpl (
        final Calendar startingDate,
        final String... adjustmentsArray)
        throws ParseException
    {
        return applyAdjustments(startingDate, adjustmentsArray);
    }

    Calendar modifyImpl (
        final Date startingDate,
        final String... adjustmentsArray)
        throws ParseException
    {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(startingDate);
        return applyAdjustments(cal, adjustmentsArray);
    }

    Calendar modifyImpl (
        final long startingMilliseconds,
        final String... adjustmentsArray)
        throws ParseException
    {
        final Calendar cal = atImpl(startingMilliseconds);
        return applyAdjustments(cal, adjustmentsArray);
    }

    Calendar noTimeImpl (
        final Calendar startingDate)
    {
        startingDate.set(Calendar.HOUR_OF_DAY, 0);
        startingDate.set(Calendar.MINUTE, 0);
        startingDate.set(Calendar.SECOND, 0);
        startingDate.set(Calendar.MILLISECOND, 0);
        return startingDate;
    }

    Date noTimeImpl (
        final Date startingDate)
    {
        final Calendar startingCal = Calendar.getInstance();
        startingCal.setTime(startingDate);
        startingCal.set(Calendar.HOUR_OF_DAY, 0);
        startingCal.set(Calendar.MINUTE, 0);
        startingCal.set(Calendar.SECOND, 0);
        startingCal.set(Calendar.MILLISECOND, 0);
        return startingCal.getTime();
    }

    Calendar nowImpl (
        final String... adjustmentsArray)
        throws ParseException
    {
        final Calendar startingDate = Calendar.getInstance();
        adjustForBusinessDate(startingDate);
        return applyAdjustments(startingDate, adjustmentsArray);
    }

    public void setBusinessDateImpl (
        final Calendar businessDate)
    {
        if (businessDate == null)
        {
            businessDateAdjustment = 0;
            return;
        }
        this.businessDateAdjustment = businessDate.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
    }

    Calendar todayImpl (
        final String... adjustmentsArray)
        throws ParseException
    {
        final Calendar startingDate = Calendar.getInstance();
        adjustForBusinessDate(startingDate);
        return applyAdjustments(noTime(startingDate), adjustmentsArray);
    }

}
