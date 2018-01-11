package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.actionItems;

import java.io.IOException;

import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.CsvParserTestUtils;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor.XpathExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.ResponseStore;

public class ResponseParserTest
{
    /**
     * Verifies a XPath variable declaration can be parsed
     * 
     * @throws IOException
     */
    @Test
    public void testXpathVariable() throws IOException
    {
        final String headers = CsvConstants.XPATH_GETTER_PREFIX + "2";
        final String value = "xpath";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            final Response response = new ResponseParser().parse(csvRecord);
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
        final String headers = CsvConstants.REGEXP_GETTER_PREFIX + "112";
        final String value = "regexp";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            final Response response = new ResponseParser().parse(csvRecord);
            Assert.assertEquals(1, response.getResponseItems().size());
            Assert.assertTrue(((ResponseStore) response.getResponseItems().get(0)).getExtractor() instanceof RegexpExtractor);
            Assert.assertEquals(value, ((ResponseStore) response.getResponseItems().get(0)).getExtractor().getExtractionExpression());
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
        final String headers = CsvConstants.XPATH + "," + CsvConstants.REGEXP_GETTER_PREFIX + "1";
        final String value = "xpath, regexp";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            new ResponseParser().parse(csvRecord);
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
        final String headers = CsvConstants.XPATH_GETTER_PREFIX + "1," + CsvConstants.REGEXP;
        final String value = "xpath, regexp";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            new ResponseParser().parse(csvRecord);
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
        final String headers = CsvConstants.XPATH_GETTER_PREFIX + "1," + CsvConstants.REGEXP_GETTER_PREFIX + "1";
        final String value = "xpath, regexp";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            new ResponseParser().parse(csvRecord);
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
        final String headers = CsvConstants.XPATH + "," + CsvConstants.REGEXP;
        final String value = "xpath, regexp";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            new ResponseParser().parse(csvRecord);
        }
    }

    /**
     * Verifies you cannot combine Regexp store and Xpath store
     * 
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTextButNoExtractorDefined() throws IOException
    {
        final String headers = CsvConstants.TEXT;
        final String value = "text";
        final String input = headers + "\n" + value;
        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            new ResponseParser().parse(csvRecord);
        }
    }
}
