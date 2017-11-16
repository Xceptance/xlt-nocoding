package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.HttpcodeValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.RegexpSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.XpathSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.ResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.MatchesValidator;

public class ActionItemParserTest extends ParserTest
{
    /*
     * Function test
     */

    protected final String fileSingleActionNoDefaultsData = path + "SAND.yml";

    // TODO [Meeting] I only "parse" these 3 and do not confirm the result?
    protected final String fileXhrSubrequests = path + "xhrSubrequests.yml";

    protected final String fileStaticSubrequests = path + "staticSubrequests.yml";

    protected final String fileComplexTestCase = path + "complexTestCase.yml";

    protected final String fileEmptyAction = path + "emptyAction.yml";

    /*
     * Error cases
     */

    protected final String fileSyntaxErrorAction = path + "syntaxErrorAction.yml";

    protected final String fileSyntaxErrorRequest = path + "syntaxErrorRequest.yml";

    protected final String fileSyntaxErrorResponse = path + "syntaxErrorResponse.yml";

    protected final String fileSyntaxErrorSubrequests = path + "syntaxErrorSubrequests.yml";

    protected final String fileSyntaxErrorXhr = path + "syntaxErrorXhr.yml";

    protected final String fileSyntaxErrorStatic = path + "syntaxErrorStatic.yml";

    protected final String fileSyntaxErrorActionNameNull = path + "syntaxErrorActionNameNull.yml";

    protected final String fileSyntaxErrorUrlNull = path + "syntaxErrorUrlNull.yml";

    protected final String fileWrongOrderAction = path + "wrongOrderAction.yml";

    protected final String fileWrongOrderSubrequest = path + "wrongOrderSubrequest.yml";

    @Test
    public void testSingleActionNoDefaultsParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSingleActionNoDefaultsData);
        final List<ScriptItem> scriptItems = parser.parse();

        Assert.assertEquals(1, scriptItems.size());

        // Assert it's an action
        Assert.assertTrue(scriptItems.get(0) instanceof LightWeigthAction);
        final LightWeigthAction action = (LightWeigthAction) scriptItems.get(0);
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
        Assert.assertTrue(validation.getSelector() instanceof XpathSelector);
        Assert.assertEquals("xpath_value_1", validation.getSelector().getSelectionExpression());

        // Assert second validation
        Assert.assertTrue(responseItems.get(2) instanceof Validator);
        validation = (Validator) responseItems.get(2);
        Assert.assertEquals("validation_name_2", validation.getValidationName());
        Assert.assertTrue(validation.getSelector() instanceof RegexpSelector);
        Assert.assertEquals("regexp_value_2", validation.getSelector().getSelectionExpression());
        Assert.assertTrue(validation.getMode() instanceof MatchesValidator);
        Assert.assertEquals("matches_value_2", ((MatchesValidator) validation.getMode()).getValidationExpression());

        // Assert first store
        Assert.assertTrue(responseItems.get(3) instanceof AbstractResponseStore);
        AbstractResponseStore store = (AbstractResponseStore) response.getResponseItems().get(3);
        Assert.assertTrue(store instanceof ResponseStore);
        Assert.assertEquals("variable_1", store.getVariableName());
        Assert.assertTrue(store.getSelector() instanceof XpathSelector);
        Assert.assertEquals("xpath_1", store.getSelector().getSelectionExpression());

        Assert.assertTrue(responseItems.get(4) instanceof AbstractResponseStore);
        store = (AbstractResponseStore) responseItems.get(4);
        Assert.assertTrue(store instanceof ResponseStore);
        Assert.assertEquals("variable_2", store.getVariableName());
        Assert.assertTrue(store.getSelector() instanceof RegexpSelector);
        Assert.assertEquals("xpath_2", store.getSelector().getSelectionExpression());
    }

    @Test
    public void testXhrSubrequestsParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileXhrSubrequests);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test
    public void testStaticSubrequestsParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileStaticSubrequests);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test
    public void testComplexTestCaseParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileComplexTestCase);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test
    public void testEmptyActionParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileEmptyAction);
        final List<ScriptItem> scriptItems = parser.parse();
        Assert.assertTrue(scriptItems.get(0) instanceof LightWeigthAction);
    }

    /*
     * Error cases
     */

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorActionParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorAction);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorRequestParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorRequest);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponse);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorSubrequestsParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorSubrequests);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorXhrParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorXhr);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorStaticParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorStatic);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorActionNameNullParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorActionNameNull);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorUrlNullParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorUrlNull);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    // TODO
    @Test(expected = JsonParseException.class)
    public void testWrongOrderActionParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileWrongOrderAction);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

    // TODO
    @Test(expected = JsonParseException.class)
    public void testWrongOrderSubrequestParsing() throws Throwable
    {
        final Parser parser = new YamlParser(fileWrongOrderSubrequest);
        @SuppressWarnings("unused")
        final List<ScriptItem> scriptItems = parser.parse();
    }

}
