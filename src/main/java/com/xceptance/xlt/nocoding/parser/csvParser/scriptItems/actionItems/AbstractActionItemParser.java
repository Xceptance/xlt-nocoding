package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.actionItems;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;

public interface AbstractActionItemParser
{
    /**
     * Extracts the information needed for the {@link AbstractActionItem} from the {@link CSVRecord}
     * 
     * @param record
     *            The {@link CSVRecord} with the action item
     * @return The AbstractActionItem defined by the CSVRecord
     */
    public AbstractActionItem parse(final CSVRecord record);
}
