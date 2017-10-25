package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public abstract class AbstractScriptItemParser
{

    public abstract List<ScriptItem> parse(JsonParser parser) throws IOException;

    /**
     * Gets the JsonNode with the specified nodeName in the JsonParser parser
     * 
     * @param nodeName
     *            The name of the node
     * @param parser
     *            The parser in which to look for the node
     * @return The JsonNode with the specified name
     * @throws JsonProcessingException
     * @throws IOException
     */
    protected ObjectNode getObjectNodeAt(final JsonParser parser) throws JsonProcessingException, IOException
    {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(parser);
    }

}
