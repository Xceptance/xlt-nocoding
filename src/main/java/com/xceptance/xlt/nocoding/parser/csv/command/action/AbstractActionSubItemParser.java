package com.xceptance.xlt.nocoding.parser.csv.command.action;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;

/**
 * Defines the interface for parsing an action item from a {@link CSVRecord}.
 * 
 * @author ckeiner
 */
public interface AbstractActionSubItemParser
{
    /**
     * Extracts the information needed for the {@link AbstractActionSubItem} from the {@link CSVRecord}
     * 
     * @param record
     *            The {@link CSVRecord} with the action item
     * @return The AbstractActionItem defined by the CSVRecord
     */
    public AbstractActionSubItem parse(final CSVRecord record);
}
