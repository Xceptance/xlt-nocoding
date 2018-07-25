package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.request.ParameterParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The Class for parsing default parameters.
 *
 * @author ckeiner
 */
public class StoreDefaultParametersParser extends AbstractStoreDefaultSubItemsParser
{

    /**
     * Parses the parameters list item to a list of {@link AbstractStoreDefaultItem}s, which consists of multiple
     * {@link StoreDefaultParameter}.
     *
     * @param defaultParametersNode
     *            The {@link JsonNode} the default parameters start at
     * @return A list of <code>StoreDefault</code>s with the parsed default parameters.
     */
    @Override
    public List<AbstractStoreDefaultItem> parse(final JsonNode defaultParametersNode)
    {
        // Create list of defaultItems
        final List<AbstractStoreDefaultItem> defaultItems = new ArrayList<>();
        // Check if the node is textual and has the value of Constants.DELETE
        if (defaultParametersNode.isTextual())
        {
            // Check if the textValue is Constants.DELETE
            if (defaultParametersNode.textValue().equals(Constants.DELETE))
            {
                // Create a StoreDefaultParameter item that deletes all default parameters
                defaultItems.add(new StoreDefaultParameter(Constants.PARAMETERS, Constants.DELETE));
            }
            else
            {
                throw new IllegalArgumentException("Default Parameters must be an ArrayNode or textual and contain " + Constants.DELETE
                                                   + " and not " + defaultParametersNode.textValue());
            }
        }
        else
        {
            // Parse parameters with the parameter parser
            final List<NameValuePair> parameters = new ParameterParser().parse(defaultParametersNode);
            for (final NameValuePair parameter : parameters)
            {
                // Create a StoreDefaultParameter for every parameter key value pair
                defaultItems.add(new StoreDefaultParameter(parameter.getName(), parameter.getValue()));
            }
        }
        // Return all default items
        return defaultItems;
    }

}
