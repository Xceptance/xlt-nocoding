package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class HeaderStore extends AbstractResponseStore
{

    private final String header;

    public HeaderStore(final String variableName, final String header)
    {
        super(variableName);
        this.header = header;
    }

    @Override
    public void store(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        final List<NameValuePair> headers = webResponse.getResponseHeaders();
        headers.forEach((final NameValuePair x) -> {
            if (x.getName().equals(header))
            {
                propertyManager.getDataStorage().storeVariable(getVariableName(), x.getValue());
            }

        });
        // Check if we found the cookie
        if (propertyManager.getDataStorage().getVariableByKey(getVariableName()) == null)
        {
            final String msg = "Header: " + header + " not found";
            throw new Exception(msg);
        }
    }

}
