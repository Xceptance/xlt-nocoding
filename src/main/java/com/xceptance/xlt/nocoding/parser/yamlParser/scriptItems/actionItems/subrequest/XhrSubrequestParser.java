package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.subrequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request.RequestParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.ResponseParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.XhrSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class XhrSubrequestParser
{

    /**
     * Parses a XhrSubrequest to a XhrSubrequest Object
     * 
     * @param node
     *            The node the item starts at
     * @return The XhrSubrequest
     * @throws IOException
     */
    public AbstractSubrequest parse(final JsonNode node) throws IOException
    {
        if (!(node instanceof ObjectNode))
        {
            throw new IllegalArgumentException("Expected ObjectNode in Xhr block but was " + node.getClass().getSimpleName());
        }
        // Initialize variables
        String name = null;
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();

        // Extract all fieldNames of the node
        final Iterator<String> fieldNames = node.fieldNames();

        // Iterate over the fieldNames
        while (fieldNames.hasNext())
        {
            // Get the next fieldName
            final String fieldName = fieldNames.next();
            AbstractActionItem actionItem = null;

            switch (fieldName)
            {
                case Constants.NAME:
                    // Check that this is the first item we parse
                    if (actionItems.isEmpty())
                    {
                        name = ParserUtils.readValue(node, fieldName);
                        XltLogger.runTimeLogger.debug("Actionname: " + name);
                        // Continue the loop so we do not create a new subrequest
                        continue;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Name must be defined as first item in action.");
                    }

                case Constants.REQUEST:
                    // Check that this is the first item we parse (excluding name)
                    if (actionItems.isEmpty())
                    {
                        // Log the Request block with the unparsed content
                        XltLogger.runTimeLogger.debug("Request: " + node.get(fieldName).toString());
                        // Create a new request parser and get the first element, so we can set Xhr to true
                        actionItem = new RequestParser().parse(node.get(fieldName)).get(0);
                        if (actionItem instanceof Request)
                        {
                            ((Request) actionItem).setXhr("true");
                        }
                        else
                        {
                            throw new IOException("Could not convert Request Item to Request Object: " + fieldName);
                        }
                        break;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Request cannot be defined after a response or subrequest.");
                    }

                case Constants.RESPONSE:
                    // Check that no subrequest was defined beforehand
                    if (actionItems.isEmpty() || actionItems.get(0) instanceof Request)
                    {
                        // Log the Response block with the unparsed content
                        XltLogger.runTimeLogger.debug("Response: " + node.get(fieldName).toString());
                        // Create a new ResponseParser and parse the response
                        actionItem = new ResponseParser().parse(node.get(fieldName)).get(0);
                        break;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Response mustn't be defined after subrequests.");
                    }

                case Constants.SUBREQUESTS:
                    XltLogger.runTimeLogger.debug("Subrequests: " + node.get(fieldName).toString());
                    // Create a new SubrequestParser and parse the subrequest
                    actionItem = new SubrequestParser().parse(node.get(fieldName)).get(0);
                    break;

                default:
                    throw new IOException("No permitted xhr subrequest item: " + fieldName);
            }
            actionItems.add(actionItem);
        }

        // Create a new Subrequest
        final AbstractSubrequest subrequest = new XhrSubrequest(name, actionItems);
        // Return the subrequest
        return subrequest;
    }

}
