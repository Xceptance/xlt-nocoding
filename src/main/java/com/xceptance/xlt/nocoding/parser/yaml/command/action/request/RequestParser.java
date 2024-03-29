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
package com.xceptance.xlt.nocoding.parser.yaml.command.action.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.htmlunit.util.NameValuePair;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.request.Request;
import com.xceptance.xlt.nocoding.parser.util.StringWrapper;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.AbstractActionSubItemParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing request items to {@link Request}s wrapped in a list of {@link AbstractActionSubItem}s.
 *
 * @author ckeiner
 */
public class RequestParser extends AbstractActionSubItemParser
{

    /**
     * Parses a request item to a <code>Request</code>.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param requestNode
     *            The {@link Node} with the request in it
     * @return The <code>Request</code> wrapped in a list of <code>AbstractActionItem</code>s.
     */
    @Override
    public List<AbstractActionSubItem> parse(final Mark context, final Node requestNode)
    {
        // Verify that an object was used and not an array
        if (!(requestNode instanceof MappingNode))
        {
            throw new ParserException("Node", context, " contains a " + requestNode.getNodeId() + " but it must contain an object",
                                      requestNode.getStartMark());
        }

        // Initialize variables
        final List<AbstractActionSubItem> actionItems = new ArrayList<AbstractActionSubItem>();
        final StringWrapper url = new StringWrapper();
        final StringWrapper method = new StringWrapper();
        final StringWrapper xhr = new StringWrapper();
        final StringWrapper encodeParameters = new StringWrapper();
        final StringWrapper body = new StringWrapper();
        final StringWrapper encodeBody = new StringWrapper();
        final List<NameValuePair> parameters = new ArrayList<>();
        final Map<String, String> headers = new HashMap<>();
        final Map<String, String> cookies = new LinkedHashMap<>();

        final List<NodeTuple> requestNodeItems = ((MappingNode) requestNode).getValue();

        requestNodeItems.forEach(item -> {
            final String itemName = YamlParserUtils.transformScalarNodeToString(requestNode.getStartMark(), item.getKeyNode());
            // Check if the name is a permitted action item
            if (!Constants.isPermittedRequestItem(itemName))
            {
                throw new ParserException("Node", context, " contains a not permitted request item", item.getKeyNode().getStartMark());
            }

            // We generally want to read the value of the fieldName but assign it to different variables
            switch (itemName)
            {
                case Constants.URL:
                    url.setValue(YamlParserUtils.transformScalarNodeToString(item.getKeyNode().getStartMark(), item.getValueNode()));
                    break;

                case Constants.METHOD:
                    method.setValue(YamlParserUtils.transformScalarNodeToString(item.getKeyNode().getStartMark(), item.getValueNode()));
                    break;

                case Constants.XHR:
                    xhr.setValue(YamlParserUtils.transformScalarNodeToString(item.getKeyNode().getStartMark(), item.getValueNode()));
                    break;

                case Constants.ENCODEPARAMETERS:
                    encodeParameters.setValue(YamlParserUtils.transformScalarNodeToString(item.getKeyNode().getStartMark(),
                                                                                          item.getValueNode()));
                    break;

                case Constants.BODY:
                    body.setValue(YamlParserUtils.transformScalarNodeToString(item.getKeyNode().getStartMark(), item.getValueNode()));
                    break;

                case Constants.ENCODEBODY:
                    encodeBody.setValue(YamlParserUtils.transformScalarNodeToString(item.getKeyNode().getStartMark(), item.getValueNode()));
                    break;

                case Constants.PARAMETERS:
                    // Create a new ParameterParser that parses parameters
                    parameters.addAll(new ParameterParser().parse(item.getKeyNode().getStartMark(), item.getValueNode()));
                    break;

                case Constants.HEADERS:
                    // Create a new HeaderParser that parses headers
                    headers.putAll(new HeaderParser().parse(item.getKeyNode().getStartMark(), item.getValueNode()));
                    break;

                case Constants.COOKIES:
                    cookies.putAll(new CookieParser().parse(item.getKeyNode().getStartMark(), item.getValueNode()));
                    break;

                default:
                    // We didn't find something fitting, so throw an Exception
                    throw new ParserException("Node", context, " contains a permitted but unknown request item",
                                              item.getKeyNode().getStartMark());
            }
        });

        // Create request out of the data
        final Request request = new Request(url.getValue());
        request.setHttpMethod(method.getValue());
        request.setXhr(xhr.getValue());
        request.setEncodeParameters(encodeParameters.getValue());
        request.setParameters(parameters);
        request.setHeaders(headers);
        request.setCookies(cookies);
        request.setBody(body.getValue());
        request.setEncodeBody(encodeBody.getValue());

        // Print a simple debug string, so you can see what was parsed
        XltLogger.runTimeLogger.info(request.toSimpleDebugString());

        // Add the request to the actionItems
        actionItems.add(request);
        // Return the actionItem list
        return actionItems;
    }

}
