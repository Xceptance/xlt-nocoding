package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.Context;

public class HeaderSelector extends AbstractSelector
{

    public HeaderSelector(final String selectionExpression)
    {
        super(selectionExpression);
    }

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
                addResult(header.getValue());
            }
        }
    }

}
