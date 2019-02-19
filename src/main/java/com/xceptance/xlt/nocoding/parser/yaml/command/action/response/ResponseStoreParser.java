package com.xceptance.xlt.nocoding.parser.yaml.command.action.response;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.xceptance.xlt.nocoding.util.Constants;

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
     * @param responseStoreNode
     *            The {@link Node} the store item starts at
     * @return A list of all <code>AbstractResponseStore</code>s with the parsed content
     */
    public List<AbstractResponseStore> parse(final Node responseStoreNode)
    {
        // Verify that an array was used and not an object
        if (!(responseStoreNode instanceof SequenceNode))
        {
            throw new ParserException("Node at", responseStoreNode.getStartMark(),
                                      " is " + responseStoreNode.getNodeId().toString() + " but needs to be an array", null);
        }

        final List<AbstractResponseStore> responseStoreList = new ArrayList<>();

        final List<Node> responseStores = ((SequenceNode) responseStoreNode).getValue();
        responseStores.forEach(singleResponseStore -> {
            // Parse a single store item
            final AbstractResponseStore responseStore = parseSingleStore(singleResponseStore);
            responseStoreList.add(responseStore);
            // Print a debug statement
            XltLogger.runTimeLogger.debug("Added " + responseStore.getVariableName() + " to the response stores.");
        });
        // Return all responseStores
        return responseStoreList;
    }

    public AbstractResponseStore parseSingleStore(final Node singleStoreWrapper)
    {
        // Verify that an object was used
        if (!(singleStoreWrapper instanceof MappingNode))
        {
            throw new ParserException("Node at", singleStoreWrapper.getStartMark(),
                                      " is " + singleStoreWrapper.getNodeId().toString() + " but needs to be an object", null);
        }

        String variableName = "";
        AbstractExtractor extractor = null;

        final List<NodeTuple> singleStore = ((MappingNode) singleStoreWrapper).getValue();

        for (final NodeTuple storeItem : singleStore)
        {
            // Get the name of the validation
            variableName = YamlParserUtils.transformScalarNodeToString(storeItem.getKeyNode());
            // Get all subitems following the variable name
            final Node storeContent = storeItem.getValueNode();
            // Verify the validation is an object
            if (!(storeContent instanceof MappingNode))
            {
                throw new ParserException("Node at", storeContent.getStartMark(),
                                          " is " + storeContent.getNodeId().toString() + " but needs to be an object", null);
            }
            // Get the subitems as list
            final List<NodeTuple> singleStoreContentItems = ((MappingNode) storeContent).getValue();
            for (final NodeTuple contentItem : singleStoreContentItems)
            {
                final String contentKey = YamlParserUtils.transformScalarNodeToString(contentItem.getKeyNode());
                // If it is an extraction, parse the extraction
                if (Constants.isPermittedExtraction(contentKey))
                {
                    // Verify, that no extractor was parsed already
                    if (extractor != null)
                    {
                        throw new ParserException("Node at", contentItem.getKeyNode().getStartMark(),
                                                  " defines a second extractor but only one definition is allowed.", null);
                    }
                    // Parse the extractor
                    extractor = new ExtractorParser(contentKey).parse(singleStoreContentItems);
                    XltLogger.runTimeLogger.debug("Extraction Mode is " + extractor);
                }
                // If it is not Group OR Group and extractor is null, throw an error
                else if (!Constants.GROUP.equals(contentKey) || extractor == null)
                {
                    throw new ParserException("Node at", contentItem.getKeyNode().getStartMark(), " defines an unknown item.", null);
                }
            }
        }

        if (extractor == null || StringUtils.isBlank(variableName))
        {
            throw new IllegalArgumentException("Could not create an AbstractResponseStore. Is an extraction missing?");
        }

        // Return the responseStores
        return new ResponseStore(variableName, extractor);
    }

}
