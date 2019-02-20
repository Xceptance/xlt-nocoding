package com.xceptance.xlt.nocoding.parser.yaml.command.action.request;

import java.util.Map;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;

import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;

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
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param cookiesNode
     *            The {@link Node} with the cookies in it
     * @return A map containing the parsed cookies
     */
    public Map<String, String> parse(final Mark context, final Node cookiesNode)
    {
        // Parse the JsonNode to a Map<String, String>
        final Map<String, String> cookies = YamlParserUtils.getSequenceNodeAsMap(context, cookiesNode);
        // Parse cookies and return them
        return cookies;
    }

}
