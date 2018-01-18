package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultCookiesParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultHeadersParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultItemParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultParametersParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultStaticsParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing default items. Default items are defined in {@link Constants#PERMITTEDLISTITEMS} and neither
 * {@link Constants#ACTION} nor {@link Constants#STORE}.
 * 
 * @author ckeiner
 */
public class StoreDefaultParser extends AbstractScriptItemParser
{

    /**
     * Parses the default item at the {@link JsonNode} to a list of {@link ScriptItem}s.
     * 
     * @param defaultNode
     *            The {@link JsonNode} the default item starts at
     * @return A list of <code>ScriptItem</code>s with the specified {@link StoreDefault}s.
     */
    @Override
    public List<ScriptItem> parse(final JsonNode defaultNode)
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        // Get the name of the item
        final String variableName = defaultNode.fieldNames().next();

        // Check if the name is a permitted action item
        if (!Constants.isPermittedListItem(variableName))
        {
            throw new IllegalArgumentException("Not a permitted list item: " + variableName);
        }
        // Since it was a permitted action item, differentiate between the name of the item
        switch (variableName)
        {
            case Constants.HEADERS:
                scriptItems.addAll(new StoreDefaultHeadersParser().parse(defaultNode.get(Constants.HEADERS)));
                break;

            case Constants.PARAMETERS:
                scriptItems.addAll(new StoreDefaultParametersParser().parse(defaultNode.get(Constants.PARAMETERS)));
                break;

            case Constants.STATIC:
                scriptItems.addAll(new StoreDefaultStaticsParser().parse(defaultNode.get(Constants.STATIC)));
                break;

            case Constants.COOKIES:
                scriptItems.addAll(new StoreDefaultCookiesParser().parse(defaultNode.get(Constants.COOKIES)));
                break;

            default:
                // We got a simple name value pairs, as such we simply want to read a single value
                scriptItems.addAll(new StoreDefaultItemParser().parse(defaultNode));
                break;
        }

        return scriptItems;
    }

}
