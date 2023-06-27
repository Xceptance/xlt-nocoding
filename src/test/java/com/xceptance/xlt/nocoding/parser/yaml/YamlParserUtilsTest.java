package com.xceptance.xlt.nocoding.parser.yaml;

import java.util.List;
import java.util.Map;

import org.htmlunit.util.NameValuePair;
import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserException;

/**
 * Tests {@link YamlParserUtils}
 *
 * @author ckeiner
 */
public class YamlParserUtilsTest
{

    private final String booleanString = "booleanString : true";

    private final String singleDecimalString = "singleDecimalString : 1";

    private final String multipleDecimalString = "multipleDecimalString : 123";

    private final String emtpyString = "emtpyString : ";

    private final String simpleString = "simpleString : string";

    private final String arrayListString = "simpleString : \n"//
                                           + "    - url\n" //
                                           + "    - url\n" //
                                           + "    - url\n";

    private final String arrayString = "arrayString : \n" + "    - element_1 : value_1\n" + "    - element_2 : value_2";

    private final String objectString = "objectString : \n" + "    element_1 : value_1\n" + "    element_2 : value_2";

    /**
     * Verifies {@link YamlParserUtils#getSequenceNodeAsNameValuePair(Node)} transforms a sequence node correctly to a
     * list of {@link NameValuePair}s.
     */
    @Test
    public void shouldTransformSequenceToNameValueList()
    {
        final String yamlSpec = arrayString;
        final Node root = YamlParserTestHelper.parseToNode(yamlSpec);
        final Node content = ((MappingNode) root).getValue().get(0).getValueNode();
        final List<NameValuePair> nvp = YamlParserUtils.getSequenceNodeAsNameValuePair(root.getStartMark(), content);
        Assert.assertEquals(2, nvp.size());
        Assert.assertEquals("element_1", nvp.get(0).getName());
        Assert.assertEquals("value_1", nvp.get(0).getValue());
        Assert.assertEquals("element_2", nvp.get(1).getName());
        Assert.assertEquals("value_2", nvp.get(1).getValue());
    }

    /**
     * Verifies {@link YamlParserUtils#getSequenceNodeAsNameValuePair(Node)} fails when the node is not a
     * {@link SequenceNode}.
     */
    @Test(expected = ParserException.class)
    public void transformSequenceToNameValueListFailsWhenNotSequence()
    {
        final String yamlSpec = objectString;
        final Node root = YamlParserTestHelper.parseToNode(yamlSpec);
        final Node content = ((MappingNode) root).getValue().get(0).getValueNode();
        YamlParserUtils.getSequenceNodeAsNameValuePair(root.getStartMark(), content);
    }

    /**
     * Verifies {@link YamlParserUtils#getSequenceNodeAsMap(Node)} transforms a sequence node correctly to a Map of
     * {@link String}s.
     */
    @Test
    public void shouldTransformSequenceToMap()
    {
        final String yamlSpec = arrayString;
        final Node root = YamlParserTestHelper.parseToNode(yamlSpec);
        final Node content = ((MappingNode) root).getValue().get(0).getValueNode();
        final Map<String, String> map = YamlParserUtils.getSequenceNodeAsMap(root.getStartMark(), content);
        Assert.assertEquals(2, map.size());
        Assert.assertTrue(map.containsKey("element_1"));
        Assert.assertEquals("value_1", map.get("element_1"));
        Assert.assertTrue(map.containsKey("element_2"));
        Assert.assertEquals("value_2", map.get("element_2"));
    }

    /**
     * Verifies {@link YamlParserUtils#getSequenceNodeAsMap(Node)} fails when the node is not a {@link SequenceNode}.
     */
    @Test(expected = ParserException.class)
    public void transformSequenceToMapFailsWhenNotSequence()
    {
        final String yamlSpec = objectString;
        final Node root = YamlParserTestHelper.parseToNode(yamlSpec);
        final Node content = ((MappingNode) root).getValue().get(0).getValueNode();
        YamlParserUtils.getSequenceNodeAsMap(root.getStartMark(), content);
    }

    /**
     * Verifies {@link YamlParserUtils#getSequenceNodeAsStringList(Node)} transforms a sequence node correctly to a Map
     * of {@link String}s.
     */
    @Test
    public void shouldTransformSequenceToStringList()
    {
        final String yamlSpec = arrayListString;
        final Node root = YamlParserTestHelper.parseToNode(yamlSpec);
        final Node content = ((MappingNode) root).getValue().get(0).getValueNode();
        final List<String> map = YamlParserUtils.getSequenceNodeAsStringList(root.getStartMark(), content);
        Assert.assertEquals(3, map.size());
        Assert.assertEquals("url", map.get(0));
        Assert.assertEquals("url", map.get(1));
        Assert.assertEquals("url", map.get(2));
    }

    /**
     * Verifies {@link YamlParserUtils#getSequenceNodeAsStringList(Node)} fails when the node is not a
     * {@link SequenceNode}.
     */
    @Test(expected = ParserException.class)
    public void transformSequenceToStringListFailsWhenNotSequence()
    {
        final String yamlSpec = objectString;
        final Node root = YamlParserTestHelper.parseToNode(yamlSpec);
        final Node content = ((MappingNode) root).getValue().get(0).getValueNode();
        YamlParserUtils.getSequenceNodeAsStringList(root.getStartMark(), content);
    }

    /**
     * Verifies {@link YamlParserUtils#transformScalarNodeToString(Node)} transforms a {@link ScalarNode} to a String.
     */
    @Test
    public void shouldTransformScalarToString()
    {
        String yamlSpec = booleanString;
        Node root = YamlParserTestHelper.parseToNode(yamlSpec);
        Node content = ((MappingNode) root).getValue().get(0).getValueNode();
        Assert.assertEquals("true", YamlParserUtils.transformScalarNodeToString(root.getStartMark(), content));

        yamlSpec = singleDecimalString;
        root = YamlParserTestHelper.parseToNode(yamlSpec);
        content = ((MappingNode) root).getValue().get(0).getValueNode();
        Assert.assertEquals("1", YamlParserUtils.transformScalarNodeToString(root.getStartMark(), content));

        yamlSpec = multipleDecimalString;
        root = YamlParserTestHelper.parseToNode(yamlSpec);
        content = ((MappingNode) root).getValue().get(0).getValueNode();
        Assert.assertEquals("123", YamlParserUtils.transformScalarNodeToString(root.getStartMark(), content));

        yamlSpec = emtpyString;
        root = YamlParserTestHelper.parseToNode(yamlSpec);
        content = ((MappingNode) root).getValue().get(0).getValueNode();
        Assert.assertEquals("", YamlParserUtils.transformScalarNodeToString(root.getStartMark(), content));

        yamlSpec = simpleString;
        root = YamlParserTestHelper.parseToNode(yamlSpec);
        content = ((MappingNode) root).getValue().get(0).getValueNode();
        Assert.assertEquals("string", YamlParserUtils.transformScalarNodeToString(root.getStartMark(), content));
    }

    /**
     * Verifies {@link YamlParserUtils#transformScalarNodeToString(Node)} fails when the node is not a
     * {@link ScalarNode}.
     */
    @Test(expected = ParserException.class)
    public void shouldTransformScalarToStringFailsWhenNotScalarNode()
    {
        final String yamlSpec = objectString;
        final Node root = YamlParserTestHelper.parseToNode(yamlSpec);
        final Node content = ((MappingNode) root).getValue().get(0).getValueNode();
        YamlParserUtils.transformScalarNodeToString(root.getStartMark(), content);
    }

}
