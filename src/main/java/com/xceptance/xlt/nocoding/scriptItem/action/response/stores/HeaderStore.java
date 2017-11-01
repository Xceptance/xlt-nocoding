package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.Context;

public class HeaderStore extends AbstractResponseStore
{
    /**
     * The name of the header
     */
    private String header;

    public HeaderStore(final String variableName, final String header)
    {
        super(variableName);
        this.header = header;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        final WebResponse webResponse = context.getWebResponse();
        // Resolve variables
        resolveValues(context);

        // If true, this throws an Exception if no cookie is found
        boolean throwException = true;
        // Get all headers
        final List<NameValuePair> headers = webResponse.getResponseHeaders();
        // For each header,
        for (final NameValuePair header : headers)
        {
            // Search for the header name
            if (header.getName().equals(getHeader()))
            {
                // And store the header
                context.storeVariable(getVariableName(), header.getValue());

                // At last, set throwException to false, so we know, that we found our specified header.
                throwException = false;
                break;
            }
        }

        // If the specified header wasn't found, throw an exception to the user
        if (throwException)
        {
            throw new Exception("Did not find specified header");
        }

    }

    private void resolveValues(final Context context)
    {
        final String resolvedValue = context.resolveString(getHeader());
        setHeader(resolvedValue);
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(final String header)
    {
        this.header = header;
    }

}
