package com.xceptance.xlt.nocoding.rebuild.parser.yamlParser.scriptItems;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.rebuild.parser.ParserTest;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreDefault;
import com.xceptance.xlt.nocoding.util.Constants;

public class DefaultItemParserTest extends ParserTest
{

    protected final String fileTestData = path + "testData.yml";

    @Test
    public void testTestData() throws Exception
    {
        final Parser parser = new YamlParser(fileTestData);
        final List<ScriptItem> scriptItems = parser.parse();
        // We only check for "bigger than 22", since we might have more than 22 elements, due to some default items.
        // Default Lists are stored with key "listname_name_x" and value "listname_value_x" whereas x is the count of the
        // element
        Assert.assertTrue("Expected element count does not match", scriptItems.size() >= 22);
        final List<NameValuePair> listOfDefaults = new ArrayList<NameValuePair>();
        listOfDefaults.add(new NameValuePair("Name", "t_name"));
        listOfDefaults.add(new NameValuePair("Url", "http://www.xceptance.com"));
        listOfDefaults.add(new NameValuePair("Httpcode", "400"));
        listOfDefaults.add(new NameValuePair("Method", "POST"));
        listOfDefaults.add(new NameValuePair("Body", "t_body"));
        listOfDefaults.add(new NameValuePair("Xhr", "false"));
        listOfDefaults.add(new NameValuePair("Encode-Parameters", "true"));
        listOfDefaults.add(new NameValuePair("Encode-Body", "true"));
        listOfDefaults.add(new NameValuePair(Constants.HEADER_KEY_NAME + "0", "header_1"));
        listOfDefaults.add(new NameValuePair(Constants.HEADER_VALUE_NAME + "0", "value"));
        listOfDefaults.add(new NameValuePair(Constants.HEADER_KEY_NAME + "1", "header_2"));
        listOfDefaults.add(new NameValuePair(Constants.HEADER_VALUE_NAME + "1", "value"));
        listOfDefaults.add(new NameValuePair(Constants.HEADER_KEY_NAME + "2", "header_2"));
        listOfDefaults.add(new NameValuePair(Constants.HEADER_VALUE_NAME + "2", "value"));
        listOfDefaults.add(new NameValuePair(Constants.PARAMETER_KEY_NAME + "0", "p_1"));
        listOfDefaults.add(new NameValuePair(Constants.PARAMETER_VALUE_NAME + "0", "v_1"));
        listOfDefaults.add(new NameValuePair(Constants.PARAMETER_KEY_NAME + "1", "p_2"));
        listOfDefaults.add(new NameValuePair(Constants.PARAMETER_VALUE_NAME + "1", "v_2"));
        listOfDefaults.add(new NameValuePair(Constants.STATIC + "0", "http://www.xceptance.com"));
        listOfDefaults.add(new NameValuePair(Constants.STATIC + "1", "http://www.xceptance.com"));
        listOfDefaults.add(new NameValuePair(Constants.STATIC + "2", "http://www.xceptance.com"));

        // Skip Store Modules

        listOfDefaults.add(new NameValuePair("Name", "delete"));
        listOfDefaults.add(new NameValuePair("Url", "delete"));
        listOfDefaults.add(new NameValuePair("Httpcode", "delete"));
        listOfDefaults.add(new NameValuePair("Method", "delete"));
        listOfDefaults.add(new NameValuePair("Body", "delete"));
        listOfDefaults.add(new NameValuePair("Xhr", "delete"));
        listOfDefaults.add(new NameValuePair("Headers", "delete"));
        listOfDefaults.add(new NameValuePair("Parameters", "delete"));
        listOfDefaults.add(new NameValuePair("Static", "delete"));
        listOfDefaults.add(new NameValuePair("Encode-Parameters", "delete"));
        listOfDefaults.add(new NameValuePair("Body", "delete"));

        for (int i = 0; i < scriptItems.size(); i++)
        {
            Assert.assertTrue(scriptItems.get(i) instanceof StoreDefault);
            final StoreDefault storeDefault = (StoreDefault) scriptItems.get(i);
            final String errorMessage = "Did not match at " + i + " - " + listOfDefaults.get(i).getName();
            Assert.assertEquals(errorMessage, listOfDefaults.get(i).getName(), storeDefault.getVariableName());
            Assert.assertEquals(errorMessage, listOfDefaults.get(i).getValue(), storeDefault.getValue());
        }

    }

}
