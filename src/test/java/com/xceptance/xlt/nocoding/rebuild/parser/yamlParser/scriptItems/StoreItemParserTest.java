package com.xceptance.xlt.nocoding.rebuild.parser.yamlParser.scriptItems;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.rebuild.parser.ParserTest;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreDefault;

public class StoreItemParserTest extends ParserTest
{
    protected final String fileTmp = path + "tmp.yml";

    @Test
    public void testFileTmp() throws Exception
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

}
