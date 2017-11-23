package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.util.ParserUtils;
import com.xceptance.xlt.nocoding.util.RecentKeyTreeMap;

public class HeaderParser
{

    /**
     * Parses the header item to a Map<String, String>
     * 
     * @param node
     *            The node the item starts at
     * @return A Map containing the headers
     * @throws IOException
     */
    public Map<String, String> parse(final JsonNode node) throws IOException
    {
        // Create a tree map that is case insensitive (since headers are case insensitive
        final RecentKeyTreeMap<String, String> headers = new RecentKeyTreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        // And transform the JsonNode to a Map and put it in the TreeMap
        headers.putAll(ParserUtils.getArrayNodeAsMap(node));
        // Return headers
        return headers;
    }

}
