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

    public abstract List<AbstractActionItem> parse(JsonNode node) throws IOException;

}
