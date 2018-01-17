package com.xceptance.xlt.nocoding.util;

import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

/**
 * Utility class for {@link AbstractActionItem}. Provides static methods for verifying the order, and filling default
 * data.
 * 
 * @author ckeiner
 */
public class ActionItemUtil
{

    /**
     * Asserts that the order of {@link Request}, {@link Response}, and {@link AbstractSubrequest} is correct.
     * 
     * @param actionItems
     *            The {@link List} you want to assert the order of
     */
    public static void assertOrder(final List<AbstractActionItem> actionItems)
    {
        boolean hasRequest = false;
        boolean hasResponse = false;
        boolean hasSubrequest = false;
        for (final AbstractActionItem actionItem : actionItems)
        {
            // If it is a Request
            if (actionItem instanceof Request)
            {
                // Verify there is no other request, response or subrequest before it
                if (hasResponse || hasSubrequest || hasRequest)
                {
                    throw new IllegalArgumentException("Request cannot be defined after a response or subrequest.");
                }
                // Set hasRequest to true
                hasRequest = true;
            }
            // If it is a Response, set hasResponse to true
            else if (actionItem instanceof Response)
            {
                hasResponse = true;
                // If an AbstractSubrequest is before Response,throw an error
                if (hasSubrequest)
                {
                    throw new IllegalArgumentException("Response mustn't be defined after subrequests.");
                }
            }
            // If an AbstractSubrequest is found, set hasSubrequest to true
            else if (actionItem instanceof AbstractSubrequest && !hasSubrequest)
            {
                hasSubrequest = true;
            }
        }
    }

    /**
     * Adds a default {@link Request} and default {@link Response} if none is found in the list of
     * {@link AbstractActionItem}.
     * 
     * @param actionItems
     *            The list to add the default items to
     * @param context
     *            The {@link Context} with the {@link DataStorage}.
     */
    public static void fillDefaultData(final List<AbstractActionItem> actionItems, final Context<?> context)
    {
        boolean hasRequest = false;
        boolean hasResponse = false;
        for (final AbstractActionItem actionItem : actionItems)
        {
            if (actionItem instanceof Request)
            {
                hasRequest = true;
            }
            if (actionItem instanceof Response)
            {
                hasResponse = true;
            }
        }
        if (!hasRequest)
        {
            actionItems.add(0, new Request());
        }
        if (!hasResponse)
        {
            actionItems.add(1, new Response());
        }
    }

    /**
     * @param start
     *            The String the defaultName should start with if no default name was defined
     * @param context
     *            The Context with the {@link DataStorage}
     * @return String that starts with <code> start + "-" </code> and adds the current index of scriptItems to it
     */
    public static String getDefaultName(final Context<?> context, final String start)
    {
        // Get the default name from the data storage
        String output = context.getDefaultItems().get(Constants.NAME);
        // If there is no default name
        if (output == null || output.isEmpty())
        {
            // Assign start + "-x" to name, whereas x is the index of the current scriptItem
            output = start + "-" + context.getScriptItemIndex();
        }
        return output;
    }

}
