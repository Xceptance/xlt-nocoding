package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class CookieStore extends AbstractResponseStore
{

    private final String cookie;

    public CookieStore(final String variableName, final String cookie)
    {
        super(variableName);
        this.cookie = cookie;
    }

    @Override
    public void store(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        final List<NameValuePair> headers = webResponse.getResponseHeaders();
        headers.forEach((final NameValuePair x) -> {
            if (x.getName().equals("Cookie"))
            {
                if (x.getValue().contains(cookie))
                {
                    final String cookieContent = x.getValue();
                    propertyManager.getDataStorage().storeVariable(getVariableName(), cookieContent);
                }
            }

        });
        // Check if we found the cookie
        if (propertyManager.getDataStorage().getVariableByKey(getVariableName()) == null)
        {
            throw new Exception("Cookie not found");
        }

    }

}
