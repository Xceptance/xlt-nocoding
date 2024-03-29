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
package com.xceptance.xlt.nocoding.parser.yaml.command.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlunit.util.NameValuePair;
import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.action.Action;
import com.xceptance.xlt.nocoding.command.action.request.Request;
import com.xceptance.xlt.nocoding.command.action.response.AbstractResponseSubItem;
import com.xceptance.xlt.nocoding.command.action.response.HttpCodeValidator;
import com.xceptance.xlt.nocoding.command.action.response.Response;
import com.xceptance.xlt.nocoding.command.action.response.Validator;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.xpath.XpathExtractor;
import com.xceptance.xlt.nocoding.command.action.response.store.AbstractResponseStore;
import com.xceptance.xlt.nocoding.command.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.command.action.response.validator.MatchesValidator;
import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;

/**
 * Tests for parsing the "Action" tag
 *
 * @author ckeiner
 */
public class ActionParserTest extends AbstractParserTest
{
    /*
     * Function test
     */

    protected String path = super.path + "action/";

    protected final String fileSingleActionNoDefaultsData = path + "SAND.yml";

    protected final String fileComplexTestCase = path + "complexTestCase.yml";

    protected final String fileEmptyAction = path + "emptyAction.yml";

    protected final String fileActionNameNull = path + "actionNameNull.yml";

    /*
     * Error cases
     */

    protected final String fileSyntaxErrorAction = path + "syntaxErrorAction.yml";

    protected final String fileSyntaxErrorActionArrayNotObject = path + "syntaxErrorActionArrayNotObject.yml";

    protected final String fileWrongOrderAction = path + "wrongOrderAction.yml";

    protected final String fileWrongOrder = path + "wrongOrder.yml";

    /**
     * Verifies an action is parsed correctly
     *
     * @throws Exception
     */
    @Test
    public void testSingleActionNoDefaultsParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<Command> scriptItems = parser.parse(fileSingleActionNoDefaultsData);

        Assert.assertEquals(1, scriptItems.size());

        // Assert it's an action
        Assert.assertTrue(scriptItems.get(0) instanceof Action);
        final Action action = (Action) scriptItems.get(0);
        Assert.assertEquals("name", action.getName());
        Assert.assertEquals(2, action.getActionItems().size());

        // Assert request
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        final Request request = (Request) action.getActionItems().get(0);
        Assert.assertEquals("GET", request.getHttpMethod());
        Assert.assertEquals("true", request.getXhr());
        Assert.assertEquals("false", request.getEncodeParameters());
        Assert.assertEquals("body", request.getBody());
        Integer counter = 1;
        for (final NameValuePair parameter : request.getParameters())
        {
            Assert.assertEquals("parameter_" + counter.toString(), parameter.getName());
            Assert.assertEquals("parameter_value_" + counter.toString(), parameter.getValue());
            counter++;
        }
        final Map<String, String> expectedHeader = new HashMap<>(2);
        expectedHeader.put("header_1", "header_value_1");
        expectedHeader.put("header_2", "header_value_2");
        Assert.assertTrue(expectedHeader.equals(request.getHeaders()));

        // Assert response
        Assert.assertTrue(action.getActionItems().get(1) instanceof Response);
        final Response response = (Response) action.getActionItems().get(1);
        final List<AbstractResponseSubItem> responseItems = response.getResponseItems();

        // Assert HttpcodeValidator
        Assert.assertTrue(responseItems.get(0) instanceof HttpCodeValidator);
        Assert.assertEquals("400", ((HttpCodeValidator) responseItems.get(0)).getHttpcode());

        // Assert first validation
        Assert.assertTrue(responseItems.get(1) instanceof Validator);
        Validator validation = (Validator) responseItems.get(1);
        Assert.assertEquals("validation_name_1", validation.getValidationName());
        Assert.assertTrue(validation.getExtractor() instanceof XpathExtractor);
        Assert.assertEquals("xpath_value_1", validation.getExtractor().getExtractionExpression());

        // Assert second validation
        Assert.assertTrue(responseItems.get(2) instanceof Validator);
        validation = (Validator) responseItems.get(2);
        Assert.assertEquals("validation_name_2", validation.getValidationName());
        Assert.assertTrue(validation.getExtractor() instanceof RegexpExtractor);
        Assert.assertEquals("regexp_value_2", validation.getExtractor().getExtractionExpression());
        Assert.assertTrue(validation.getMethod() instanceof MatchesValidator);
        Assert.assertEquals("matches_value_2", ((MatchesValidator) validation.getMethod()).getValidationExpression());

        // Assert first store
        Assert.assertTrue(responseItems.get(3) instanceof AbstractResponseStore);
        AbstractResponseStore store = (AbstractResponseStore) response.getResponseItems().get(3);
        Assert.assertTrue(store instanceof ResponseStore);
        Assert.assertEquals("variable_1", store.getVariableName());
        Assert.assertTrue(store.getExtractor() instanceof XpathExtractor);
        Assert.assertEquals("xpath_1", store.getExtractor().getExtractionExpression());

        Assert.assertTrue(responseItems.get(4) instanceof AbstractResponseStore);
        store = (AbstractResponseStore) responseItems.get(4);
        Assert.assertTrue(store instanceof ResponseStore);
        Assert.assertEquals("variable_2", store.getVariableName());
        Assert.assertTrue(store.getExtractor() instanceof RegexpExtractor);
        Assert.assertEquals("xpath_2", store.getExtractor().getExtractionExpression());
    }

    /**
     * Verifies all tags can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testComplexTestCaseParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileComplexTestCase);
    }

    /**
     * Verifies "Action: " with nothing in it can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testEmptyActionParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<Command> scriptItems = parser.parse(fileEmptyAction);
        Assert.assertTrue(scriptItems.get(0) instanceof Action);
    }

    /**
     * Verifies no error is thrown when "Name" beneath "Action" is empty
     *
     * @throws Exception
     */
    @Test
    public void testActionNameNullParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileActionNameNull);
    }

    /*
     * Error cases
     */

    /**
     * Verifies an error happens when "Action" has an invalid tag
     *
     * @throws Exception
     */
    @Test(expected = ParserException.class)
    public void testSyntaxErrorActionParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorAction);
    }

    @Test
    public void testSyntaxErrorActionParsingLineNumber() throws IOException
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorAction);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(0, parserException.getContextMark().getLine());
            Assert.assertEquals(2, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Action" has an array beneath it and not objects
     *
     * @throws Exception
     */
    @Test
    public void testSyntaxErrorActionArrayNotObjectParsing() throws Exception
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileSyntaxErrorActionArrayNotObject);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(0, parserException.getContextMark().getLine());
            Assert.assertEquals(1, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Response" is defined before "Request"
     *
     * @throws Exception
     */
    @Test
    public void testWrongOrderActionParsing() throws Exception
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileWrongOrderAction);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(0, parserException.getContextMark().getLine());
            Assert.assertEquals(15, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error happens when "Subrequests" is defined before "Request"
     *
     * @throws Throwable
     */
    @Test
    public void testWrongOrderParsing() throws Throwable
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileWrongOrder);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(0, parserException.getContextMark().getLine());
            Assert.assertEquals(28, parserException.getProblemMark().getLine());
        }
    }

}
