package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

/**
 * Parses a script item and returns a list of it (since i.e. StoreItemParser creates multiple ScriptItem)
 * 
 * @author ckeiner
 */
public abstract class AbstractScriptItemParser
{

    public abstract List<ScriptItem> parse(JsonParser parser) throws IOException;

}
