package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.storeDefault;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;

/**
 * Abstract class that defines the method for parsing default items.
 * 
 * @author ckeiner
 */
public abstract class AbstractStoreDefaultParser
{
    /**
     * Parses the default item at the specified {@link JsonNode}.
     * 
     * @param defaultItemNode
     *            The <code>JsonNode</code> the default item starts at
     * @return {@link StoreDefault}
     */
    public abstract List<StoreDefault> parse(JsonNode defaultItemNode);
}
