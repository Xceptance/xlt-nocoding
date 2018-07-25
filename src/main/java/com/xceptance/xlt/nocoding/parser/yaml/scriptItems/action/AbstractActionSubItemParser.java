package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.action;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionSubItem;

/**
 * The class for parsing action items.
 * 
 * @author ckeiner
 */
public abstract class AbstractActionSubItemParser
{

    /**
     * Parses the defined action item at the node.
     * 
     * @param actionItemNode
     *            The {@link JsonNode} the item starts at
     * @return A list of {@link AbstractActionSubItem}
     */
    public abstract List<AbstractActionSubItem> parse(JsonNode actionItemNode);

}
