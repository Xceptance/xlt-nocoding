package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;

/**
 * The class for parsing static subrequests.
 * 
 * @author ckeiner
 */
public class StaticItemParser
{

    /**
     * Parses the static subrequest item in the static block to a {@link StaticSubrequest}.
     * 
     * @param record
     *            The {@link CSVRecord} the item starts at
     * @return A <code>StaticSubrequest</code> with the parsed URLs
     */
    public StaticSubrequest parse(final CSVRecord record)
    {
        // Initialize variables
        final List<String> urls = new ArrayList<String>();

        // Build an iterator over the headers
        final Iterator<String> headerIterator = record.toMap().keySet().iterator();
        while (headerIterator.hasNext())
        {
            // Get the next header
            final String header = headerIterator.next();
            // Get the value of the header
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
