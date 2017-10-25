package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.yamlParser.ParserUtil;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request.HeaderParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request.ParameterParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreDefault;
import com.xceptance.xlt.nocoding.util.Constants;

public class DefaultItemParser extends AbstractScriptItemParser
{

    @Override
    public List<ScriptItem> parse(final JsonParser parser) throws IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        String variableName = parser.getText();
        String value = null;
        if (variableName.equals(Constants.HEADERS))
        {
            final JsonNode jsonNode = ParserUtil.getNodeAt(Constants.HEADERS, parser);
            final Map<String, String> headers = new HeaderParser().parse(jsonNode);
            Integer counter = 0;
            for (final Map.Entry<String, String> header : headers.entrySet())
            {
                variableName = Constants.HEADER_KEY_NAME + counter.toString();
                value = header.getKey();
                scriptItems.add(new StoreDefault(variableName, value));

                variableName = Constants.HEADER_VALUE_NAME + counter.toString();
                value = header.getValue();
                scriptItems.add(new StoreDefault(variableName, value));
                counter++;
            }
        }
        else if (variableName.equals(Constants.PARAMETERS))
        {
            final JsonNode jsonNode = ParserUtil.getNodeAt(Constants.PARAMETERS, parser);
            final List<NameValuePair> parameters = new ParameterParser().parse(jsonNode);
            Integer counter = 0;
            for (final NameValuePair parameter : parameters)
            {
                variableName = Constants.PARAMETER_KEY_NAME + counter.toString();
                value = parameter.getName();
                scriptItems.add(new StoreDefault(variableName, value));
                variableName = Constants.PARAMETER_VALUE_NAME + counter.toString();
                value = parameter.getValue();
                scriptItems.add(new StoreDefault(variableName, value));
                counter++;
            }
        }
        else
        {
            value = parser.nextTextValue();
            scriptItems.add(new StoreDefault(variableName, value));
        }

        return scriptItems;
    }

}
