package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.request.HeaderParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing default headers.
 *
 * @author ckeiner
 */
public class StoreDefaultHeadersParser extends AbstractStoreDefaultSubItemsParser
{

    /**
     * Parses the headers list item to a list of {@link AbstractStoreDefaultItem}s which consists of multiple
     * {@link StoreDefaultHeader}.
     *
     * @param defaultHeadersNode
     *            The {@link Node} the default headers start at
     * @return A list of <code>StoreDefault</code>s with the parsed default headers.
     */
    @Override
    public List<AbstractStoreDefaultItem> parse(final Node defaultHeadersNode)
    {
        // Create list of defaultItems
        final List<AbstractStoreDefaultItem> defaultItems = new ArrayList<>();

        if (defaultHeadersNode instanceof ScalarNode)
        {
            final String value = YamlParserUtils.transformScalarNodeToString(defaultHeadersNode);
            if (value.equals(Constants.DELETE))
            {
                defaultItems.add(new StoreDefaultHeader(Constants.HEADERS, Constants.DELETE));
            }
            else
            {
                throw new ParserException("Node at", defaultHeadersNode.getStartMark(),
                                          " is " + value + " but needs to be an array or be " + Constants.DELETE, null);
            }
        }
        else
        {
            // Parse headers with the header parser
            final Map<String, String> headers = new HeaderParser().parse(defaultHeadersNode);
            for (final Map.Entry<String, String> header : headers.entrySet())
            {
                // Create a StoreDefaultHeader for every header key value pair
                defaultItems.add(new StoreDefaultHeader(header.getKey(), header.getValue()));
            }
        }
        // Return all default items
        return defaultItems;
    }

}
