package com.xceptance.xlt.nocoding.parser.csv.scriptItems;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.csv.CsvConstants;
import com.xceptance.xlt.nocoding.parser.csv.scriptItems.ActionItemParser;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.HttpcodeValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.MatchesValidator;

public class ActionItemParserTest
{

    /**
     * Verifies all headers are parsed properly
     * 
     * @throws IOException
     */
    @Test
    public void testAllParsing() throws IOException
    {
        final String headers = CsvConstants.TYPE + "," + CsvConstants.NAME + "," + CsvConstants.URL + "," + CsvConstants.METHOD + ","
                               + CsvConstants.PARAMETERS + "," + CsvConstants.ENCODED + "," + CsvConstants.RESPONSECODE + ","
                               + CsvConstants.REGEXP + "," + CsvConstants.TEXT + "," + CsvConstants.REGEXP_GETTER_PREFIX + "1";
        final String type = CsvConstants.TYPE_ACTION;
        final String name = "TestAction";
        final String url = "url";
        final String method = "GET";
        final String parameters = "param1=val1&param2=val2";
        final String encoded = "false";
        final String responsecode = "200";
        final String regexp = "regexp";
        final String text = "text";
        final String regexpVar = "regexpVar";
        final String value = type + "," + name + "," + url + "," + method + "," + parameters + "," + encoded + "," + responsecode + ","
                             + regexp + "," + text + "," + regexpVar;
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);
        Action action = null;

        for (final CSVRecord csvRecord : records)
        {
            action = new ActionItemParser().parse(csvRecord);
        }

        // Validate action
        Assert.assertNotNull("Action is null", action);
        Assert.assertEquals(name, action.getName());
        Assert.assertEquals(2, action.getActionItems().size());

        // Validate request
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        final Request request = (Request) action.getActionItems().get(0);
        Assert.assertEquals(url, request.getUrl());
        Assert.assertEquals(method, request.getHttpMethod());
        int i = 1;
        for (final NameValuePair parameter : request.getParameters())
        {
            Assert.assertEquals("param" + i, parameter.getName());
            Assert.assertEquals("val" + i, parameter.getValue());
            i++;
        }
        Assert.assertEquals(encoded, request.getEncodeBody());
        Assert.assertEquals(encoded, request.getEncodeParameters());

        // Validate response
        Assert.assertTrue(action.getActionItems().get(1) instanceof Response);
        final List<AbstractResponseItem> responseItems = ((Response) action.getActionItems().get(1)).getResponseItems();
        Assert.assertEquals(3, responseItems.size());
        // Validate httpcode
        Assert.assertTrue(responseItems.get(0) instanceof HttpcodeValidator);
        Assert.assertEquals(responsecode, ((HttpcodeValidator) responseItems.get(0)).getHttpcode());
        // Validate validator
        Assert.assertTrue(responseItems.get(1) instanceof Validator);
        final Validator validator = (Validator) responseItems.get(1);

        Assert.assertEquals("Validate " + name, validator.getValidationName());
        Assert.assertTrue(validator.getExtractor() instanceof RegexpExtractor);
        Assert.assertEquals(regexp, validator.getExtractor().getExtractionExpression());
        Assert.assertTrue("Validator is not a " + MatchesValidator.class.getSimpleName(),
                          validator.getMethod() instanceof MatchesValidator);
        Assert.assertEquals(text, ((MatchesValidator) validator.getMethod()).getValidationExpression());
        // Validate response store
        Assert.assertTrue(responseItems.get(2) instanceof ResponseStore);
        final ResponseStore responseStore = (ResponseStore) responseItems.get(2);
        Assert.assertTrue(responseStore.getExtractor() instanceof RegexpExtractor);
        Assert.assertEquals(regexpVar, responseStore.getExtractor().getExtractionExpression());
    }

}
