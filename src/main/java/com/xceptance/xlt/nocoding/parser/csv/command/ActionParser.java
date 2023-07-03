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

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.Action;
import com.xceptance.xlt.nocoding.parser.csv.CsvConstants;
import com.xceptance.xlt.nocoding.parser.csv.command.action.RequestParser;
import com.xceptance.xlt.nocoding.parser.csv.command.action.ResponseParser;

/**
 * The class for parsing an action item.
 *
 * @author ckeiner
 */
public class ActionParser
{

    /**
     * Parses the action item to a list of {@link Command}s.
     *
     * @param record
     *            The {@link CSVRecord} with the the action item
     * @return The {@link Action} defined by the CSVRecord
     */
    public Action parse(final CSVRecord record)
    {
        String name = null;
        final List<AbstractActionSubItem> actionItems = new ArrayList<>(2);
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
        final Action action = new Action(name, actionItems);
        // Return the action
        return action;
    }

}
