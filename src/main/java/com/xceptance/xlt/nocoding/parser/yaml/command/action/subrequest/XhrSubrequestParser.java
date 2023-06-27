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
package com.xceptance.xlt.nocoding.parser.yaml.command.action.subrequest;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.request.Request;
import com.xceptance.xlt.nocoding.command.action.subrequest.XhrSubrequest;
import com.xceptance.xlt.nocoding.parser.util.StringWrapper;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.request.RequestParser;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.response.ResponseParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing the Xhr Subrequest item.
 *
 * @author ckeiner
 */
public class XhrSubrequestParser
{

    /**
     * Parses the Xhr item to a {@link XhrSubrequest}.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param xhrNode
     *            The {@link Node} with the xhr subrequest item
     * @return The <code>XhrSubrequest</code> with the parsed data
     */
    public XhrSubrequest parse(final Mark context, final Node xhrNode)
    {
        // Verify the node is an ObjectNode or a NullNode
        if (!(xhrNode instanceof MappingNode))
        {
            throw new ParserException("Node", context, " contains a " + xhrNode.getNodeId() + " but it must contain a mapping",
                                      xhrNode.getStartMark());
        }
        // Initialize variables
        final StringWrapper nameWrapper = new StringWrapper();
        final List<AbstractActionSubItem> actionItems = new ArrayList<>(3);

        final List<NodeTuple> actionNodeItems = ((MappingNode) xhrNode).getValue();

        actionNodeItems.forEach(item -> {
            final String itemName = YamlParserUtils.transformScalarNodeToString(xhrNode.getStartMark(), item.getKeyNode());
            // Check if the name is a permitted action item
            if (!Constants.isPermittedActionItem(itemName))
            {
                throw new ParserException("Node", context, " contans a not permitted Xhr Action item", item.getKeyNode().getStartMark());
            }
            AbstractActionSubItem actionItem = null;

            // Differentiate between what kind of ActionItem this is
            switch (itemName)
            {
                case Constants.NAME:
                    // Check that this is the first item we parse
                    if (actionItems.isEmpty())
                    {
                        // Save the name
                        nameWrapper.setValue(YamlParserUtils.transformScalarNodeToString(item.getKeyNode().getStartMark(),
                                                                                         item.getValueNode()));
                        if (nameWrapper != null)
                        {
                            XltLogger.runTimeLogger.debug("Xhr Subrequest Name: " + nameWrapper.getValue());
                        }
                        break;
                    }
                    else
                    {
                        throw new ParserException("Node", context, " defines a Name but not as first item.",
                                                  item.getKeyNode().getStartMark());
                    }

                case Constants.REQUEST:
                    // Check that this is the first item we parse (excluding name)
                    if (actionItems.isEmpty())
                    {
                        XltLogger.runTimeLogger.debug("Parsing Request");
                        // Set parser to the request parser
                        actionItem = new RequestParser().parse(item.getKeyNode().getStartMark(), item.getValueNode()).get(0);
                        if (actionItem instanceof Request)
                        {
                            ((Request) actionItem).setXhr("true");
                        }
                        else
                        {
                            throw new ParserException("Node", context, "contains an item, that could not be converted to a request object.",
                                                      item.getValueNode().getStartMark());
                        }
                        break;
                    }
                    else
                    {
                        throw new ParserException("Node", context,
                                                  " defines a request after another request, a response or a subrequest, which isn't allowed.",
                                                  item.getKeyNode().getStartMark());
                    }

                case Constants.RESPONSE:
                    // Check that no subrequest was defined beforehand
                    if (actionItems.isEmpty() || actionItems.get(0) instanceof Request)
                    {
                        XltLogger.runTimeLogger.debug("Parsing Response");
                        // Set parser to the response parser
                        actionItem = new ResponseParser().parse(item.getKeyNode().getStartMark(), item.getValueNode()).get(0);
                        break;
                    }
                    else
                    {
                        throw new ParserException("Node", context,
                                                  " defines a response after another response or a subrequest, which isn't allowed.",
                                                  item.getKeyNode().getStartMark());
                    }

                case Constants.SUBREQUESTS:
                    XltLogger.runTimeLogger.debug("Parsing Subrequest");
                    // Set parser to subrequest parser
                    actionItem = new SubrequestsParser().parse(item.getKeyNode().getStartMark(), item.getValueNode()).get(0);
                    break;

                default:
                    // We didn't find something fitting, so throw an Exception
                    throw new ParserException("Node", context, " contains a permitted but unknown item", item.getKeyNode().getStartMark());
            }
            if (actionItem != null)
            {
                actionItems.add(actionItem);
            }
        });

        // Return the subrequest
        return new XhrSubrequest(nameWrapper.getValue(), actionItems);
    }

}
