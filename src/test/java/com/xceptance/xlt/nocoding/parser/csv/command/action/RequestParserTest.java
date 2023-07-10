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
import org.htmlunit.util.NameValuePair;
import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.action.request.Request;
import com.xceptance.xlt.nocoding.parser.csv.CsvConstants;
import com.xceptance.xlt.nocoding.parser.csv.command.CsvParserTestUtils;

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
