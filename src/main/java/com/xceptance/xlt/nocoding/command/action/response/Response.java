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
package com.xceptance.xlt.nocoding.command.action.response;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.WebResponse;

import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.util.NoCodingPropertyAdmin;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * The expected response to a request. A response has a list of response items, which validate or store something of the
 * {@link WebResponse} defined in the {@link Context}.
 *
 * @author ckeiner
 */
public class Response extends AbstractActionSubItem
{

    /**
     * The list of {@link AbstractResponseSubItem} to execute
     */
    private final List<AbstractResponseSubItem> responseItems;

    /**
     * Creates an instance of {@link Response} that creates an ArrayList for {@link #responseItems} and adds a
     * {@link HttpCodeValidator}.
     */
    public Response()
    {
        this(new ArrayList<AbstractResponseSubItem>(1));
    }

    /**
     * Creates an instance of {@link Response} with the specified responseItems
     *
     * @param responseItems
     *            The list of {@link AbstractResponseSubItem} to execute
     */
    public Response(final List<AbstractResponseSubItem> responseItems)
    {
        this.responseItems = responseItems;
    }

    /**
     * Fills in default data, then executes every item in {@link #responseItems}
     */
    @Override
    public void execute(final Context<?> context) throws Exception
    {
        // Fill default data
        fillDefaultData(context);
        // Execute every AbstractResponseItem
        for (final AbstractResponseSubItem abstractResponseItem : responseItems)
        {
            abstractResponseItem.execute(context);
        }
    }

    public List<AbstractResponseSubItem> getResponseItems()
    {
        return responseItems;
    }

    /**
     * Adds a {@link HttpCodeValidator} to the {@link #responseItems} if none is specified
     */
    void fillDefaultData(final Context<?> context)
    {
        final boolean defaultResponseCodeValidation = context.getPropertyByKey(NoCodingPropertyAdmin.DEFAULT_HTTP_CODE_VALIDATION, true);
        if (defaultResponseCodeValidation)
        {
            boolean hasHttpcodeValidator = false;
            // Look for an instance of HttpcodeValidator
            for (final AbstractResponseSubItem responseItem : responseItems)
            {
                if (responseItem instanceof HttpCodeValidator)
                {
                    // If a HttpcodeValidator was found, set hasHttpcodeValidator to true
                    hasHttpcodeValidator = true;
                }
            }
            // If no HttpcodeValidator was found, add one at the beginning
            if (!hasHttpcodeValidator)
            {
                responseItems.add(0, new HttpCodeValidator(null));
            }
        }
    }

}
