package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.selector;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.CookieSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.HeaderSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.RegexpSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.XpathSelector;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class SelectorParser
{
    final String identifier;

    public SelectorParser(final String identifier)
    {
        this.identifier = identifier;
    }

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
        // return the selector
        return selector;
    }

}
