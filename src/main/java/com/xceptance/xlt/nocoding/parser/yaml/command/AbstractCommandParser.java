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
package com.xceptance.xlt.nocoding.parser.yaml.command;

import java.util.List;

import org.yaml.snakeyaml.nodes.Node;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.command.Command;

/**
 * The abstract class for parsing commands.
 *
 * @author ckeiner
 */
public abstract class AbstractCommandParser
{
    /**
     * Parses the command from the given {@link JsonNode}.
     *
     * @param scriptItemNode
     *            The <code>Node</code> the command starts at
     * @return A list of all {@link Command}s defined by the specified <code>JsonNode</code>
     */
    public abstract List<Command> parse(Node scriptItemNode);
}
