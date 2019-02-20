package com.xceptance.xlt.nocoding.parser.yaml.command.action.request;

import java.util.Map;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;

import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;

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
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param headersNode
     *            The {@link Node} with the headers in it
     * @return A map containing the parsed headers
     */
    public Map<String, String> parse(final Mark context, final Node headersNode)
    {
        // Parse the JsonNode to a Map<String, String>
        final Map<String, String> headers = YamlParserUtils.getSequenceNodeAsMap(context, headersNode);
        // Parse headers and return them
        return headers;
    }

}
