package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Stores all headers with the name provided by {@link #getSelectionExpression()}. Headers are located in
 * {@link Context#getWebResponse()}.
 * 
 * @author ckeiner
 */
public class HeaderSelector extends AbstractSelector
{

    /**
     * Creates an instance of {@link HeaderSelector}, sets {@link #selectionExpression} and creates an {@link ArrayList} for
     * {@link #result}.
     * 
     * @param selectionExpression
     *            The name of the header
     */
    public HeaderSelector(final String selectionExpression)
    {
        super(selectionExpression);
    }

    /**
     * Iterates over all headers in {@link Context#getWebResponse()} and stores every header with the name
     * {@link #getSelectionExpression()} via {@link #addResult(String)}.
     */
    @Override
    public void execute(final Context context)
    {
        // Resolve variables
        resolveValues(context);

        // Get all headers
        final List<NameValuePair> headers = context.getWebResponse().getResponseHeaders();
        // For each header,
        for (final NameValuePair header : headers)
        {
            // Search for the header name
            if (header.getName().equals(getSelectionExpression()))
            {
                // Add the value to the result list
                addResult(header.getValue());
            }
        }
    }

}
