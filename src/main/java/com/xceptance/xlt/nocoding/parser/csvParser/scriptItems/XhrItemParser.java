package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public class XhrItemParser extends AbstractScriptItemParser
{

    @Override
    public ScriptItem parse(final JsonNode node)
    {

        // An XhrItemParser is practically an Action, therefore we simply execute ActionParser
        final ScriptItem scriptItems = new ActionItemParser().parse(node);

        return scriptItems;
    }

}
