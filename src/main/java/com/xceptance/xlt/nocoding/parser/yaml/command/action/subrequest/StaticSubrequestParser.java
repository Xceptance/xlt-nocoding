package com.xceptance.xlt.nocoding.parser.yaml.command.action.subrequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.xceptance.xlt.nocoding.command.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;

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
     * @param staticNode
     *            The {@link JsonNode} the static subrequest item starts at
     * @return A <code>StaticSubrequest</code> with the parsed URLs
     */
    public StaticSubrequest parse(final JsonNode staticNode)
    {
        final List<String> urls = new ArrayList<>();
        // Verify staticNode is an ArrayNode
        if (!(staticNode instanceof ArrayNode))
        {
            throw new IllegalArgumentException("Expected ArrayNode in Static block but was " + staticNode.getClass().getSimpleName());
        }
        // Create an iterator over the elements, that is every url
        final Iterator<JsonNode> staticUrlsIterator = staticNode.elements();
        // As long as there are elements, read the url and save it
        while (staticUrlsIterator.hasNext())
        {
            // Read the url
            final String url = YamlParserUtils.readSingleValue(staticUrlsIterator.next());
            // Add it to the list
            urls.add(url);
        }

        // If there were no urls, throw an Exception
        if (urls.isEmpty())
        {
            throw new IllegalArgumentException("No urls found!");
        }
        // Return the specified StaticSubrequest
        return new StaticSubrequest(urls);
    }

}
