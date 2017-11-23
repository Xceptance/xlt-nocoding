package com.xceptance.xlt.nocoding.parser;

import java.io.File;
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

    public Parser(final String pathToFile)
    {
        this.file = new File(pathToFile);
    }

    /**
     * Interface method, that uses a file to parse the content as a list of ScriptItems
     * 
     * @return The list of ScriptItems that are to be executed in that exact order
     * @throws Exception
     */
    public abstract List<ScriptItem> parse() throws Exception;

    public File getFile()
    {
        return file;
    }
}
