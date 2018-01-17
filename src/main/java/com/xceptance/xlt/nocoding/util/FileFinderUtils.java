package com.xceptance.xlt.nocoding.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.xceptance.xlt.nocoding.parser.ParserFactory;

/**
 * Provides methods for verifying if a file exists and getting an existing file from a list of Strings.
 * 
 * @author ckeiner
 */
public class FileFinderUtils
{
    /**
     * Goes through all provided file paths and tries to find a path to an existing file. If the extension is missing, it
     * adds all known extensions via {@link ParserFactory#getExtensions()} and searches for the file.
     * 
     * @param possiblePaths
     *            All possible filepaths to look into, with or without file extension
     * @return The path to a found file
     * @throws FileNotFoundException
     *             if no existing file could be found
     */
    public static String getExistingFilepath(final List<String> possiblePaths) throws FileNotFoundException
    {
        String correctPath = null;
        for (final String filepath : possiblePaths)
        {
            // Get the extensions
            final String fileExtension = FilenameUtils.getExtension(filepath);
            if (!fileExtension.isEmpty())
            {
                // Check if the file exists
                if (FileFinderUtils.existsFileFor(filepath))
                {
                    // If it does, this is the correct path
                    correctPath = filepath;
                    // Exit the loop
                    break;
                }
            }

            // If the file extension is empty, try to add all known extensions
            else if (fileExtension.isEmpty())
            {
                // Get all possible extensions
                final List<String> extensions = ParserFactory.getInstance().getExtensions();
                // Try to add each extension and stop when a file is found
                for (final String extension : extensions)
                {
                    // Add .extension to the path
                    final String fullPath = filepath + "." + extension;
                    // Check if it exists
                    if (FileFinderUtils.existsFileFor(fullPath))
                    {
                        correctPath = fullPath;
                        break;
                    }
                }
                // If a file has been found, the correctPath isn't null anymore, therefore break out of the outer loop
                if (correctPath != null)
                {
                    break;
                }
            }
        }
        // Since we cannot say for sure if we found an existing file, throw an error if correctPath is still null
        if (correctPath == null)
        {
            String allFiles = "";
            for (final String path : possiblePaths)
            {
                allFiles += path + "\n";
            }
            // Remove last \n
            allFiles = allFiles.substring(0, allFiles.length() - 1);
            String extensions = "";
            for (final String extension : ParserFactory.getInstance().getExtensions())
            {
                extensions += extension + ", ";
            }
            // Remove comma
            extensions = extensions.substring(0, extensions.length() - 2);

            final String errorMessage = "No script file found in any of these locations:\n" + allFiles
                                        + " with the following extensions added if none was supplied: " + extensions;
            throw new FileNotFoundException(errorMessage);
        }
        return correctPath;
    }

    /**
     * Checks if at the location of <code>fullPath</code> anything can be found.<br>
     * At first, it creates a file at the path and if it exists, the function returns true. If no File exists, it creates a
     * URL and tries to open the Stream. If the Stream can be opened, then we can read from there and return true.<br>
     * 
     * @param fullPath
     *            The path to the file, with the file extension
     * @return True if either a File or URL can read from the provided <code>fullPath</code><br>
     */
    public static boolean existsFileFor(final String fullPath)
    {
        boolean exists = false;

        // Check if a file exists at the location
        if (new File(fullPath).exists())
        {
            exists = true;
        }
        // If no file could be found
        else
        {
            try
            {
                // Try to build a URL out of fullPath
                final URL file = new URL(fullPath);
                if (file != null)
                {
                    // Try to open the stream
                    file.openStream();
                    // We only execute this line, if the stream could be opened
                    exists = true;
                }
            }
            // Catch MalformedURLException and every other IO exception that occurs when opening the Stream, thus telling us, that
            // we cannot find something at the location
            catch (final IOException e)
            {
                exists = false;
            }
        }
        // Return the value of exists
        return exists;
    }
}
