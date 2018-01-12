package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.XhrSubrequest;

/**
 * The class for parsing a Xhr Subrequest item.
 * 
 * @author ckeiner
 */
public class XhrItemParser
{

    /**
     * Extracts the information needed for a xhr subrequest from the {@link CSVRecord} and creates a {@link XhrSubrequest}
     * out of it.
     * 
     * @param record
     *            The CSVRecord that describes the XhrSubrequest
     * @return A <code>XhrSubrequest</code> with the parsed data
     */
    public XhrSubrequest parse(final CSVRecord record)
    {
        final Action action = new ActionItemParser().parse(record);
        return new XhrSubrequest(action.getName(), action.getActionItems());
    }

}
