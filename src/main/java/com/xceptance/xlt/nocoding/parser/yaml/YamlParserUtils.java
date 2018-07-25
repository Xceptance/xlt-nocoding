package com.xceptance.xlt.nocoding.parser.yaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.JsonParseException;

/**
 * Utility methods for parsing.
 * 
 * @author ckeiner
 */
public class YamlParserUtils
{

    /**
     * Converts an ArrayNode consisting of ObjectNodes with simple Key/Value-Pairs to a Map
     * 
     * @param jsonNode
     *            The ArrayNode consisting of ObjectNodes
     * @return A map of the parsed content
     */
    public static Map<String, String> getArrayNodeAsMap(final JsonNode jsonNode)
    {
        if (!(jsonNode instanceof ArrayNode))
        {
            throw new IllegalArgumentException("Expected ArrayNode but was " + jsonNode.getClass().getSimpleName());
        }

        final Map<String, String> map = new HashMap<String, String>();
        // Get iterator for all elements of the ArrayNode
        final Iterator<JsonNode> iterator = jsonNode.elements();

        // while we have elements in the array
        while (iterator.hasNext())
        {
            // Get the next node
            final JsonNode current = iterator.next();
            // The array consists of objects, so an ArrayNode has ObjectNodes. Therefore, we iterate over the fieldNames
            final Iterator<String> fieldNames = current.fieldNames();

            // While we have field names
            while (fieldNames.hasNext())
            {
                // Get the next field name (which is also the name of the variable we want to store)
                final String fieldName = fieldNames.next();
                // And extract the value (which is the value of the variable)
                final String textValue = YamlParserUtils.readSingleValue(current.get(fieldName));
                map.put(fieldName, textValue);
            }
        }
        return map;
    }

    /**
     * Converts an ArrayNode consisting of ObjectNodes with simple Key/Value-Pairs to a List of {@link NameValuePair}s
     * 
     * @param jsonNode
     *            The ArrayNode consisting of ObjectNodes
     * @return A list of <code>NameValuePair</code>s that describe the parameters
     */
    public static List<NameValuePair> getArrayNodeAsNameValuePair(final JsonNode jsonNode)
    {
        if (!(jsonNode instanceof ArrayNode))
        {
            throw new IllegalArgumentException("Expected ArrayNode but was " + jsonNode.getClass().getSimpleName());
        }

        final List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        // Get iterator for all elements of the ArrayNode
        final Iterator<JsonNode> iterator = jsonNode.elements();

        // while we have elements in the array
        while (iterator.hasNext())
        {
            // Get the next node
            final JsonNode current = iterator.next();
            // The array consists of objects, so an ArrayNode has ObjectNodes. Therefore, we iterate over the fieldNames
            final Iterator<String> fieldNames = current.fieldNames();

            // While we have field names
            while (fieldNames.hasNext())
            {
                // Get the next field name (which is also the name of the variable we want to store)
                final String fieldName = fieldNames.next();
                // And extract the value (which is the value of the variable)
                final String textValue = YamlParserUtils.readSingleValue(current.get(fieldName));
                // Construct the StoreItem with the fieldName and the textValue
                nvp.add(new NameValuePair(fieldName, textValue));
            }
        }
        return nvp;
    }

    /**
     * Gets the root <code>ObjectNode</code> in the {@link JsonParser} parser via {@link ObjectMapper#readTree(JsonParser)}.
     * 
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
     * Gets the JsonNode with the specified <code>fieldName</code> in the JsonParser parser
     * 
     * @param fieldName
     *            The name of the field
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

    /**
     * Parses the value at <code>node</code> with the field name <code>fieldName</code> to a String.
     * 
     * @param node
     *            The node from which to read the <code>fieldName</code>
     * @param fieldName
     *            The name of the field from which to read the value
     * @return The value as {@link String}
     */
    public static String readValue(final JsonNode node, final String fieldName)
    {
        return YamlParserUtils.readSingleValue(node.get(fieldName));
    }

    /**
     * Parses the value at <code>node</code> to a String.
     * 
     * @param node
     *            The node from which to read the value
     * @return The value as {@link String}
     */
    public static String readSingleValue(final JsonNode node)
    {
        String value = null;
        final JsonNodeType type = node.getNodeType();
        if (type.equals(JsonNodeType.BOOLEAN))
        {
            value = Boolean.toString(node.asBoolean());
        }
        else if (type.equals(JsonNodeType.NUMBER))
        {
            value = Integer.toString(node.asInt());
        }
        else if (type.equals(JsonNodeType.STRING))
        {
            value = node.textValue();
        }
        else if (type.equals(JsonNodeType.NULL))
        {
            value = "";
        }
        else
        {
            throw new JsonParseException("Unknown node type " + type.toString());
        }
        return value;
    }

}
