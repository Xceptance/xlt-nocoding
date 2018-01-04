package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.HttpcodeValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor.XpathExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.MatchesValidator;

public class ActionItemParserTest extends CsvScriptItemParserTest
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
        final Iterable<CSVRecord> records = buildRecord(input);
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

    /**
     * Verifies a XPath variable declaration can be parsed
     * 
     * @throws IOException
     */
    @Test
    public void testXpathVariable() throws IOException
    {
        final String value = "xpath";
        final String input = CsvConstants.XPATH_GETTER_PREFIX + "112" + "\n" + value;
        final Iterable<CSVRecord> records = buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            final Action action = new ActionItemParser().parse(csvRecord);
            Assert.assertEquals(2, action.getActionItems().size());
            Assert.assertTrue(action.getActionItems().get(1) instanceof Response);
            final Response response = (Response) action.getActionItems().get(1);
            Assert.assertEquals(1, response.getResponseItems().size());
            Assert.assertTrue(((ResponseStore) response.getResponseItems().get(0)).getExtractor() instanceof XpathExtractor);
            Assert.assertEquals(value, ((ResponseStore) response.getResponseItems().get(0)).getExtractor().getExtractionExpression());
        }
    }

    /**
     * Verifies a Regexp variable declaration can be parsed
     * 
     * @throws IOException
     */
    @Test
    public void testRegexp() throws IOException
    {
        final String value = "regexp";
        final String input = CsvConstants.REGEXP_GETTER_PREFIX + "112" + "\n" + value;
        final Iterable<CSVRecord> records = buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            final Action action = new ActionItemParser().parse(csvRecord);
            Assert.assertEquals(2, action.getActionItems().size());
            Assert.assertTrue(action.getActionItems().get(1) instanceof Response);
            final Response response = (Response) action.getActionItems().get(1);
            Assert.assertEquals(1, response.getResponseItems().size());
            Assert.assertTrue(((ResponseStore) response.getResponseItems().get(0)).getExtractor() instanceof RegexpExtractor);
            Assert.assertEquals(value, ((ResponseStore) response.getResponseItems().get(0)).getExtractor().getExtractionExpression());
        }
    }

    /**
     * Verifies the minimal declaration of a csv, that is only the URL is provided, can be parsed
     * 
     * @throws IOException
     */
    @Test
    public void testMinimalDeclaration() throws IOException
    {
        final String headers = CsvConstants.URL;
        final String value = "url";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            final Action action = new ActionItemParser().parse(csvRecord);
            Assert.assertEquals(1, action.getActionItems().size());
            Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
            final Request request = (Request) action.getActionItems().get(0);
            Assert.assertEquals(value, request.getUrl());
        }
    }

    /**
     * Verifies parameters are properly parsed to {@link NameValuePair}s
     * 
     * @throws IOException
     */
    @Test
    public void testParameterParsing() throws IOException
    {
        final String headers = CsvConstants.PARAMETERS;
        final String value = "param1=val1&param2=val2";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            final Action action = new ActionItemParser().parse(csvRecord);
            Assert.assertEquals(1, action.getActionItems().size());
            Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
            final Request request = (Request) action.getActionItems().get(0);
            Assert.assertEquals(2, request.getParameters().size());
            int i = 1;
            for (final NameValuePair parameter : request.getParameters())
            {
                Assert.assertEquals("param" + i, parameter.getName());
                Assert.assertEquals("val" + i, parameter.getValue());
                i++;
            }
        }
    }

    /**
     * Verifies you cannot combine Regexp and Xpath validations
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegExpAndXpathValidation() throws IOException
    {
        final String headers = CsvConstants.XPATH + CsvConstants.REGEXP;
        final String value = "xpath, regexp";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            new ActionItemParser().parse(csvRecord);
        }
    }

    /**
     * Verifies you cannot combine Regexp validation and Xpath store
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegExpValidationAndXpathStore() throws IOException
    {
        final String headers = CsvConstants.XPATH + CsvConstants.REGEXP_GETTER_PREFIX + "1";
        final String value = "xpath, regexp";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            new ActionItemParser().parse(csvRecord);
        }
    }

    /**
     * Verifies you cannot combine Regexp store and Xpath validation
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegExpStoreAndXpathValidation() throws IOException
    {
        final String headers = CsvConstants.XPATH_GETTER_PREFIX + "1" + CsvConstants.REGEXP;
        final String value = "xpath, regexp";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            new ActionItemParser().parse(csvRecord);
        }
    }

    /**
     * Verifies you cannot combine Regexp store and Xpath store
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegExpStoreAndXpathStore() throws IOException
    {
        final String headers = CsvConstants.XPATH_GETTER_PREFIX + "1" + CsvConstants.REGEXP_GETTER_PREFIX + "1";
        final String value = "xpath, regexp";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            new ActionItemParser().parse(csvRecord);
        }
    }

}
