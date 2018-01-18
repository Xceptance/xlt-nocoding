package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.extractor.ExtractorParser;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing store items in the response item.
 * 
 * @author ckeiner
 */
public class ResponseStoreParser
{

    /**
     * Parses the store item in the response block to a list of {@link AbstractResponseStore}
     * 
     * @param responseStoreNode
     *            The {@link JsonNode} the store item starts at
     * @return A list of all <code>AbstractResponseStore</code>s with the parsed content
     */
    public List<AbstractResponseStore> parse(final JsonNode responseStoreNode)
    {
        // Verify that an array was used and not an object
        if (!(responseStoreNode instanceof ArrayNode))
        {
            throw new IllegalArgumentException("Expected ArrayNode in the store block but was "
                                               + responseStoreNode.getClass().getSimpleName());
        }
        // Initialize variables
        String variableName = null;
        final List<AbstractResponseStore> responseStores = new ArrayList<AbstractResponseStore>();
        // Get an Iterator over every ResponseStore
        final Iterator<JsonNode> iterator = responseStoreNode.elements();

        // As long as we have another element
        while (iterator.hasNext())
        {
            // Get the next element, therefore the ObjectNode with a single ResponseStore in it
            final JsonNode current = iterator.next();
            // Get an iterator over the fieldNames, which should only be the name of the variable
            final Iterator<String> fieldName = current.fieldNames();

            // As long as we have another fieldName
            while (fieldName.hasNext())
            {
                // Get the next fieldName, which is also the name of the variable
                variableName = fieldName.next();

                /*
                 * Substructure of Store
                 */

                // Get the substructure, that is the ObjectNode with the information of the ResponseStore
                final JsonNode storeContent = current.get(variableName);
                // Verify that it is an ObjectNode
                if (!(storeContent instanceof ObjectNode))
                {
                    throw new IllegalArgumentException("Expected ObjectNode after the variable name, " + variableName + ", but was "
                                                       + responseStoreNode.getClass().getSimpleName());
                }
                // And get an iterator over the fieldNames, that is the content of a single ResponseStore
                final Iterator<String> name = storeContent.fieldNames();
                // If we have a next name
                if (name.hasNext())
                {
                    AbstractExtractor extractor = null;
                    // Build an extractor depending on the next element in the iterator
                    String nextName = name.next();
                    if (Constants.isPermittedExtraction(nextName))
                    {
                        extractor = new ExtractorParser(nextName).parse(storeContent);
                        // If we have another name, it must be Constants.GROUP
                        if (name.hasNext())
                        {
                            nextName = name.next();
                            // Verify the value of the nextName is the allowed field Constants.GROUP
                            if (nextName.equals(Constants.GROUP))
                            {
                                // TODO should ResponseStore really check this again? Extractor already does
                                // Verify the extractor is a RegexpExtractor, as no other AbstractExtractor has a second argument
                                if (!(extractor instanceof RegexpExtractor))
                                {
                                    throw new IllegalArgumentException("Not a permitted ResponseStore item : " + nextName);
                                }
                            }
                            else
                            {
                                throw new IllegalArgumentException("Unexpected argument " + nextName);
                            }
                        }
                        responseStores.add(new ResponseStore(variableName, extractor));
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
