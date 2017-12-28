package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.HttpcodeValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor.XpathExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.MatchesValidator;

public class ActionItemParserTest extends AbstractScriptItemParserTest
{
    /**
     * Tests parsing with all possible headers
     */
    @Test
    public void testFullParse()
    {
        final String name = "TestAction";
        final String url = "url";
        final String method = "GET";
        final String param0 = "param_1=val_1";
        final String param1 = "param2=val2";
        final String encoded = "false";
        final String responseCode = "303";
        final String xpath = "./h1";
        final String text = "This is a title";
        final String xpath1 = "./h2";
        final String xpath3 = "./h3";
        final JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        final ObjectNode actionNodeContent = nodeFactory.objectNode();
        actionNodeContent.put(CsvConstants.TYPE, CsvConstants.TYPE_ACTION);
        actionNodeContent.put(CsvConstants.NAME, name);
        actionNodeContent.put(CsvConstants.URL, url);
        actionNodeContent.put(CsvConstants.METHOD, method);
        actionNodeContent.put(CsvConstants.PARAMETERS, param0 + "&" + param1);
        actionNodeContent.put(CsvConstants.ENCODED, encoded);
        actionNodeContent.put(CsvConstants.RESPONSECODE, responseCode);
        actionNodeContent.put(CsvConstants.XPATH, xpath);
        actionNodeContent.put(CsvConstants.TEXT, text);
        actionNodeContent.put(CsvConstants.XPATH_GETTER_PREFIX + "1", xpath1);
        actionNodeContent.put(CsvConstants.XPATH_GETTER_PREFIX + "3", xpath3);

        final ActionItemParser parser = new ActionItemParser();
        final Action action = parser.parse(actionNodeContent);
        Assert.assertEquals(name, action.getName());

        AbstractActionItem item = action.getActionItems().get(0);
        Assert.assertTrue(item instanceof Request);
        final Request request = (Request) item;
        // Url
        Assert.assertEquals(url, request.getUrl());
        // Method
        Assert.assertEquals(method, request.getHttpMethod());
        // Parameters
        Assert.assertEquals(param0, request.getParameters().get(0).getName() + "=" + request.getParameters().get(0).getValue());
        Assert.assertEquals(param1, request.getParameters().get(1).getName() + "=" + request.getParameters().get(1).getValue());
        // Encoded
        Assert.assertEquals(encoded, request.getEncodeParameters());
        Assert.assertEquals(encoded, request.getEncodeBody());

        item = action.getActionItems().get(1);
        Assert.assertTrue(item instanceof Response);
        final Response response = (Response) item;
        // ResponseCode
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpcodeValidator);
        Assert.assertEquals(responseCode, ((HttpcodeValidator) response.getResponseItems().get(0)).getHttpcode());

        // Validator - Xpath + Text
        Assert.assertTrue(response.getResponseItems().get(1) instanceof Validator);
        final Validator validator = (Validator) response.getResponseItems().get(1);
        // Xpath
        Assert.assertTrue(validator.getExtractor() instanceof XpathExtractor);
        Assert.assertEquals(xpath, validator.getExtractor().getExtractionExpression());
        // Text/MatchesValidator
        Assert.assertTrue(validator.getMethod() instanceof MatchesValidator);
        Assert.assertEquals(text, ((MatchesValidator) validator.getMethod()).getValidationExpression());

        // Response Store for xpath1
        Assert.assertTrue(response.getResponseItems().get(2) instanceof ResponseStore);
        ResponseStore responseStore = (ResponseStore) response.getResponseItems().get(2);
        Assert.assertTrue(responseStore.getExtractor() instanceof XpathExtractor);
        Assert.assertEquals(xpath1, responseStore.getExtractor().getExtractionExpression());

        // Response Store for xpath3
        Assert.assertTrue(response.getResponseItems().get(3) instanceof ResponseStore);
        responseStore = (ResponseStore) response.getResponseItems().get(3);
        Assert.assertTrue(responseStore.getExtractor() instanceof XpathExtractor);
        Assert.assertEquals(xpath3, responseStore.getExtractor().getExtractionExpression());
    }
}
