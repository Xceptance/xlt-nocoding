package com.xceptance.xlt.nocoding.parser.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param node
     *            The SequenceNode consisting of MappingNodes
     * @return A list of <code>NameValuePair</code>s that describe the assignments
     */
    public static List<NameValuePair> getSequenceNodeAsNameValuePair(final Node node)
    {
        // Verify node is an array
        if (!(node instanceof SequenceNode))
        {
            throw new ParserException("Node at", node.getStartMark(), " is " + node.getNodeId().toString() + " but needs to be an array",
                                      null);
        }
        final List<NameValuePair> nvp = new ArrayList<>();
        // For each item of the array
        ((SequenceNode) node).getValue()
                             .forEach(assignment -> {
                                 // Verify the item is a mapping node
                                 if (assignment instanceof MappingNode)
                                 {
                                     // For each single item
                                     ((MappingNode) assignment).getValue().forEach(pair -> {
                                         if (pair.getKeyNode() instanceof ScalarNode && pair.getValueNode() instanceof ScalarNode)
                                         {
                                             final String name = ((ScalarNode) pair.getKeyNode()).getValue();
                                             final String value = ((ScalarNode) pair.getValueNode()).getValue();
                                             nvp.add(new NameValuePair(name, value));
                                         }
                                         else
                                         {
                                             throw new ParserException("Node at", node.getStartMark(), " contains non-scalar child",
                                                                       pair.getKeyNode().getStartMark());
                                         }
                                     });
                                 }
                             });
        return nvp;
    }

    /**
     * Converts a {@link SequenceNode} consisting of {@link MappingNode}s with simple Key/Value-Pairs to a Map of
     * Strings.
     *
     * @param node
     *            The SequenceNode consisting of MappingNodes
     * @return A Map of <code>String</code>s that describe the assignments.
     */
    public static Map<String, String> getSequenceNodeAsMap(final Node node)
    {
        // Verify node is an array
        if (!(node instanceof SequenceNode))
        {
            throw new ParserException("Node at", node.getStartMark(), " is " + node.getNodeId().toString() + " but needs to be an array",
                                      null);
        }
        final Map<String, String> map = new HashMap<>();
        // For each item of the array
        ((SequenceNode) node).getValue()
                             .forEach(assignment -> {
                                 // Verify the item is a mapping node
                                 if (assignment instanceof MappingNode)
                                 {
                                     // For each single item
                                     ((MappingNode) assignment).getValue().forEach(pair -> {
                                         if (pair.getKeyNode() instanceof ScalarNode && pair.getValueNode() instanceof ScalarNode)
                                         {
                                             final String name = ((ScalarNode) pair.getKeyNode()).getValue();
                                             final String value = ((ScalarNode) pair.getValueNode()).getValue();
                                             map.put(name, value);
                                         }
                                         else
                                         {
                                             throw new ParserException("Node at", node.getStartMark(), " contains non-scalar child",
                                                                       pair.getKeyNode().getStartMark());
                                         }
                                     });
                                 }
                             });
        return map;
    }

    /**
     * Converts a {@link SequenceNode} consisting of {@link ScalarNode}s with a string element to a List of Strings
     *
     * @param node
     *            The SequenceNode consisting of ScalarNodes
     * @return A list of <code>String</code>s that describe the element of the sequence node
     */
    public static List<String> getSequenceNodeAsStringList(final Node node)
    {
        // Verify node is an array
        if (!(node instanceof SequenceNode))
        {
            throw new ParserException("Node at", node.getStartMark(), " is " + node.getNodeId().toString() + " but needs to be an array",
                                      null);
        }
        final List<String> singleElement = new ArrayList<>();
        // For each item of the array
        ((SequenceNode) node).getValue()
                             .forEach(assignment -> {
                                 // Transform the scalar to a String and add it to the list
                                 singleElement.add(transformScalarNodeToString(assignment));
                             });
        return singleElement;
    }

    /**
     * Verifies the {@link Node} is a {@link ScalarNode} and returns its value as String.
     * 
     * @param node
     *            The Node from which the value should be read.
     * @return The String value of the node.
     */
    public static String transformScalarNodeToString(final Node node)
    {
        if (!(node instanceof ScalarNode))
        {
            throw new ParserException("Node at", node.getStartMark(), " is " + node.getNodeId().toString() + " but needs to be a scalar",
                                      null);
        }

        return ((ScalarNode) node).getValue();
    }

}
