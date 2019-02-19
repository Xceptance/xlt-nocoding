package com.xceptance.xlt.nocoding.parser.yaml.command.store;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.nodes.Node;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.store.Store;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;

/**
 * The class for parsing store items.
 *
 * @author ckeiner
 */
public class StoreParser
{

    /**
     * Parses the store item to a list of {@link Command}s.
     *
     * @param storeNode
     *            The {@link Node} with the store item
     * @return A list of <code>ScriptItem</code>s with all specified {@link Store}s.
     */
    public static List<Command> parse(final Node storeNode)
    {
        final List<Command> scriptItems = new ArrayList<>();

        // Convert the node to a list of NameValuePair so it retains its order
        final List<NameValuePair> storeItems = YamlParserUtils.getSequenceNodeAsNameValuePair(storeNode);

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
