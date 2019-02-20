package com.xceptance.xlt.nocoding.parser.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Utility methods for parsing.
 *
 * @author ckeiner
 */
public class YamlParserUtils
{
    /**
     * Converts a {@link SequenceNode} consisting of {@link MappingNode}s with simple Key/Value-Pairs to a List of
     * {@link NameValuePair}s
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param node
     *            The SequenceNode consisting of MappingNodes
     * @return A list of <code>NameValuePair</code>s that describe the assignments
     */
    public static List<NameValuePair> getSequenceNodeAsNameValuePair(final Mark context, final Node node)
    {
        // Verify node is an array
        if (!(node instanceof SequenceNode))
        {
            throw new ParserException("Node at", context, " is " + node.getNodeId() + " but needs to be an array", node.getStartMark());
        }
        final List<NameValuePair> nvp = new ArrayList<>();
        // For each item of the array
        ((SequenceNode) node).getValue().forEach(assignment -> {
            if (assignment instanceof MappingNode)
            {
                // For each single item
                ((MappingNode) assignment).getValue().forEach(pair -> {
                    // Scalars are expected and they are checked in the transform-method
                    final String name = transformScalarNodeToString(assignment.getStartMark(), pair.getKeyNode());
                    final String value = transformScalarNodeToString(assignment.getStartMark(), pair.getValueNode());
                    nvp.add(new NameValuePair(name, value));
                });
            }
            else
            {
                throw new ParserException("Node", node.getStartMark(),
                                          " contains a " + assignment.getNodeId() + " but it must contain a mapping",
                                          assignment.getStartMark());
            }
        });
        return nvp;
    }

    /**
     * Converts a {@link SequenceNode} consisting of {@link MappingNode}s with simple Key/Value-Pairs to a Map of
     * Strings.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param node
     *            The SequenceNode consisting of MappingNodes
     * @return A Map of <code>String</code>s that describe the assignments.
     */
    public static Map<String, String> getSequenceNodeAsMap(final Mark context, final Node node)
    {
        // Verify node is an array
        if (!(node instanceof SequenceNode))
        {
            throw new ParserException("Node", context, " contains a " + context + " but it must contain an array", node.getStartMark());
        }
        final Map<String, String> map = new HashMap<>();
        // For each item of the array
        ((SequenceNode) node).getValue().forEach(assignment -> {
            if (assignment instanceof MappingNode)
            {
                // For each single item
                ((MappingNode) assignment).getValue().forEach(pair -> {
                    // Scalars are expected and they are checked in the transform-method
                    final String name = transformScalarNodeToString(assignment.getStartMark(), pair.getKeyNode());
                    final String value = transformScalarNodeToString(assignment.getStartMark(), pair.getValueNode());
                    map.put(name, value);
                });
            }
            else
            {
                throw new ParserException("Node", node.getStartMark(),
                                          " contains a " + assignment.getNodeId() + " but it must contain a mapping",
                                          assignment.getStartMark());
            }
        });
        return map;
    }

    /**
     * Converts a {@link SequenceNode} consisting of {@link ScalarNode}s with a string element to a List of Strings
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param node
     *            The SequenceNode consisting of ScalarNodes
     * @return A list of <code>String</code>s that describe the element of the sequence node
     */
    public static List<String> getSequenceNodeAsStringList(final Mark context, final Node node)
    {
        // Verify node is an array
        if (!(node instanceof SequenceNode))
        {
            throw new ParserException("Node", context, " contains a " + node.getNodeId() + " but it must contain an array",
                                      node.getStartMark());
        }
        final List<String> singleElement = new ArrayList<>();
        // For each item of the array
        ((SequenceNode) node).getValue()
                             .forEach(assignment -> {
                                 // Transform the scalar to a String and add it to the list
                                 singleElement.add(transformScalarNodeToString(node.getStartMark(), assignment));
                             });
        return singleElement;
    }

    /**
     * Verifies the {@link Node} is a {@link ScalarNode} and returns its value as String.
     * 
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param node
     *            The Node from which the value should be read.
     * @return The String value of the node.
     */
    public static String transformScalarNodeToString(final Mark context, final Node node)
    {
        if (!(node instanceof ScalarNode))
        {
            throw new ParserException("Node", context, " contains a " + node.getNodeId() + " but it must contain a scalar",
                                      node.getStartMark());
        }

        return ((ScalarNode) node).getValue();
    }

}
