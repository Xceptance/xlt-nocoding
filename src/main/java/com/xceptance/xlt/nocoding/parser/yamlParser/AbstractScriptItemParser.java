package com.xceptance.xlt.nocoding.parser.yamlParser;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public abstract class AbstractScriptItemParser
{

    public abstract List<ScriptItem> parse(JsonParser parser) throws IOException;

}
