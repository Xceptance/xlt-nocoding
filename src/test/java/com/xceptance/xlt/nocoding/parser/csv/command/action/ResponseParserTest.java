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
package com.xceptance.xlt.nocoding.parser.csv.command.action;

import java.io.IOException;

import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.action.response.Response;
import com.xceptance.xlt.nocoding.command.action.response.Validator;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.xpath.XpathExtractor;
import com.xceptance.xlt.nocoding.command.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.parser.csv.CsvConstants;
import com.xceptance.xlt.nocoding.parser.csv.command.CsvParserTestUtils;

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
     * Verifies that if {@link CsvConstants#NAME} is mapped and neither null nor empty, the name of the validation is
     * "Validate " with the value of <code>CsvConstants.NAME</code> added
     *
     * @throws IOException
     */
    @Test
    public void testValidationNameWithNameDefined() throws IOException
    {
        final String name = "testAction";
        final String headers = CsvConstants.NAME + "," + CsvConstants.XPATH;
        final String value = name + ", xpath";
        final String input = headers + "\n" + value;
        final String validationName = "Validate" + " " + name;
        Response response = null;

        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);
        for (final CSVRecord csvRecord : records)
        {
            response = new ResponseParser().parse(csvRecord);
        }
        Assert.assertTrue(response.getResponseItems().get(0) instanceof Validator);
        final Validator validator = (Validator) response.getResponseItems().get(0);
        Assert.assertEquals(validationName, validator.getValidationName());
    }

    /**
     * Verifies that if {@link CsvConstants#NAME} is mapped and null, the name of the validation is also empty
     *
     * @throws IOException
     */
    @Test
    public void testValidationNameWithNameNotDefined() throws IOException
    {
        final String headers = CsvConstants.NAME + "," + CsvConstants.XPATH;
        final String value = ", xpath";
        final String input = headers + "\n" + value;
        Response response = null;

        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);
        for (final CSVRecord csvRecord : records)
        {
            response = new ResponseParser().parse(csvRecord);
        }
        Assert.assertTrue(response.getResponseItems().get(0) instanceof Validator);
        final Validator validator = (Validator) response.getResponseItems().get(0);
        Assert.assertTrue(validator.getValidationName().isEmpty());
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
