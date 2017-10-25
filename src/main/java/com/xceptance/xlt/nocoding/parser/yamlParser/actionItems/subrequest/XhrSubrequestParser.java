package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.subrequest;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request.RequestParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.response.ResponseParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.XHRSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;

public class XhrSubrequestParser
{

    /**
     * Parses a XhrSubrequest to a XhrSubrequest Object
     * 
     * @param node
     *            The node the item starts at
     * @return The XhrSubrequest
     * @throws IOException
     */
    public AbstractSubrequest parse(final JsonNode node) throws IOException
    {
        final Iterator<String> fieldNames = node.fieldNames();
        String name = null;
        Request request = null;
        Response response = null;

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
                    final AbstractActionItem actionItem = new RequestParser().parse(node.get(fieldName)).get(0);
                    if (actionItem instanceof Request)
                    {
                        request = (Request) actionItem;
                        request.setXhr("true");
                    }
                    else
                    {
                        throw new IOException("Could not convert Request Item to Request Object: " + fieldName);
                    }
                    break;

                case Constants.RESPONSE:
                    XltLogger.runTimeLogger.debug("Response: " + node.get(fieldName).toString());
                    final AbstractActionItem responseItem = new ResponseParser().parse(node.get(fieldName)).get(0);
                    if (responseItem instanceof Response)
                    {
                        response = (Response) responseItem;
                    }
                    else
                    {
                        throw new IOException("Could not convert Respose Item to Response Object: " + fieldName);
                    }
                    break;

                // case Constants.SUBREQUESTS:
                // XltLogger.runTimeLogger.debug("Subrequests: " + node.get(fieldName).toString());
                // actionItems.addAll(handleSubrequest(node.get(fieldName)));
                // break;

                default:
                    throw new IOException("No permitted xhr subrequest item: " + fieldName);
            }
        }

        final AbstractSubrequest subrequest = new XHRSubrequest(name, request, response);
        return subrequest;
    }

}
