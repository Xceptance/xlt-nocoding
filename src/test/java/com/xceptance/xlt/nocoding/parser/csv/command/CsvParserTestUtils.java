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
package com.xceptance.xlt.nocoding.parser.csv.command;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.parser.csv.CsvConstants;

public class CsvParserTestUtils
{
    public static Iterable<CSVRecord> buildRecord(final String input) throws IOException
    {
        final StringReader in = new StringReader(input);
        return CsvConstants.CSV_FORMAT.withFirstRecordAsHeader().parse(in);
    }
}
