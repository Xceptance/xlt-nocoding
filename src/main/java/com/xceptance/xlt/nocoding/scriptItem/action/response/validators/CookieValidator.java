package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.List;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class CookieValidator extends AbstractValidator
{
    protected String cookie;

    protected String text;

    public CookieValidator(final String validationName, final String cookie)
    {
        this(validationName, cookie, null);
    }

    public CookieValidator(final String validationName, final String cookie, final String text)
    {
        super(validationName);
        this.cookie = cookie;
        this.text = text;
    }

    @Override
    public void validate(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
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
                    // Finally check if the text attribute is specified
                    if (text != null)
                    {
                        // If it is, assert that the cookie content is the same as the text attribute
                        Assert.assertEquals("Content did not match", header.getValue().substring(cookie.length()), text);
                    }
                    // At last, set throwException to false, so we know, that we found our specified cookie.
                    throwException = false;
                    break;
                }
            }
        }

        if (throwException)
        {
            throw new Exception("Did not find specified cookie");
        }

    }

    private void resolveValues(final PropertyManager propertyManager)
    {
        String resolvedValue = propertyManager.resolveString(cookie);
        cookie = resolvedValue;
        if (text != null)
        {
            resolvedValue = propertyManager.resolveString(cookie);
            text = resolvedValue;
        }

    }

}
