package com.xceptance.xlt.nocoding.parser.yamlParser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class ParserUtil
{

    public static Map<String, String> getAsMap(final JsonNode node)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public static List<NameValuePair> getAsNameValuePair(final JsonNode node)
    {
        // TODO Auto-generated method stub
        return null;
    }

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
    public static ObjectNode getNodeAt(final JsonParser parser) throws JsonProcessingException, IOException
    {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(parser);
    }

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
    public static JsonNode getNodeAt(final String fieldName, final JsonParser parser) throws JsonProcessingException, IOException
    {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode objectNode = mapper.readTree(parser);
        return objectNode.get(fieldName);
    }

}
