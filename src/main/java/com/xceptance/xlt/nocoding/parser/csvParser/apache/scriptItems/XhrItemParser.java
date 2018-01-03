package com.xceptance.xlt.nocoding.parser.csvParser.apache.scriptItems;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.XhrSubrequest;

public class XhrItemParser
{

    public XhrSubrequest parse(final CSVRecord record)
    {
        final Action action = new ActionItemParser().parse(record);
        return new XhrSubrequest(action.getName(), action.getActionItems());
    }

}
