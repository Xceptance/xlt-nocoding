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
package com.xceptance.xlt.nocoding.parser.yaml.command.action.response;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.response.AbstractResponseSubItem;
import com.xceptance.xlt.nocoding.command.action.response.HttpCodeValidator;
import com.xceptance.xlt.nocoding.command.action.response.Response;
import com.xceptance.xlt.nocoding.parser.util.StringWrapper;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.AbstractActionSubItemParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing the response.
 *
 * @author ckeiner
 */
public class ResponseParser extends AbstractActionSubItemParser
{

    /**
     * Parses the response item to a {@link Response} wrapped in a list of {@link AbstractActionSubItem}s.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param responseNode
     *            The {@link Node} with the response item
     * @return The <code>Response</code> wrapped in a list of <code>AbstractActionItem</code>s
     */
    @Override
    public List<AbstractActionSubItem> parse(final Mark context, final Node responseNode)
    {
        // Verify that an object was used and not an array
        if (!(responseNode instanceof MappingNode))
        {
            throw new ParserException("Node", context, " contains a " + responseNode.getNodeId() + " but it must contain an object",
                                      responseNode.getStartMark());
        }

        // Initialize variables
        final List<AbstractActionSubItem> actionItems = new ArrayList<>();
        final StringWrapper httpcode = new StringWrapper();
        final List<AbstractResponseSubItem> responseItems = new ArrayList<>();

        final List<NodeTuple> requestNodeItems = ((MappingNode) responseNode).getValue();

        requestNodeItems.forEach(item -> {
            final String itemName = YamlParserUtils.transformScalarNodeToString(responseNode.getStartMark(), item.getKeyNode());
            // Check if the name is a permitted action item
            if (!Constants.isPermittedResponseItem(itemName))
            {
                throw new ParserException("Node", context, " contains a not permitted response item", item.getKeyNode().getStartMark());
            }

            switch (itemName)
            {
                case Constants.HTTPCODE:
                    // Extract the httpcode
                    httpcode.setValue(YamlParserUtils.transformScalarNodeToString(responseNode.getStartMark(), item.getValueNode()));
                    // Add the validator for the httpcode to the responseItems
                    responseItems.add(new HttpCodeValidator(httpcode.getValue()));
                    XltLogger.runTimeLogger.debug("Added HttpcodeValidator with the Httpcode " + httpcode);
                    break;

                case Constants.VALIDATION:
                    // Create a new validation parser and add the result to the responseItems
                    responseItems.addAll(new ValidationParser().parse(responseNode.getStartMark(), item.getValueNode()));
                    XltLogger.runTimeLogger.debug("Added Validation");
                    break;

                case Constants.STORE:
                    // Create a new response store parser and add the result to the responseItems
                    responseItems.addAll(new ResponseStoreParser().parse(responseNode.getStartMark(), item.getValueNode()));
                    XltLogger.runTimeLogger.debug("Added Store");
                    break;

                default:
                    // We didn't find something fitting, so throw an Exception
                    throw new ParserException("Node", context, " contains a permitted but unknown response item",
                                              item.getKeyNode().getStartMark());
            }
        });

        // Add a new response with the responseItems to the actionItems
        actionItems.add(new Response(responseItems));
        // Return the actionItems
        return actionItems;
    }

}
