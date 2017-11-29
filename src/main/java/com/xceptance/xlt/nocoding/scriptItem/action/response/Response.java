package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * The expected response to a request. A response has a list of response items, which validate or store something of the
 * {@link WebResponse} defined in the {@link Context}.
 * 
 * @author ckeiner
 */
public class Response extends AbstractActionItem
{

    /**
     * The list of {@link AbstractResponseItem} to execute
     */
    private final List<AbstractResponseItem> responseItems;

    /**
     * Creates an instance of {@link Response} that creates an {@link ArrayList} for {@link #responseItems} and adds a
     * {@link HttpcodeValidator}.
     */
    public Response()
    {
        this(new ArrayList<AbstractResponseItem>(1));
        responseItems.add(new HttpcodeValidator(null));
    }

    /**
     * Creates an instance of {@link Response} with the specified responseItems
     * 
     * @param responseItems
     *            The list of {@link AbstractResponseItem} to execute
     */
    public Response(final List<AbstractResponseItem> responseItems)
    {
        this.responseItems = responseItems;
    }

    /**
     * Fills in default data, then executes every item in {@link #responseItems}
     */
    @Override
    public void execute(final Context context) throws Throwable
    {
        // Fill default data
        fillDefaultData(context);
        // Execute every AbstractResponseItem
        for (final AbstractResponseItem abstractResponseItem : responseItems)
        {
            abstractResponseItem.execute(context);
        }
    }

    public List<AbstractResponseItem> getResponseItems()
    {
        return responseItems;
    }

    /**
     * Adds a {@link HttpcodeValidator} to the {@link #responseItems} if none is specified
     */
    public void fillDefaultData(final Context context)
    {
        boolean hasHttpcodeValidator = false;
        // Look for an instance of HttpcodeValidator
        for (final AbstractResponseItem responseItem : responseItems)
        {
            if (responseItem instanceof HttpcodeValidator)
            {
                // If a HttpcodeValidator was found, set hasHttpcodeValidator to true
                hasHttpcodeValidator = true;
            }
        }
        // If no HttpcodeValidator was found, add one at the beginning
        if (!hasHttpcodeValidator)
        {
            responseItems.add(0, new HttpcodeValidator(null));
        }
    }

}
