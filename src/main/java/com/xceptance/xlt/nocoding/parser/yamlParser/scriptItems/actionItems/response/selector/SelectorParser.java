package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.selector;

import java.util.Iterator;

import org.junit.Assert;

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
    final Iterator<String> iterator;

    public SelectorParser(final Iterator<String> iterator)
    {
        this.iterator = iterator;
    }

    public AbstractSelector parse(final JsonNode node)
    {
        AbstractSelector selector = null;

        if (iterator.hasNext())
        {
            // Get the next expression
            final String selectorName = iterator.next();
            // Verify its a permitted selection mode
            Assert.assertTrue("Not permitted selection mode: " + selectorName, Constants.isPermittedSelectionMode(selectorName));
            // Get the associated value
            final String selectorExpression = ParserUtils.readValue(node, selectorName);
            // Build a selector depending on the name of the selector
            if (selectorName.equals(Constants.XPATH))
            {
                selector = new XpathSelector(selectorExpression);
            }
            else if (selectorName.equals(Constants.REGEXP))
            {
                selector = new RegexpSelector(selectorExpression);
            }
            else if (selectorName.equals(Constants.HEADER))
            {
                selector = new HeaderSelector(selectorExpression);
            }
            else if (selectorName.equals(Constants.COOKIE))
            {
                selector = new CookieSelector(selectorExpression);
            }
        }
        // return the selector
        return selector;
    }

}
