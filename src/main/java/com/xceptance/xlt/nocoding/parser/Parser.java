package com.xceptance.xlt.nocoding.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

/**
 * Defines the behavior of all parsers.
 * 
 * @author ckeiner
 */
public abstract class Parser
{
    /**
     * Parses the content of the file at the location of the parameter to a list of {@link ScriptItem}s
     * 
     * @param pathToFile
     *            The String that describes the path to the file
     * @return The list of ScriptItems that are to be executed in that exact order
     * @throws IOException
     */
    public abstract List<ScriptItem> parse(String pathToFile) throws IOException;

    /**
     * Returns all acceptable extensions for this Parser
     * 
     * @return A list of extensions that are associated with this Parser
     */
    public abstract List<String> getExtensions();

    /**
     * Creates a reader based on <code>pathToFile</code>. First, it tries to create a FileReader by creating a file. If a
     * {@link FileNotFoundException} is encountered, it creates a {@link URL} from the <code>pathToFile</code> and creates a
     * {@link InputStreamReader} based on {@link URL#openStream()}.
     * 
     * @param pathToFile
     *            The path to the file
     * @return A {@link Reader} with the information of the file
     * @throws IOException
     */
    public Reader createReader(final String pathToFile) throws IOException
    {
        Reader fileReader = null;
        final File file = new File(pathToFile);
        if (file.exists())
        {
            fileReader = new FileReader(file);
        }
        else
        {
            try
            {
                fileReader = new InputStreamReader(new URL(pathToFile).openStream());
            }
            catch (final MalformedURLException e)
            {
                throw new FileNotFoundException("Neither File nor URL found for " + pathToFile + ", because " + e.getMessage());
            }
        }
        return fileReader;
    }
}
