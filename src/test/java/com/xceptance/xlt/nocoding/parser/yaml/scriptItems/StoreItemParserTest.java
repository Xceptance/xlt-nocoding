package com.xceptance.xlt.nocoding.parser.yaml.scriptItems;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;

/**
 * Tests for parsing the "Store" tag
 * 
 * @author ckeiner
 */
public class StoreItemParserTest extends ParserTest
{
    protected final String fileStore = path + "store.yml";

    protected final String fileStoreObjectNotArray = path + "syntaxErrorStoreObjectNotArray.yml";

    /**
     * Verifies two store items can be parsed
     * 
     * @throws Exception
     */
    @Test
    public void testStoreParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<ScriptItem> scriptItems = parser.parse(fileStore);

        Assert.assertEquals(7, scriptItems.size());

        // Assert it's a StoreItem
        Assert.assertTrue(scriptItems.get(0) instanceof StoreItem);
        StoreItem storeItem = (StoreItem) scriptItems.get(0);
        Assert.assertEquals("var_1", storeItem.getVariableName());
        Assert.assertEquals("val_1", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(1) instanceof StoreItem);
        storeItem = (StoreItem) scriptItems.get(1);
        Assert.assertEquals("var_2", storeItem.getVariableName());
        Assert.assertEquals("val_1", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(2) instanceof StoreItem);
        storeItem = (StoreItem) scriptItems.get(2);
        Assert.assertEquals("name_3", storeItem.getVariableName());
        Assert.assertEquals("${var_2}", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(3) instanceof StoreItem);
        storeItem = (StoreItem) scriptItems.get(3);
        Assert.assertEquals("var_4", storeItem.getVariableName());
        Assert.assertEquals("${var_2}", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(4) instanceof StoreItem);
        storeItem = (StoreItem) scriptItems.get(4);
        Assert.assertEquals("var_5", storeItem.getVariableName());
        Assert.assertEquals("${var_4}", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(5) instanceof StoreItem);
        storeItem = (StoreItem) scriptItems.get(5);
        Assert.assertEquals("1234", storeItem.getVariableName());
        Assert.assertEquals("12", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(6) instanceof StoreItem);
        storeItem = (StoreItem) scriptItems.get(6);
        Assert.assertEquals("var_1", storeItem.getVariableName());
        Assert.assertEquals("", storeItem.getValue());

    }

    /**
     * Verifies an error happens when "Store" has objects beneath it and not an array
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testStoreObjectParse() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileStoreObjectNotArray);
    }
}
