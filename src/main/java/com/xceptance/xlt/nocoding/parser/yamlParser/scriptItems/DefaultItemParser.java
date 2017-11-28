package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request.HeaderParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.request.ParameterParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultStatic;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Parses a default item to a {@link List}<{@link ScriptItem}>. Default items are defined in
 * {@link Constants#PERMITTEDLISTITEMS} and neither {@link Constants#ACTION} nor {@link Constants#STORE}.
 * 
 * @author ckeiner
 */
public class DefaultItemParser extends AbstractScriptItemParser
{

    /**
     * Parses the default item at {@link JsonNode} root to a {@link List}<{@link ScriptItem}>.
     * 
     * @param root
     *            The node the default item starts at
     * @return A {@link List}<{@link ScriptItem}> with the specified {@link StoreDefault}.
     * @throws IOException
     */
    @Override
    public List<ScriptItem> parse(final JsonNode root) throws IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        String value = null;
        // Get the name of the item
        String variableName = root.fieldNames().next();

        // Check if the name is a permitted action item
        if (!Constants.isPermittedListItem(variableName))
        {
            throw new IllegalArgumentException("Not a permitted list item: " + variableName);
        }
        if (variableName.equals(Constants.HEADERS))
        {
            final JsonNode jsonNode = root.get(Constants.HEADERS);
            if (jsonNode.isTextual() && jsonNode.textValue().equals(Constants.DELETE))
            {
                variableName = Constants.HEADERS;
                value = Constants.DELETE;
                scriptItems.add(new StoreDefaultHeader(variableName, value));
            }
            else
            {
                final Map<String, String> headers = new HeaderParser().parse(jsonNode);
                for (final Map.Entry<String, String> header : headers.entrySet())
                {
                    variableName = header.getKey();
                    value = header.getValue();
                    scriptItems.add(new StoreDefaultHeader(variableName, value));
                }
            }
        }
        else if (variableName.equals(Constants.PARAMETERS))
        {
            final JsonNode jsonNode = root.get(Constants.PARAMETERS);
            if (jsonNode.isTextual() && jsonNode.textValue().equals(Constants.DELETE))
            {
                variableName = Constants.PARAMETERS;
                value = Constants.DELETE;
                scriptItems.add(new StoreDefaultParameter(variableName, value));
            }
            else
            {
                final List<NameValuePair> parameters = new ParameterParser().parse(jsonNode);
                for (final NameValuePair parameter : parameters)
                {
                    variableName = parameter.getName();
                    value = parameter.getValue();
                    scriptItems.add(new StoreDefaultParameter(variableName, value));
                }
            }
        }
        else if (variableName.equals(Constants.STATIC))
        {
            final JsonNode jsonNode = root.get(Constants.STATIC);
            if (jsonNode.isTextual() && jsonNode.textValue().equals(Constants.DELETE))
            {
                variableName = Constants.STATIC;
                value = Constants.DELETE;
                scriptItems.add(new StoreDefaultStatic(variableName, value));
            }
            else
            {
                final Iterator<JsonNode> elementIterator = jsonNode.elements();
                while (elementIterator.hasNext())
                {
                    final JsonNode nextNode = elementIterator.next();
                    // variableName isn't used, but we set it to "Static" to remain the meaning
                    variableName = Constants.STATIC;
                    value = ParserUtils.readSingleValue(nextNode);
                    scriptItems.add(new StoreDefaultStatic(variableName, value));
                }
            }
        }
        else
        {
            // We get simple name value pairs, as such we simply want to read
            final JsonNode jsonNode = root.get(variableName);
            value = ParserUtils.readSingleValue(jsonNode);
            scriptItems.add(new StoreDefaultItem(variableName, value));
        }

        return scriptItems;
    }

}
