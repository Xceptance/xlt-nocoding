package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.actionItems.request;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * The class for parsing the cookies item to a map with a String key and a String value.
 * 
 * @author ckeiner
 */
public class CookieParser
{
    /**
     * Parses the cookie item to a map with a String key and a String value
     * 
     * @param cookiesNode
     *            The {@link JsonNode} with the cookies in it
     * @return A map containing the parsed cookies
     */
    public Map<String, String> parse(final JsonNode cookiesNode)
    {
        // Parse the JsonNode to a Map<String, String>
        final Map<String, String> cookies = ParserUtils.getArrayNodeAsMap(cookiesNode);
        // Parse cookies and return them
        return cookies;
    }

}
