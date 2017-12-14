package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public class XhrItemParser extends AbstractScriptItemParser
{

    @Override
    public List<ScriptItem> parse(final JsonNode node)
    {

        // An XhrItemParser is practically an Action, therefore we simply execute ActionParser
        final List<ScriptItem> scriptItems = new ActionItemParser().parse(node);
        // TODO Auto-generated method stub
        return scriptItems;
    }

}
