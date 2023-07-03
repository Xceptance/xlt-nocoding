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
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;

/**
 * The class for parsing static subrequests.
 *
 * @author ckeiner
 */
public class StaticSubrequestParser
{

    /**
     * Parses the static subrequest item in the static block to a {@link StaticSubrequest}.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param staticNode
     *            The {@link Node} the static subrequest item starts at
     * @return A <code>StaticSubrequest</code> with the parsed URLs
     */
    public StaticSubrequest parse(final Mark context, final Node staticNode)
    {
        // Verify that we have an array
        if (!(staticNode instanceof SequenceNode))
        {
            throw new ParserException("Node at", context, " is " + staticNode.getNodeId().toString() + " but needs to be an array",
                                      staticNode.getStartMark());
        }
        final List<String> urls = new ArrayList<>();
        // Create an iterator over the elements, that is every url
        final List<Node> urlNodes = ((SequenceNode) staticNode).getValue();

        // As long as there are elements, read the url and save it
        urlNodes.forEach(urlNode -> {
            urls.add(YamlParserUtils.transformScalarNodeToString(staticNode.getStartMark(), urlNode));
        });

        // If there were no urls, throw an Exception
        if (urls.isEmpty())
        {
            throw new ParserException("Node", context, " contains no urls", staticNode.getStartMark());
        }
        // Return the specified StaticSubrequest
        return new StaticSubrequest(urls);
    }

}
