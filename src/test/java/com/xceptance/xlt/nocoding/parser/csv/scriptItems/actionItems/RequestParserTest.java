package com.xceptance.xlt.nocoding.parser.csv.scriptItems.actionItems;

import java.io.IOException;

import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.csv.CsvConstants;
import com.xceptance.xlt.nocoding.parser.csv.scriptItems.CsvParserTestUtils;
import com.xceptance.xlt.nocoding.parser.csv.scriptItems.actionItems.RequestParser;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;

public class RequestParserTest
{
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
        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            final Request request = new RequestParser().parse(csvRecord);
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
        final Iterable<CSVRecord> records = CsvParserTestUtils.buildRecord(input);

        for (final CSVRecord csvRecord : records)
        {
            final Request request = new RequestParser().parse(csvRecord);
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

}
