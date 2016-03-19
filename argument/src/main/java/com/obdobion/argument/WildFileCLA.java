package com.obdobion.argument;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.obdobion.argument.WildFileCLA.WildFile;

public class WildFileCLA extends AbstractCLA<WildFile>
{
    static final private boolean DEBUG = false;

    static public class WildFile
    {

        /**
         * @param wildcard
         * @param _pathSep is not currently used
         * @return
         */
        static public Pattern convertDirWildCardToRegx (String wildcard, String _pathSep)
        {
            if (wildcard == null || wildcard.length() == 0)
                return Pattern.compile("\\.");

            String regex;
            if (wildcard.charAt(0) != '.' && wildcard.charAt(0) != '\\')
            {
                regex = ".\\" + wildcard;
            } else
                regex = wildcard;

            regex = regex.replaceAll("\\\\", "\\\\\\\\");
            regex = regex.replaceAll("[.]", "\\\\.");
            regex = regex.replaceAll("[?]", ".?");
            regex = regex.replaceAll("\\*\\*", ".`");
            regex = regex.replaceAll("[*]", "[^\\\\\\\\]*");
            regex = regex.replaceAll("`", "*");

            return Pattern.compile("^" + regex + "$", Pattern.CASE_INSENSITIVE);
        }

        /**
         * @param wildcard
         * @param _pathSep is not currently used
         * @return
         */
        static public Pattern convertFileWildCardToRegx (String wildcard, String _pathSep)
        {
            String regex = wildcard;
            regex = regex.replaceAll("[.]", "\\\\.");
            regex = regex.replaceAll("[?]", ".?");
            regex = regex.replaceAll("[*]", ".*");
            return Pattern.compile("^" + regex + "$", Pattern.CASE_INSENSITIVE);
        }

        static public boolean wildcardsUsed (String inputPattern)
        {
            return Pattern.matches(".*[?*].*", inputPattern);
        }

        final List<String> userSuppliedPatterns;
        private List<File> files;

        WildFile()
        {
            userSuppliedPatterns = new ArrayList<>();
        }

        void add (String userPattern)
        {
            userSuppliedPatterns.add(userPattern);
        }

        String get (int userSuppliedIndex)
        {
            return userSuppliedPatterns.get(userSuppliedIndex);
        }

        public List<File> files () throws ParseException, IOException
        {
            if (files == null)
            {
                files = new ArrayList<>();
                for (String pattern : userSuppliedPatterns)
                    resolveDirectoryIntoFiles(new File("."), pattern);
            }
            return files;
        }

        void resolveDirectoryIntoFiles (File directory, String userSuppliedPattern) throws ParseException, IOException
        {
            File exactRequest = new File(userSuppliedPattern);
            File dir = exactRequest.getParentFile();
            if (dir == null)
                dir = new File("");

            Pattern dirPattern = convertDirWildCardToRegx(dir.toString(), File.pathSeparator);
            Pattern filePattern = convertFileWildCardToRegx(exactRequest.getName(), File.pathSeparator);

            if (DEBUG)
            {
                System.out.println("user supplied " + userSuppliedPattern);
                System.out.println("dirPattern  " + dirPattern.toString());
                System.out.println("filePattern " + filePattern.toString());
            }

            final Matcher directoryMatcher = dirPattern.matcher("");
            final Matcher fileMatcher = filePattern.matcher("");

            File[] foundDirectories = directory.listFiles(
                    new FileFilter()
                    {
                        @Override
                        public boolean accept (File pathname)
                        {
                            if (pathname.isDirectory())
                            {
                                return true;
                            }
                            return false;
                        }
                    });

            File[] foundFiles = null;
            {
                foundFiles = directory.listFiles(
                        new FileFilter()
                        {
                            @Override
                            public boolean accept (File pathname)
                            {
                                if (pathname.isDirectory())
                                {
                                    return false;
                                }
                                if (DEBUG)
                                {
                                    System.out.print("matching file " + pathname.getPath());
                                }
                                try
                                {
                                    directoryMatcher.reset(pathname.getParentFile().getPath());
                                    boolean directoryMatchFlag = directoryMatcher.matches();
                                    if (DEBUG)
                                        if (directoryMatchFlag)
                                            System.out.print(" dir matches: " + pathname.getParentFile().getPath());
                                        else
                                            System.out
                                                    .print(" dir NOT matching: " + pathname.getParentFile().getPath());
                                    if (directoryMatchFlag)
                                    {
                                        fileMatcher.reset(pathname.getName());
                                        boolean matchFlag = fileMatcher.matches();
                                        if (DEBUG)
                                            System.out.print(" file matches");
                                        return matchFlag;
                                    }
                                    return false;
                                } finally
                                {
                                    if (DEBUG)
                                    {
                                        System.out.println();
                                    }
                                }
                            }
                        });
            }

            if (foundDirectories != null)
            {
                for (File oneFile : foundDirectories)
                    resolveDirectoryIntoFiles(oneFile, userSuppliedPattern);
            }

            if (foundFiles != null)
            {
                for (File oneFile : foundFiles)
                    files.add(oneFile);
            }
        }
    }

    WildFile wildFile = new WildFile();

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
        sb.append(CLAFactory.TYPE_WILDFILE);
    }

    @Override
    public WildFile convert (final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        wildFile.add(valueStr);
        return wildFile;
    }

    @Override
    public String defaultInstanceClass ()
    {
        return "com.obdobion.argument.WildFileCLA.WildFile";
    }

    @Override
    protected void exportCommandLineData (final StringBuilder out, final int occ)
    {
        out.append('"');
        out.append(getValue(occ).get(0).replaceAll("\"", "\\\\\""));
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
        xmlEncode(getValue(occ).get(0), out);
    }

    @Override
    public void reset ()
    {
        super.reset();
        wildFile = new WildFile();
    }
}
