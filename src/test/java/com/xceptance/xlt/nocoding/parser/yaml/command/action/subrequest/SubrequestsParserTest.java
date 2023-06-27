/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.parser.yaml.command.action.subrequest;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;

/**
 * Tests for parsing the "Subrequests" tag
 *
 * @author ckeiner
 */
public class SubrequestsParserTest extends AbstractParserTest
{
    protected final String path = super.path + "action/subrequests/";

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
    @Test
    public void testSyntaxErrorSubrequestsParsing() throws IOException
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorSubrequests);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(4, parserException.getContextMark().getLine());
            Assert.assertEquals(5, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Subrequests" has objects beneath it and not arrays
     *
     * @throws IOException
     */
    @Test
    public void testSyntaxErrorSubrequestsObjectNotArrayParsing() throws IOException
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorSubrequestsObjectNotArray);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(1, parserException.getContextMark().getLine());
            Assert.assertEquals(2, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Static" beneath "Subrequests" has objects beneath it and not arrays.<br>
     * Since Snakeyml parses once before Jackson and this isn't valid syntax, a Snakeyml exception is excepted.
     *
     * @throws IOException
     */
    @Test
    public void testSyntaxErrorSubrequestsStaticItemObjectNotArrayParsing() throws IOException
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorSubrequestsStaticItemObjectNotArray);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(2, parserException.getContextMark().getLine());
            Assert.assertEquals(3, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Xhr" beneath "Subrequests" has an array beneath it and not objects
     *
     * @throws IOException
     */
    @Test
    public void testSyntaxErrorSubrequestsXhrItemArrayNotObjectParsing() throws IOException
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorSubrequestsXhrItemArrayNotObject);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(2, parserException.getContextMark().getLine());
            Assert.assertEquals(3, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Xhr" beneath "Subrequests" has an invalid tag
     *
     * @throws IOException
     */
    @Test
    public void testSyntaxErrorXhrParsing() throws IOException
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorXhr);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(5, parserException.getContextMark().getLine());
            Assert.assertEquals(7, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Static" beneath "Subrequests" has a single value
     *
     * @throws IOException
     */
    @Test
    public void testSyntaxErrorStaticParsing() throws IOException
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorStatic);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(5, parserException.getContextMark().getLine());
            Assert.assertEquals(6, parserException.getProblemMark().getLine());
        }
    }

}
