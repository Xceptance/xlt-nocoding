package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultStaticSubrequest;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing default static subrequests.
 *
 * @author ckeiner
 */
public class StoreDefaultStaticSubrequestsParser extends AbstractStoreDefaultSubItemsParser
{

    /**
     * Parses the static subrequest list item to a list of {@link AbstractStoreDefaultItem}s which consists of multiple
     * {@link StoreDefaultStaticSubrequest}.
     *
     * @param staticNode
     *            The {@link Node} the default static subrequests start at
     * @return A list of <code>StoreDefault</code>s with the parsed default static subrequests.
     */
    @Override
    public List<AbstractStoreDefaultItem> parse(final Node staticNode)
    {
        // Create list of defaultItems
        final List<AbstractStoreDefaultItem> defaultItems = new ArrayList<>();

        if (staticNode instanceof ScalarNode)
        {
            final String value = YamlParserUtils.transformScalarNodeToString(staticNode);
            if (value.equals(Constants.DELETE))
            {
                defaultItems.add(new StoreDefaultStaticSubrequest(Constants.DELETE));
            }
            else
            {
                throw new ParserException("Node at", staticNode.getStartMark(),
                                          " is " + value + " but needs to be an array or be " + Constants.DELETE, null);
            }
        }
        else
        {
            for (final String url : YamlParserUtils.getSequenceNodeAsStringList(staticNode))
            {
                // Create a new StoreDefaultStatic and add it to the defaultItems
                defaultItems.add(new StoreDefaultStaticSubrequest(url));
            }
        }
        // Return all default items
        return defaultItems;
    }

}
