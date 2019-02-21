package com.xceptance.xlt.nocoding.parser.yaml.command.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.Action;
import com.xceptance.xlt.nocoding.command.action.request.Request;
import com.xceptance.xlt.nocoding.parser.util.StringWrapper;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.request.RequestParser;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.response.ResponseParser;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.subrequest.SubrequestsParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing an action item.
 *
 * @author ckeiner
 */
public class ActionParser
{

    /**
     * Parses the action item to a list of {@link Command}s.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param actionNode
     *            The {@link Node} with the the action item
     * @return A list of <code>ScriptItem</code>s containing a single {@link Action}.
     */
    public static List<Command> parse(final Mark context, final Node actionNode)
    {
        // Initialize variables
        final StringWrapper nameWrapper = new StringWrapper();
        final List<AbstractActionSubItem> actionItems = new ArrayList<>(3);
        final List<Command> scriptItems = new ArrayList<>(1);

        // If the action node is a scalar with no value, then an empty action was specified
        if (actionNode instanceof ScalarNode && StringUtils.isBlank(((ScalarNode) actionNode).getValue()))
        {
            // Return an empty action
            scriptItems.add(new Action());
            return scriptItems;
        }
        // Verify the actionNode is neither an array or a scalar
        if (!(actionNode instanceof MappingNode))
        {
            throw new ParserException("Node", context, " contains a " + actionNode.getNodeId() + " but needs to be an object",
                                      actionNode.getStartMark());
        }

        final List<NodeTuple> actionNodeItems = ((MappingNode) actionNode).getValue();

        actionNodeItems.forEach(item -> {
            final String itemName = YamlParserUtils.transformScalarNodeToString(actionNode.getStartMark(), item.getKeyNode());
            // Check if the name is a permitted action item
            if (!Constants.isPermittedActionItem(itemName))
            {
                throw new ParserException("Node", context, " contains a not permitted action item",
                                          ((ScalarNode) item.getKeyNode()).getStartMark());
            }
            AbstractActionSubItemParser actionItemParser = null;

            // Differentiate between what kind of ActionItem this is
            switch (itemName)
            {
                case Constants.NAME:
                    // Check that this is the first item we parse
                    if (actionItems.isEmpty())
                    {
                        // Save the name
                        nameWrapper.setValue(YamlParserUtils.transformScalarNodeToString(actionNode.getStartMark(), item.getValueNode()));
                        if (nameWrapper != null)
                        {
                            XltLogger.runTimeLogger.debug("Actionname: " + nameWrapper);
                        }
                        break;
                    }
                    else
                    {
                        throw new ParserException("Node", context, " defines a Name but not as first item.",
                                                  item.getKeyNode().getStartMark());
                    }

                case Constants.REQUEST:
                    // Check that this is the first item we parse (excluding name)
                    if (actionItems.isEmpty())
                    {
                        XltLogger.runTimeLogger.debug("Parsing Request");
                        // Set parser to the request parser
                        actionItemParser = new RequestParser();
                        break;
                    }
                    else
                    {
                        throw new ParserException("Node", context,
                                                  " defines a request after another request, a response or a subrequest but must be defined before them.",
                                                  item.getKeyNode().getStartMark());
                    }

                case Constants.RESPONSE:
                    // Check that no subrequest was defined beforehand
                    if (actionItems.isEmpty() || actionItems.get(0) instanceof Request)
                    {
                        XltLogger.runTimeLogger.debug("Parsing Response");
                        // Set parser to the response parser
                        actionItemParser = new ResponseParser();
                        break;
                    }
                    else
                    {
                        throw new ParserException("Node", context,
                                                  " defines a response after another response or a subrequest but must be defined before it.",
                                                  item.getKeyNode().getStartMark());

                    }

                case Constants.SUBREQUESTS:
                    XltLogger.runTimeLogger.debug("Parsing Subrequest");
                    // Set parser to subrequest parser
                    actionItemParser = new SubrequestsParser();
                    break;

                default:
                    // We didn't find something fitting, so throw an Exception
                    throw new ParserException("Node", context, " contains a permitted but unknown item", item.getKeyNode().getStartMark());
            }

            // If we specified an actionItemParser
            if (actionItemParser != null)
            {
                // Parse the current item and add it to the actionItems
                actionItems.addAll(actionItemParser.parse(item.getKeyNode().getStartMark(), item.getValueNode()));
            }

        });
        // Add the action to the script items
        scriptItems.add(new Action(nameWrapper.getValue(), actionItems));

        // Return all scriptItems
        return scriptItems;
    }

}
