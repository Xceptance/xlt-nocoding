package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.AbstractActionItemParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.HttpcodeValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Parses a response item to a {@link Response} wrapped in a {@link List}<{@link AbstractActionItem}>.
 * 
 * @author ckeiner
 */
public class ResponseParser extends AbstractActionItemParser
{

    /**
     * Parses the response item to a {@link Response}.
     * 
     * @param node
     *            The node the item starts at
     * @return The {@link Response} wrapped in a {@link List}<{@link AbstractActionItem}>
     * @throws IOException
     */
    @Override
    public List<AbstractActionItem> parse(final JsonNode node) throws IllegalArgumentException
    {
        // Verify that an object was used and not an array
        if (!(node instanceof ObjectNode))
        {
            throw new IllegalArgumentException("Expected ObjectNode in response but was " + node.getClass().getSimpleName());
        }

        // Initialize variables
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        String httpcode = null;
        final List<AbstractResponseItem> responseItems = new ArrayList<AbstractResponseItem>();

        // Get an iterator over the fieldNames
        final Iterator<String> fieldNames = node.fieldNames();

        // As long as we have a fieldName
        while (fieldNames.hasNext())
        {
            // Get the next fieldName
            final String fieldName = fieldNames.next();

            // Check if the name is a permitted request item
            if (!Constants.isPermittedResponseItem(fieldName))
            {
                throw new IllegalArgumentException("Not a permitted response item: " + fieldName);
            }

            switch (fieldName)
            {
                case Constants.HTTPCODE:
                    // Extract the httpcode
                    httpcode = ParserUtils.readValue(node, fieldName);
                    // Add the validator for the httpcode to the responseItems
                    responseItems.add(new HttpcodeValidator(httpcode));
                    XltLogger.runTimeLogger.debug("Added Httpcode " + httpcode);
                    break;

                case Constants.VALIDATION:
                    // Create a new validation parser and add the result to the responseItems
                    responseItems.addAll(new ValidationParser().parse(node.get(fieldName)));
                    XltLogger.runTimeLogger.debug("Added Validation");
                    break;

                case Constants.STORE:
                    // Create a new response store parser and add the result to the responseItems
                    responseItems.addAll(new ResponseStoreParser().parse(node.get(fieldName)));
                    XltLogger.runTimeLogger.debug("Added Validation");
                    break;

                default:
                    throw new NotImplementedException("Permitted response item but no parser specified: " + fieldName);
            }
        }
        // Add the response to the actionItems
        actionItems.add(new Response(responseItems));
        // Return it
        return actionItems;
    }

}
