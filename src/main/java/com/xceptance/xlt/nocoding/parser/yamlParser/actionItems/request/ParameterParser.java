package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.ParserUtil;

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
        final List<NameValuePair> parameters = ParserUtil.getArrayNodeAsNameValuePair(node);
        return parameters;
    }

}
