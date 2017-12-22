package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.subrequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * The class for parsing static subrequests.
 * 
 * @author ckeiner
 */
public class StaticSubrequestParser
{

    /**
     * Parses the static subrequest item in the static block to a {@link StaticSubrequest}.
     * 
     * @param node
     *            The {@link JsonNode} the item starts at
     * @return A <code>StaticSubrequest</code> with the parsed URLs
     */
    public StaticSubrequest parse(final JsonNode node)
    {
        final List<String> urls = new ArrayList<>();
        if (!(node instanceof ArrayNode))
        {
            throw new IllegalArgumentException("Expected ArrayNode in Static block but was " + node.getClass().getSimpleName());
        }
        // Create an iterator over the elements
        final Iterator<JsonNode> staticUrlsIterator = node.elements();
        while (staticUrlsIterator.hasNext())
        {
            // Read the url
            final String url = ParserUtils.readSingleValue(staticUrlsIterator.next());
            // Add it to the list
            urls.add(url);
        }

        // If there are no urls, throw an Exception
        if (urls.isEmpty())
        {
            throw new IllegalArgumentException("No urls found");
        }
        // Return the specified StaticSubrequest
        return new StaticSubrequest(urls);
    }

}
