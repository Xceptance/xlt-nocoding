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
package com.xceptance.xlt.nocoding.parser.yaml.command.action;

import java.util.List;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;

import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;

/**
 * The class for parsing action items.
 *
 * @author ckeiner
 */
public abstract class AbstractActionSubItemParser
{

    /**
     * Parses the defined action item at the node.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param actionItemNode
     *            The {@link Node} the item starts at
     * @return A list of {@link AbstractActionSubItem}
     */
    public abstract List<AbstractActionSubItem> parse(final Mark context, final Node actionItemNode);

}
