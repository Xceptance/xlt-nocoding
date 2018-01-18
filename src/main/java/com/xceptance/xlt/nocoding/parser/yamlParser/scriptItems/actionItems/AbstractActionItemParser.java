package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;

/**
 * The class for parsing action items.
 * 
 * @author ckeiner
 */
public abstract class AbstractActionItemParser
{

    /**
     * Parses the defined action item at the node.
     * 
     * @param actionItemNode
     *            The {@link JsonNode} the item starts at
     * @return A list of {@link AbstractActionItem}
     */
    public abstract List<AbstractActionItem> parse(JsonNode actionItemNode);

}
