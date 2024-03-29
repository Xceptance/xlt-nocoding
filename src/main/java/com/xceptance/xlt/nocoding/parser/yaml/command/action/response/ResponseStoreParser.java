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

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.command.action.response.store.AbstractResponseStore;
import com.xceptance.xlt.nocoding.command.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.response.extractor.ExtractorParser;

/**
 * The class for parsing store items in the response item.
 *
 * @author ckeiner
 */
public class ResponseStoreParser
{
    /**
     * Parses the store item in the response block to a list of {@link AbstractResponseStore}
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param responseStoreNode
     *            The {@link Node} the store item starts at
     * @return A list of all <code>AbstractResponseStore</code>s with the parsed content
     */
    public List<AbstractResponseStore> parse(final Mark context, final Node responseStoreNode)
    {
        // Verify that an array was used and not an object
        if (!(responseStoreNode instanceof SequenceNode))
        {
            throw new ParserException("Node", context, " contains a " + responseStoreNode.getNodeId() + " but it must contain an array",
                                      responseStoreNode.getStartMark());
        }

        final List<AbstractResponseStore> responseStoreList = new ArrayList<>();

        final List<Node> responseStores = ((SequenceNode) responseStoreNode).getValue();
        responseStores.forEach(singleResponseStore -> {
            // Parse a single store item
            final AbstractResponseStore responseStore = parseSingleStore(responseStoreNode.getStartMark(), singleResponseStore);
            responseStoreList.add(responseStore);
            // Print a debug statement
            XltLogger.runTimeLogger.debug("Added " + responseStore.getVariableName() + " to the response stores.");
        });
        // Return all responseStores
        return responseStoreList;
    }

    protected AbstractResponseStore parseSingleStore(final Mark context, final Node singleStoreWrapper)
    {
        // Verify that an object was used
        if (!(singleStoreWrapper instanceof MappingNode))
        {
            throw new ParserException("Node", context, " contains a " + singleStoreWrapper.getNodeId() + " but it must contain an object",
                                      singleStoreWrapper.getStartMark());
        }

        String variableName = "";
        AbstractExtractor extractor = null;

        final List<NodeTuple> singleStore = ((MappingNode) singleStoreWrapper).getValue();

        for (final NodeTuple storeItem : singleStore)
        {
            // Get the name of the variable
            variableName = YamlParserUtils.transformScalarNodeToString(singleStoreWrapper.getStartMark(), storeItem.getKeyNode());

            // Get all subitems following the variable name
            final Node storeContent = storeItem.getValueNode();

            // Verify the extraction is an object
            if (!(storeContent instanceof MappingNode))
            {
                throw new ParserException("Node", storeItem.getKeyNode().getStartMark(),
                                          " contains a " + storeContent.getNodeId().toString() + " but it must contain an object",
                                          storeContent.getStartMark());
            }

            // Get the subitems as list
            final List<NodeTuple> singleStoreContentItems = ((MappingNode) storeContent).getValue();

            // Verify, that no extractor was parsed already
            if (extractor != null)
            {
                throw new ParserException("Node", context, " contains two definitions of an extractor but only one is allowed.",
                                          storeItem.getKeyNode().getStartMark());
            }

            // Parse the extractor
            extractor = new ExtractorParser().parse(storeItem.getKeyNode().getStartMark(), singleStoreContentItems);
            XltLogger.runTimeLogger.debug("Extraction Mode is " + extractor);
        }

        if (extractor == null || StringUtils.isBlank(variableName))
        {
            throw new ParserException("Node", context,
                                      " either does not contain an extractor or has an empty variable name which isn't allowed.",
                                      singleStoreWrapper.getStartMark());
        }

        // Return the responseStores
        return new ResponseStore(variableName, extractor);
    }
}
