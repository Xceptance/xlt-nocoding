package com.xceptance.xlt.nocoding.parser.yaml.command.action.subrequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.AbstractActionSubItemParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing subrequests.
 *
 * @author ckeiner
 */
public class SubrequestsParser extends AbstractActionSubItemParser
{

    /**
     * Parses the subrequest item in the action block to a list of {@link AbstractSubrequest}s. A subrequest item can
     * consist of multiple subrequests.
     *
     * @param subrequestNode
     *            The {@link JsonNode} the subrequest item starts at
     * @return A list with all specified subrequest in the node
     */
    @Override
    public List<AbstractActionSubItem> parse(final JsonNode subrequestNode)
    {
        // Verify that we have an ArrayNode
        if (!(subrequestNode instanceof ArrayNode))
        {
            throw new IllegalArgumentException("Expected ArrayNode in Subrequest but was " + subrequestNode.getClass().getSimpleName());
        }
        // Initialize Variables
        final List<AbstractSubrequest> subrequests = new ArrayList<>();

        // Get an iterator over the elements, that is every single subrequest
        final Iterator<JsonNode> iterator = subrequestNode.elements();

        // Iterate over every subrequest
        while (iterator.hasNext())
        {
            // Get the current node, which describes a single subrequest
            final JsonNode current = iterator.next();
            // Get the fieldNames of the current node, which should only contain the type of the subrequest (i.e. XHR,
            // Static)
            final Iterator<String> fieldNames = current.fieldNames();

            // Iterate over the fieldNames
            while (fieldNames.hasNext())
            {
                // Extract the first fieldName, which specifies, what kind of subrequest this is
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
                        subrequests.add(new XhrSubrequestParser().parse(current.get(fieldName)));
                        break;

                    case Constants.STATIC:
                        // Create StaticSubrequestParser and parse the current node
                        subrequests.add(new StaticSubrequestParser().parse(current.get(fieldName)));
                        break;

                    default:
                        throw new NotImplementedException("Permitted subrequest item but no parsing specified: " + fieldName);
                }

            }

        }
        // Create a new AbstractActionItem list
        final List<AbstractActionSubItem> actionItems = new ArrayList<>();
        // Add all subrequests to it
        actionItems.addAll(subrequests);
        // Return the list with all subrequests
        return actionItems;
    }

}
