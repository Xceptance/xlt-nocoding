package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.selector;

import org.apache.commons.lang3.NotImplementedException;

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
 * item in it and parses it to an {@link AbstractSelector}. Also checks if a {@link Constants#GROUP} is specified at the
 * {@link JsonNode} and if {@link Constants#GROUP} is specified, verifies the {@link AbstractSelector} is a
 * {@link RegexpSelector}.
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
     * Parses the selection item in node to an {@link AbstractSelector}. Also checks if a group is specified and verifies it
     * is at a {@link RegexpSelector}.
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
        final boolean hasGroup = node.has(Constants.GROUP);
        // Build a selector depending on the name of the selector
        if (identifier.equals(Constants.XPATH))
        {
            selector = new XpathSelector(selectorExpression);
        }
        else if (identifier.equals(Constants.REGEXP))
        {
            if (hasGroup)
            {
                selector = new RegexpSelector(selectorExpression, ParserUtils.readValue(node, Constants.GROUP));
            }
            else
            {
                selector = new RegexpSelector(selectorExpression);
            }
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
            throw new NotImplementedException("Permitted Selection but no parsing specified: " + identifier);
        }

        if (hasGroup && !(selector instanceof RegexpSelector))
        {
            throw new IllegalArgumentException(Constants.GROUP + " only allowed with RegexpSelector, but is "
                                               + selector.getClass().getSimpleName());
        }

        // return the selector
        return selector;
    }

}
