package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class StoreItemParser extends AbstractScriptItemParser
{

    public List<ScriptItem> parse(final JsonParser parser) throws IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        // Get the current JsonNode from the parser where the Store item is located

        final JsonNode jsonNode = ParserUtils.getNodeAt(Constants.STORE, parser);

        final Map<String, String> storeItems = ParserUtils.getArrayNodeAsMap(jsonNode);

        for (final Map.Entry<String, String> storeItem : storeItems.entrySet())
        {
            scriptItems.add(new StoreItem(storeItem.getKey(), storeItem.getValue()));
            XltLogger.runTimeLogger.debug("Added " + storeItem.getKey() + "=" + storeItem.getValue() + " to parameters");
        }

        // Return all StoreItems
        return scriptItems;

    }

}
