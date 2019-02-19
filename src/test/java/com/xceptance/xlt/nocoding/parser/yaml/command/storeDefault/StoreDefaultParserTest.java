package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.nodes.Node;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.store.Store;
import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultCookie;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultStaticSubrequest;
import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserTestHelper;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests for parsing default items
 *
 * @author ckeiner
 */
public class StoreDefaultParserTest extends AbstractParserTest
{

    protected final String fileTestData = path + "testData.yml";

    protected final String fileTmp = path + "tmp.yml";

    /**
     * Verifies Name and Url can be parsed on root level
     *
     * @throws Exception
     */
    @Test
    public void testFileTmpParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<Command> scriptItems = parser.parse(fileTmp);

        Assert.assertEquals(2, scriptItems.size());

        // Assert it's an action
        Assert.assertTrue(scriptItems.get(0) instanceof AbstractStoreDefaultItem);
        final AbstractStoreDefaultItem first = (AbstractStoreDefaultItem) scriptItems.get(0);
        Assert.assertEquals("Name", first.getVariableName());
        Assert.assertEquals("name", first.getValue());

        final AbstractStoreDefaultItem second = (AbstractStoreDefaultItem) scriptItems.get(1);
        Assert.assertEquals("Url", second.getVariableName());
        Assert.assertEquals("url", second.getValue());

    }

    /**
     * Verifies all default tags are parsed properly
     *
     * @throws Exception
     */
    @Test
    public void testTestDataParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<Command> scriptItems = parser.parse(fileTestData);
        // We only check for "bigger than 22", since we might have more than 22 elements, due to some default items.
        // Default Lists are stored with key "listname_name_x" and value "listname_value_x" whereas x is the count of
        // the
        // element
        Assert.assertTrue("Expected element count does not match", scriptItems.size() >= 22);

        // Headers are stored in an arbitrary order
        final Map<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("header_1", "value");
        defaultHeaders.put("header_2", "value");
        defaultHeaders.put("header_3", "value");

        final List<NameValuePair> listOfDefaults = new ArrayList<>();
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

        // This is cookie item
        listOfDefaults.add(new NameValuePair("cookieName_1", "cookieValue_1"));
        listOfDefaults.add(new NameValuePair("cookieName_2", "cookieValue_2"));

        listOfDefaults.add(new NameValuePair("Name", "Delete"));
        listOfDefaults.add(new NameValuePair("Url", "Delete"));
        listOfDefaults.add(new NameValuePair("Httpcode", "Delete"));
        // Next item is index 20
        listOfDefaults.add(new NameValuePair("Method", "Delete"));
        listOfDefaults.add(new NameValuePair("Body", "Delete"));
        listOfDefaults.add(new NameValuePair("Xhr", "Delete"));
        listOfDefaults.add(new NameValuePair("Headers", "Delete"));
        listOfDefaults.add(new NameValuePair("Parameters", "Delete"));
        listOfDefaults.add(new NameValuePair("Static", "Delete"));
        listOfDefaults.add(new NameValuePair("Encode-Parameters", "Delete"));
        listOfDefaults.add(new NameValuePair("Encode-Body", "Delete"));

        for (int i = 0, listIndex = 0; i < scriptItems.size(); i++, listIndex++)
        {
            if (scriptItems.get(i) instanceof AbstractStoreDefaultItem)
            {
                // Get the i.th item
                final AbstractStoreDefaultItem storeDefault = (AbstractStoreDefaultItem) scriptItems.get(i);
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
            else if (scriptItems.get(i) instanceof Store)
            {
                final Store storeDefault = (Store) scriptItems.get(i);
                final String errorMessage = "StoreItem does not match at loopIndex " + i + " and listIndex " + listIndex;
                Assert.assertEquals(errorMessage, listOfDefaults.get(listIndex).getName(), storeDefault.getVariableName());
                Assert.assertEquals(errorMessage, listOfDefaults.get(listIndex).getValue(), storeDefault.getValue());
            }
            else
            {
                throw new IllegalArgumentException("Neither StoreDefault nor StoreItem.");
            }
        }

    }

    /**
     * Verifies default cookies can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testDefaultCookieParsing() throws Exception
    {
        final String firstCookieName = "name_1";
        final String firstCookieValue = "val_1";
        final String secondCookieName = "name_2";
        final String secondCookieValue = "val_2";
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(firstCookieName, firstCookieValue);
        final ArrayNode array = jf.arrayNode();
        array.add(content);

        final String yamlSpec = "- " + firstCookieName + " : " + firstCookieValue + "\n" //
                                + "- " + secondCookieName + " : " + secondCookieValue + "\n";

        final Node defaultCookieContent = YamlParserTestHelper.parseToNode(yamlSpec);

        final List<AbstractStoreDefaultItem> storeDefaults = new StoreDefaultCookiesParser().parse(defaultCookieContent);
        Assert.assertEquals(2, storeDefaults.size());
        AbstractStoreDefaultItem storeDefault = storeDefaults.get(0);
        Assert.assertTrue(storeDefault instanceof StoreDefaultCookie);
        Assert.assertEquals(firstCookieName, storeDefault.getVariableName());
        Assert.assertEquals(firstCookieValue, storeDefault.getValue());

        storeDefault = storeDefaults.get(1);
        Assert.assertTrue(storeDefault instanceof StoreDefaultCookie);
        Assert.assertEquals(secondCookieName, storeDefault.getVariableName());
        Assert.assertEquals(secondCookieValue, storeDefault.getValue());
    }

    /**
     * Verifies default parameters can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testDefaultParameterParsing() throws Exception
    {
        final String firstParameterName = "name_1";
        final String firstParameterValue = "val_1";
        final String secondParameterName = "name_2";
        final String secondParameterValue = "val_2";

        final String yamlSpec = "- " + firstParameterName + " : " + firstParameterValue + "\n" //
                                + "- " + secondParameterName + " : " + secondParameterValue + "\n";

        final Node defaultParameterContent = YamlParserTestHelper.parseToNode(yamlSpec);

        final List<AbstractStoreDefaultItem> storeDefaults = new StoreDefaultParametersParser().parse(defaultParameterContent);
        Assert.assertEquals(2, storeDefaults.size());
        AbstractStoreDefaultItem storeDefault = storeDefaults.get(0);
        Assert.assertTrue(storeDefault instanceof StoreDefaultParameter);
        Assert.assertEquals(firstParameterName, storeDefault.getVariableName());
        Assert.assertEquals(firstParameterValue, storeDefault.getValue());

        storeDefault = storeDefaults.get(1);
        Assert.assertTrue(storeDefault instanceof StoreDefaultParameter);
        Assert.assertEquals(secondParameterName, storeDefault.getVariableName());
        Assert.assertEquals(secondParameterValue, storeDefault.getValue());
    }

    /**
     * Verifies default headers can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testDefaultHeaderParsing() throws Exception
    {
        final String firstHeaderName = "name_1";
        final String firstHeaderValue = "val_1";

        final String yamlSpec = "- " + firstHeaderName + " : " + firstHeaderValue + "\n";

        final Node defaultHeaderContent = YamlParserTestHelper.parseToNode(yamlSpec);

        final List<AbstractStoreDefaultItem> storeDefaults = new StoreDefaultHeadersParser().parse(defaultHeaderContent);
        Assert.assertEquals(1, storeDefaults.size());
        final AbstractStoreDefaultItem storeDefault = storeDefaults.get(0);
        Assert.assertTrue(storeDefault instanceof StoreDefaultHeader);
        Assert.assertEquals(firstHeaderName, storeDefault.getVariableName());
        Assert.assertEquals(firstHeaderValue, storeDefault.getValue());
    }

    /**
     * Verifies default static urls can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testDefaultStaticParsing() throws Exception
    {
        final String url = "url";
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ArrayNode array = jf.arrayNode();
        array.add(url);
        array.add(url);
        array.add(url);

        final String yamlSpec = "- " + url + "\n" //
                                + "- " + url + "\n" //
                                + "- " + url + "\n";

        final Node defaultStaticContent = YamlParserTestHelper.parseToNode(yamlSpec);

        final List<AbstractStoreDefaultItem> storeDefaultList = new StoreDefaultStaticSubrequestsParser().parse(defaultStaticContent);
        Assert.assertEquals(3, storeDefaultList.size());
        final AbstractStoreDefaultItem storeDefault = storeDefaultList.get(0);
        Assert.assertTrue(storeDefault instanceof StoreDefaultStaticSubrequest);
        Assert.assertEquals(Constants.STATIC, storeDefault.getVariableName());
        Assert.assertEquals(url, storeDefault.getValue());
    }

}
