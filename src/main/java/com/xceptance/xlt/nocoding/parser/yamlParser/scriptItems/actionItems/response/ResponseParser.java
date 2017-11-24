package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class ResponseParser extends AbstractActionItemParser
{

    /**
     * Parses the response item to the Response object
     * 
     * @param node
     *            The node the item starts at
     * @return The response with the specified values
     * @throws IOException
     */
    @Override
    public List<AbstractActionItem> parse(final JsonNode node) throws IOException
    {

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
            // Get it
            final String fieldName = fieldNames.next();
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
                    responseItems.addAll(new ValidationParser().parse(node.get(fieldName)));
                    XltLogger.runTimeLogger.debug("Added Validation");
                    break;

                case Constants.STORE:
                    responseItems.addAll(new ResponseStoreParser().parse(node.get(fieldName)));
                    XltLogger.runTimeLogger.debug("Added Validation");
                    break;

                default:
                    throw new IOException("No permitted response item: " + fieldName);
            }
        }

        actionItems.add(new Response(responseItems));
        return actionItems;
    }

}
