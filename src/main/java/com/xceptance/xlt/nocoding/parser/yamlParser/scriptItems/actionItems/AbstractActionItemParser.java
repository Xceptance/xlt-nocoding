package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;

/**
 * Parses an ActionItem and returns a list of it (since a subrequest can consist of multiple ActionItem)
 * 
 * @author ckeiner
 */
public abstract class AbstractActionItemParser
{

    /**
     * Parses the defined action item at the node.
     * 
     * @param node
     *            The node the item starts at
     * @return {@link List}<{@link AbstractActionItem}>
     * @throws IOException
     */
    public abstract List<AbstractActionItem> parse(JsonNode node) throws IOException;

}
