package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.store;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.scriptItems.AbstractScriptItemParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.store.Store;

/**
 * The class for parsing store items.
 * 
 * @author ckeiner
 */
public class StoreParser extends AbstractScriptItemParser
{

    /**
     * Parses the store item to a list of {@link ScriptItem}s.
     * 
     * @param storeNode
     *            The {@link JsonNode} with the store item
     * @return A list of <code>ScriptItem</code>s with all specified {@link Store}s.
     */
    @Override
    public List<ScriptItem> parse(final JsonNode storeNode)
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();

        // Convert the node to a list of NameValuePair so it retains its order
        final List<NameValuePair> storeItems = YamlParserUtils.getArrayNodeAsNameValuePair(storeNode);

        for (final NameValuePair storeItem : storeItems)
        {
            // Add a new StoreItem to the scriptItems
            scriptItems.add(new Store(storeItem.getName(), storeItem.getValue()));
            // Log the added item
            XltLogger.runTimeLogger.debug("Added " + storeItem.getName() + "=" + storeItem.getValue() + " to StoreItems");
        }

        // Return all StoreItems
        return scriptItems;

    }

}
