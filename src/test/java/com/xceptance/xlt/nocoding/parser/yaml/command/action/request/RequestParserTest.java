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
package com.xceptance.xlt.nocoding.parser.yaml.command.action.request;

import java.io.IOException;

import org.junit.Assert;
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

    protected final String path = super.path + "action/request/";

    protected final String fileSyntaxErrorRequest = path + "syntaxErrorRequest.yml";

    protected final String fileSyntaxErrorRequestArrayNotObject = path + "syntaxErrorRequestArrayNotObject.yml";

    protected final String fileUrlNull = path + "urlNull.yml";

    /**
     * Verifies an error happens when "Request" has an invalid tag
     *
     * @throws IOException
     */
    @Test
    public void testSyntaxErrorRequestParsing() throws IOException
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorRequest);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(2, parserException.getContextMark().getLine());
            Assert.assertEquals(3, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Request" has an array beneath it and not objects
     *
     * @throws IOException
     */
    @Test
    public void testSyntaxErrorRequestArrayNotObjectParsing() throws IOException
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorRequestArrayNotObject);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(1, parserException.getContextMark().getLine());
            Assert.assertEquals(2, parserException.getProblemMark().getLine());
        }
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
