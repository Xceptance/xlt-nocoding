package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.CookieStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.HeaderStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.RegExpStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.XpathStore;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class ResponseStoreParser
{

    /**
     * Parses the store item in the response block to List<AbstractResponseStore>
     * 
     * @param node
     *            The node the item starts at
     * @return A list of the specified store items
     * @throws IOException
     */
    public List<AbstractResponseStore> parse(final JsonNode node) throws IOException
    {
        // Initialize variables
        String variableName = null;
        final List<AbstractResponseStore> responseStores = new ArrayList<AbstractResponseStore>();
        // Get an iterator over the elements
        final Iterator<JsonNode> iterator = node.elements();

        // As long as we have another element
        while (iterator.hasNext())
        {
            // Get it
            final JsonNode current = iterator.next();
            // Get an iterator over the fieldNames
            final Iterator<String> fieldName = current.fieldNames();

            // As long as we have another fieldName
            while (fieldName.hasNext())
            {
                // Extract the fieldName as the name of the variable
                variableName = fieldName.next();
                // Get the substructure (which is an Object)
                final JsonNode storeContent = current.get(variableName);
                // Get the fieldNames of the substructure
                final Iterator<String> name = storeContent.fieldNames();
                // Iterate over the content
                while (name.hasNext())
                {
                    // Get the left hand expression
                    final String leftHandExpression = name.next();
                    switch (leftHandExpression)
                    {
                        case Constants.XPATH:
                            // Create a new XpathStore and add it to all responseStores
                            responseStores.add(new XpathStore(variableName, ParserUtils.readValue(storeContent, leftHandExpression)));
                            break;

                        case Constants.REGEXP:
                            // Extract the pattern
                            final String pattern = ParserUtils.readValue(storeContent, leftHandExpression);
                            String group = null;
                            // If we have another name, this means the optional group is specified
                            if (name.hasNext())
                            {
                                final String nextName = name.next();
                                if (nextName.equals(Constants.GROUP))
                                {
                                    group = ParserUtils.readValue(storeContent, name.next());
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Unknown response store item: " + nextName);
                                }
                            }
                            // Add a new RegExpStore to all responseStores
                            responseStores.add(new RegExpStore(variableName, pattern, group));
                            break;

                        case Constants.HEADER:
                            // Create a new HeaderStore and add it to all responseStores
                            responseStores.add(new HeaderStore(variableName, ParserUtils.readValue(storeContent, leftHandExpression)));
                            break;

                        case Constants.COOKIE:
                            // Create a new CookieStore and add it to all responseStores
                            responseStores.add(new CookieStore(variableName, ParserUtils.readValue(storeContent, leftHandExpression)));
                            break;

                        default:
                            // If we find an unknown item, throw an Exception
                            throw new IOException("No permitted response store item: " + leftHandExpression);
                    }
                }

                // Log the variableName
                XltLogger.runTimeLogger.debug("Added " + variableName + " to ResponseStore");
            }
        }

        // Return all responseStores
        return responseStores;
    }

}
