package com.xceptance.xlt.nocoding.parser.yaml.command.action.request;

import java.util.List;

import org.htmlunit.util.NameValuePair;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;

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
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param parameterNode
     *            The {@link Node} with the parameters in it
     * @return A list of <code>NameValuePair</code>s containing the parsed parameters
     */
    public List<NameValuePair> parse(final Mark context, final Node parameterNode)
    {
        // Transform the JsonNode to a list of NameValuePairs
        final List<NameValuePair> parameters = YamlParserUtils.getSequenceNodeAsNameValuePair(context, parameterNode);
        // Return the list
        return parameters;
    }

}
