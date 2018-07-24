package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.actionItems.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yaml.scriptItems.actionItems.response.extractor.ExtractorParser;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
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
            throw new IllegalArgumentException("Expected ArrayNode after Store, but was " + responseStoreNode.getClass().getSimpleName());
        }
        final List<AbstractResponseStore> responseStores = new ArrayList<AbstractResponseStore>();
        // Get an Iterator over every ResponseStore
        final Iterator<JsonNode> iteratorOverStores = responseStoreNode.elements();

        // As long as we have another element
        while (iteratorOverStores.hasNext())
        {
            // Get the next element, therefore the ObjectNode with a single ResponseStore in it
            final JsonNode storeNode = iteratorOverStores.next();
            final AbstractResponseStore responseStore = parseSingleStore(storeNode);
            // Print a debug statement
            XltLogger.runTimeLogger.debug("Added " + responseStore.getVariableName() + " to the response stores.");
            responseStores.add(responseStore);
        }
        return responseStores;
    }

    public AbstractResponseStore parseSingleStore(final JsonNode singleStore)
    {
        AbstractResponseStore responseStore = null;
        // Get an iterator over the fieldNames, which should only be the name of the variable
        final String variableName = singleStore.fieldNames().next();

        // Get the substructure, that is the ObjectNode with the information of the ResponseStore
        final JsonNode storeContent = singleStore.get(variableName);
        // Verify that it is an ObjectNode
        if (!(storeContent instanceof ObjectNode))
        {
            throw new IllegalArgumentException("Expected ObjectNode after " + variableName + ", but was "
                                               + storeContent.getClass().getSimpleName());
        }
        // And get an iterator over the fieldNames, that is the content of a single ResponseStore
        final Iterator<String> contentFields = storeContent.fieldNames();
        // If we have a next name
        if (contentFields.hasNext())
        {
            AbstractExtractor extractor = null;
            // Build an extractor depending on the next element in the iterator
            String nextName = contentFields.next();
            if (Constants.isPermittedExtraction(nextName))
            {
                extractor = new ExtractorParser(nextName).parse(storeContent);
                // If we have another name, it must be Constants.GROUP
                if (contentFields.hasNext())
                {
                    nextName = contentFields.next();
                    // Verify the value of the nextName is the allowed field Constants.GROUP
                    if (nextName.equals(Constants.GROUP))
                    {
                        // Do nothing
                    }
                    else
                    {
                        throw new IllegalArgumentException("Unexpected argument " + nextName);
                    }
                }
                responseStore = new ResponseStore(variableName, extractor);
            }
        }

        if (responseStore == null)
        {
            throw new IllegalArgumentException("Could not create an AbstractResponseStore. Is an extraction missing?");
        }
        // Return all responseStores
        return responseStore;
    }

}
