package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultCookie;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class StoreDefaultCookieParser extends AbstractStoreDefaultParser
{

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
                defaultItems.add(new StoreDefaultCookie(Constants.COOKIES, Constants.DELETE));
            }
            else
            {
                throw new IllegalArgumentException("Default Cookie must be an ArrayNode or textual and contain " + Constants.DELETE
                                                   + " and not " + node.textValue());
            }
        }
        else
        {
            // Parse headers with the header parser
            final List<NameValuePair> cookies = ParserUtils.getArrayNodeAsNameValuePair(node);
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
