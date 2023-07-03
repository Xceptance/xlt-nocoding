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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.command.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.parser.csv.CsvConstants;

/**
 * The class for parsing Static Subrequests from a {@link CSVRecord}.
 *
 * @author ckeiner
 */
public class StaticSubrequestParser
{

    /**
     * Extracts the information needed for a Static Subrequest from the {@link CSVRecord} and creates a
     * {@link StaticSubrequest} out of it.
     *
     * @param record
     *            The CSVRecord that describes the Static Subrequest
     * @return A <code>StaticSubrequest</code> with the parsed URL
     */
    public StaticSubrequest parse(final CSVRecord record)
    {
        // Initialize variables
        final List<String> urls = new ArrayList<>();

        String url = record.get(CsvConstants.URL);

        // Remove quotation marks from the url, if url starts and ends with it
        if (url.startsWith("\"") && url.endsWith("\""))
        {
            url = url.substring(1, url.length() - 1);
        }

        urls.add(url);

        // Return a new StaticSubrequest with all urls
        return new StaticSubrequest(urls);
    }

}
