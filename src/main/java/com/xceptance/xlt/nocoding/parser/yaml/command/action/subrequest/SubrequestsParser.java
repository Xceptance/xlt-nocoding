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
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.AbstractActionSubItemParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing subrequests.
 *
 * @author ckeiner
 */
public class SubrequestsParser extends AbstractActionSubItemParser
{

    /**
     * Parses the subrequest item in the action block to a list of {@link AbstractSubrequest}s. A subrequest item can
     * consist of multiple subrequests.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param subrequestNode
     *            The {@link Node} the subrequest item starts at
     * @return A list with all specified subrequest in the node
     */
    @Override
    public List<AbstractActionSubItem> parse(final Mark context, final Node subrequestNode)
    {
        // Verify that we have an array
        if (!(subrequestNode instanceof SequenceNode))
        {
            throw new ParserException("Node", context, " contains a " + subrequestNode.getNodeId() + " but it must contain an array",
                                      subrequestNode.getStartMark());
        }
        // Initialize Variables
        final List<AbstractSubrequest> subrequests = new ArrayList<>();

        final List<Node> subrequestItems = ((SequenceNode) subrequestNode).getValue();
        subrequestItems.forEach(subrequestWrapper -> {
            // Verify that an array was used and not an object
            if (!(subrequestWrapper instanceof MappingNode))
            {
                throw new ParserException("Node", context, " contains " + subrequestWrapper.getNodeId() + " but it must contain a mapping",
                                          subrequestWrapper.getStartMark());
            }
            final List<NodeTuple> subrequestList = ((MappingNode) subrequestWrapper).getValue();
            subrequestList.forEach(subrequest -> {
                final String subrequestName = YamlParserUtils.transformScalarNodeToString(subrequestWrapper.getStartMark(),
                                                                                          subrequest.getKeyNode());
                // Depending on the name, create the correct Parser and execute it
                switch (subrequestName)
                {
                    case Constants.XHR:
                        // Create an XhrSubrequestParser and parse it
                        subrequests.add(new XhrSubrequestParser().parse(subrequestWrapper.getStartMark(), subrequest.getValueNode()));
                        break;

                    case Constants.STATIC:
                        // Create StaticSubrequestParser and parse the current node
                        subrequests.add(new StaticSubrequestParser().parse(subrequestWrapper.getStartMark(), subrequest.getValueNode()));
                        break;

                    default:
                        throw new ParserException("Node", context, " contains an unknown item.", subrequest.getKeyNode().getStartMark());
                }
            });
        });

        // Create a new AbstractActionItem list
        final List<AbstractActionSubItem> actionItems = new ArrayList<>();
        // Add all subrequests to it
        actionItems.addAll(subrequests);
        // Return the list with all subrequests
        return actionItems;
    }

}
