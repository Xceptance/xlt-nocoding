package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.storeDefault;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultStaticRequest;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing default static subrequests.
 * 
 * @author ckeiner
 */
public class StoreDefaultStaticsParser extends AbstractStoreDefaultSubItemParser
{

    /**
     * Parses the static subrequest list item to a list of {@link AbstractStoreDefaultItem}s which consists of multiple
     * {@link StoreDefaultStaticRequest}.
     * 
     * @param staticNode
     *            The {@link JsonNode} the default static subrequests start at
     * @return A list of <code>StoreDefault</code>s with the parsed default static subrequests.
     */
    @Override
    public List<AbstractStoreDefaultItem> parse(final JsonNode staticNode)
    {
        // Create list of defaultItems
        final List<AbstractStoreDefaultItem> defaultItems = new ArrayList<>();
        // Check if the node is textual
        if (staticNode.isTextual())
        {
            // Check if the textValue is Constants.DELETE
            if (staticNode.textValue().equals(Constants.DELETE))
            {
                // Create a StoreDefaultHeader item that deletes all default headers
                defaultItems.add(new StoreDefaultStaticRequest(Constants.DELETE));
            }
            else
            {
                throw new IllegalArgumentException("Default Static must be an ArrayNode or textual and contain " + Constants.DELETE
                                                   + " and not " + staticNode.textValue());
            }
        }
        else
        {
            // Get an iterator over the elements
            final Iterator<JsonNode> elementIterator = staticNode.elements();
            while (elementIterator.hasNext())
            {
                // Get the next node
                final JsonNode nextNode = elementIterator.next();
                // Read the url
                final String url = YamlParserUtils.readSingleValue(nextNode);
                // Create a new StoreDefaultStatic and add it to the defaultItems
                defaultItems.add(new StoreDefaultStaticRequest(url));
            }
        }
        // Return all default items
        return defaultItems;
    }

}
