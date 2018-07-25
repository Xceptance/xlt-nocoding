package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultCookie;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing default cookies.
 *
 * @author ckeiner
 */
public class StoreDefaultCookiesParser extends AbstractStoreDefaultSubItemsParser
{

    /**
     * Parses the cookies list item to a list of {@link AbstractStoreDefaultItem}s which consists of multiple
     * {@link StoreDefaultCookie}.
     *
     * @param defaultCookiesNode
     *            The {@link JsonNode} the default cookies start at
     * @return A list of <code>StoreDefault</code>s with the parsed default cookies.
     */
    @Override
    public List<AbstractStoreDefaultItem> parse(final JsonNode defaultCookiesNode)
    {
        // Create list of defaultItems
        final List<AbstractStoreDefaultItem> defaultItems = new ArrayList<>();
        // Check if the node is textual
        if (defaultCookiesNode.isTextual())
        {
            // Check if the textValue is Constants.DELETE
            if (defaultCookiesNode.textValue().equals(Constants.DELETE))
            {
                // Create a StoreDefaultHeader item that deletes all default headers
                defaultItems.add(new StoreDefaultCookie(Constants.COOKIES, Constants.DELETE));
            }
            else
            {
                throw new IllegalArgumentException("Default Cookie must be an ArrayNode or textual and contain " + Constants.DELETE
                                                   + " and not " + defaultCookiesNode.textValue());
            }
        }
        else
        {
            // Parse the ArrayNode as NameValuePair
            final List<NameValuePair> cookies = YamlParserUtils.getArrayNodeAsNameValuePair(defaultCookiesNode);
            for (final NameValuePair cookie : cookies)
            {
                // Create a StoreDefaultHeader for every header key value pair
                defaultItems.add(new StoreDefaultCookie(cookie.getName(), cookie.getValue()));
            }
        }
        // Return all default items
        return defaultItems;
    }

}
