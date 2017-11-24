package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.selector;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.CookieSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.HeaderSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.RegexpSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.XpathSelector;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Takes an identifier (which is an element of {@link JsonNode#fieldNames()}) of a {@link JsonNode} with the selection
 * item in it and parses it to an {@link AbstractSelector}.
 * 
 * @author ckeiner
 */
public class SelectorParser
{
    /**
     * The identifier of the selection item, should be an item in {@link Constants#PERMITTEDSELECTIONMODE}.
     */
    final String identifier;

    public SelectorParser(final String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Parses the selection item in node to an {@link AbstractSelector}
     * 
     * @param node
     *            The node of the validation item
     * @return The specified {@link AbstractSelector}
     */
    public AbstractSelector parse(final JsonNode node)
    {
        AbstractSelector selector = null;
        // Get the associated value
        final String selectorExpression = ParserUtils.readValue(node, identifier);
        // Build a selector depending on the name of the selector
        if (identifier.equals(Constants.XPATH))
        {
            selector = new XpathSelector(selectorExpression);
        }
        else if (identifier.equals(Constants.REGEXP))
        {
            selector = new RegexpSelector(selectorExpression);
        }
        else if (identifier.equals(Constants.HEADER))
        {
            selector = new HeaderSelector(selectorExpression);
        }
        else if (identifier.equals(Constants.COOKIE))
        {
            selector = new CookieSelector(selectorExpression);
        }
        else
        {
            throw new IllegalArgumentException("Unknown Selection : " + identifier);
        }
        // return the selector
        return selector;
    }

}
