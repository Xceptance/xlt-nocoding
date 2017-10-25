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

        String variableName = null;
        final List<AbstractResponseStore> responseStore = new ArrayList<AbstractResponseStore>();
        // Go through every element
        final Iterator<JsonNode> iterator = node.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldName = current.fieldNames();

            while (fieldName.hasNext())
            {
                // Name of the variable
                variableName = fieldName.next();
                // The substructure
                final JsonNode storeContent = current.get(variableName);
                final Iterator<String> name = storeContent.fieldNames();
                // Iterate over the content
                while (name.hasNext())
                {
                    final String leftHandExpression = name.next();
                    switch (leftHandExpression)
                    {
                        case Constants.XPATH:
                            // Xpath Magic
                            responseStore.add(new XpathStore(variableName, storeContent.get(leftHandExpression).textValue()));
                            break;

                        case Constants.REGEXP:
                            final String pattern = storeContent.get(leftHandExpression).textValue();
                            String group = null;
                            // if we have another fieldName, this means the optional group is specified
                            if (name.hasNext())
                            {
                                group = storeContent.get(name.next()).textValue();
                            }
                            responseStore.add(new RegExpStore(variableName, pattern, group));
                            break;

                        case Constants.HEADER:
                            responseStore.add(new HeaderStore(variableName, storeContent.get(leftHandExpression).textValue()));
                            break;

                        case Constants.COOKIE:
                            responseStore.add(new CookieStore(variableName, storeContent.get(leftHandExpression).textValue()));
                            break;

                        default:
                            throw new IOException("No permitted response store item: " + leftHandExpression);
                    }
                }

                XltLogger.runTimeLogger.debug("Added " + variableName + " to ResponseStore");
            }
        }

        return responseStore;
    }

}
