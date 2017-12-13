package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.HeaderExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.CountValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.MatchesValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.TextValidator;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests for parsing the "Validate" tag beneath "Response"
 * 
 * @author ckeiner
 */
public class ValidationParserTest
{

    /**
     * Verifies an error is thrown if two validation modes are found
     * 
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSyntaxErrorResponseValidationTwoValidationModes() throws Exception
    {
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(Constants.REGEXP, "pattern");
        content.put(Constants.TEXT, "pattern");
        content.put(Constants.MATCHES, "pattern");
        final ObjectNode name = jf.objectNode();
        name.set("val_Name_1", content);

        final ArrayNode validate = jf.arrayNode();
        validate.add(name);
        new ValidationParser().parse(validate);
    }

    /**
     * Verifies an error is thrown if two extractors are found
     * 
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSyntaxErrorResponseValidationTwoExtractors() throws Exception
    {
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(Constants.REGEXP, "pattern");
        content.put(Constants.XPATH, "xpath");
        content.put(Constants.MATCHES, "pattern");
        final ObjectNode name = jf.objectNode();
        name.set("val_Name_1", content);

        final ArrayNode validate = jf.arrayNode();
        validate.add(name);
        new ValidationParser().parse(validate);
    }

    /**
     * Verifies an error is thrown if a group is parsed but the extractor is not a {@link RegexpExtractor}
     * 
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGroupValidation() throws Exception
    {
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(Constants.COOKIE, "cookieName");
        content.put(Constants.TEXT, "cookieValue");
        content.put(Constants.GROUP, "3");
        final ObjectNode name = jf.objectNode();
        name.set("val_Name_1", content);

        final ArrayNode validate = jf.arrayNode();
        validate.add(name);
        new ValidationParser().parse(validate);
    }

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
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(Constants.REGEXP, extractionExpression);
        content.put(Constants.MATCHES, validationExpression);
        content.put(Constants.GROUP, groupExpression);
        final ObjectNode name = jf.objectNode();
        name.set("val_Name_1", content);

        final ArrayNode validate = jf.arrayNode();
        validate.add(name);
        final Validator validator = new ValidationParser().parse(validate).get(0);
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
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(Constants.COOKIE, extractionExpression);
        content.put(Constants.TEXT, validationExpression);
        final ObjectNode name = jf.objectNode();
        name.set("val_Name_1", content);

        final ArrayNode validate = jf.arrayNode();
        validate.add(name);
        final Validator validator = new ValidationParser().parse(validate).get(0);
        Assert.assertTrue(validator.getExtractor() instanceof CookieExtractor);
        Assert.assertEquals(extractionExpression, validator.getExtractor().getExtractionExpression());

        Assert.assertTrue(validator.getMethod() instanceof TextValidator);
        Assert.assertEquals(validationExpression, ((TextValidator) validator.getMethod()).getValidationExpression());

    }

    /**
     * Verifies {@link HeaderExtractor} and no {@link AbstractValidationMethod} can be parsed
     * 
     * @throws Exception
     */
    @Test
    public void testHeaderValidation() throws Exception
    {
        final String extractionExpression = "HeaderName";
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(Constants.HEADER, extractionExpression);
        final ObjectNode name = jf.objectNode();
        name.set("val_Name_1", content);

        final ArrayNode validate = jf.arrayNode();
        validate.add(name);
        final Validator validator = new ValidationParser().parse(validate).get(0);
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
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(Constants.HEADER, extractionExpression);
        content.put(Constants.COUNT, validationExpression);
        final ObjectNode name = jf.objectNode();
        name.set("val_Name_1", content);

        final ArrayNode validate = jf.arrayNode();
        validate.add(name);
        final Validator validator = new ValidationParser().parse(validate).get(0);
        Assert.assertTrue(validator.getExtractor() instanceof HeaderExtractor);
        Assert.assertEquals(extractionExpression, validator.getExtractor().getExtractionExpression());

        Assert.assertTrue(validator.getMethod() instanceof CountValidator);

    }

}
