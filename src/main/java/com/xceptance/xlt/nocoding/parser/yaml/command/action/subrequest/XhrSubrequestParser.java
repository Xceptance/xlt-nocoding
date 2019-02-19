package com.xceptance.xlt.nocoding.parser.yaml.command.action.subrequest;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.request.Request;
import com.xceptance.xlt.nocoding.command.action.subrequest.XhrSubrequest;
import com.xceptance.xlt.nocoding.parser.util.StringWrapper;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.request.RequestParser;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.response.ResponseParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing the Xhr Subrequest item.
 *
 * @author ckeiner
 */
public class XhrSubrequestParser
{

    /**
     * Parses the Xhr item to a {@link XhrSubrequest}.
     *
     * @param xhrNode
     *            The {@link Node} with the xhr subrequest item
     * @return The <code>XhrSubrequest</code> with the parsed data
     */
    public XhrSubrequest parse(final Node xhrNode)
    {
        // Verify the node is an ObjectNode or a NullNode
        if (!(xhrNode instanceof MappingNode))
        {
            throw new ParserException("Node at", xhrNode.getStartMark(),
                                      " is " + xhrNode.getNodeId().toString() + " but needs to be a mapping", null);
        }
        // Initialize variables
        final StringWrapper nameWrapper = new StringWrapper();
        final List<AbstractActionSubItem> actionItems = new ArrayList<>(3);

        final List<NodeTuple> actionNodeItems = ((MappingNode) xhrNode).getValue();

        actionNodeItems.forEach(item -> {
            final String itemName = YamlParserUtils.transformScalarNodeToString(item.getKeyNode());
            // Check if the name is a permitted action item
            if (!Constants.isPermittedActionItem(itemName))
            {
                throw new ParserException("Node at", ((ScalarNode) item.getKeyNode()).getStartMark(), " is not a permitted Xhr Action item",
                                          null);
            }
            AbstractActionSubItem actionItem = null;

            // Differentiate between what kind of ActionItem this is
            switch (itemName)
            {
                case Constants.NAME:
                    // Check that this is the first item we parse
                    if (actionItems.isEmpty())
                    {
                        // Save the name
                        nameWrapper.setValue(YamlParserUtils.transformScalarNodeToString(item.getValueNode()));
                        if (nameWrapper != null)
                        {
                            XltLogger.runTimeLogger.debug("Xhr Subrequest Name: " + nameWrapper.getValue());
                        }
                        break;
                    }
                    else
                    {
                        throw new ParserException("The name of the Xhr Subrequest at", ((ScalarNode) item.getKeyNode()).getStartMark(),
                                                  " must be defined as first item.", ((ScalarNode) item.getValueNode()).getStartMark());
                    }

                case Constants.REQUEST:
                    // Check that this is the first item we parse (excluding name)
                    if (actionItems.isEmpty())
                    {
                        XltLogger.runTimeLogger.debug("Parsing Request");
                        // Set parser to the request parser
                        actionItem = new RequestParser().parse(item.getValueNode()).get(0);
                        if (actionItem instanceof Request)
                        {
                            ((Request) actionItem).setXhr("true");
                        }
                        else
                        {
                            throw new ParserException("Could not vonvert request item to request object ",
                                                      ((ScalarNode) item.getValueNode()).getStartMark(), "", null);
                        }
                        break;
                    }
                    else
                    {
                        throw new ParserException("The request of the Xhr Subrequest at", ((ScalarNode) item.getKeyNode()).getStartMark(),
                                                  " must be define before a response and subrequest.",
                                                  ((ScalarNode) item.getValueNode()).getStartMark());
                    }

                case Constants.RESPONSE:
                    // Check that no subrequest was defined beforehand
                    if (actionItems.isEmpty() || actionItems.get(0) instanceof Request)
                    {
                        XltLogger.runTimeLogger.debug("Parsing Response");
                        // Set parser to the response parser
                        actionItem = new ResponseParser().parse(item.getValueNode()).get(0);
                        break;
                    }
                    else
                    {
                        throw new ParserException("The response of the Xhr Subrequest at", ((ScalarNode) item.getKeyNode()).getStartMark(),
                                                  " must be define before a subrequest.",
                                                  ((ScalarNode) item.getValueNode()).getStartMark());

                    }

                case Constants.SUBREQUESTS:
                    XltLogger.runTimeLogger.debug("Parsing Subrequest");
                    // Set parser to subrequest parser
                    actionItem = new SubrequestsParser().parse(item.getValueNode()).get(0);
                    break;

                default:
                    // We didn't find something fitting, so throw an Exception
                    throw new ParserException("Node at", item.getKeyNode().getStartMark(), " is permitted but unknown", null);
            }
            if (actionItem != null)
            {
                actionItems.add(actionItem);
            }
        });

        // Return the subrequest
        return new XhrSubrequest(nameWrapper.getValue(), actionItems);
    }

}
