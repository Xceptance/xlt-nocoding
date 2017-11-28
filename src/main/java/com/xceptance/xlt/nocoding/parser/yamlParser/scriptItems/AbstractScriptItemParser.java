package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

/**
 * Parses a script item and returns a list of it (since i.e. StoreItemParser creates multiple ScriptItem)
 * 
 * @author ckeiner
 */
public abstract class AbstractScriptItemParser
{

    /**
     * Parses the list item at the specified {@link JsonNode} root.
     * 
     * @param root
     *            The node the list item starts at
     * @return {@link List}<{@link ScriptItem}>
     * @throws IOException
     */
    public abstract List<ScriptItem> parse(JsonNode root) throws IOException;

}
