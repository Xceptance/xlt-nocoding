package com.xceptance.xlt.nocoding.parser.yaml.command.store;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.util.NameValuePair;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.store.Store;
import com.xceptance.xlt.nocoding.command.store.StoreClear;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.util.Constants;

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
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param storeNode
     *            The {@link Node} with the store item
     * @return A list of <code>ScriptItem</code>s with all specified {@link Store}s.
     */
    public static List<Command> parse(final Mark context, final Node storeNode)
    {
        final List<Command> scriptItems = new ArrayList<>();

        // Store: Delete
        if (storeNode instanceof ScalarNode)
        {
            final String value = YamlParserUtils.transformScalarNodeToString(context, storeNode);
            if (Constants.DELETE.equals(value))
            {
                // that is Store: Delete
                scriptItems.add(new StoreClear());
            }
            else
            {
                throw new ParserException("Node", context,
                                          " contains " + value + " but it must either contain an array or " + Constants.DELETE,
                                          storeNode.getStartMark());
            }
        }
        // Store:
        // Name: Value
        else if (storeNode instanceof SequenceNode)
        {
            // Convert the node to a list of NameValuePair so it retains its order
            final List<NameValuePair> storeItems = YamlParserUtils.getSequenceNodeAsNameValuePair(context, storeNode);

            for (final NameValuePair storeItem : storeItems)
            {
                // Add a new StoreItem to the scriptItems
                scriptItems.add(new Store(storeItem.getName(), storeItem.getValue()));
                // Log the added item
                XltLogger.runTimeLogger.debug("Added " + storeItem.getName() + "=" + storeItem.getValue() + " to StoreItems");
            }
        }
        else
        {
            throw new ParserException("Node", context, " must either contain an array or " + Constants.DELETE, storeNode.getStartMark());
        }

        // Return all StoreItems
        return scriptItems;

    }

}