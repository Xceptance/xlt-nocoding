package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.ParserUtil;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.util.Constants;

public class StoreItemParser extends AbstractScriptItemParser
{

    public List<ScriptItem> parse(final JsonParser parser) throws IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        // Get the current JsonNode from the parser where the Store item is located

        final JsonNode jsonNode = ParserUtil.getNodeAt(Constants.STORE, parser);

        // Since a store item is parsed to an array, get the iterator over the elements
        final Iterator<JsonNode> iterator = jsonNode.elements();

        // while we have elements in the array
        while (iterator.hasNext())
        {
            // Get the next node
            final JsonNode current = iterator.next();
            // The array consists of objects, so an ArrayNode has ObjectNodes. Therefore, extract the fieldNames iterator
            final Iterator<String> fieldNames = current.fieldNames();

            // While we have field names
            while (fieldNames.hasNext())
            {
                // Get the next field name (which is also the name of the variable we want to store)
                final String fieldName = fieldNames.next();
                // And extract the value (which is the value of the variable)
                final String textValue = current.get(fieldName).textValue();
                // Construct the StoreItem with the fieldName and the textValue
                scriptItems.add(new StoreItem(fieldName, textValue));
                XltLogger.runTimeLogger.debug("Added " + fieldName + "=" + textValue + " to parameters");
            }
        }
        // Return all StoreItems
        return scriptItems;

    }

}
