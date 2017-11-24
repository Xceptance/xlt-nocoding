package com.xceptance.xlt.nocoding.parser.yamlParser;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public class YamlParserTest extends ParserTest
{
    protected final String fileEmptyFile = path + "emptyFile.yml";

    protected final String fileNotExistingFile = path + "notExistingFile.yml";

    protected final String fileSyntaxErrorRoot = path + "syntaxErrorRoot.yml";

    protected final String fileSyntaxErrorRootObjectNotArray = path + "syntaxErrorRootObjectNotArray.yml";

    @Test
    public void testEmptyFileParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileEmptyFile);
        final List<ScriptItem> scriptItems = parser.parse();

        Assert.assertTrue(scriptItems.isEmpty());
    }

    @Test(expected = FileNotFoundException.class)
    public void testNotExistingFileParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileNotExistingFile);
        final List<ScriptItem> scriptItems = parser.parse();
        Assert.assertTrue(scriptItems.isEmpty());
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorRootParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorRoot);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSyntaxErrorRootObjectNotArrayParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorRootObjectNotArray);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

}
