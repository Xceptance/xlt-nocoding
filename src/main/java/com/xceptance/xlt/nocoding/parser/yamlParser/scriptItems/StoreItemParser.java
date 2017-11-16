package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class StoreItemParser extends AbstractScriptItemParser
{

    public List<ScriptItem> parse(final JsonNode root) throws IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();

        // Get the current JsonNode from the parser where the Store item is located
        final JsonNode jsonNode = root.get(Constants.STORE);

        // Convert the node to a Map
        final Map<String, String> storeItems = ParserUtils.getArrayNodeAsMap(jsonNode);

        // For each Map Entry
        for (final Map.Entry<String, String> storeItem : storeItems.entrySet())
        {
            // Add a new StoreItem to the scriptItems
            scriptItems.add(new StoreItem(storeItem.getKey(), storeItem.getValue()));
            // Log the added item
            XltLogger.runTimeLogger.debug("Added " + storeItem.getKey() + "=" + storeItem.getValue() + " to StoreItems");
        }

        // Return all StoreItems
        return scriptItems;

    }

}
