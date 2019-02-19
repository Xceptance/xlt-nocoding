package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
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
     *            The {@link Node} the default parameters start at
     * @return A list of <code>StoreDefault</code>s with the parsed default parameters.
     */
    @Override
    public List<AbstractStoreDefaultItem> parse(final Node defaultParametersNode)
    {
        // Create list of defaultItems
        final List<AbstractStoreDefaultItem> defaultItems = new ArrayList<>();

        if (defaultParametersNode instanceof ScalarNode)
        {
            final String value = YamlParserUtils.transformScalarNodeToString(defaultParametersNode);
            if (value.equals(Constants.DELETE))
            {
                defaultItems.add(new StoreDefaultParameter(Constants.PARAMETERS, Constants.DELETE));
            }
            else
            {
                throw new ParserException("Node at", defaultParametersNode.getStartMark(),
                                          " is " + value + " but needs to be an array or be " + Constants.DELETE, null);
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
