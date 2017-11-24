package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public class SubrequestParserTest extends ParserTest
{

    protected final String fileXhrSubrequests = path + "xhrSubrequests.yml";

    protected final String fileStaticSubrequests = path + "staticSubrequests.yml";

    protected final String fileSyntaxErrorSubrequests = path + "syntaxErrorSubrequests.yml";

    protected final String fileSyntaxErrorSubrequestsObjectNotArray = path + "syntaxErrorSubrequestsObjectNotArray.yml";

    protected final String fileSyntaxErrorSubrequestsStaticItemObjectNotArray = path + "syntaxErrorSubrequestsStaticItemObjectNotArray.yml";

    protected final String fileSyntaxErrorSubrequestsXhrItemArrayNotObject = path + "syntaxErrorSubrequestsXhrItemArrayNotObject.yml";

    protected final String fileSyntaxErrorXhr = path + "syntaxErrorXhr.yml";

    protected final String fileSyntaxErrorStatic = path + "syntaxErrorStatic.yml";

    protected final String fileWrongOrderSubrequest = path + "wrongOrderSubrequest.yml";

    @Test
    public void testXhrSubrequestsParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileXhrSubrequests);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test
    public void testStaticSubrequestsParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileStaticSubrequests);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorSubrequestsParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorSubrequests);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorSubrequestsObjectNotArrayParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorSubrequestsObjectNotArray);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorSubrequestsStaticItemObjectNotArrayParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorSubrequestsStaticItemObjectNotArray);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorSubrequestsXhrItemArrayNotObjectParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorSubrequestsXhrItemArrayNotObject);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorXhrParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorXhr);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorStaticParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorStatic);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testWrongOrderSubrequestParsing() throws Throwable
    {
        final Parser parser = new YamlParser(fileWrongOrderSubrequest);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }
}
