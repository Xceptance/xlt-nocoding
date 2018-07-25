package com.xceptance.xlt.nocoding.parser.yaml;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

/**
 * Tests for parsing multiple tags, i.e. "Store" and "Action"
 * 
 * @author ckeiner
 */
public class YamlParserTest extends AbstractParserTest
{
    protected final String fileEmptyFile = path + "emptyFile.yml";

    protected final String fileNotExistingFile = path + "notExistingFile.yml";

    protected final String fileSyntaxErrorRoot = path + "syntaxErrorRoot.yml";

    protected final String fileSyntaxErrorRootObjectNotArray = path + "syntaxErrorRootObjectNotArray.yml";

    /**
     * Verifies an empty file can be parsed
     * 
     * @throws Exception
     */
    @Test
    public void testEmptyFileParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<ScriptItem> scriptItems = parser.parse(fileEmptyFile);
        Assert.assertTrue(scriptItems.isEmpty());
    }

    /**
     * Verifies an error is thrown if the file is not found
     * 
     * @throws Exception
     */
    @Test(expected = FileNotFoundException.class)
    public void testNotExistingFileParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<ScriptItem> scriptItems = parser.parse(fileNotExistingFile);
        Assert.assertTrue(scriptItems.isEmpty());
    }

    /**
     * Verifies an error is thrown when an invalid list item is found
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorRootParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorRoot);
    }

    /**
     * Verifies an error is thrown when the list items are objects and not in an array
     * 
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSyntaxErrorRootObjectNotArrayParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorRootObjectNotArray);
    }

}
