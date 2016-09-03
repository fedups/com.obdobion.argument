package com.obdobion.argument.type;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * WildFiles class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
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
     * @param aSingleKnownFile a {@link java.io.File} object.
     */
    public WildFiles(final File aSingleKnownFile)
    {
        userSuppliedPatterns = new ArrayList<>();
        files = new ArrayList<>();
        files.add(aSingleKnownFile);
    }

    void add(final String userPattern)
    {
        userSuppliedPatterns.add(userPattern);
    }

    /**
     * <p>
     * files.
     * </p>
     *
     * @return a {@link java.util.List} object.
     * @throws java.text.ParseException if any.
     * @throws java.io.IOException if any.
     */
    public List<File> files() throws ParseException, IOException
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

    String get(final int userSuppliedIndex)
    {
        return userSuppliedPatterns.get(userSuppliedIndex);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (final String pattern : userSuppliedPatterns)
        {
            if (!firstTime)
            {
                sb.append(" ");
                firstTime = false;
            }
            sb.append(pattern);
        }
        return sb.toString();
    }

}
