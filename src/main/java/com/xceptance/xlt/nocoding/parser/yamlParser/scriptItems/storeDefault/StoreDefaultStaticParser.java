package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultStatic;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class StoreDefaultStaticParser extends AbstractStoreDefaultParser
{

    @Override
    public List<StoreDefault> parse(final JsonNode node) throws IOException
    {
        final List<StoreDefault> defaultItems = new ArrayList<>();
        if (node.isTextual() && node.textValue().equals(Constants.DELETE))
        {
            defaultItems.add(new StoreDefaultStatic(Constants.STATIC, Constants.DELETE));
        }
        else
        {
            final Iterator<JsonNode> elementIterator = node.elements();
            while (elementIterator.hasNext())
            {
                final JsonNode nextNode = elementIterator.next();
                final String value = ParserUtils.readSingleValue(nextNode);
                // variableName isn't used, but we set it to "Static" to remain some sort of meaning
                defaultItems.add(new StoreDefaultStatic(Constants.STATIC, value));
            }
        }
        return defaultItems;
    }

}
