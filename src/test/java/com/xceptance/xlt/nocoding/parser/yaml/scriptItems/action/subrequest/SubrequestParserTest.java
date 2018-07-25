package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.action.subrequest;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;

/**
 * Tests for parsing the "Subrequests" tag
 * 
 * @author ckeiner
 */
public class SubrequestParserTest extends ParserTest
{
    protected final String path = super.path + "actionItems/subrequests/";

    protected final String fileXhrSubrequests = path + "xhrSubrequests.yml";

    protected final String fileStaticSubrequests = path + "staticSubrequests.yml";

    protected final String fileSyntaxErrorSubrequests = path + "syntaxErrorSubrequests.yml";

    protected final String fileSyntaxErrorSubrequestsObjectNotArray = path + "syntaxErrorSubrequestsObjectNotArray.yml";

    protected final String fileSyntaxErrorSubrequestsStaticItemObjectNotArray = path + "syntaxErrorSubrequestsStaticItemObjectNotArray.yml";

    protected final String fileSyntaxErrorSubrequestsXhrItemArrayNotObject = path + "syntaxErrorSubrequestsXhrItemArrayNotObject.yml";

    protected final String fileSyntaxErrorXhr = path + "syntaxErrorXhr.yml";

    protected final String fileSyntaxErrorStatic = path + "syntaxErrorStatic.yml";

    /**
     * Verifies a XhrSubrequest can be parsed
     * 
     * @throws IOException
     */
    @Test
    public void testXhrSubrequestsParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileXhrSubrequests);
    }

    /**
     * Verifies a static subrequest can be parsed
     * 
     * @throws IOException
     */
    @Test
    public void testStaticSubrequestsParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileStaticSubrequests);
    }

    /**
     * Verifies an error happens when "Subrequests" has an invalid tag
     * 
     * @throws IOException
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorSubrequestsParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorSubrequests);
    }

    /**
     * Verifies an error happens when "Subrequests" has objects beneath it and not arrays
     * 
     * @throws IOException
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorSubrequestsObjectNotArrayParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorSubrequestsObjectNotArray);
    }

    /**
     * Verifies an error happens when "Static" beneath "Subrequests" has objects beneath it and not arrays
     * 
     * @throws IOException
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorSubrequestsStaticItemObjectNotArrayParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorSubrequestsStaticItemObjectNotArray);
    }

    /**
     * Verifies an error happens when "Xhr" beneath "Subrequests" has an array beneath it and not objects
     * 
     * @throws IOException
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorSubrequestsXhrItemArrayNotObjectParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorSubrequestsXhrItemArrayNotObject);
    }

    /**
     * Verifies an error happens when "Xhr" beneath "Subrequests" has an invalid tag
     * 
     * @throws IOException
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorXhrParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorXhr);
    }

    /**
     * Verifies an error happens when "Static" beneath "Subrequests" has a single value
     * 
     * @throws IOException
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorStaticParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorStatic);
    }

}
