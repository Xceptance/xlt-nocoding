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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class ParserUtil
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
                String textValue = current.get(fieldName).textValue();
                if (textValue == null || textValue.equals("null"))
                {
                    textValue = current.get(fieldName).toString();
                    if (textValue == null || textValue.equals("null"))
                    {
                        textValue = "";
                    }
                }
                // Construct the StoreItem with the fieldName and the textValue
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
                String textValue = current.get(fieldName).textValue();
                // TODO
                /*
                 * For some reason, a single digit cannot be parsed with textValue(), but multiple can. However, multiple digits cannot
                 * be parsed with toString() So we need to do both of these methods, to find out if we really have null or simply a
                 * single/multiple digit
                 */
                if (textValue == null || textValue.equals("null"))
                {
                    textValue = current.get(fieldName).toString();
                    if (textValue == null || textValue.equals("null"))
                    {
                        textValue = "";
                    }
                }
                // Construct the StoreItem with the fieldName and the textValue
                nvp.add(new NameValuePair(fieldName, textValue));
            }
        }
        return nvp;
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
