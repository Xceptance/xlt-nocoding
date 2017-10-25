package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;

public class ParameterParser
{

    /**
     * Parses the parameter item to a List<NameValuePair>
     * 
     * @param node
     *            The node the item starts at
     * @return A List containing the parameters
     * @throws IOException
     */
    public List<NameValuePair> parse(final JsonNode node) throws IOException
    {
        // parameters are transformed to a JSONArray, thus we cannot directly use the fieldname iterator

        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        final Iterator<JsonNode> iterator = node.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldName = current.fieldNames();

            while (fieldName.hasNext())
            {
                final String field = fieldName.next();
                String textValue = current.get(field).textValue();
                // TODO talk about this
                // Since parameters might be empty and the parser writes "null" we check for it and then remove it
                if (textValue == null || textValue.equals("null"))
                {
                    // numbers wont get read with "textValue" so we have to first figure out if it is a number
                    textValue = current.get(field).toString();
                    // if we still have null, the field really was null
                    if (textValue == null || textValue.equals("null"))
                    {
                        textValue = "";
                    }
                }
                parameters.add(new NameValuePair(field, textValue));
                XltLogger.runTimeLogger.debug("Added " + field + "=" + textValue + " to parameters");
            }
        }

        return parameters;
    }

}
