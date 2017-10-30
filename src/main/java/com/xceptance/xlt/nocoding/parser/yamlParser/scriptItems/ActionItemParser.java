package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.AbstractActionItemParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request.RequestParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.response.ResponseParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.subrequest.SubrequestParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class ActionItemParser extends AbstractScriptItemParser
{

    /**
     * Parses an Action ScriptItem.
     * 
     * @param parser
     *            Where the current parser is
     * @return A list of LightWeightActions containing a single action.
     * @throws IOException
     */
    @Override
    public List<ScriptItem> parse(final JsonParser parser) throws IOException
    {
        // Initialize variables
        String name = null;
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();

        // Get the current objectNode
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        // Create a jsonNode iterator so we can iterate over the ArrayNode
        final Iterator<JsonNode> iterator = objectNode.elements();

        // While we have elements in our Node
        while (iterator.hasNext())
        {
            // Get the next element
            final JsonNode node = iterator.next();
            // Get the fieldName of the objects in the array node
            final Iterator<String> fieldNames = node.fieldNames();

            while (fieldNames.hasNext())
            {
                final String fieldName = fieldNames.next();
                AbstractActionItemParser actionItemParser = null;

                // Differentiate between what kind of ActionItem this is
                switch (fieldName)
                {
                    case Constants.NAME:
                        // Save the name
                        name = node.get(fieldName).textValue();
                        XltLogger.runTimeLogger.debug("Actionname: " + name);
                        break;

                    case Constants.REQUEST:
                        XltLogger.runTimeLogger.debug("Request: " + node.get(fieldName).toString());
                        // Set parser to the request parser
                        actionItemParser = new RequestParser();
                        break;

                    case Constants.RESPONSE:
                        XltLogger.runTimeLogger.debug("Response: " + node.get(fieldName).toString());
                        // Set parser to the response parser
                        actionItemParser = new ResponseParser();
                        break;

                    case Constants.SUBREQUESTS:
                        XltLogger.runTimeLogger.debug("Subrequests: " + node.get(fieldName).toString());
                        // Set parser to subrequest parser
                        actionItemParser = new SubrequestParser();
                        break;

                    default:
                        // We iterate over the field names, so there is no way we have an Array/Object Start. Thus if we find something that
                        // isn't specified, it's not a permitted action item and we want to throw an exception.
                        throw new JsonParseException("No permitted action item: " + fieldName, parser.getCurrentLocation());
                }

                // If we specified an actionItemParser
                if (actionItemParser != null)
                {
                    // Parse the current item and add it to the actionItems
                    actionItems.addAll(actionItemParser.parse(node.get(fieldName)));
                }
            }
        }

        // Check if we got a name
        if (name.equals(null) || name.equals(""))
        {
            throw new IOException("Name is null");
        }

        // Create a new ScriptItem
        final ScriptItem scriptItem = new LightWeigthAction(name, actionItems);
        // And add it to a List of scriptItems so it matches the signature
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>(1);
        scriptItems.add(scriptItem);
        // Return all scriptItems
        return scriptItems;
    }

}
