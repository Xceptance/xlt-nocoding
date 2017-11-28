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

public class StoreDefaultHeaderParser extends AbstractStoreDefaultParser
{

    @Override
    public List<StoreDefault> parse(final JsonNode node) throws IOException
    {
        final List<StoreDefault> defaultItems = new ArrayList<>();
        if (node.isTextual() && node.textValue().equals(Constants.DELETE))
        {
            defaultItems.add(new StoreDefaultHeader(Constants.HEADERS, Constants.DELETE));
        }
        else
        {
            final Map<String, String> headers = new HeaderParser().parse(node);
            for (final Map.Entry<String, String> header : headers.entrySet())
            {
                defaultItems.add(new StoreDefaultHeader(header.getKey(), header.getValue()));
            }
        }
        return defaultItems;
    }

}
