package com.xceptance.xlt.nocoding.parser.yaml.command.action.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.response.AbstractResponseSubItem;
import com.xceptance.xlt.nocoding.command.action.response.HttpCodeValidator;
import com.xceptance.xlt.nocoding.command.action.response.Response;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.AbstractActionSubItemParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing the response.
 *
 * @author ckeiner
 */
public class ResponseParser extends AbstractActionSubItemParser
{

    /**
     * Parses the response item to a {@link Response} wrapped in a list of {@link AbstractActionSubItem}s.
     *
     * @param responseNode
     *            The {@link JsonNode} with the response item
     * @return The <code>Response</code> wrapped in a list of <code>AbstractActionItem</code>s
     */
    @Override
    public List<AbstractActionSubItem> parse(final JsonNode responseNode)
    {
        // Verify that an object was used and not an array
        if (!(responseNode instanceof ObjectNode))
        {
            throw new IllegalArgumentException("Expected ObjectNode in response but was " + responseNode.getClass().getSimpleName());
        }

        // Initialize variables
        final List<AbstractActionSubItem> actionItems = new ArrayList<>();
        String httpcode = null;
        final List<AbstractResponseSubItem> responseItems = new ArrayList<>();

        // Get an iterator over the fieldNames
        final Iterator<String> fieldNames = responseNode.fieldNames();

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
                    httpcode = YamlParserUtils.readValue(responseNode, fieldName);
                    // Add the validator for the httpcode to the responseItems
                    responseItems.add(new HttpCodeValidator(httpcode));
                    XltLogger.runTimeLogger.debug("Added HttpcodeValidator with the Httpcode " + httpcode);
                    break;

                case Constants.VALIDATION:
                    // Create a new validation parser and add the result to the responseItems
                    responseItems.addAll(new ValidationParser().parse(responseNode.get(fieldName)));
                    XltLogger.runTimeLogger.debug("Added Validation");
                    break;

                case Constants.STORE:
                    // Create a new response store parser and add the result to the responseItems
                    responseItems.addAll(new ResponseStoreParser().parse(responseNode.get(fieldName)));
                    XltLogger.runTimeLogger.debug("Added Store");
                    break;

                default:
                    throw new NotImplementedException("Permitted response item but no parser specified: " + fieldName);
            }
        }
        // Add a new response with the responseItems to the actionItems
        actionItems.add(new Response(responseItems));
        // Return the actionItems
        return actionItems;
    }

}
