package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;

public class StaticItemParser
{

    public StaticSubrequest parse(final CSVRecord record)
    {
        // Initialize variables
        final List<String> urls = new ArrayList<String>();

        // Build an iterator over the headers
        final Iterator<String> headerIterator = record.toMap().keySet().iterator();
        while (headerIterator.hasNext())
        {
            final String header = headerIterator.next();
            final String value = record.get(header);

            switch (header)
            {
                case CsvConstants.URL:
                    // Read the value and save it in url
                    String url = value.trim();
                    // Remove quotation marks in the beginning and end
                    final String quotationMark = "\"";
                    if (url.startsWith(quotationMark) && url.endsWith(quotationMark))
                    {
                        url = url.substring(1, url.length() - 1);
                    }
                    // Add the url to the list of urls
                    urls.add(url);
                    break;

                default:
                    break;
            }
        }
        return new StaticSubrequest(urls);
    }

}
