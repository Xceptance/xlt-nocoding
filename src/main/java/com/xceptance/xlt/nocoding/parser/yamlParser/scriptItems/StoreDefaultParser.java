package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultCookieParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultHeaderParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultItemParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultParameterParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultStaticParser;
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
     * @param root
     *            The {@link JsonNode} the default item starts at
     * @return A list of <code>ScriptItem</code>s with the specified {@link StoreDefault}s.
     */
    @Override
    public List<ScriptItem> parse(final JsonNode root)
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        // Get the name of the item
        final String variableName = root.fieldNames().next();

        // Check if the name is a permitted action item
        if (!Constants.isPermittedListItem(variableName))
        {
            throw new IllegalArgumentException("Not a permitted list item: " + variableName);
        }
        // Since it was a permitted action item, differentiate between the name of the item
        if (variableName.equals(Constants.HEADERS))
        {
            scriptItems.addAll(new StoreDefaultHeaderParser().parse(root.get(Constants.HEADERS)));
        }
        else if (variableName.equals(Constants.PARAMETERS))
        {
            scriptItems.addAll(new StoreDefaultParameterParser().parse(root.get(Constants.PARAMETERS)));
        }
        else if (variableName.equals(Constants.STATIC))
        {
            scriptItems.addAll(new StoreDefaultStaticParser().parse(root.get(Constants.STATIC)));
        }
        else if (variableName.equals(Constants.COOKIES))
        {
            scriptItems.addAll(new StoreDefaultCookieParser().parse(root.get(Constants.COOKIES)));
        }
        else
        {
            // We get simple name value pairs, as such we simply want to read a single value
            scriptItems.addAll(new StoreDefaultItemParser().parse(root));
        }

        return scriptItems;
    }

}
