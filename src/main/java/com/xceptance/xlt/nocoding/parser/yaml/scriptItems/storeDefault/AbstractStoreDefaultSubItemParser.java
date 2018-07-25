package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.storeDefault;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.AbstractStoreDefaultItem;

/**
 * Abstract class that defines the method for parsing default items.
 * 
 * @author ckeiner
 */
public abstract class AbstractStoreDefaultSubItemParser
{
    /**
     * Parses the default item at the specified {@link JsonNode}.
     * 
     * @param defaultItemNode
     *            The <code>JsonNode</code> the default item starts at
     * @return {@link AbstractStoreDefaultItem}
     */
    public abstract List<AbstractStoreDefaultItem> parse(JsonNode defaultItemNode);
}
