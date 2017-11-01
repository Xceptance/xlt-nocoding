package com.xceptance.xlt.nocoding.rebuild.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class ParserUtilsTest
{

    protected String booleanString = "booleanString : true";

    protected String singleDecimalString = "singleDecimal : 1";

    protected String multipleDecimalString = "multipleDecimal : 123";

    protected String emtpyString = "empty : ";

    protected String arrayString = "array : \n" + "    - element_1 : value_1\n" + "    - element_2 : value_2";

    // Array with decimal, boolean, emtpy, null, string
    protected String mixedArrayString = "mixedArray : \n" + "    - element_1 : 1\n" + "    - element_2 : true\n" + "    - element_3 : \n"
                                        + "    - element_4 : null\n" + "    - element_5 : string";

    protected String objectString = "object : \n" + "    element_1 : value_1\n" + "    element_2 : value_2";

    // Object with decimal, boolean, emtpy, null, string
    protected String mixedObjectString = "mixedObject : \n" + "    element_1 : 1\n" + "    element_2 : true\n" + "    element_3 : \n"
                                         + "    element_4 : null\n" + "    element_5 : string";

    protected String simpleString = "simple : string";

    @Test
    public void testGetNodeAt() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(simpleString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertEquals("simple", objectNode.fieldNames().next());
        Assert.assertEquals("string", node.textValue());
        Assert.assertFalse(elements.hasNext());
    }

    @Test
    public void testGetArrayNodeAsMap() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(arrayString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("array", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isArray());

        final Map<String, String> map = ParserUtils.getArrayNodeAsMap(node);
        Assert.assertEquals(2, map.size());
        Assert.assertTrue(map.containsKey("element_1"));
        Assert.assertEquals("value_1", map.get("element_1"));
        Assert.assertTrue(map.containsKey("element_2"));
        Assert.assertEquals("value_2", map.get("element_2"));
    }

    @Test
    public void testGetArrayNodeAsNameValuePair() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(arrayString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("array", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isArray());

        final List<NameValuePair> nvp = ParserUtils.getArrayNodeAsNameValuePair(node);
        Assert.assertEquals(2, nvp.size());
        Assert.assertEquals("element_1", nvp.get(0).getName());
        Assert.assertEquals("value_1", nvp.get(0).getValue());
        Assert.assertEquals("element_2", nvp.get(1).getName());
        Assert.assertEquals("value_2", nvp.get(1).getValue());
    }

    @Test
    public void testGetArrayNodeAsMapMixed() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(mixedArrayString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("mixedArray", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isArray());

        final Map<String, String> map = ParserUtils.getArrayNodeAsMap(node);
        Assert.assertEquals(5, map.size());
        Assert.assertTrue(map.containsKey("element_1"));
        Assert.assertEquals("1", map.get("element_1"));
        Assert.assertTrue(map.containsKey("element_2"));
        Assert.assertEquals("true", map.get("element_2"));
        Assert.assertTrue(map.containsKey("element_3"));
        Assert.assertEquals("", map.get("element_3"));
        Assert.assertTrue(map.containsKey("element_4"));
        Assert.assertEquals("null", map.get("element_4"));
        Assert.assertTrue(map.containsKey("element_5"));
        Assert.assertEquals("string", map.get("element_5"));
    }

    @Test
    public void testGetArrayNodeAsNameValuePairMixed() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(mixedArrayString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("mixedArray", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isArray());

        final List<NameValuePair> nvp = ParserUtils.getArrayNodeAsNameValuePair(node);
        Assert.assertEquals(5, nvp.size());
        Assert.assertEquals("element_1", nvp.get(0).getName());
        Assert.assertEquals("1", nvp.get(0).getValue());
        Assert.assertEquals("element_2", nvp.get(1).getName());
        Assert.assertEquals("true", nvp.get(1).getValue());
        Assert.assertEquals("element_3", nvp.get(2).getName());
        Assert.assertEquals("", nvp.get(2).getValue());
        Assert.assertEquals("element_4", nvp.get(3).getName());
        Assert.assertEquals("null", nvp.get(3).getValue());
        Assert.assertEquals("element_5", nvp.get(4).getName());
        Assert.assertEquals("string", nvp.get(4).getValue());
    }

}
