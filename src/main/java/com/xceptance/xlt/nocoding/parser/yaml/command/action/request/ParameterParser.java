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

import java.util.List;

import org.htmlunit.util.NameValuePair;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;

import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;

/**
 * The class for parsing the parameter items to a list of {@link NameValuePair}s
 *
 * @author ckeiner
 */
public class ParameterParser
{

    /**
     * Parses a parameter item to a list of <code>NameValuePair</code>s
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param parameterNode
     *            The {@link Node} with the parameters in it
     * @return A list of <code>NameValuePair</code>s containing the parsed parameters
     */
    public List<NameValuePair> parse(final Mark context, final Node parameterNode)
    {
        // Transform the JsonNode to a list of NameValuePairs
        final List<NameValuePair> parameters = YamlParserUtils.getSequenceNodeAsNameValuePair(context, parameterNode);
        // Return the list
        return parameters;
    }

}
