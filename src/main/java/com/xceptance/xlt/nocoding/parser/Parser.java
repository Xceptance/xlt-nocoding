package com.xceptance.xlt.nocoding.parser;

import java.io.IOException;
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
}
