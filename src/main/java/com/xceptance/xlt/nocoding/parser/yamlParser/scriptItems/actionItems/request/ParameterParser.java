package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * The class for parsing the parameter items to a list of {@link NameValuePair}s
 * 
 * @author ckeiner
 */
public class ParameterParser
{

    /**
     * Parses a parameter item to a list of <code>NameValuePair</code>s
     * 
     * @param node
     *            The {@link JsonNode} with the parameters in it
     * @return A list containing the parsed parameters
     */
    public List<NameValuePair> parse(final JsonNode node)
    {
        // Transform the JsonNode to a List of NameValuePairs
        final List<NameValuePair> parameters = ParserUtils.getArrayNodeAsNameValuePair(node);
        // Return the list
        return parameters;
    }

}
