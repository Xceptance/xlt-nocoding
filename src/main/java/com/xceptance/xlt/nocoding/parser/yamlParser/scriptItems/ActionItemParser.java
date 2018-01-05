package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.AbstractActionItemParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request.RequestParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.ResponseParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.subrequest.SubrequestParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * The class for parsing an action item.
 * 
 * @author ckeiner
 */
public class ActionItemParser extends AbstractScriptItemParser
{

    /**
     * Parses the action item to a list of {@link ScriptItem}s.
     * 
     * @param root
     *            The {@link JsonNode} with the the action item
     * @return A list of <code>ScriptItem</code>s containing a single {@link Action}.
     */
    @Override
    public List<ScriptItem> parse(final JsonNode root)
    {
        // Initialize variables
        String name = null;
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>(3);
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>(1);

        // TODO root.elements? wieso? -> durch fieldNames ersetzen?
        // Create a jsonNode iterator so we can iterate over the ArrayNode
        final Iterator<JsonNode> iterator = root.elements();

        // While we have elements in our Node
        while (iterator.hasNext())
        {
            // Get the next element
            final JsonNode node = iterator.next();
            // Verify that this is either a NullNode or an ObjectNode
            if (!(node instanceof NullNode) && !(node instanceof ObjectNode))
            {
                throw new IllegalArgumentException("Action must either be emtpy or an ObjectNode, and not a "
                                                   + node.getClass().getSimpleName());
            }
            // Get the fieldName of the objects in the array node
            final Iterator<String> fieldNames = node.fieldNames();

            while (fieldNames.hasNext())
            {
                // Get the next fieldName
                final String fieldName = fieldNames.next();
                AbstractActionItemParser actionItemParser = null;

                // Check if the name is a permitted action item
                if (!Constants.isPermittedActionItem(fieldName))
                {
                    throw new IllegalArgumentException("Not a permitted action item: " + fieldName);
                }

                // Differentiate between what kind of ActionItem this is
                switch (fieldName)
                {
                    case Constants.NAME:
                        // Check that this is the first item we parse
                        if (actionItems.isEmpty())
                        {
                            // Save the name
                            name = ParserUtils.readValue(node, fieldName);
                            if (name != null)
                            {
                                XltLogger.runTimeLogger.debug("Actionname: " + name);
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
                            XltLogger.runTimeLogger.debug("Request: " + node.get(fieldName).toString());
                            // Set parser to the request parser
                            actionItemParser = new RequestParser();
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
                            XltLogger.runTimeLogger.debug("Response: " + node.get(fieldName).toString());
                            // Set parser to the response parser
                            actionItemParser = new ResponseParser();
                            break;
                        }
                        else
                        {
                            throw new IllegalArgumentException("Response mustn't be defined after subrequests.");
                        }

                    case Constants.SUBREQUESTS:
                        XltLogger.runTimeLogger.debug("Subrequests: " + node.get(fieldName).toString());
                        // Set parser to subrequest parser
                        actionItemParser = new SubrequestParser();
                        break;

                    default:
                        // If it has any other value, throw a NotImplementedException
                        throw new NotImplementedException("Permitted action item but no parsing specified: " + fieldName);
                }

                // If we specified an actionItemParser
                if (actionItemParser != null)
                {
                    // Parse the current item and add it to the actionItems
                    actionItems.addAll(actionItemParser.parse(node.get(fieldName)));
                }
            }
        }
        // Add the action to the script items
        scriptItems.add(new Action(name, actionItems));

        // Return all scriptItems
        return scriptItems;
    }

}
