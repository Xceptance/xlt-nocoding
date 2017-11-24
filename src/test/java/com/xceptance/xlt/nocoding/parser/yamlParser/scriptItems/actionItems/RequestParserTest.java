package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public class RequestParserTest extends ParserTest
{

    protected final String fileSyntaxErrorRequest = path + "syntaxErrorRequest.yml";

    protected final String fileSyntaxErrorRequestArrayNotObject = path + "syntaxErrorRequestArrayNotObject.yml";

    protected final String fileSyntaxErrorUrlNull = path + "syntaxErrorUrlNull.yml";

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorRequestParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorRequest);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorRequestArrayNotObjectParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorRequestArrayNotObject);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorUrlNullParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorUrlNull);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

}
