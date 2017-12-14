package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public abstract class AbstractScriptItemParser
{
    public abstract ScriptItem parse(JsonNode node);
}
