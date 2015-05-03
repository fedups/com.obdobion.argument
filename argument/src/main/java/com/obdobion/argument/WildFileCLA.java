package com.obdobion.argument;

import java.io.File;
import java.io.FileFilter;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WildFileCLA extends AbstractCLA<String>
{
    public WildFileCLA(final char _keychar)
    {
        super(_keychar);
    }

    public WildFileCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public WildFileCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType (StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_WildFILE);
    }

    /**
     * @param wildcard
     * @param _pathSep is not currently used
     * @return
     */
    static public Pattern convertWildCardToRegx (String wildcard, String _pathSep, boolean _caseSensitive)
    {
        String regex = wildcard;
        regex = regex.replaceAll("[.]", "\\\\.");
        regex = regex.replaceAll("[?]", ".?");
        regex = regex.replaceAll("[*]", ".*");
        return Pattern.compile(regex, _caseSensitive
                ? Pattern.CASE_INSENSITIVE
                : 0);
    }

    static public boolean wildcardsUsed (String inputPattern)
    {
        return Pattern.matches(".*[?*].*", inputPattern);
    }

    @Override
    public File[] getValueAsFileArray () throws ParseException
    {
        final File[][] multiResult = new File[size()][];
        int foundCnt = 0;

        for (int r = 0; r < size(); r++)
        {
            /*
             * Load up the array with instances of Files that are found by
             * searching with the wildPattern.
             * 
             * The path will be the current working directory if no pathChar can
             * be found.
             */
            File exactRequest = new File(getValue(r));
            File dir = exactRequest.getParentFile();
            if (dir == null)
                dir = new File(".");
            // System.out.println(dir.getAbsolutePath() + " " +
            // exactRequest.getName());

            Pattern pattern = convertWildCardToRegx(exactRequest.getName(), File.pathSeparator, isCaseSensitive());
            final Matcher matcher = pattern.matcher("");

            File[] files = dir.listFiles(
                    new FileFilter()
                    {
                        @Override
                        public boolean accept (File pathname)
                        {
                            if (pathname.isDirectory())
                            {
                                return false;
                            }
                            matcher.reset(pathname.getName());
                            return matcher.matches();
                        }
                    });
            if (!wildcardsUsed(getValue(r)) && files.length == 0)
                /*
                 * At least one file must be found for each wildcard pattern.
                 * This allows for the natural expectation that if a specific
                 * (non-wildcard) pattern is given that the file is expected to
                 * be there.
                 */
                throw new ParseException("file not found: " + getValue(r), 0);

            foundCnt += files.length;
            multiResult[r] = files;
        }
        /*
         * Convert the array of arrays into a single dimension array
         */
        File[] result = new File[foundCnt];
        int r = 0;
        for (File[] oneResult : multiResult)
            for (File oneFile : oneResult)
                result[r++] = oneFile;
        return result;
    }

    @Override
    public String convert (final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        if (_caseSensitive)
            return valueStr;
        return valueStr.toLowerCase();
    }

    @Override
    public String defaultInstanceClass ()
    {
        return "java.io.File";
    }

    @Override
    protected void exportCommandLineData (final StringBuilder out, final int occ)
    {
        out.append('"');
        out.append(getValue(occ).replaceAll("\"", "\\\\\""));
        out.append('"');
    }

    @Override
    protected void exportNamespaceData (final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ));
        out.append("\n");
    }

    @Override
    protected void exportXmlData (final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ), out);
    }
}
