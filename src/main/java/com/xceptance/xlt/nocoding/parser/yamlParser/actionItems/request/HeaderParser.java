package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.util.ParserUtils;

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
        final Map<String, String> headers = ParserUtils.getArrayNodeAsMap(node);
        return headers;
    }

}
