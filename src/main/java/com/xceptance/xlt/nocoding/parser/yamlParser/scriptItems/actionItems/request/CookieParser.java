package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * The class for parsing the cookies item to a list of {@link NameValuePair}s
 * 
 * @author ckeiner
 */
public class CookieParser
{
    /**
     * Parses a cookie item to a list of <code>NameValuePair</code>s
     * 
     * @param parameterNode
     *            The {@link JsonNode} with the parameters in it
     * @return A list of <code>NameValuePair</code>s containing the parsed parameters
     */
    public List<NameValuePair> parse(final JsonNode cookiesNode)
    {
        // Transform the JsonNode to a list of NameValuePairs
        final List<NameValuePair> cookies = ParserUtils.getArrayNodeAsNameValuePair(cookiesNode);
        // Return the list
        return cookies;
    }

}
