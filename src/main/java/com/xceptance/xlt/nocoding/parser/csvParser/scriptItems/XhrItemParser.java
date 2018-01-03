package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.XhrSubrequest;

/**
 * Parses a node to a {@link XhrSubrequest}.
 * 
 * @author ckeiner
 */
public class XhrItemParser
{

    /**
     * Parses the node to a {@link XhrSubrequest} by using {@link ActionItemParser} and taking
     * {@link Action#getActionItems()}.
     * 
     * @param node
     *            The node the {@link XhrSubrequest} is in
     * @return An {@link XhrSubrequest} with the specified data
     */
    public XhrSubrequest parse(final JsonNode node)
    {
        // Execute ActionItemParser and save it in action
        final Action action = new ActionItemParser().parse(node);
        // Build a new XhrSubrequest with the actionItems of the action
        return new XhrSubrequest(action.getName(), action.getActionItems());
    }

}
