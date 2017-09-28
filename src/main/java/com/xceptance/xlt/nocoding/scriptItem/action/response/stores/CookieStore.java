package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class CookieStore extends AbstractResponseStore
{

    private String cookie;

    public CookieStore(final String variableName, final String cookie)
    {
        super(variableName);
        this.cookie = cookie;
    }

    @Override
    public void store(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        // Resolve variables
        resolveValues(propertyManager);

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
                // And verify the cookie by
                // grabbing the name in the beginning
                final String cookieName = header.getValue().substring(0, cookie.length());
                // and comparing it with the input name
                if (cookieName.equals(cookie))
                {
                    // If the specified cookie was found, get its content
                    final String cookieContent = header.getValue().substring(cookie.length());
                    // And store it in our storage
                    propertyManager.getDataStorage().storeVariable(getVariableName(), cookieContent);

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

    private void resolveValues(final PropertyManager propertyManager)
    {
        final String resolvedValue = propertyManager.resolveString(cookie);
        cookie = resolvedValue;
    }

}
