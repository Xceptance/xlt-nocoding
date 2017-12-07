package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultCookieParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultHeaderParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultItemParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultParameterParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.storeDefault.StoreDefaultStaticParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultCookie;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultStatic;
import com.xceptance.xlt.nocoding.util.Constants;

public class DefaultItemParserTest extends ParserTest
{

    protected final String fileTestData = path + "testData.yml";

    protected final String fileTmp = path + "tmp.yml";

    @Test
    public void testFileTmpParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileTmp);
        final List<ScriptItem> scriptItems = parser.parse();

        Assert.assertEquals(2, scriptItems.size());

        // Assert it's an action
        Assert.assertTrue(scriptItems.get(0) instanceof StoreDefault);
        final StoreDefault first = (StoreDefault) scriptItems.get(0);
        Assert.assertEquals("Name", first.getVariableName());
        Assert.assertEquals("name", first.getValue());

        final StoreDefault second = (StoreDefault) scriptItems.get(1);
        Assert.assertEquals("Url", second.getVariableName());
        Assert.assertEquals("url", second.getValue());

    }

    @Test
    public void testTestDataParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileTestData);
        final List<ScriptItem> scriptItems = parser.parse();
        // We only check for "bigger than 22", since we might have more than 22 elements, due to some default items.
        // Default Lists are stored with key "listname_name_x" and value "listname_value_x" whereas x is the count of the
        // element
        Assert.assertTrue("Expected element count does not match", scriptItems.size() >= 22);

        // Headers are stored in an arbitrary order
        final Map<String, String> defaultHeaders = new HashMap<String, String>();
        defaultHeaders.put("header_1", "value");
        defaultHeaders.put("header_2", "value");
        defaultHeaders.put("header_3", "value");

        final List<NameValuePair> listOfDefaults = new ArrayList<NameValuePair>();
        listOfDefaults.add(new NameValuePair("Name", "t_name"));
        listOfDefaults.add(new NameValuePair("Url", "http://www.xceptance.com"));
        listOfDefaults.add(new NameValuePair("Httpcode", "400"));
        listOfDefaults.add(new NameValuePair("Method", "POST"));
        listOfDefaults.add(new NameValuePair("Body", "t_body"));
        listOfDefaults.add(new NameValuePair("Xhr", "false"));
        listOfDefaults.add(new NameValuePair("Encode-Parameters", "true"));
        listOfDefaults.add(new NameValuePair("Encode-Body", "true"));

        listOfDefaults.add(new NameValuePair("p_1", "v_1"));
        listOfDefaults.add(new NameValuePair("p_2", "v_2"));
        // Next item is index 10
        listOfDefaults.add(new NameValuePair(Constants.STATIC, "http://www.xceptance.com"));
        listOfDefaults.add(new NameValuePair(Constants.STATIC, "http://www.xceptance.com"));
        listOfDefaults.add(new NameValuePair(Constants.STATIC, "http://www.xceptance.com"));

        // This a store item
        listOfDefaults.add(new NameValuePair("variable_1", "value_1"));
        listOfDefaults.add(new NameValuePair("variable_2", "value_2"));

        listOfDefaults.add(new NameValuePair("Name", "Delete"));
        listOfDefaults.add(new NameValuePair("Url", "Delete"));
        listOfDefaults.add(new NameValuePair("Httpcode", "Delete"));
        listOfDefaults.add(new NameValuePair("Method", "Delete"));
        listOfDefaults.add(new NameValuePair("Body", "Delete"));
        // Next item is index 20
        listOfDefaults.add(new NameValuePair("Xhr", "Delete"));
        listOfDefaults.add(new NameValuePair("Headers", "Delete"));
        listOfDefaults.add(new NameValuePair("Parameters", "Delete"));
        listOfDefaults.add(new NameValuePair("Static", "Delete"));
        listOfDefaults.add(new NameValuePair("Encode-Parameters", "Delete"));
        listOfDefaults.add(new NameValuePair("Encode-Body", "Delete"));

        for (int i = 0, listIndex = 0; i < scriptItems.size(); i++, listIndex++)
        {
            if (scriptItems.get(i) instanceof StoreDefault)
            {
                // Get the i.th item
                final StoreDefault storeDefault = (StoreDefault) scriptItems.get(i);
                // If it is a header, check if the header list contains the variableName
                if (scriptItems.get(i) instanceof StoreDefaultHeader)
                {
                    if (!storeDefault.getValue().equals("Delete"))
                    {
                        final String errorMessage = "Header not found at loopIndex " + i + " and listIndex " + listIndex;
                        Assert.assertTrue(errorMessage, defaultHeaders.containsKey(storeDefault.getVariableName()));
                        Assert.assertEquals(errorMessage, defaultHeaders.get(storeDefault.getVariableName()), storeDefault.getValue());
                        // Decrement listIndex so we do not screw up accessing our list
                        listIndex--;
                    }
                }
                else
                {
                    final String errorMessage = "DefaultStore does not match at loopIndex " + i + " and listIndex " + listIndex;
                    Assert.assertEquals(errorMessage, listOfDefaults.get(listIndex).getName(), storeDefault.getVariableName());
                    Assert.assertEquals(errorMessage, listOfDefaults.get(listIndex).getValue(), storeDefault.getValue());
                }
            }
            else if (scriptItems.get(i) instanceof StoreItem)
            {
                final StoreItem storeDefault = (StoreItem) scriptItems.get(i);
                final String errorMessage = "StoreItem does not match at loopIndex " + i + " and listIndex " + listIndex;
                Assert.assertEquals(errorMessage, listOfDefaults.get(listIndex).getName(), storeDefault.getVariableName());
                Assert.assertEquals(errorMessage, listOfDefaults.get(listIndex).getValue(), storeDefault.getValue());
            }
            else
            {
                Assert.assertFalse("Neither StoreDefault nor StoreItem.", true);
            }
        }

    }

    @Test
    public void testDefaultCookieParsing() throws Exception
    {
        final String name = "name_1";
        final String value = "val_1";
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(name, value);
        final ArrayNode array = jf.arrayNode();
        array.add(content);

        final StoreDefault storeDefault = new StoreDefaultCookieParser().parse(array).get(0);
        Assert.assertTrue(storeDefault instanceof StoreDefaultCookie);
        Assert.assertEquals(name, storeDefault.getVariableName());
        Assert.assertEquals(value, storeDefault.getValue());
    }

    @Test
    public void testDefaultParameterParsing() throws Exception
    {
        final String name = "name_1";
        final String value = "val_1";
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(name, value);
        final ArrayNode array = jf.arrayNode();
        array.add(content);

        final StoreDefault storeDefault = new StoreDefaultParameterParser().parse(array).get(0);
        Assert.assertTrue(storeDefault instanceof StoreDefaultParameter);
        Assert.assertEquals(name, storeDefault.getVariableName());
        Assert.assertEquals(value, storeDefault.getValue());
    }

    @Test
    public void testDefaultHeaderParsing() throws Exception
    {
        final String name = "name_1";
        final String value = "val_1";
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(name, value);
        final ArrayNode array = jf.arrayNode();
        array.add(content);

        final StoreDefault storeDefault = new StoreDefaultHeaderParser().parse(array).get(0);
        Assert.assertTrue(storeDefault instanceof StoreDefaultHeader);
        Assert.assertEquals(name, storeDefault.getVariableName());
        Assert.assertEquals(value, storeDefault.getValue());
    }

    @Test
    public void testDefaultStaticParsing() throws Exception
    {
        final String url = "url";
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ArrayNode array = jf.arrayNode();
        array.add(url);
        array.add(url);
        array.add(url);

        final List<StoreDefault> storeDefaultList = new StoreDefaultStaticParser().parse(array);
        Assert.assertEquals(3, storeDefaultList.size());
        final StoreDefault storeDefault = storeDefaultList.get(0);
        Assert.assertTrue(storeDefault instanceof StoreDefaultStatic);
        Assert.assertEquals(Constants.STATIC, storeDefault.getVariableName());
        Assert.assertEquals(url, storeDefault.getValue());
    }

    @Test
    public void testDefaultItemParsing() throws Exception
    {
        final String name = "name_1";
        final String value = "val_1";
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(name, value);

        final StoreDefault storeDefault = new StoreDefaultItemParser().parse(content).get(0);
        Assert.assertTrue(storeDefault instanceof StoreDefaultItem);
        Assert.assertEquals(name, storeDefault.getVariableName());
        Assert.assertEquals(value, storeDefault.getValue());
    }

}
