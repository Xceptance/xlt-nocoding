package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.actionItems.RequestParser;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.actionItems.ResponseParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.XhrSubrequest;

/**
 * The class for parsing a Xhr Subrequest item from a {@link CSVRecord}.
 * 
 * @author ckeiner
 */
public class XhrItemParser
{

    /**
     * Extracts the information needed for a Xhr Subrequest from the {@link CSVRecord} and creates a {@link XhrSubrequest}
     * out of it.
     * 
     * @param record
     *            The CSVRecord that describes the Xhr Subrequest
     * @return A <code>XhrSubrequest</code> with the parsed data
     */
    public XhrSubrequest parse(final CSVRecord record)
    {
        String name = null;
        final List<AbstractActionItem> actionItems = new ArrayList<>(2);
        // Get the name if it is mapped
        if (record.isMapped(CsvConstants.NAME))
        {
            name = record.get(CsvConstants.NAME);
        }
        // Parse the request
        actionItems.add(new RequestParser().parse(record));
        // Parse the response
        actionItems.add(new ResponseParser().parse(record));
        // Create the action
        final XhrSubrequest xhrSubrequest = new XhrSubrequest(name, actionItems);
        // Return the action
        return xhrSubrequest;
    }

}
