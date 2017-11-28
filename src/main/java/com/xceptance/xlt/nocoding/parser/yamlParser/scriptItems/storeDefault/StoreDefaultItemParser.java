package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultItem;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Parses a simple key-value-item that is neither {@link Constants#HEADERS}, nor {@link Constants#PARAMETERS}, nor
 * {@link Constants#STATIC}.
 * 
 * @author ckeiner
 */
public class StoreDefaultItemParser extends AbstractStoreDefaultParser
{

    /**
     * Parses the key-value-pair to a {@link List}<{@link StoreDefault}>
     * 
     * @param node
     *            The {@link JsonNode} the default item starts at
     * @return A {@link List}<{@link StoreDefault}> containing a single {@link StoreDefaultItem}.
     * @throws IOException
     */
    @Override
    public List<StoreDefault> parse(final JsonNode node) throws IOException
    {
        // Create new default items list
        final List<StoreDefault> defaultItems = new ArrayList<>();
        // Get the name of the default item
        final String variableName = node.fieldNames().next();
        // Get the value of the default item
        final String value = ParserUtils.readSingleValue(node.get(variableName));
        // Create the new StoreDefaultItem and add it to the default items list
        defaultItems.add(new StoreDefaultItem(variableName, value));

        return defaultItems;
    }

}
