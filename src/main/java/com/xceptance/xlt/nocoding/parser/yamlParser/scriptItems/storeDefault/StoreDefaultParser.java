package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.AbstractScriptItemParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Parses a default item to a {@link List}<{@link ScriptItem}>. Default items are defined in
 * {@link Constants#PERMITTEDLISTITEMS} and neither {@link Constants#ACTION} nor {@link Constants#STORE}.
 * 
 * @author ckeiner
 */
public class StoreDefaultParser extends AbstractScriptItemParser
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
        // Get the name of the item
        final String variableName = root.fieldNames().next();

        // Check if the name is a permitted action item
        if (!Constants.isPermittedListItem(variableName))
        {
            throw new IllegalArgumentException("Not a permitted list item: " + variableName);
        }
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
        else
        {
            // We get simple name value pairs, as such we simply want to read a single value
            scriptItems.addAll(new StoreDefaultItemParser().parse(root));
            // final JsonNode jsonNode = root.get(variableName);
            // value = ParserUtils.readSingleValue(jsonNode);
            // scriptItems.add(new StoreDefaultItem(variableName, value));
        }

        return scriptItems;
    }

}
