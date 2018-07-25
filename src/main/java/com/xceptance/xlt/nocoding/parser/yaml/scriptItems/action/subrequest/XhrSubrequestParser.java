package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.action.subrequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.scriptItems.action.request.RequestParser;
import com.xceptance.xlt.nocoding.parser.yaml.scriptItems.action.response.ResponseParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.scriptItem.action.request.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.XhrSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing the Xhr Subrequest item.
 * 
 * @author ckeiner
 */
public class XhrSubrequestParser
{

    /**
     * Parses the Xhr item to a {@link XhrSubrequest}.
     * 
     * @param xhrNode
     *            The {@link JsonNode} with the xhr subrequest item
     * @return The <code>XhrSubrequest</code> with the parsed data
     */
    public XhrSubrequest parse(final JsonNode xhrNode)
    {
        // Verify the node is an ObjectNode or a NullNode
        if (!(xhrNode instanceof NullNode) && !(xhrNode instanceof ObjectNode))
        {
            throw new IllegalArgumentException("Expected NullNode or ObjectNode in Xhr block but was "
                                               + xhrNode.getClass().getSimpleName());
        }
        // Initialize variables
        String name = null;
        final List<AbstractActionSubItem> actionItems = new ArrayList<AbstractActionSubItem>();

        // Extract all fieldNames of the node, which is the information of the node (i.e. Name, Request)
        final Iterator<String> fieldNames = xhrNode.fieldNames();

        // Iterate over the fieldNames
        while (fieldNames.hasNext())
        {
            // Get the next fieldName
            final String fieldName = fieldNames.next();
            AbstractActionSubItem actionItem = null;

            // Check if the fieldName is a permitted action item, and thus a permitted XhrSubrequestItem
            if (!Constants.isPermittedActionItem(fieldName))
            {
                throw new IllegalArgumentException("Not a permitted XhrSubrequest item: " + fieldName);
            }

            switch (fieldName)
            {
                case Constants.NAME:
                    // Check that this is the first item we parse
                    if (actionItems.isEmpty())
                    {
                        name = YamlParserUtils.readValue(xhrNode, fieldName);
                        if (name != null)
                        {
                            XltLogger.runTimeLogger.debug("Xhr Subrequest Name: " + name);
                        }
                        break;
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
                        XltLogger.runTimeLogger.debug("Request: " + xhrNode.get(fieldName).toString());
                        // Create a new request parser and get the first element, so we can set Xhr to true
                        actionItem = new RequestParser().parse(xhrNode.get(fieldName)).get(0);
                        if (actionItem instanceof Request)
                        {
                            ((Request) actionItem).setXhr("true");
                        }
                        else
                        {
                            throw new IllegalStateException("Could not convert Request Item to Request Object: " + fieldName);
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
                        XltLogger.runTimeLogger.debug("Response: " + xhrNode.get(fieldName).toString());
                        // Create a new ResponseParser and parse the response
                        actionItem = new ResponseParser().parse(xhrNode.get(fieldName)).get(0);
                        break;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Response mustn't be defined after subrequests.");
                    }

                case Constants.SUBREQUESTS:
                    XltLogger.runTimeLogger.debug("Subrequests: " + xhrNode.get(fieldName).toString());
                    // Create a new SubrequestParser and parse the subrequest
                    actionItem = new SubrequestsParser().parse(xhrNode.get(fieldName)).get(0);
                    break;

                default:
                    throw new NotImplementedException("Permitted XhrSubrequest item but no parsing specified: " + fieldName);
            }
            if (actionItem != null)
            {
                actionItems.add(actionItem);
            }
        }

        // Return the subrequest
        return new XhrSubrequest(name, actionItems);
    }

}
