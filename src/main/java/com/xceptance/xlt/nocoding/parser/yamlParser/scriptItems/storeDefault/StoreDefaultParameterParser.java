package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request.ParameterParser;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.util.Constants;

public class StoreDefaultParameterParser extends AbstractStoreDefaultParser
{

    @Override
    public List<StoreDefault> parse(final JsonNode node) throws IOException
    {
        final List<StoreDefault> defaultItems = new ArrayList<>();
        if (node.isTextual() && node.textValue().equals(Constants.DELETE))
        {
            defaultItems.add(new StoreDefaultParameter(Constants.PARAMETERS, Constants.DELETE));
        }
        else
        {
            final List<NameValuePair> parameters = new ParameterParser().parse(node);
            for (final NameValuePair parameter : parameters)
            {
                defaultItems.add(new StoreDefaultParameter(parameter.getName(), parameter.getValue()));
            }
        }
        return defaultItems;
    }

}
