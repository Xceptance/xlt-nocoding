package com.xceptance.xlt.nocoding.util;

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

    private final String booleanString = "booleanString : true";

    private final String singleDecimalString = "singleDecimalString : 1";

    private final String multipleDecimalString = "multipleDecimalString : 123";

    private final String emtpyString = "emtpyString : ";

    private final String simpleString = "simpleString : string";

    private final String arrayString = "arrayString : \n" + "    - element_1 : value_1\n" + "    - element_2 : value_2";

    // Array with decimal, boolean, emtpy, null, string, multiple decimal
    private final String mixedArrayString = "mixedArrayString : \n" + "    - element_1 : 1\n" + "    - element_2 : true\n"
                                            + "    - element_3 : \n" + "    - element_4 : \"null\"\n" + "    - element_5 : string\n"
                                            + "    - element_6 : 123\n";

    private final String objectString = "objectString : \n" + "    element_1 : value_1\n" + "    element_2 : value_2";

    // Object with decimal, boolean, emtpy, null, string, multiple decimal
    private final String mixedObjectString = "mixedObjectString : \n" + "    element_1 : 1\n" + "    element_2 : true\n"
                                             + "    element_3 : \n" + "    element_4 : null\n" + "    element_5 : string\n"
                                             + "    element_6 : 123\n";

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
        Assert.assertEquals("simpleString", objectNode.fieldNames().next());
        Assert.assertEquals("string", node.textValue());
        Assert.assertFalse(elements.hasNext());
    }

    @Test
    public void testGetNodeAtFieldName() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        JsonParser parser = yaml.createParser(mixedObjectString);

        JsonNode node = ParserUtils.getNodeAt("mixedObjectString", parser);
        Assert.assertNotNull(node);
        Assert.assertTrue(node.fieldNames().hasNext());

        parser = yaml.createParser(mixedArrayString);

        node = ParserUtils.getNodeAt("mixedArrayString", parser);
        Assert.assertNotNull(node);
        Assert.assertTrue(node.elements().hasNext());
        Assert.assertFalse(node.fieldNames().hasNext());

    }

    @Test
    public void testGetArrayNodeAsMap() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(arrayString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("arrayString", objectNode.fieldNames().next());
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
        Assert.assertEquals("arrayString", objectNode.fieldNames().next());
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
        Assert.assertEquals("mixedArrayString", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isArray());

        final Map<String, String> map = ParserUtils.getArrayNodeAsMap(node);
        Assert.assertEquals(6, map.size());
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
        Assert.assertTrue(map.containsKey("element_6"));
        Assert.assertEquals("123", map.get("element_6"));
    }

    @Test
    public void testGetArrayNodeAsNameValuePairMixed() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(mixedArrayString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("mixedArrayString", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isArray());

        final List<NameValuePair> nvp = ParserUtils.getArrayNodeAsNameValuePair(node);
        Assert.assertEquals(6, nvp.size());
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
        Assert.assertEquals("element_6", nvp.get(5).getName());
        Assert.assertEquals("123", nvp.get(5).getValue());
    }

    @Test
    public void testReadSingleBooleanValue() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(booleanString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("booleanString", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isBoolean());

        final String data = ParserUtils.readSingleValue(node);
        Assert.assertEquals("true", data);
    }

    @Test
    public void testReadSingleSingleDecimalValue() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(singleDecimalString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("singleDecimalString", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isInt());

        final String data = ParserUtils.readSingleValue(node);
        Assert.assertEquals("1", data);
    }

    @Test
    public void testReadSingleMultipleDecimalValue() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(multipleDecimalString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("multipleDecimalString", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isInt());

        final String data = ParserUtils.readSingleValue(node);
        Assert.assertEquals("123", data);
    }

    @Test
    public void testReadSingleEmptyStringValue() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(emtpyString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("emtpyString", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isNull());

        final String data = ParserUtils.readSingleValue(node);
        Assert.assertEquals("", data);
    }

    @Test
    public void testReadSingleSimpleStringValue() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(simpleString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("simpleString", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isTextual());

        final String data = ParserUtils.readSingleValue(node);
        Assert.assertEquals("string", data);
    }

    @Test
    public void testReadObjectValue() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(objectString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("objectString", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());

        String data = ParserUtils.readValue(node, "element_1");
        Assert.assertEquals("value_1", data);
        data = ParserUtils.readValue(node, "element_2");
        Assert.assertEquals("value_2", data);
    }

    @Test
    public void testReadSingleDecimalValue() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(singleDecimalString);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> elements = objectNode.elements();
        Assert.assertEquals("singleDecimalString", objectNode.fieldNames().next());
        Assert.assertNotNull(elements);
        Assert.assertTrue(elements.hasNext());

        final JsonNode node = elements.next();
        Assert.assertFalse(elements.hasNext());
        Assert.assertTrue(node.isInt());

        final String data = ParserUtils.readSingleValue(node);
        Assert.assertEquals("1", data);
    }

}
