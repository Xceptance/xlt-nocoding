package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.extractor;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.HeaderExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor.XpathExtractor;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

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
     * {@link Constants#GROUP} is specified in the item at the <code>JsonNode</code> and if <code>Constants#GROUP</code> is
     * specified, verifies the <code>AbstractExtractor</code> is a {@link RegexpExtractor}.
     * 
     * @param node
     *            The <code>JsonNode</code> of the extraction item
     * @return The <code>AbstractExtractor</code> corresponding to the identifier. <br>
     *         For example, {@link Constants#REGEXP} is parsed to a <code>RegexpExtractor</code>.
     */
    public AbstractExtractor parse(final JsonNode node)
    {
        AbstractExtractor extractor = null;
        // Get the associated value
        final String extractorExpression = ParserUtils.readValue(node, identifier);
        final boolean hasGroup = node.has(Constants.GROUP);
        // Build a extractor depending on the name of the selector
        if (identifier.equals(Constants.XPATH))
        {
            extractor = new XpathExtractor(extractorExpression);
        }
        else if (identifier.equals(Constants.REGEXP))
        {
            if (hasGroup)
            {
                extractor = new RegexpExtractor(extractorExpression, ParserUtils.readValue(node, Constants.GROUP));
            }
            else
            {
                extractor = new RegexpExtractor(extractorExpression);
            }
        }
        else if (identifier.equals(Constants.HEADER))
        {
            extractor = new HeaderExtractor(extractorExpression);
        }
        else if (identifier.equals(Constants.COOKIE))
        {
            extractor = new CookieExtractor(extractorExpression);
        }
        else
        {
            throw new NotImplementedException("Permitted Extraction but no parsing specified: " + identifier);
        }

        if (hasGroup && !(extractor instanceof RegexpExtractor))
        {
            throw new IllegalArgumentException(Constants.GROUP + " only allowed with RegexpExtractor, but is "
                                               + extractor.getClass().getSimpleName());
        }

        // Return the extractor
        return extractor;
    }

}
