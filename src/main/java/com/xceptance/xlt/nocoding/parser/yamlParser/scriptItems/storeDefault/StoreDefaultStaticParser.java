package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultStatic;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * The class for parsing default static subrequests.
 * 
 * @author ckeiner
 */
public class StoreDefaultStaticParser extends AbstractStoreDefaultParser
{

    /**
     * Parses the static subrequest list item to a list of {@link StoreDefault}s which consists of multiple
     * {@link StoreDefaultStatic}.
     * 
     * @param node
     *            The {@link JsonNode} the default static subrequests start at
     * @return A list of <code>StoreDefault</code>s with the parsed default static subrequests.
     */
    @Override
    public List<StoreDefault> parse(final JsonNode node)
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
                defaultItems.add(new StoreDefaultStatic(Constants.DELETE));
            }
            else
            {
                throw new IllegalArgumentException("Default Static must be an ArrayNode or textual and contain " + Constants.DELETE
                                                   + " and not " + node.textValue());
            }
        }
        else
        {
            // Get an iterator over the elements
            final Iterator<JsonNode> elementIterator = node.elements();
            while (elementIterator.hasNext())
            {
                // Get the next node
                final JsonNode nextNode = elementIterator.next();
                // Read the url
                final String url = ParserUtils.readSingleValue(nextNode);
                // Create a new StoreDefaultStatic and add it to the defaultItems
                defaultItems.add(new StoreDefaultStatic(url));
            }
        }
        // Return all default items
        return defaultItems;
    }

}
