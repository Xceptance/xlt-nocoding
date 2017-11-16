package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;

public class StoreItemParserTest extends ParserTest
{
    protected final String fileStore = path + "store.yml";

    @Test
    public void testStoreParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileStore);
        final List<ScriptItem> scriptItems = parser.parse();

        Assert.assertEquals(5, scriptItems.size());

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

    }
}
