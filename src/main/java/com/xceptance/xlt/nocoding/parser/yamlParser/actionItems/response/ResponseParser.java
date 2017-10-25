package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.AbstractActionItemParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class ResponseParser extends AbstractActionItemParser
{

    public ResponseParser()
    {
        // TODO Auto-generated constructor stub
    }

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
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        String httpcode = null;
        final List<AbstractValidator> validators = new ArrayList<AbstractValidator>();
        final List<AbstractResponseStore> responseStore = new ArrayList<AbstractResponseStore>();

        final Iterator<String> fieldNames = node.fieldNames();

        while (fieldNames.hasNext())
        {
            final String fieldName = fieldNames.next();
            switch (fieldName)
            {
                case Constants.HTTPCODE:
                    httpcode = ParserUtils.readExpectedIntegerValue(node, fieldName);
                    XltLogger.runTimeLogger.debug("Added Httpcode " + httpcode);
                    break;

                case Constants.VALIDATION:
                    validators.addAll(new ValidationParser().parse(node.get(fieldName)));
                    XltLogger.runTimeLogger.debug("Added Validation");
                    break;

                case Constants.STORE:
                    responseStore.addAll(new ResponseStoreParser().parse(node.get(fieldName)));
                    XltLogger.runTimeLogger.debug("Added Validation");
                    break;

                default:
                    throw new IOException("No permitted response item: " + fieldName);
            }
        }

        actionItems.add(new Response(httpcode, responseStore, validators));
        return actionItems;
    }

}
