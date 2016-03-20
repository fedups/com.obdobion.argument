package com.obdobion.argument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class WildFiles
{
    final List<String> userSuppliedPatterns;
    private List<File> files;

    WildFiles()
    {
        userSuppliedPatterns = new ArrayList<>();
    }

    /**
     * This is used in FunnelSort to override the provided list of files with
     * one of them for each run.
     *
     * @param aSingleKnownFile
     */
    public WildFiles(final File aSingleKnownFile)
    {
        userSuppliedPatterns = new ArrayList<>();
        files = new ArrayList<>();
        files.add(aSingleKnownFile);
    }

    void add (final String userPattern)
    {
        userSuppliedPatterns.add(userPattern);
    }

    public List<File> files () throws ParseException, IOException
    {
        if (files == null)
        {
            files = new ArrayList<>();
            for (final String pattern : userSuppliedPatterns)
            {
                files.addAll(new WildPath(pattern).files());
            }
        }
        return files;
    }

    String get (final int userSuppliedIndex)
    {
        return userSuppliedPatterns.get(userSuppliedIndex);
    }

}