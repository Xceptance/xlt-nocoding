package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.actionItems.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yaml.scriptItems.actionItems.AbstractActionItemParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * The class for parsing request items to {@link Request}s wrapped in a list of {@link AbstractActionItem}s.
 * 
 * @author ckeiner
 */
public class RequestParser extends AbstractActionItemParser
{

    /**
     * Parses a request item to a <code>Request</code>.
     * 
     * @param requestNode
     *            The {@link JsonNode} with the request in it
     * @return The <code>Request</code> wrapped in a list of <code>AbstractActionItem</code>s.
     */
    @Override
    public List<AbstractActionItem> parse(final JsonNode requestNode)
    {
        // Verify that an object was used and not an array
        if (!(requestNode instanceof ObjectNode))
        {
            throw new IllegalArgumentException("Expected ObjectNode in request but was " + requestNode.getClass().getSimpleName());
        }

        // Initialize variables
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        String url = "";
        String method = null;
        String xhr = null;
        String encodeParameters = null;
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        final Map<String, String> headers = new HashMap<String, String>();
        final Map<String, String> cookies = new LinkedHashMap<String, String>();
        String body = null;
        String encodeBody = null;

        // Get an iterator over the fieldNames
        final Iterator<String> fieldNames = requestNode.fieldNames();

        // As long as we have a fieldName
        while (fieldNames.hasNext())
        {
            // Get the next fieldName
            final String fieldName = fieldNames.next();

            // Check if the name is a permitted request item
            if (!Constants.isPermittedRequestItem(fieldName))
            {
                throw new IllegalArgumentException("Not a permitted request item: " + fieldName);
            }

            // We generally want to read the value of the fieldName but assign it to different variables
            switch (fieldName)
            {
                case Constants.URL:
                    url = ParserUtils.readValue(requestNode, fieldName);
                    // XltLogger.runTimeLogger.debug("URL: " + url);
                    break;

                case Constants.METHOD:
                    method = ParserUtils.readValue(requestNode, fieldName);
                    // XltLogger.runTimeLogger.debug("Method: " + method);
                    break;

                case Constants.XHR:
                    xhr = ParserUtils.readValue(requestNode, fieldName);
                    // final String xhr2 = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("Xhr: " + xhr);
                    break;

                case Constants.ENCODEPARAMETERS:
                    encodeParameters = ParserUtils.readValue(requestNode, fieldName);
                    // XltLogger.runTimeLogger.debug("EncodeParameters: " + encodeParameters);
                    break;

                case Constants.PARAMETERS:
                    // Create a new ParameterParser that parses parameters
                    parameters.addAll(new ParameterParser().parse(requestNode.get(fieldName)));
                    break;

                case Constants.HEADERS:
                    // Create a new HeaderParser that parses headers
                    headers.putAll(new HeaderParser().parse(requestNode.get(fieldName)));
                    break;

                case Constants.COOKIES:
                    cookies.putAll(new CookieParser().parse(requestNode.get(fieldName)));
                    break;

                case Constants.BODY:
                    body = ParserUtils.readValue(requestNode, fieldName);
                    // XltLogger.runTimeLogger.debug("Body: " + body);
                    break;

                case Constants.ENCODEBODY:
                    encodeBody = ParserUtils.readValue(requestNode, fieldName);
                    // XltLogger.runTimeLogger.debug("EncodeBody: " + encodeBody);
                    break;

                default:
                    // If it has any other value, throw a NotImplementedException
                    throw new NotImplementedException("Permitted request item but no parser specified: " + fieldName);
            }
        }

        // Create request out of the data
        final Request request = new Request(url);
        request.setHttpMethod(method);
        request.setXhr(xhr);
        request.setEncodeParameters(encodeParameters);
        request.setParameters(parameters);
        request.setHeaders(headers);
        request.setCookies(cookies);
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
