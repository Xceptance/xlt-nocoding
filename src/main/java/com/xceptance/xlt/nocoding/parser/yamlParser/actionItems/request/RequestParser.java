package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.AbstractActionItemParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class RequestParser extends AbstractActionItemParser
{

    /**
     * Parses a JsonNode to a request object
     * 
     * @param node
     *            The node of the Request
     * @return A list of all {@link AbstractActionItem}s.
     */
    @Override
    public List<AbstractActionItem> parse(final JsonNode node) throws IOException
    {
        // Initialize variables
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        String url = "";
        String method = null;
        String xhr = null;
        String encodeParameters = null;
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        final Map<String, String> headers = new HashMap<String, String>();
        String body = null;
        String encodeBody = null;

        // Get an iterator over the fieldNames
        final Iterator<String> fieldNames = node.fieldNames();

        // As long as we have a fieldName
        while (fieldNames.hasNext())
        {
            // Get it
            final String fieldName = fieldNames.next();

            // Depending on the fieldName, we want to generally do the same thing but assign it to different variables
            switch (fieldName)
            {
                case Constants.URL:
                    url = ParserUtils.readValue(node, fieldName);
                    // XltLogger.runTimeLogger.debug("URL: " + url);
                    break;

                case Constants.METHOD:
                    method = ParserUtils.readValue(node, fieldName);
                    // XltLogger.runTimeLogger.debug("Method: " + method);
                    break;

                case Constants.XHR:
                    xhr = ParserUtils.readValue(node, fieldName);
                    // final String xhr2 = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("Xhr: " + xhr);
                    break;

                case Constants.ENCODEPARAMETERS:
                    encodeParameters = ParserUtils.readValue(node, fieldName);
                    // XltLogger.runTimeLogger.debug("EncodeParameters: " + encodeParameters);
                    break;

                case Constants.PARAMETERS:
                    // Create a new ParameterParser that parses parameters
                    parameters.addAll(new ParameterParser().parse(node.get(fieldName)));
                    break;

                case Constants.HEADERS:
                    // Create a new HeaderParser that parses headers
                    headers.putAll(new HeaderParser().parse(node.get(fieldName)));
                    break;

                case Constants.BODY:
                    body = ParserUtils.readValue(node, fieldName);
                    // XltLogger.runTimeLogger.debug("Body: " + body);
                    break;

                case Constants.ENCODEBODY:
                    encodeBody = ParserUtils.readValue(node, fieldName);
                    // XltLogger.runTimeLogger.debug("EncodeBody: " + encodeBody);
                    break;

                default:
                    // If there is an unknown field, throw an Exception
                    throw new IOException("No permitted request item: " + fieldName);
            }
        }

        // If we do not have a url, throw an Exception
        if (url.equals(null) || url.equals(""))
        {
            throw new IOException("Url not specified");
        }

        // Create request out of the data
        final Request request = new Request(url);
        request.setMethod(method);
        request.setXhr(xhr);
        request.setEncodeParameters(encodeParameters);
        request.setParameters(parameters);
        request.setHeaders(headers);
        request.setBody(body);
        request.setEncodeBody(encodeBody);

        // Print a simple debug string, so you can see what was parsed
        XltLogger.runTimeLogger.info(request.toSimpleDebugString());

        // Add the request to the actionItems
        actionItems.add(request);
        // Return the actionItem list
        return actionItems;
    }

}
