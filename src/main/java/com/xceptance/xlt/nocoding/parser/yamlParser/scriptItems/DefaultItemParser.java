package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request.HeaderParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.request.ParameterParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultStatic;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultStoreItem;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

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
            final JsonNode jsonNode = ParserUtils.getNodeAt(Constants.HEADERS, parser);
            final Map<String, String> headers = new HeaderParser().parse(jsonNode);
            for (final Map.Entry<String, String> header : headers.entrySet())
            {
                variableName = header.getKey();
                value = header.getValue();
                scriptItems.add(new StoreDefaultHeader(variableName, value));
            }
        }
        else if (variableName.equals(Constants.PARAMETERS))
        {
            final JsonNode jsonNode = ParserUtils.getNodeAt(Constants.PARAMETERS, parser);
            final List<NameValuePair> parameters = new ParameterParser().parse(jsonNode);
            for (final NameValuePair parameter : parameters)
            {
                variableName = parameter.getName();
                value = parameter.getValue();
                scriptItems.add(new StoreDefaultParameter(variableName, value));
            }
        }
        else if (variableName.equals(Constants.STATIC))
        {
            final JsonNode jsonNode = ParserUtils.getNodeAt(Constants.STATIC, parser);
            final List<NameValuePair> parameters = new ParameterParser().parse(jsonNode);
            for (final NameValuePair parameter : parameters)
            {
                // TODO variableName isn't used
                variableName = Constants.STATIC;
                value = parameter.getValue();
                scriptItems.add(new StoreDefaultStatic(variableName, value));
            }
        }
        else if (variableName.equals(Constants.STORE))
        {
            final JsonNode jsonNode = ParserUtils.getNodeAt(Constants.STORE, parser);
            final List<NameValuePair> parameters = new ParameterParser().parse(jsonNode);
            for (final NameValuePair parameter : parameters)
            {
                variableName = parameter.getName();
                value = parameter.getName();
                scriptItems.add(new StoreDefaultStoreItem(variableName, value));
            }
        }
        else
        {
            value = parser.nextTextValue();
            scriptItems.add(new StoreDefaultItem(variableName, value));
        }

        return scriptItems;
    }

}
