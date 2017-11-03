package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class ParameterParser
{

    /**
     * Parses the parameter item to a List<NameValuePair>
     * 
     * @param node
     *            The node the item starts at
     * @return A List containing the parameters
     * @throws IOException
     */
    public List<NameValuePair> parse(final JsonNode node) throws IOException
    {
        // Transform the JsonNode to a List of NameValuePairs
        final List<NameValuePair> parameters = ParserUtils.getArrayNodeAsNameValuePair(node);
        // Return the list
        return parameters;
    }

}
