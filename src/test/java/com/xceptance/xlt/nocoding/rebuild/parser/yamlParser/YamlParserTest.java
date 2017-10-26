package com.xceptance.xlt.nocoding.rebuild.parser.yamlParser;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.rebuild.parser.ParserTest;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public class YamlParserTest extends ParserTest
{
    protected final String fileEmptyFile = path + "emptyFile.yml";

    protected final String fileNotExistingFile = path + "notExistingFile.yml";

    @Test
    public void testEmptyFile() throws Exception
    {
        final Parser parser = new YamlParser(fileEmptyFile);
        final List<ScriptItem> scriptItems = parser.parse();

        Assert.assertTrue(scriptItems.isEmpty());
    }

    @Test(expected = FileNotFoundException.class)
    public void testNotExistingFile() throws Exception
    {
        final Parser parser = new YamlParser(fileNotExistingFile);
        final List<ScriptItem> scriptItems = parser.parse();
        Assert.assertTrue(scriptItems.isEmpty());
    }

}
