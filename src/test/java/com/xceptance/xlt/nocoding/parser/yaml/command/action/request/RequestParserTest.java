package com.xceptance.xlt.nocoding.parser.yaml.command.action.request;

import java.io.IOException;

import org.junit.Test;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;

/**
 * Tests for parsing the "Request" tag
 *
 * @author ckeiner
 */
public class RequestParserTest extends AbstractParserTest
{

    protected final String path = super.path + "actionItems/request/";

    protected final String fileSyntaxErrorRequest = path + "syntaxErrorRequest.yml";

    protected final String fileSyntaxErrorRequestArrayNotObject = path + "syntaxErrorRequestArrayNotObject.yml";

    protected final String fileUrlNull = path + "urlNull.yml";

    /**
     * Verifies an error happens when "Request" has an invalid tag
     *
     * @throws IOException
     */
    @Test(expected = ParserException.class)
    public void testSyntaxErrorRequestParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorRequest);
    }

    /**
     * Verifies an error happens when "Request" has an array beneath it and not objects
     *
     * @throws IOException
     */
    @Test(expected = ParserException.class)
    public void testSyntaxErrorRequestArrayNotObjectParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorRequestArrayNotObject);
    }

    /**
     * Verifies a empty url can be parsed
     *
     * @throws IOException
     */
    @Test
    public void testUrlNullParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileUrlNull);
    }

}
