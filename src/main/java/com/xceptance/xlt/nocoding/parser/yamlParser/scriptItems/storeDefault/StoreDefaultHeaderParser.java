package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request.HeaderParser;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Parses the Headers list item to a {@link List}<{@link StoreDefault}> which consists of multiple
 * {@link StoreDefaultHeader}.
 * 
 * @author ckeiner
 */
public class StoreDefaultHeaderParser extends AbstractStoreDefaultParser
{

    /**
     * Parses the Headers list item to a {@link List}<{@link StoreDefault}> which consists of multiple
     * {@link StoreDefaultHeader}
     * 
     * @param node
     *            The node the default item starts at
     * @return A {@link List}<{@link StoreDefault}> which consists of multiple {@link StoreDefaultHeader}
     * @throws IOException
     */
    @Override
    public List<StoreDefault> parse(final JsonNode node) throws IOException
    {
        // Create list of defaultItems
        final List<StoreDefault> defaultItems = new ArrayList<>();
        // Check if the node is textual
        if (node.isTextual())
        {
            // Check if the textValue is Constants.DELETE
            if (node.textValue().equals(Constants.DELETE))
            {
                // Create a StoreDefaultHeader item that deletes all default headers
                defaultItems.add(new StoreDefaultHeader(Constants.HEADERS, Constants.DELETE));
            }
            else
            {
                throw new IllegalArgumentException("Default Headers must be an ArrayNode or textual and contain " + Constants.DELETE
                                                   + " and not " + node.textValue());
            }
        }
        else
        {
            // Parse headers with the header parser
            final Map<String, String> headers = new HeaderParser().parse(node);
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
