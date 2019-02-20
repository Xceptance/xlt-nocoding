package com.xceptance.xlt.nocoding.parser.yaml.command.action.response.extractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.parser.ParserException;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.HeaderExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.xpath.XpathExtractor;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Takes an identifier (which is an element of {@link JsonNode#fieldNames()}) of a <code>JsonNode</code> with the
 * extraction item in it and parses it to an {@link AbstractExtractor}.
 *
 * @author ckeiner
 */
public class ExtractorParser
{
    /**
     * The identifier of the extraction item
     */
    final String identifier;

    public ExtractorParser(final String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Parses the extraction item in the {@link JsonNode} to an {@link AbstractExtractor}. Also checks if
     * {@link Constants#GROUP} is specified in the item at the <code>JsonNode</code> and if <code>Constants#GROUP</code>
     * is specified, verifies the <code>AbstractExtractor</code> is a {@link RegexpExtractor}.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param node
     *            The list of <code>NodeTuple</code> containing the extraction
     * @return The <code>AbstractExtractor</code> corresponding to the identifier. <br>
     *         For example, {@link Constants#REGEXP} is parsed to a <code>RegexpExtractor</code>.
     */
    public AbstractExtractor parse(final Mark context, final List<NodeTuple> nodeTuples)
    {
        // Transform the NodeTuples to a map of strings
        final Map<String, String> map = new HashMap<String, String>();
        Node groupNode = null;
        for (final NodeTuple nodeTuple : nodeTuples)
        {
            final String key = YamlParserUtils.transformScalarNodeToString(context, nodeTuple.getKeyNode());
            if (key.equals(identifier) || key.equals(Constants.GROUP))
            {
                if (key.equals(Constants.GROUP))
                {
                    groupNode = nodeTuple.getKeyNode();
                }
                final String value = YamlParserUtils.transformScalarNodeToString(context, nodeTuple.getValueNode());
                map.put(key, value);
            }
        }
        nodeTuples.forEach(node -> {
            final String key = YamlParserUtils.transformScalarNodeToString(context, node.getKeyNode());
            if (key.equals(identifier) || key.equals(Constants.GROUP))
            {
                final String value = YamlParserUtils.transformScalarNodeToString(context, node.getValueNode());
                map.put(key, value);
            }
        });

        final boolean hasGroup = map.containsKey(Constants.GROUP);

        AbstractExtractor extractor = null;
        // Get the associated value
        final String extractorExpression = map.get(identifier);
        // Build a extractor depending on the name of the selector
        switch (identifier)
        {
            case Constants.XPATH:
                extractor = new XpathExtractor(extractorExpression);
                break;

            case Constants.REGEXP:
                if (hasGroup)
                {
                    extractor = new RegexpExtractor(extractorExpression, map.get(Constants.GROUP));
                }
                else
                {
                    extractor = new RegexpExtractor(extractorExpression);
                }
                break;

            case Constants.HEADER:
                extractor = new HeaderExtractor(extractorExpression);
                break;

            case Constants.COOKIE:
                extractor = new CookieExtractor(extractorExpression);
                break;

            default:
                throw new ParserException("Node", context, " contains a permitted extraction but it is unknown.",
                                          nodeTuples.get(0).getKeyNode().getStartMark());
        }

        // Throw an Exception when Constants.GROUP is specified, but the extractor is not a RegexpExtractor
        if (hasGroup && !(extractor instanceof RegexpExtractor))
        {
            throw new ParserException("Node", context,
                                      " defines a " + extractor.getClass().getSimpleName() + " and " + Constants.GROUP
                                                       + " of which the latter is only allowed with a "
                                                       + RegexpExtractor.class.getSimpleName(),
                                      groupNode.getStartMark());
        }

        // Return the extractor
        return extractor;
    }

}
