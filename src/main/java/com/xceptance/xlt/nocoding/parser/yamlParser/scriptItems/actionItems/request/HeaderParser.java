package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.util.ParserUtils;
import com.xceptance.xlt.nocoding.util.RecentKeyTreeMap;

/**
 * The class for parsing the header items to a map with a String key and a String value
 * 
 * @author ckeiner
 */
public class HeaderParser
{

    /**
     * Parses the header item to a map with a String key and a String value
     * 
     * @param node
     *            The {@link JsonNode} with the headers in it
     * @return A map containing the parsed headers
     */
    public Map<String, String> parse(final JsonNode node)
    {
        // Create a tree map that is case insensitive (since headers are case insensitive)
        final RecentKeyTreeMap<String, String> headers = new RecentKeyTreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        // And transform the JsonNode to a Map and put it in the TreeMap
        headers.putAll(ParserUtils.getArrayNodeAsMap(node));
        // Return headers
        return headers;
    }

}
