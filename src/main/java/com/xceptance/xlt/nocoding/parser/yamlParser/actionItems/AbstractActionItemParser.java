package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;

public abstract class AbstractActionItemParser
{

    public abstract List<AbstractActionItem> parse(JsonNode node) throws IOException;

}
