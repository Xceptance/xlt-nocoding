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

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.action.response.Validator;
import com.xceptance.xlt.nocoding.command.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.HeaderExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.command.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.CountValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.MatchesValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.TextValidator;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserTestHelper;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests for parsing the "Validate" tag beneath "Response"
 *
 * @author ckeiner
 */
public class ValidationParserTest
{
    /**
     * Verifies {@link RegexpExtractor} and {@link MatchesValidator} can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testRegExpMatchesGroupValidation() throws Exception
    {
        final String extractionExpression = "RegExpPattern";
        final String validationExpression = "MatchesPattern";
        final String groupExpression = "5";
        final String yamlSpec = "- val_Name_1 : \n" //
                                + "    " + Constants.REGEXP + " : " + extractionExpression + "\n" //
                                + "    " + Constants.MATCHES + " : " + validationExpression + "\n" //
                                + "    " + Constants.GROUP + " : " + groupExpression + "\n";

        final Node validationContent = YamlParserTestHelper.parseToNode(yamlSpec);

        final Validator validator = new ValidationParser().parse(validationContent.getStartMark(), validationContent).get(0);

        Assert.assertTrue(validator.getExtractor() instanceof RegexpExtractor);
        Assert.assertEquals(extractionExpression, validator.getExtractor().getExtractionExpression());
        Assert.assertEquals(groupExpression, ((RegexpExtractor) validator.getExtractor()).getGroup());
        Assert.assertTrue(validator.getMethod() instanceof MatchesValidator);
        Assert.assertEquals(validationExpression, ((MatchesValidator) validator.getMethod()).getValidationExpression());
    }

    /**
     * Verifies {@link CookieExtractor} and {@link TextValidator} can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testCookieTextValidation() throws Exception
    {
        final String extractionExpression = "CookieName";
        final String validationExpression = "CookieValue";
        final String yamlSpec = "- val_Name_1 : \n" //
                                + "    " + Constants.COOKIE + " : " + extractionExpression + "\n" //
                                + "    " + Constants.TEXT + " : " + validationExpression + "\n";

        final Node validationContent = YamlParserTestHelper.parseToNode(yamlSpec);

        final Validator validator = new ValidationParser().parse(validationContent.getStartMark(), validationContent).get(0);

        Assert.assertTrue(validator.getExtractor() instanceof CookieExtractor);
        Assert.assertEquals(extractionExpression, validator.getExtractor().getExtractionExpression());

        Assert.assertTrue(validator.getMethod() instanceof TextValidator);
        Assert.assertEquals(validationExpression, ((TextValidator) validator.getMethod()).getValidationExpression());

    }

    /**
     * Verifies {@link HeaderExtractor} and no {@link AbstractValidator} can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testHeaderValidation() throws Exception
    {
        final String extractionExpression = "HeaderName";
        final String yamlSpec = "- val_Name_1 : \n" //
                                + "    " + Constants.HEADER + " : " + extractionExpression + "\n";

        final Node validationContent = YamlParserTestHelper.parseToNode(yamlSpec);

        final Validator validator = new ValidationParser().parse(validationContent.getStartMark(), validationContent).get(0);
        Assert.assertTrue(validator.getExtractor() instanceof HeaderExtractor);
        Assert.assertEquals(extractionExpression, validator.getExtractor().getExtractionExpression());
        Assert.assertNull(validator.getMethod());
    }

    /**
     * Validates {@link HeaderExtractor} and {@link CountValidator} can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testHeaderCountValidation() throws Exception
    {
        final String extractionExpression = "HeaderName";
        final String validationExpression = "5";
        final String yamlSpec = "- val_Name_1 : \n" //
                                + "    " + Constants.HEADER + " : " + extractionExpression + "\n" //
                                + "    " + Constants.COUNT + " : " + validationExpression + "\n";

        final Node validationContent = YamlParserTestHelper.parseToNode(yamlSpec);

        final Validator validator = new ValidationParser().parse(validationContent.getStartMark(), validationContent).get(0);
        Assert.assertTrue(validator.getExtractor() instanceof HeaderExtractor);
        Assert.assertEquals(extractionExpression, validator.getExtractor().getExtractionExpression());

        Assert.assertTrue(validator.getMethod() instanceof CountValidator);
    }

    /*
     * Errors
     */

    /**
     * Verifies an error is thrown if two validation modes are found
     *
     * @throws Exception
     */
    @Test
    public void testSyntaxErrorResponseValidationTwoValidationModes() throws Exception
    {
        final String yamlSpec = "- val_Name_1 : \n" //
                                + "    " + Constants.REGEXP + " : pattern\n" //
                                + "    " + Constants.TEXT + " : pattern\n" //
                                + "    " + Constants.MATCHES + " : pattern\n";

        final Node validationContent = YamlParserTestHelper.parseToNode(yamlSpec);

        try
        {
            new ValidationParser().parse(validationContent.getStartMark(), validationContent);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(0, parserException.getContextMark().getLine());
            Assert.assertEquals(3, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error is thrown if two extractors are found
     *
     * @throws Exception
     */
    @Test
    public void testSyntaxErrorResponseValidationTwoExtractors() throws Exception
    {
        final String yamlSpec = "- val_Name_1 : \n" //
                                + "    " + Constants.REGEXP + " : pattern\n" //
                                + "    " + Constants.XPATH + " : pattern\n"//
                                + "    " + Constants.TEXT + " : pattern\n";

        final Node validationContent = YamlParserTestHelper.parseToNode(yamlSpec);
        try
        {
            new ValidationParser().parse(validationContent.getStartMark(), validationContent);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(0, parserException.getContextMark().getLine());
            Assert.assertEquals(2, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error is thrown if a group is parsed but the extractor is not a {@link RegexpExtractor}
     *
     * @throws Exception
     */
    @Test
    public void testInvalidGroupValidation() throws Exception
    {
        final String yamlSpec = "- val_Name_1 : \n" //
                                + "    " + Constants.COOKIE + " : cookieName\n" //
                                + "    " + Constants.TEXT + " : cookieValue\n" //
                                + "    " + Constants.GROUP + " : 2\n";

        final Node validationContent = YamlParserTestHelper.parseToNode(yamlSpec);
        try
        {
            new ValidationParser().parse(validationContent.getStartMark(), validationContent);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(0, parserException.getContextMark().getLine());
            Assert.assertEquals(3, parserException.getProblemMark().getLine());
        }
    }

}
