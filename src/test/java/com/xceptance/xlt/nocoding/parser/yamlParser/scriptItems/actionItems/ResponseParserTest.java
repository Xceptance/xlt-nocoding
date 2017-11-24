package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public class ResponseParserTest extends ParserTest
{

    protected final String fileSyntaxErrorResponse = path + "syntaxErrorResponse.yml";

    protected final String fileSyntaxErrorResponseArrayNotObject = path + "syntaxErrorResponseArrayNotObject.yml";

    protected final String fileSyntaxErrorResponseStoreItemArrayNotObject = path + "syntaxErrorResponseStoreItemArrayNotObject.yml";

    protected final String fileSyntaxErrorResponseStoreObjectNotArray = path + "syntaxErrorResponseStoreObjectNotArray.yml";

    protected final String fileSyntaxErrorResponseValidationItemArrayNotObject = path
                                                                                 + "syntaxErrorResponseValidationItemArrayNotObject.yml";

    protected final String fileSyntaxErrorResponseValidationObjectNotArray = path + "syntaxErrorResponseValidationObjectNotArray.yml";

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponse);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseArrayNotObjectParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponseArrayNotObject);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseStoreItemArrayNotObjectParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponseStoreItemArrayNotObject);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseStoreObjectNotArrayParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponseStoreObjectNotArray);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseValidationItemArrayNotObjectParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponseValidationItemArrayNotObject);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseValidationObjectNotArrayParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponseValidationObjectNotArray);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

}
