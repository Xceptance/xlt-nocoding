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

    @Override
    public List<AbstractActionItem> parse(final JsonNode node) throws IOException
    {
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        String url = "";
        String method = null;
        String xhr = null;
        String encodeParameters = null;
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        final Map<String, String> headers = new HashMap<String, String>();
        String body = null;
        String encodeBody = null;

        final Iterator<String> fieldNames = node.fieldNames();

        while (fieldNames.hasNext())
        {
            final String fieldName = fieldNames.next();

            switch (fieldName)
            {
                case Constants.URL:
                    url = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("URL: " + url);
                    break;

                case Constants.METHOD:
                    method = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("Method: " + method);
                    break;

                case Constants.XHR:
                    xhr = ParserUtils.readExpectedBooleanValue(node, fieldName);
                    // final String xhr2 = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("Xhr: " + xhr);
                    break;

                case Constants.ENCODEPARAMETERS:
                    encodeParameters = ParserUtils.readExpectedBooleanValue(node, fieldName);
                    // XltLogger.runTimeLogger.debug("EncodeParameters: " + encodeParameters);
                    break;

                case Constants.PARAMETERS:
                    // Parameter Magic
                    parameters.addAll(new ParameterParser().parse(node.get(fieldName)));
                    break;

                case Constants.HEADERS:
                    headers.putAll(new HeaderParser().parse(node.get(fieldName)));
                    break;

                case Constants.BODY:
                    body = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("Body: " + body);
                    break;

                case Constants.ENCODEBODY:
                    encodeBody = ParserUtils.readExpectedBooleanValue(node, fieldName);
                    // XltLogger.runTimeLogger.debug("EncodeBody: " + encodeBody);
                    break;

                default:
                    throw new IOException("No permitted request item: " + fieldName);
            }
        }

        if (url.equals(null) || url.equals(""))
        {
            throw new IOException("Url not specified");
        }

        final Request request = new Request(url);
        request.setMethod(method);
        request.setXhr(xhr);
        request.setEncodeParameters(encodeParameters);
        request.setParameters(parameters);
        request.setHeaders(headers);
        request.setBody(body);
        request.setEncodeBody(encodeBody);

        XltLogger.runTimeLogger.info(request.toSimpleDebugString());

        actionItems.add(request);
        return actionItems;
    }

}
