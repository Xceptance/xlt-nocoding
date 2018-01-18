package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

/**
 * The abstract class for parsing script items.
 * 
 * @author ckeiner
 */
public abstract class AbstractScriptItemParser
{

    /**
     * Parses the script item at the specified {@link JsonNode}.
     * 
     * @param scriptItemNode
     *            The <code>JsonNode</code> the script item starts at
     * @return A list of all {@link ScriptItem}s defined by the specified <code>JsonNode</code>
     */
    public abstract List<ScriptItem> parse(JsonNode scriptItemNode);

}
