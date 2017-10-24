package com.xceptance.xlt.nocoding.parser;

import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

/**
 * Defines the interface for all parsers.
 * 
 * @author ckeiner
 */
public interface Parser
{
    /**
     * Interface method, that should search for the correct file and parse the content as a list of ScriptItems
     * 
     * @return The list of ScriptItems that are to be executed in that exact order
     * @throws Exception
     */
    public List<ScriptItem> parse() throws Exception;
}
