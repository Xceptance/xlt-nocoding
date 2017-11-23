package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

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

    public HeaderSelector(final String selectionExpression)
    {
        super(selectionExpression);
    }

    /**
     * Iterates over all headers in {@link Context#getWebResponse()} and stores every header with the name
     * {@link #getSelectionExpression()}.
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
                // Add it to the result list
                addResult(header.getValue());
            }
        }
    }

}
