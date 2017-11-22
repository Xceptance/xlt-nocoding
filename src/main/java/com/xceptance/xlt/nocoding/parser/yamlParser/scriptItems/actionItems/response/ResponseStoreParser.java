package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.selector.SelectorParser;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.RegexpSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.GroupResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.ResponseStore;
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

                /*
                 * Substructure of validation
                 */

                // Get the fieldNames of the substructure
                final Iterator<String> name = storeContent.fieldNames();
                // Iterate over the content
                if (name.hasNext())
                {
                    // Build a selector depending on the next element in the iterator
                    final AbstractSelector selector = new SelectorParser(name.next()).parse(storeContent);
                    // If we have another name
                    if (name.hasNext())
                    {
                        final String nextName = name.next();
                        // Verify the selector is a RegexpSelector, as no other AbstractSelector has a second argument
                        if (selector instanceof RegexpSelector)
                        {
                            // Verify the name of the next name is the allowed field "Group"
                            if (nextName.equals(Constants.GROUP))
                            {
                                // And create a GroupResponseStore
                                responseStores.add(new GroupResponseStore(variableName, selector,
                                                                          ParserUtils.readValue(storeContent, nextName)));
                            }
                            else
                            {
                                throw new IllegalArgumentException("Not a permitted ResponseStore item : " + nextName);
                            }
                        }
                        else
                        {
                            throw new IllegalArgumentException("Unexpected argument " + nextName);
                        }
                    }
                    // If we don't have another name
                    else
                    {
                        // Simply create a normal ResponseStore
                        responseStores.add(new ResponseStore(variableName, selector));
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
