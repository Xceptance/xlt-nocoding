package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;

/**
 * Tests for parsing the "Request" tag
 * 
 * @author ckeiner
 */
public class RequestParserTest extends ParserTest
{

    protected final String path = super.path + "actionItems/request/";

    protected final String fileSyntaxErrorRequest = path + "syntaxErrorRequest.yml";

    protected final String fileSyntaxErrorRequestArrayNotObject = path + "syntaxErrorRequestArrayNotObject.yml";

    protected final String fileUrlNull = path + "urlNull.yml";

    /**
     * Verifies an error happens when "Request" has an invalid tag
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorRequestParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorRequest);
        parser.parse();
    }

    /**
     * Verifies an error happens when "Request" has an array beneath it and not objects
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorRequestArrayNotObjectParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorRequestArrayNotObject);
        parser.parse();
    }

    /**
     * Verifies a empty url can be parsed
     * 
     * @throws Exception
     */
    @Test
    public void testUrlNullParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileUrlNull);
        parser.parse();
    }

}
