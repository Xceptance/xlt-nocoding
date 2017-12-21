package com.xceptance.xlt.nocoding.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

/**
 * Defines the interface for all parsers.
 * 
 * @author ckeiner
 */
public abstract class Parser
{
    /**
     * The yaml file
     */
    protected final File file;

    /**
     * Creates a {@link File} with the provided path to the file.
     * 
     * @param pathToFile
     *            The path to the file.
     */
    public Parser(final String pathToFile)
    {
        this.file = new File(pathToFile);
    }

    /**
     * Interface method, that uses a file to parse the content as a list of ScriptItems
     * 
     * @return The list of ScriptItems that are to be executed in that exact order
     * @throws IOException
     */
    public abstract List<ScriptItem> parse() throws IOException;

    public File getFile()
    {
        return file;
    }
}
