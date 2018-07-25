package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultHeader;
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
     *            The {@link JsonNode} the default headers start at
     * @return A list of <code>StoreDefault</code>s with the parsed default headers.
     */
    @Override
    public List<AbstractStoreDefaultItem> parse(final JsonNode defaultHeadersNode)
    {
        // Create list of defaultItems
        final List<AbstractStoreDefaultItem> defaultItems = new ArrayList<>();
        // Check if the node is textual
        if (defaultHeadersNode.isTextual())
        {
            // Check if the textValue is Constants.DELETE
            if (defaultHeadersNode.textValue().equals(Constants.DELETE))
            {
                // Create a StoreDefaultHeader item that deletes all default headers
                defaultItems.add(new StoreDefaultHeader(Constants.HEADERS, Constants.DELETE));
            }
            else
            {
                throw new IllegalArgumentException("Default Headers must be an ArrayNode or textual and contain " + Constants.DELETE
                                                   + " and not " + defaultHeadersNode.textValue());
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
