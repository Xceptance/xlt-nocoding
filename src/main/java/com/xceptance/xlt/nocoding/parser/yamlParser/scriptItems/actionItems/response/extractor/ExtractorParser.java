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
 * Takes an identifier (which is an element of {@link JsonNode#fieldNames()}) of a {@link JsonNode} with the extraction
 * item in it and parses it to an {@link AbstractExtractor}. Also checks if a {@link Constants#GROUP} is specified at
 * the {@link JsonNode} and if {@link Constants#GROUP} is specified, verifies the {@link AbstractExtractor} is a
 * {@link RegexpExtractor}.
 * 
 * @author ckeiner
 */
public class ExtractorParser
{
    /**
     * The identifier of the extraction item, must be in {@link Constants#PERMITTEDEXTRACTIONMODE}.
     */
    final String identifier;

    public ExtractorParser(final String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Parses the extraction item in the {@link JsonNode} to an {@link AbstractExtractor}. Also checks if a group is
     * specified and verifies it is at a {@link RegexpExtractor}.
     * 
     * @param node
     *            The node of the extraction item
     * @return The specified {@link AbstractExtractor}
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
