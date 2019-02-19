package com.xceptance.xlt.nocoding.parser.yaml.command.action.request;

import java.util.List;

import org.yaml.snakeyaml.nodes.Node;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;

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
     * @param parameterNode
     *            The {@link Node} with the parameters in it
     * @return A list of <code>NameValuePair</code>s containing the parsed parameters
     */
    public List<NameValuePair> parse(final Node parameterNode)
    {
        // Transform the JsonNode to a list of NameValuePairs
        final List<NameValuePair> parameters = YamlParserUtils.getSequenceNodeAsNameValuePair(parameterNode);
        // Return the list
        return parameters;
    }

}
