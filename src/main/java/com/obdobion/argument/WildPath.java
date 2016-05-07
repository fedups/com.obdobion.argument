package com.obdobion.argument;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A path pattern can contain three types of wild cards; ?, *, and **. This is
 * not a regex.
 *
 * @author Chris DeGreef
 *
 */
public class WildPath
{
    static final boolean DEBUG                         = false;

    final static Matcher pathSegmentMatcher            = Pattern.compile("[^\\\\/]+").matcher("");
    final static Matcher wildCardInSegmentMatcher      = Pattern.compile("[*?]").matcher("");
    final static Matcher firstSegmentIsAbsoluteMatcher = Pattern.compile("^([\\\\/]|.:).*").matcher("");

    /**
     * @param wildcard
     * @return
     */
    static Pattern convertDirWildCardToRegx (final String wildcard)
    {
        if (wildcard == null || wildcard.length() == 0)
            return Pattern.compile("\\.");

        String regex;
        regex = wildcard;

        regex = regex.replaceAll("\\\\", "\\\\\\\\");
        regex = regex.replaceAll("[.]", "\\\\.");
        regex = regex.replaceAll("[?]", ".");
        regex = regex.replaceAll("\\*\\*", ".`");
        regex = regex.replaceAll("[*]", "[^\\\\\\\\]*");
        regex = regex.replaceAll("`", "*");

        return Pattern.compile("^" + regex + "$", Pattern.CASE_INSENSITIVE);
    }

    /**
     * @param wildcard
     * @return
     */
    static Pattern convertFileWildCardToRegx (final String wildcard)
    {
        String regex = wildcard;
        regex = regex.replaceAll("[.]", "\\\\.");
        regex = regex.replaceAll("[?]", ".");
        regex = regex.replaceAll("[*]", ".*");
        return Pattern.compile("^" + regex + "$", Pattern.CASE_INSENSITIVE);
    }

    String       userSuppliedPattern;
    List<String> pathSegments = new ArrayList<>();
    String       startingPath;
    Matcher      directoryMatcher;
    Matcher      fileMatcher;
    List<File>   files        = new ArrayList<>();

    public WildPath(final String pattern)
    {
        userSuppliedPattern = pattern;
        parsePattern(pattern);

        final File exactRequest = new File(userSuppliedPattern);
        File dir = exactRequest.getParentFile();
        if (dir == null)
            dir = new File(".");

        final Pattern dirPattern = convertDirWildCardToRegx(dir.toString());
        final Pattern filePattern = convertFileWildCardToRegx(exactRequest.getName());

        if (DEBUG)
        {
            System.out.println("user supplied " + userSuppliedPattern);
            System.out.println("dirPattern  " + dirPattern.toString());
            System.out.println("filePattern " + filePattern.toString());
        }

        directoryMatcher = dirPattern.matcher("");
        fileMatcher = filePattern.matcher("");
    }

    public List<File> files () throws ParseException, IOException
    {
        return files(new File(startingPath));
    }

    List<File> files (final File directory) throws ParseException, IOException
    {
        final File[] foundDirectories = directory.listFiles(
                new FileFilter()
                {
                    @Override
                    public boolean accept (final File pathname)
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
                        public boolean accept (final File pathname)
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
                                final boolean directoryMatchFlag = directoryMatcher.matches();
                                if (DEBUG)
                                    if (directoryMatchFlag)
                                        System.out.print(" dir matches: " + pathname.getParentFile().getPath());
                                    else
                                        System.out.print(" dir NOT matching: "
                                            + pathname.getParentFile().getPath()
                                            + " with "
                                            + directoryMatcher.pattern().pattern());
                                if (directoryMatchFlag)
                                {
                                    fileMatcher.reset(pathname.getName());
                                    final boolean matchFlag = fileMatcher.matches();
                                    if (DEBUG)
                                        if (matchFlag)
                                            System.out.print(" file matches");
                                        else
                                            System.out.print(" not matching");
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
            for (final File oneDir : foundDirectories)
                files(oneDir);
        }

        if (foundFiles != null)
        {
            for (final File oneFile : foundFiles)
                files.add(oneFile);
        }
        return files;
    }

    synchronized void parsePattern (final String pattern)
    {
        pathSegmentMatcher.reset(pattern);
        while (pathSegmentMatcher.find())
        {
            pathSegments.add(pathSegmentMatcher.group());
        }
        /*
         * The last "segment" must always be the file specification.
         */
        pathSegments.remove(pathSegments.size() - 1);
        /*
         * Find the longest absolute path from the beginning of the specified
         * path.
         */
        final StringBuilder startingPathBuilder = new StringBuilder();
        firstSegmentIsAbsoluteMatcher.reset(pattern);
        if (firstSegmentIsAbsoluteMatcher.matches())
            startingPathBuilder.append("/");
        for (final String segment : pathSegments)
        {
            wildCardInSegmentMatcher.reset(segment);
            if (wildCardInSegmentMatcher.find())
                break;
            startingPathBuilder.append(segment);
            startingPathBuilder.append("/");
        }
        startingPath = startingPathBuilder.toString();
        if (startingPath.length() == 0)
            startingPath = ".";
    }

    String startingPath ()
    {
        return startingPath;
    }
}