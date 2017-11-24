package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.subrequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.AbstractActionItemParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class SubrequestParser extends AbstractActionItemParser
{

    /**
     * Parses the subrequest item in the action block to List<AbstractSubrequest>
     * 
     * @param node
     *            The node the item starts at
     * @return A list with all specified subrequest under that subrequest block
     * @throws IOException
     */
    @Override
    public List<AbstractActionItem> parse(final JsonNode node) throws IOException
    {
        if (!(node instanceof ArrayNode))
        {
            throw new IllegalArgumentException("Expected ArrayNode in Subrequest but was " + node.getClass().getSimpleName());
        }
        // Initialize Variables
        final List<AbstractSubrequest> subrequest = new ArrayList<AbstractSubrequest>();

        // Get an iterator over the elements
        final Iterator<JsonNode> iterator = node.elements();

        // Iterate over the elements
        while (iterator.hasNext())
        {
            // Get the current node
            final JsonNode current = iterator.next();
            // Get the fieldNames of the current node
            final Iterator<String> fieldNames = current.fieldNames();

            // Iterate over the fieldNames
            while (fieldNames.hasNext())
            {
                // Extract the first fieldName, which specifies which kind of subrequest this is
                final String fieldName = fieldNames.next();
                switch (fieldName)
                {
                    case Constants.XHR:
                        // Create an XhrSubrequestParser and parse it
                        subrequest.add(new XhrSubrequestParser().parse(current.get(fieldName)));
                        break;

                    case Constants.STATIC:
                        // Create a list of urls
                        final List<String> urls = new ArrayList<String>();
                        // Get the node with the urls
                        final JsonNode staticUrls = current.get(fieldName);
                        if (!(staticUrls instanceof ArrayNode))
                        {
                            throw new IllegalArgumentException("Expected ArrayNode in Static block but was "
                                                               + node.getClass().getSimpleName());
                        }
                        // Create an iterator over the elements
                        final Iterator<JsonNode> staticUrlsIterator = staticUrls.elements();
                        while (staticUrlsIterator.hasNext())
                        {
                            // Read the url
                            final String url = ParserUtils.readSingleValue(staticUrlsIterator.next());
                            // Add it to the list
                            urls.add(url);
                        }
                        // Catch empty url list
                        if (urls.isEmpty())
                        {
                            throw new IllegalArgumentException("No urls found");
                        }

                        // Add all request to the static subrequests
                        subrequest.add(new StaticSubrequest(urls));
                        break;

                    default:
                        throw new IOException("No permitted subrequest item: " + fieldName);
                }

            }

        }
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        actionItems.addAll(subrequest);
        return actionItems;
    }

}
