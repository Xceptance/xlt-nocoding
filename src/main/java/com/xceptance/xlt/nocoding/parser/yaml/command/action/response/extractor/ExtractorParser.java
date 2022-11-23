package com.xceptance.xlt.nocoding.parser.yaml.command.action.response.extractor;

import java.util.List;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.HeaderExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.xpath.XpathExtractor;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Takes a list of {@link NodeTuple} objects with the extraction definition in it and parses it to an
 * {@link AbstractExtractor}.
 *
 * @author ckeiner
 */
public class ExtractorParser
{
    /**
     * Parses the extraction item definition in the given list of {@link NodeTuple} objects to an
     * {@link AbstractExtractor}. Also performs plausibility checks and complains if things are missing or cannot be
     * combined together.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param nodeTuples
     *            The list of {@link NodeTuple} containing the extraction
     * @return The corresponding {@link AbstractExtractor}.
     */
    public AbstractExtractor parse(final Mark context, final List<NodeTuple> nodeTuples)
    {
        Node extractionMethodNode = null;
        Node groupNode = null;

        String extractionMethod = null;
        String extractionExpression = null;
        String additionalRegex = null;
        String group = null;

        for (final NodeTuple nodeTuple : nodeTuples)
        {
            final Node keyNode = nodeTuple.getKeyNode();
            final String key = YamlParserUtils.transformScalarNodeToString(context, nodeTuple.getKeyNode());
            final String value = YamlParserUtils.transformScalarNodeToString(context, nodeTuple.getValueNode());

            if (Constants.isPermittedExtraction(key))
            {
                if (extractionMethod == null)
                {
                    // no extraction set so far
                    extractionMethod = key;
                    extractionExpression = value;
                    extractionMethodNode = keyNode;
                }
                else if ((extractionMethod.equals(Constants.COOKIE) || extractionMethod.equals(Constants.HEADER))
                         && key.equals(Constants.REGEXP))
                {
                    // upgrade Cookie/Header to Cookie/Header with regex
                    additionalRegex = value;
                }
                else if (extractionMethod.equals(Constants.REGEXP) && (key.equals(Constants.COOKIE) || key.equals(Constants.HEADER)))
                {
                    // upgrade Regex to Cookie/Header with regex
                    extractionMethod = key;
                    additionalRegex = extractionExpression;
                    extractionExpression = value;
                    extractionMethodNode = keyNode;
                }
                else
                {
                    // error: multiple extractions specified
                    throw new ParserException("Node", context, " specifies multiple extraction methods at once.",
                                              nodeTuple.getKeyNode().getStartMark());
                }
            }
            else if (key.equals(Constants.GROUP))
            {
                group = value;
                groupNode = keyNode;
            }
        }

        // validity checks
        if (extractionMethodNode == null)
        {
            // error: no extraction method specified
            throw new ParserException("Node", context, " does not specify a valid extraction method.", null);
        }

        if (groupNode != null)
        {
            if (extractionMethod.equals(Constants.XPATH))
            {
                // error: group not expected here
                throw new ParserException("Node", context, " specifies a Group node which cannot be used together with an XPath node.",
                                          groupNode.getStartMark());
            }
            else if ((extractionMethod.equals(Constants.COOKIE) || extractionMethod.equals(Constants.HEADER)) && additionalRegex == null)
            {
                // error: group without regex
                throw new ParserException("Node", context, " specifies a Group node without a Regex node.", groupNode.getStartMark());
            }
        }

        // build the extractor
        switch (extractionMethod)
        {
            case Constants.COOKIE:
                return new CookieExtractor(extractionExpression, additionalRegex, group);

            case Constants.HEADER:
                return new HeaderExtractor(extractionExpression, additionalRegex, group);

            case Constants.REGEXP:
                return new RegexpExtractor(extractionExpression, group);

            case Constants.XPATH:
                return new XpathExtractor(extractionExpression);

            default:
                throw new ParserException("Node", context, " contains a permitted extraction but it is unknown.",
                                          extractionMethodNode.getStartMark());
        }
    }
}
