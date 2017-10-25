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
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.util.Constants;

public class ActionItemParser extends AbstractScriptItemParser
{

    @Override
    public List<ScriptItem> parse(final JsonParser parser) throws IOException
    {
        // Initialize variables
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        String name = null;
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();

        // Create a jsonNode iterator so we can iterate over the ArrayNode
        final ObjectNode objectNode = getObjectNodeAt(parser);
        final Iterator<JsonNode> iterator = objectNode.elements();

        while (iterator.hasNext())
        {
            final JsonNode node = iterator.next();
            // Get the fieldName of the objects in the array node
            final Iterator<String> fieldNames = node.fieldNames();

            while (fieldNames.hasNext())
            {
                final String fieldName = fieldNames.next();

                switch (fieldName)
                {
                    case Constants.NAME:
                        name = node.get(fieldName).textValue();
                        XltLogger.runTimeLogger.debug("Actionname: " + name);
                        break;

                    case Constants.REQUEST:
                        XltLogger.runTimeLogger.debug("Request: " + node.get(fieldName).toString());
                        actionItems.add(handleRequest(node.get(fieldName)));
                        break;

                    case Constants.RESPONSE:
                        XltLogger.runTimeLogger.debug("Response: " + node.get(fieldName).toString());
                        actionItems.add(handleResponse(node.get(fieldName)));
                        break;

                    case Constants.SUBREQUESTS:
                        XltLogger.runTimeLogger.debug("Subrequests: " + node.get(fieldName).toString());
                        actionItems.addAll(handleSubrequest(node.get(fieldName)));
                        break;

                    default:
                        // We iterate over the field names, so there is no way we have an Array/Object Start
                        throw new JsonParseException("No permitted action item: " + fieldName, parser.getCurrentLocation());
                }
            }
        }

        final ScriptItem scriptItem = new LightWeigthAction(name, actionItems);
        scriptItems.add(scriptItem);
        return scriptItems;
    }

}
