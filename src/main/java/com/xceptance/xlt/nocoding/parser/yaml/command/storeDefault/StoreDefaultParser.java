package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.parser.ParserException;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultValue;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing default items. Default items are defined in {@link Constants#PERMITTEDLISTITEMS} and neither
 * {@link Constants#ACTION} nor {@link Constants#STORE}.
 *
 * @author ckeiner
 */
public class StoreDefaultParser
{

    /**
     * Parses the default item at the {@link JsonNode} to a list of {@link Command}s.
     *
     * @param node
     *            The {@link NodeTuple} that contains teh default item
     * @return A list of <code>ScriptItem</code>s with the specified {@link AbstractStoreDefaultItem}s.
     */
    public static List<Command> parse(final NodeTuple node)
    {
        final List<Command> scriptItems = new ArrayList<>();

        final String defaultItemName = YamlParserUtils.transformScalarNodeToString(node.getKeyNode());
        String value = null;

        // Since it was a permitted list item, differentiate between the name of the item
        switch (defaultItemName)
        {
            case Constants.NAME:
                value = YamlParserUtils.transformScalarNodeToString(node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.HTTPCODE:
                value = YamlParserUtils.transformScalarNodeToString(node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.URL:
                value = YamlParserUtils.transformScalarNodeToString(node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.METHOD:
                value = YamlParserUtils.transformScalarNodeToString(node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.ENCODEPARAMETERS:
                value = YamlParserUtils.transformScalarNodeToString(node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.ENCODEBODY:
                value = YamlParserUtils.transformScalarNodeToString(node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.BODY:
                value = YamlParserUtils.transformScalarNodeToString(node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.XHR:
                value = YamlParserUtils.transformScalarNodeToString(node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.HEADERS:
                scriptItems.addAll(new StoreDefaultHeadersParser().parse(node.getValueNode()));
                break;

            case Constants.PARAMETERS:
                scriptItems.addAll(new StoreDefaultParametersParser().parse(node.getValueNode()));
                break;

            case Constants.COOKIES:
                scriptItems.addAll(new StoreDefaultCookiesParser().parse(node.getValueNode()));
                break;

            case Constants.STATIC:
                scriptItems.addAll(new StoreDefaultStaticSubrequestsParser().parse(node.getValueNode()));
                break;

            default:
                // We didn't find something fitting, so throw an Exception
                throw new ParserException("Node at", node.getKeyNode().getStartMark(), " is permitted but unknown", null);
        }

        return scriptItems;
    }

}
