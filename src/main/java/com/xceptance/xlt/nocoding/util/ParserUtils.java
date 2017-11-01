package com.xceptance.xlt.nocoding.util;

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
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.JsonParseException;

public class ParserUtils
{

    /**
     * Converts an ArrayNode consisting of ObjectNodes with simple Key/Value-Pairs to a Map
     * 
     * @param jsonNode
     *            The ArrayNode consisting of ObjectNodes
     * @return
     */
    public static Map<String, String> getArrayNodeAsMap(final JsonNode jsonNode)
    {
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
                final String textValue = ParserUtils.readSingleValue(current.get(fieldName));
                map.put(fieldName, textValue);
            }
        }
        return map;
    }

    /**
     * Converts an ArrayNode consisting of ObjectNodes with simple Key/Value-Pairs to a List<NameValuePair>
     * 
     * @param jsonNode
     *            The ArrayNode consisting of ObjectNodes
     * @return
     */
    public static List<NameValuePair> getArrayNodeAsNameValuePair(final JsonNode jsonNode)
    {
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
                final String textValue = ParserUtils.readSingleValue(current.get(fieldName));
                // Construct the StoreItem with the fieldName and the textValue
                nvp.add(new NameValuePair(fieldName, textValue));
            }
        }
        return nvp;
    }

    /**
     * Gets the ObjectNode in the JsonParser parser
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

    /**
     * Parses an expected boolean value at node with the field name fieldName
     * 
     * @param node
     * @param fieldName
     * @return
     */
    public static String readExpectedBooleanValue(final JsonNode node, final String fieldName)
    {
        String value = node.get(fieldName).textValue();

        if (value == null)
        {
            // We didn't get a variable
            value = Boolean.toString(node.get(fieldName).asBoolean());
        }
        return value;
    }

    /**
     * Parses an expected integer value at node with the field name fieldName
     * 
     * @param node
     * @param fieldName
     * @return
     */
    public static String readExpectedIntegerValue(final JsonNode node, final String fieldName)
    {
        // get node type!
        // node.getNodeType()
        String value = node.get(fieldName).textValue();

        if (value == null)
        {
            // We didn't get a variable
            value = Integer.toString(node.get(fieldName).asInt());
        }
        return value;
    }

    /**
     * Parses an expected string value at node with the field name fieldName. However, if we get null, we try to parse it as
     * boolean and then as integer
     * 
     * @param node
     * @param fieldName
     * @return
     */
    public static String readExpectedStringValue(final JsonNode node, final String fieldName)
    {
        String value = node.get(fieldName).textValue();

        if (value == null)
        {
            value = readExpectedBooleanValue(node, fieldName);
            if (value == null)
            {
                value = readExpectedIntegerValue(node, fieldName);
            }
        }
        return value;
    }

    /**
     * Parses an expected string value at node with the field name fieldName. However, if we get null, we try to parse it as
     * boolean and then as integer
     * 
     * @param node
     * @param fieldName
     * @return
     */
    public static String readValue(final JsonNode node, final String fieldName)
    {
        String value = null;
        final JsonNodeType type = node.get(fieldName).getNodeType();
        if (type.equals(JsonNodeType.BOOLEAN))
        {
            value = Boolean.toString(node.get(fieldName).asBoolean());
        }
        else if (type.equals(JsonNodeType.NUMBER))
        {
            value = Integer.toString(node.get(fieldName).asInt());
        }
        else if (type.equals(JsonNodeType.STRING))
        {
            node.get(fieldName).textValue();
        }
        // TODO Null or Missing?
        else if (type.equals(JsonNodeType.NULL))
        {
            value = null;
        }
        else
        {
            throw new JsonParseException("Unknown node type");
        }
        return value;
    }

    /**
     * Parses an expected string value at node with the field name fieldName. However, if we get null, we try to parse it as
     * boolean and then as integer
     * 
     * @param node
     * @param fieldName
     * @return
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
        // TODO Null or Missing?
        else if (type.equals(JsonNodeType.NULL))
        {
            value = null;
        }
        else
        {
            throw new JsonParseException("Unknown node type");
        }
        return value;
    }

}
