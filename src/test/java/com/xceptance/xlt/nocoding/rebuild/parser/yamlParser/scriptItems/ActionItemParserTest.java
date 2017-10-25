package com.xceptance.xlt.nocoding.rebuild.parser.yamlParser.scriptItems;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreDefault;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.RegExpStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.XpathStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.RegExpValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.XPathValidator;

public class ActionItemParserTest
{
    private final String path = "./target/test-classes/test-scripts/";

    private final String fileTmp = path + "tmp.yml";

    private final String fileXhrSubrequests = path + "xhrSubrequests.yml";

    private final String fileTestData = path + "testData.yml";

    private final String fileEmptyFile = path + "emptyFile.yml";

    private final String fileNotExistingFile = path + "notExistingFile.yml";

    private final String fileSingleActionNoDefaultsData = path + "SAND.yml";

    private final String fileStaticSubrequests = path + "staticSubrequests.yml";

    private final String fileComplexTestCase = path + "complexTestCase.yml";

    private final String fileSyntaxErrorRoot = path + "syntaxErrorRoot.yml";

    private final String fileSyntaxErrorAction = path + "syntaxErrorAction.yml";

    private final String fileSyntaxErrorRequest = path + "syntaxErrorRequest.yml";

    private final String fileSyntaxErrorResponse = path + "syntaxErrorResponse.yml";

    private final String fileSyntaxErrorSubrequests = path + "syntaxErrorSubrequests.yml";

    private final String fileSyntaxErrorXhr = path + "syntaxErrorXhr.yml";

    private final String fileSyntaxErrorStatic = path + "syntaxErrorStatic.yml";

    private final String fileSyntaxErrorActionNameNull = path + "syntaxErrorActionNameNull.yml";

    private final String fileSyntaxErrorUrlNull = path + "syntaxErrorUrlNull.yml";

    @Test
    public void testSingleActionNoDefaults() throws Exception
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
        Assert.assertEquals("GET", request.getMethod());
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
        Assert.assertEquals("400", response.getHttpcode());
        // Assert validator
        AbstractValidator validation = response.getValidation().get(0);
        Assert.assertTrue(validation instanceof XPathValidator);
        final XPathValidator xpathVal = (XPathValidator) validation;
        Assert.assertEquals("validation_name_1", xpathVal.getValidationName());
        Assert.assertEquals("xpath_value_1", xpathVal.getxPathExpression());
        validation = response.getValidation().get(1);
        Assert.assertTrue(validation instanceof RegExpValidator);
        final RegExpValidator regExpVal = (RegExpValidator) validation;
        Assert.assertEquals("validation_name_2", regExpVal.getValidationName());
        Assert.assertEquals("regexp_value_2", regExpVal.getPattern());
        Assert.assertEquals("matches_value_2", regExpVal.getText());
        // Assert store
        AbstractResponseStore store = response.getResponseStore().get(0);
        Assert.assertTrue(store instanceof XpathStore);
        final XpathStore xpathStore = (XpathStore) store;
        Assert.assertEquals("variable_1", xpathStore.getVariableName());
        Assert.assertEquals("xpath_1", xpathStore.getxPathExpression());
        store = response.getResponseStore().get(1);
        Assert.assertTrue(store instanceof RegExpStore);
        final RegExpStore regExpStore = (RegExpStore) store;
        Assert.assertEquals("variable_2", regExpStore.getVariableName());
        Assert.assertEquals("xpath_2", regExpStore.getPattern());
    }

    @Test
    public void testFileTmp() throws Exception
    {
        final Parser parser = new YamlParser(fileTmp);
        final List<ScriptItem> scriptItems = parser.parse();

        Assert.assertEquals(2, scriptItems.size());

        // Assert it's an action
        Assert.assertTrue(scriptItems.get(0) instanceof StoreDefault);
        final StoreDefault first = (StoreDefault) scriptItems.get(0);
        Assert.assertEquals("Name", first.getVariableName());
        Assert.assertEquals("name", first.getValue());

        final StoreDefault second = (StoreDefault) scriptItems.get(1);
        Assert.assertEquals("Url", second.getVariableName());
        Assert.assertEquals("url", second.getValue());

    }

    @Test
    public void testEmptyFile() throws Exception
    {
        final Parser parser = new YamlParser(fileEmptyFile);
        final List<ScriptItem> scriptItems = parser.parse();

        Assert.assertTrue(scriptItems.isEmpty());
    }

    @Test(expected = FileNotFoundException.class)
    public void testNotExistingFile() throws Exception
    {
        final Parser parser = new YamlParser(fileNotExistingFile);
        final List<ScriptItem> scriptItems = parser.parse();
        Assert.assertTrue(scriptItems.isEmpty());
    }

}
