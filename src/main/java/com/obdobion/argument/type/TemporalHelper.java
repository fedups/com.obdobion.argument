package com.obdobion.argument.type;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>TemporalHelper class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class TemporalHelper
{
    static Matcher[]                     predefinedMat;
    static SimpleDateFormat[]            predefinedFmt;
    static String[]                      predefinedAlg;
    static final int                     numberOfDateFormats = 5;
    static final int                     numberOfTimeFormats = 5;
    static int                           fmtMakerIndex       = -1;
    static final int                     numberOfAlgos       = 2;
    static final String                  specialAlgoTODAY    = "today";
    static final String                  specialAlgoNOW      = "now";
    /** Constant <code>outputSDF</code> */
    static public final SimpleDateFormat outputSDF           = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss.SSS");

    static private void createFormat(final String format)
    {
        if (format.charAt(0) == '*')
        {
            predefinedFmt[fmtMakerIndex] = null;
            predefinedAlg[fmtMakerIndex] = format.substring(1);
        } else
        {
            predefinedFmt[fmtMakerIndex] = new SimpleDateFormat(format.trim());
            predefinedAlg[fmtMakerIndex] = null;
        }
    }

    static private Matcher createMatcher(final String format)
    {
        return Pattern.compile(format.trim(), Pattern.CASE_INSENSITIVE).matcher("");
    }

    /**
     * Create these so that the matchers are paired with the date formats. When
     * the matcher succeeds then the date format will be used.
     */
    synchronized static public void createPredefinedDateFormats()
    {
        if (predefinedMat != null)
            return;

        final String mmFmt = "[0-9]{1,2}";
        final String ddFmt = "[0-9]{1,2}";
        final String yyFmt = "[0-9]{4}";
        final char[] seps = { '-', '/' };
        final char[] spaceSeps = { ' ', '_', '@' };

        predefinedMat = new Matcher[spaceSeps.length
                * numberOfDateFormats
                * seps.length
                * numberOfTimeFormats
                + numberOfAlgos];
        predefinedFmt = new SimpleDateFormat[spaceSeps.length
                * numberOfDateFormats
                * seps.length
                * numberOfTimeFormats
                + numberOfAlgos];
        predefinedAlg = new String[spaceSeps.length
                * numberOfDateFormats
                * seps.length
                * numberOfTimeFormats
                + numberOfAlgos];

        String dFormat;
        String dMatcher;

        for (int space = 0; space < spaceSeps.length; space++)
        {
            for (int s = 0; s < seps.length; s++)
            {
                final char sep = seps[s];

                dMatcher = mmFmt + sep + ddFmt + sep + yyFmt;
                dFormat = "MM" + sep + "dd" + sep + "yyyy";
                createWithTimePattern(dFormat, dMatcher, spaceSeps[space]);

                dMatcher = yyFmt + sep + mmFmt + sep + ddFmt;
                dFormat = "yyyy" + sep + "MM" + sep + "dd";
                createWithTimePattern(dFormat, dMatcher, spaceSeps[space]);

                dMatcher = yyFmt + sep + mmFmt;
                dFormat = "yyyy" + sep + "MM";
                createWithTimePattern(dFormat, dMatcher, spaceSeps[space]);

                dMatcher = mmFmt + sep + yyFmt;
                dFormat = "MM" + sep + "yyyy";
                createWithTimePattern(dFormat, dMatcher, spaceSeps[space]);
                /*
                 * No date, just time of day, assuming today as the date part.
                 */
                createWithTimePattern("", "", spaceSeps[space]);
            }

            dMatcher = specialAlgoTODAY;
            dFormat = "*" + specialAlgoTODAY;
            createWithTimePattern(dFormat, dMatcher, spaceSeps[space]);

            dMatcher = specialAlgoNOW;
            dFormat = "*" + specialAlgoNOW;
            createWithTimePattern(dFormat, dMatcher, spaceSeps[space]);
        }
    }

    static private void createWithTimePattern(final String dFormat, final String dMatcher, final char space)
    {
        final String hhFmt = "[0-9]{1,2}";
        final String miFmt = "[0-9]{1,2}";
        final String ssFmt = "[0-9]{1,2}";
        final String zzFmt = "[0-9]{1,3}";
        final char tSep = ':';
        final String milliSep = "\\.";

        if (dFormat.length() > 0)
        {
            /*
             * Without time.
             */
            predefinedMat[++fmtMakerIndex] = createMatcher(dMatcher);
            createFormat(dFormat);

            if (predefinedFmt[fmtMakerIndex] == null) // special algo
                return;
        }

        String tFormat;
        String tMatcher;
        /*
         * With HH:mm:ss.SSS
         */
        tMatcher = hhFmt + tSep + miFmt + tSep + ssFmt + milliSep + zzFmt;
        tFormat = "HH" + tSep + "mm" + tSep + "ss" + "." + "SSS";
        predefinedMat[++fmtMakerIndex] = createMatcher(dMatcher + space + tMatcher);
        createFormat(dFormat + space + tFormat);
        /*
         * With HH:mm:ss
         */
        tMatcher = hhFmt + tSep + miFmt + tSep + ssFmt;
        tFormat = "HH" + tSep + "mm" + tSep + "ss";
        predefinedMat[++fmtMakerIndex] = createMatcher(dMatcher + space + tMatcher);
        createFormat(dFormat + space + tFormat);
        /*
         * With HH:mm
         */
        tMatcher = hhFmt + tSep + miFmt;
        tFormat = "HH" + tSep + "mm";
        predefinedMat[++fmtMakerIndex] = createMatcher(dMatcher + space + tMatcher);
        createFormat(dFormat + space + tFormat);
        /*
         * With HH
         */
        tMatcher = hhFmt;
        tFormat = "HH";
        predefinedMat[++fmtMakerIndex] = createMatcher(dMatcher + space + tMatcher);
        createFormat(dFormat + space + tFormat);
    }
}
