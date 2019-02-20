package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultValue;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing single, default key-value items. <br>
 * Therefore, it does not parse {@link Constants#HEADERS}, nor {@link Constants#PARAMETERS}, nor
 * {@link Constants#STATIC}, etc.
 *
 * @author ckeiner
 */
public class StoreDefaultValuesParser extends AbstractStoreDefaultSubItemsParser
{

    /**
     * Parses the default item to a list of {@link AbstractStoreDefaultItem}s which consists of a single
     * {@link StoreDefaultValue}.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param defaultItemNode
     *            The {@link Node} the default key-value item start at
     * @return A list of <code>StoreDefault</code>s with the parsed default key-value item.
     */
    @Override
    public List<AbstractStoreDefaultItem> parse(final Mark context, final Node defaultItemNode)
    {
        if (!(defaultItemNode instanceof MappingNode))
        {
            throw new ParserException("Node", context, " contains a " + defaultItemNode.getNodeId() + " but it must contain a mapping",
                                      defaultItemNode.getStartMark());
        }

        // Create new default items list
        final List<AbstractStoreDefaultItem> defaultItems = new ArrayList<>();

        final List<NodeTuple> defaultItemWrapper = ((MappingNode) defaultItemNode).getValue();
        defaultItemWrapper.forEach(defaultItem -> {
            // Get the name of the default item
            final String variableName = YamlParserUtils.transformScalarNodeToString(defaultItemNode.getStartMark(),
                                                                                    defaultItem.getKeyNode());
            // Get the value of the default item
            final String value = YamlParserUtils.transformScalarNodeToString(defaultItemNode.getStartMark(), defaultItem.getKeyNode());
            // Create the new StoreDefaultItem and add it to the default items list
            defaultItems.add(new StoreDefaultValue(variableName, value));
        });

        return defaultItems;
    }

}
