package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.Context;

public class CookieStore extends AbstractResponseStore
{

    private String cookie;

    public CookieStore(final String variableName, final String cookie)
    {
        super(variableName);
        this.cookie = cookie;
    }

    @Override
    public void store(final Context context, final WebResponse webResponse) throws Exception
    {
        // Resolve variables
        resolveValues(context);

        // If true, this throws an Exception if no cookie is found
        boolean throwException = true;
        // Get all headers
        final List<NameValuePair> headers = webResponse.getResponseHeaders();
        // For each header,
        for (final NameValuePair header : headers)
        {
            // Search for the Set-Cookie header
            if (header.getName().equals("Set-Cookie"))
            {
                // And verify if this is the correct cookie by
                // grabbing the cookie name
                final int equalSignPosition = header.getValue().indexOf("=");
                final String cookieName = header.getValue().substring(0, equalSignPosition);
                // and comparing it with the input name
                if (cookieName.equals(cookie))
                {
                    // If the specified cookie was found, get its content
                    // by getting the cookie content, that is until the first semicolon
                    final int semicolonPosition = header.getValue().indexOf(";");
                    // Content starts after the equal sign (position+1) and ends before the semicolon
                    final String cookieContent = header.getValue().substring(cookie.length() + 1, semicolonPosition);
                    // And store the content in our storage
                    context.getDataStorage().storeVariable(getVariableName(), cookieContent);

                    // At last, set throwException to false, so we know, that we found our specified cookie.
                    throwException = false;
                    break;
                }
            }
        }

        // If the specified cookie wasn't found, we need to throw an exception to the user
        if (throwException)
        {
            throw new Exception("Did not find specified cookie");
        }

    }

    private void resolveValues(final Context context)
    {
        final String resolvedValue = context.resolveString(cookie);
        cookie = resolvedValue;
    }

}
