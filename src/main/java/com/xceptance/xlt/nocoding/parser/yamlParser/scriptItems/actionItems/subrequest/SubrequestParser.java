package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.subrequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.AbstractActionItemParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Parses the Subrequests item in the action block to {@link List}<{@link AbstractSubrequest}> since there can be
 * multiple {@link AbstractSubrequest} in the Subrequests block.
 * 
 * @author ckeiner
 */
public class SubrequestParser extends AbstractActionItemParser
{

    /**
     * Parses the subrequest item in the action block to {@link List}<{@link AbstractSubrequest}>
     * 
     * @param node
     *            The ArrayNode the item starts at
     * @return A list with all specified subrequest under that subrequest block
     */
    @Override
    public List<AbstractActionItem> parse(final JsonNode node)
    {
        // Verify that we have an ArrayNode
        if (!(node instanceof ArrayNode))
        {
            throw new IllegalArgumentException("Expected ArrayNode in Subrequest but was " + node.getClass().getSimpleName());
        }
        // Initialize Variables
        final List<AbstractSubrequest> subrequest = new ArrayList<AbstractSubrequest>();

        // Get an iterator over the elements
        final Iterator<JsonNode> iterator = node.elements();

        // Iterate over the elements
        while (iterator.hasNext())
        {
            // Get the current node
            final JsonNode current = iterator.next();
            // Get the fieldNames of the current node
            final Iterator<String> fieldNames = current.fieldNames();

            // Iterate over the fieldNames
            while (fieldNames.hasNext())
            {
                // Extract the first fieldName, which specifies which kind of subrequest this is
                final String fieldName = fieldNames.next();

                // Check if the name is a permitted request item
                if (!Constants.isPermittedSubRequestItem(fieldName))
                {
                    throw new IllegalArgumentException("Not a permitted subrequest item: " + fieldName);
                }

                // Depending on the name, create the correct Parser and execute it
                switch (fieldName)
                {
                    case Constants.XHR:
                        // Create an XhrSubrequestParser and parse it
                        subrequest.add(new XhrSubrequestParser().parse(current.get(fieldName)));
                        break;

                    case Constants.STATIC:
                        // Create StaticSubrequestParser and parse the current node
                        subrequest.add(new StaticSubrequestParser().parse(current.get(fieldName)));
                        break;

                    default:
                        throw new NotImplementedException("Permitted subrequest item but no parsing specified: " + fieldName);
                }

            }

        }
        // Create new AbstractActionItem list
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        // Add all subrequests to it
        actionItems.addAll(subrequest);
        // Return the list with all subrequests
        return actionItems;
    }

}
