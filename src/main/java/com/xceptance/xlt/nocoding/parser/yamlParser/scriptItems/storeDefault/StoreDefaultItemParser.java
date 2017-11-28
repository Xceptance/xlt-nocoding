package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultItem;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class StoreDefaultItemParser extends AbstractStoreDefaultParser
{

    @Override
    public List<StoreDefault> parse(final JsonNode node) throws IOException
    {
        final List<StoreDefault> defaultItems = new ArrayList<>();
        final String variableName = node.fieldNames().next();
        final String value = ParserUtils.readSingleValue(node.get(variableName));
        defaultItems.add(new StoreDefaultItem(variableName, value));

        return defaultItems;
    }

}
