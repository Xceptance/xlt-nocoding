package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;

/**
 * The class for parsing Static Subrequests from a {@link CSVRecord}.
 * 
 * @author ckeiner
 */
public class StaticItemParser
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
        final List<String> urls = new ArrayList<String>();

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
