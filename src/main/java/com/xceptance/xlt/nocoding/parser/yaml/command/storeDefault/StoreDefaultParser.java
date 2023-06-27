/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;
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
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param node
     *            The {@link NodeTuple} that contains teh default item
     * @return A list of <code>ScriptItem</code>s with the specified {@link AbstractStoreDefaultItem}s.
     */
    public static List<Command> parse(final Mark context, final NodeTuple node)
    {
        final List<Command> scriptItems = new ArrayList<>();

        final String defaultItemName = YamlParserUtils.transformScalarNodeToString(context, node.getKeyNode());
        String value = null;

        // Since it was a permitted list item, differentiate between the name of the item
        switch (defaultItemName)
        {
            case Constants.NAME:
                value = YamlParserUtils.transformScalarNodeToString(context, node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.HTTPCODE:
                value = YamlParserUtils.transformScalarNodeToString(context, node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.URL:
                value = YamlParserUtils.transformScalarNodeToString(context, node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.METHOD:
                value = YamlParserUtils.transformScalarNodeToString(context, node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.ENCODEPARAMETERS:
                value = YamlParserUtils.transformScalarNodeToString(context, node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.ENCODEBODY:
                value = YamlParserUtils.transformScalarNodeToString(context, node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.BODY:
                value = YamlParserUtils.transformScalarNodeToString(context, node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.XHR:
                value = YamlParserUtils.transformScalarNodeToString(context, node.getValueNode());
                scriptItems.add(new StoreDefaultValue(defaultItemName, value));
                break;

            case Constants.HEADERS:
                scriptItems.addAll(new StoreDefaultHeadersParser().parse(context, node.getValueNode()));
                break;

            case Constants.PARAMETERS:
                scriptItems.addAll(new StoreDefaultParametersParser().parse(context, node.getValueNode()));
                break;

            case Constants.COOKIES:
                scriptItems.addAll(new StoreDefaultCookiesParser().parse(context, node.getValueNode()));
                break;

            case Constants.STATIC:
                scriptItems.addAll(new StoreDefaultStaticSubrequestsParser().parse(context, node.getValueNode()));
                break;

            default:
                // We didn't find something fitting, so throw an Exception
                throw new ParserException("Node", context, " contains a permitted but unknown list item", node.getKeyNode().getStartMark());
        }

        return scriptItems;
    }

}
