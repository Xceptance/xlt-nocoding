package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.XhrSubrequest;

public class XhrItemParser
{

    public AbstractSubrequest parse(final JsonNode node)
    {

        // An XhrItemParser is practically an Action, therefore we simply execute ActionParser
        final Action action = new ActionItemParser().parse(node);
        return new XhrSubrequest(null, action.getActionItems());
    }

}
