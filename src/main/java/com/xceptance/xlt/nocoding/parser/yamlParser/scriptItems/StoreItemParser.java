package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Parses the Store block to a {@link List}<{@link ScriptItem}>.
 * 
 * @author ckeiner
 */
public class StoreItemParser extends AbstractScriptItemParser
{

    /**
     * Parses the Store block to a {@link List}<{@link ScriptItem}>.
     * 
     * @param root
     *            The {@link JsonNode} the Store block starts at
     * @return {@link List}<{@link ScriptItem}> with all {@link StoreItem}
     * @throws IOException
     */
    @Override
    public List<ScriptItem> parse(final JsonNode root) throws IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();

        // Get the current JsonNode from the parser where the Store item is located
        final JsonNode jsonNode = root.get(Constants.STORE);

        // Convert the node to a list of NameValuePair so it retains its order
        final List<NameValuePair> storeItems = ParserUtils.getArrayNodeAsNameValuePair(jsonNode);

        for (final NameValuePair storeItem : storeItems)
        {
            // Add a new StoreItem to the scriptItems
            scriptItems.add(new StoreItem(storeItem.getName(), storeItem.getValue()));
            // Log the added item
            XltLogger.runTimeLogger.debug("Added " + storeItem.getName() + "=" + storeItem.getValue() + " to StoreItems");
        }

        // Return all StoreItems
        return scriptItems;

    }

}
