package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.request.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.HttpcodeValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpath.XpathExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.MatchesValidator;

/**
 * Tests for parsing the "Action" tag
 * 
 * @author ckeiner
 */
public class ActionParserTest extends ParserTest
{
    /*
     * Function test
     */

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
        final List<ScriptItem> scriptItems = parser.parse(fileSingleActionNoDefaultsData);

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
        final Map<String, String> expectedHeader = new HashMap<String, String>(2);
        expectedHeader.put("header_1", "header_value_1");
        expectedHeader.put("header_2", "header_value_2");
        Assert.assertTrue(expectedHeader.equals(request.getHeaders()));

        // Assert response
        Assert.assertTrue(action.getActionItems().get(1) instanceof Response);
        final Response response = (Response) action.getActionItems().get(1);
        final List<AbstractResponseItem> responseItems = response.getResponseItems();

        // Assert HttpcodeValidator
        Assert.assertTrue(responseItems.get(0) instanceof HttpcodeValidator);
        Assert.assertEquals("400", ((HttpcodeValidator) responseItems.get(0)).getHttpcode());

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
        final List<ScriptItem> scriptItems = parser.parse(fileEmptyAction);
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
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorActionParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorAction);
    }

    /**
     * Verifies an error happens when "Action" has an array beneath it and not objects
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorActionArrayNotObjectParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorActionArrayNotObject);
    }

    /**
     * Verifies an error happens when "Response" is defined before "Request"
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testWrongOrderActionParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileWrongOrderAction);
    }

    /**
     * Verifies an error happens when "Subrequests" is defined before "Request"
     * 
     * @throws Throwable
     */
    @Test(expected = JsonParseException.class)
    public void testWrongOrderParsing() throws Throwable
    {
        final Parser parser = new YamlParser();
        parser.parse(fileWrongOrder);
    }

}
