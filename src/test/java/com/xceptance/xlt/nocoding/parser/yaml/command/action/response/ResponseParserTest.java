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
package com.xceptance.xlt.nocoding.parser.yaml.command.action.response;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.action.response.AbstractResponseSubItem;
import com.xceptance.xlt.nocoding.command.action.response.HttpCodeValidator;
import com.xceptance.xlt.nocoding.command.action.response.Response;
import com.xceptance.xlt.nocoding.command.action.response.Validator;
import com.xceptance.xlt.nocoding.command.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.command.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserTestHelper;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests for parsing the Response Tag
 *
 * @author ckeiner
 */
public class ResponseParserTest extends AbstractParserTest
{
    protected final String path = super.path + "action/response/";

    protected final String fileSyntaxErrorResponse = path + "syntaxErrorResponse.yml";

    protected final String fileSyntaxErrorResponseArrayNotObject = path + "syntaxErrorResponseArrayNotObject.yml";

    protected final String fileSyntaxErrorResponseStoreItemArrayNotObject = path + "syntaxErrorResponseStoreItemArrayNotObject.yml";

    protected final String fileSyntaxErrorResponseStoreObjectNotArray = path + "syntaxErrorResponseStoreObjectNotArray.yml";

    protected final String fileSyntaxErrorResponseValidationItemArrayNotObject = path
                                                                                 + "syntaxErrorResponseValidationItemArrayNotObject.yml";

    protected final String fileSyntaxErrorResponseValidationObjectNotArray = path + "syntaxErrorResponseValidationObjectNotArray.yml";

    /**
     * Verifies an error happens when "Response" has an invalid tag
     *
     * @throws Exception
     */
    @Test
    public void testSyntaxErrorResponseParsing() throws Exception
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorResponse);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(4, parserException.getContextMark().getLine());
            Assert.assertEquals(5, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Response" has an array beneath it and not objects
     *
     * @throws Exception
     */
    @Test
    public void testSyntaxErrorResponseArrayNotObjectParsing() throws Exception
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorResponseArrayNotObject);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(1, parserException.getContextMark().getLine());
            Assert.assertEquals(2, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when the variable beneath "Store" beneath "Response" has an array beneath it and not
     * objects
     *
     * @throws Exception
     */
    @Test
    public void testSyntaxErrorResponseStoreItemArrayNotObjectParsing() throws Exception
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorResponseStoreItemArrayNotObject);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(3, parserException.getContextMark().getLine());
            Assert.assertEquals(4, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Store" beneath "Response" has an object beneath it and not an array
     *
     * @throws Exception
     */
    @Test
    public void testSyntaxErrorResponseStoreObjectNotArrayParsing() throws Exception
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorResponseStoreObjectNotArray);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(2, parserException.getContextMark().getLine());
            Assert.assertEquals(3, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when the validation beneath "Validate" beneath "Response" has an array beneath it and
     * not objects
     *
     * @throws Exception
     */
    @Test
    public void testSyntaxErrorResponseValidationItemArrayNotObjectParsing() throws Exception
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorResponseValidationItemArrayNotObject);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(3, parserException.getContextMark().getLine());
            Assert.assertEquals(4, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Validate" beneath "Response" has an object beneath it and not an array
     *
     * @throws Exception
     */
    @Test
    public void testSyntaxErrorResponseValidationObjectNotArrayParsing() throws Exception
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorResponseValidationObjectNotArray);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(2, parserException.getContextMark().getLine());
            Assert.assertEquals(3, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies Response is parsed correctly
     *
     * @throws Exception
     */
    @Test
    public void testResponseParsing() throws Exception
    {
        // Httpcode
        final String httpcode = "200";
        // Validation
        final String validation_name = "validationName";
        final String validation_extractionType = Constants.REGEXP;
        final String validation_extractionExpression = ".*";
        // Store
        final String store_name = "variableName";
        final String store_extractionType = Constants.COOKIE;
        final String store_extractionExpression = "cookieName";

        final String yamlSpec = "Httpcode : " + httpcode + "\n" //
                                + "Validate : \n" //
                                + "    - " + validation_name + " :\n" //
                                + "        " + validation_extractionType + " : " + validation_extractionExpression + "\n" //
                                + "Store : \n" //
                                + "    - " + store_name + " :\n" //
                                + "        " + store_extractionType + " : " + store_extractionExpression;

        final Node responseContent = YamlParserTestHelper.parseToNode(yamlSpec);

        // Parse response
        final Response response = (Response) new ResponseParser().parse(responseContent.getStartMark(), responseContent).get(0);

        // Verify response is correct
        final List<AbstractResponseSubItem> responseItems = response.getResponseItems();
        Assert.assertTrue(responseItems.get(0) instanceof HttpCodeValidator);
        Assert.assertEquals(httpcode, ((HttpCodeValidator) responseItems.get(0)).getHttpcode());
        Assert.assertTrue(responseItems.get(1) instanceof Validator);
        final Validator validator = (Validator) responseItems.get(1);
        Assert.assertEquals(validation_name, validator.getValidationName());
        Assert.assertTrue(validator.getExtractor() instanceof RegexpExtractor);
        Assert.assertEquals(validation_extractionExpression, validator.getExtractor().getExtractionExpression());
        Assert.assertNull(validator.getMethod());

        Assert.assertTrue(responseItems.get(2) instanceof ResponseStore);
        final ResponseStore store = (ResponseStore) responseItems.get(2);
        Assert.assertEquals(store_name, store.getVariableName());
        Assert.assertTrue(store.getExtractor() instanceof CookieExtractor);
        Assert.assertEquals(store_extractionExpression, store.getExtractor().getExtractionExpression());
    }

}
