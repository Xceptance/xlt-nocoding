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

import java.util.List;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;

import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;

/**
 * Abstract class that defines the method for parsing default items.
 *
 * @author ckeiner
 */
public abstract class AbstractStoreDefaultSubItemsParser
{
    /**
     * Parses the default item at the specified {@link Node}.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param defaultItemNode
     *            The <code>Node</code> the default item starts at
     * @return {@link AbstractStoreDefaultItem}
     */
    public abstract List<AbstractStoreDefaultItem> parse(final Mark context, final Node defaultItemNode);
}
