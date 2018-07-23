package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultCookiesParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultHeadersParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultParametersParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultStaticsParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultItem;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * The class for parsing default items. Default items are defined in {@link Constants#PERMITTEDLISTITEMS} and neither
 * {@link Constants#ACTION} nor {@link Constants#STORE}.
 * 
 * @author ckeiner
 */
public class StoreDefaultParser extends AbstractScriptItemParser
{
    String variableName;

    public StoreDefaultParser(final String variableName)
    {
        this.variableName = variableName;
    }

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

        // Since it was a permitted action item, differentiate between the name of the item
        switch (variableName)
        {

            case Constants.NAME:
                scriptItems.add(parseSingleItem(defaultNode));
                break;

            case Constants.HTTPCODE:
                scriptItems.add(parseSingleItem(defaultNode));
                break;

            case Constants.URL:
                scriptItems.add(parseSingleItem(defaultNode));
                break;

            case Constants.METHOD:
                scriptItems.add(parseSingleItem(defaultNode));
                break;

            case Constants.ENCODEPARAMETERS:
                scriptItems.add(parseSingleItem(defaultNode));
                break;

            case Constants.ENCODEBODY:
                scriptItems.add(parseSingleItem(defaultNode));
                break;

            case Constants.XHR:
                scriptItems.add(parseSingleItem(defaultNode));
                break;

            case Constants.HEADERS:
                scriptItems.addAll(new StoreDefaultHeadersParser().parse(defaultNode));
                break;

            case Constants.PARAMETERS:
                scriptItems.addAll(new StoreDefaultParametersParser().parse(defaultNode));
                break;

            case Constants.COOKIES:
                scriptItems.addAll(new StoreDefaultCookiesParser().parse(defaultNode));
                break;

            case Constants.BODY:
                scriptItems.add(parseSingleItem(defaultNode));
                break;

            case Constants.STATIC:
                scriptItems.addAll(new StoreDefaultStaticsParser().parse(defaultNode));
                break;

            default:
                // We didn't find something fitting, so throw an Exception
                throw new IllegalArgumentException("Unknown default list item: " + variableName);
        }

        return scriptItems;
    }

    private ScriptItem parseSingleItem(final JsonNode defaultNode)
    {
        // Get the value of the default item
        final String value = ParserUtils.readSingleValue(defaultNode);
        // Create the new StoreDefaultItem and return it
        return new StoreDefaultItem(variableName, value);
    }

}
