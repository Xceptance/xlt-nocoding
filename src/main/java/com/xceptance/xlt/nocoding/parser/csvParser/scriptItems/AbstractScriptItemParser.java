package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public abstract class AbstractScriptItemParser
{
    public abstract List<ScriptItem> parse(JsonNode node);
}
