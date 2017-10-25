package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.api.util.XltLogger;

public class HeaderParser
{

    /**
     * Parses the header item to a Map<String, String>
     * 
     * @param node
     *            The node the item starts at
     * @return A Map containing the headers
     * @throws IOException
     */
    public Map<String, String> parse(final JsonNode node) throws IOException
    {
        // headers are transformed to a JSONArray

        final Map<String, String> headers = new HashMap<String, String>();

        final Iterator<JsonNode> iterator = node.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldName = current.fieldNames();

            while (fieldName.hasNext())
            {
                final String field = fieldName.next();
                final String textValue = current.get(field).textValue();
                headers.put(field, textValue);
                XltLogger.runTimeLogger.debug("Added " + field + "=" + headers.get(field) + " to parameters");
            }
        }

        return headers;
    }

}
