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
import java.util.Map;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.request.HeaderParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing default headers.
 *
 * @author ckeiner
 */
public class StoreDefaultHeadersParser extends AbstractStoreDefaultSubItemsParser
{

    /**
     * Parses the headers list item to a list of {@link AbstractStoreDefaultItem}s which consists of multiple
     * {@link StoreDefaultHeader}.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param defaultHeadersNode
     *            The {@link Node} the default headers start at
     * @return A list of <code>StoreDefault</code>s with the parsed default headers.
     */
    @Override
    public List<AbstractStoreDefaultItem> parse(final Mark context, final Node defaultHeadersNode)
    {
        // Create list of defaultItems
        final List<AbstractStoreDefaultItem> defaultItems = new ArrayList<>();

        if (defaultHeadersNode instanceof ScalarNode)
        {
            final String value = YamlParserUtils.transformScalarNodeToString(context, defaultHeadersNode);
            if (value.equals(Constants.DELETE))
            {
                defaultItems.add(new StoreDefaultHeader(Constants.HEADERS, Constants.DELETE));
            }
            else
            {
                throw new ParserException("Node", context,
                                          " contains " + value + " but it must either contain an array or " + Constants.DELETE,
                                          defaultHeadersNode.getStartMark());
            }
        }
        else
        {
            // Parse headers with the header parser
            final Map<String, String> headers = new HeaderParser().parse(context, defaultHeadersNode);
            for (final Map.Entry<String, String> header : headers.entrySet())
            {
                // Create a StoreDefaultHeader for every header key value pair
                defaultItems.add(new StoreDefaultHeader(header.getKey(), header.getValue()));
            }
        }
        // Return all default items
        return defaultItems;
    }

}
