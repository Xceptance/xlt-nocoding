package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public abstract class AbstractResponseStore
{
    protected final String variableName;

    public AbstractResponseStore(final String variableName)
    {
        this.variableName = variableName;
    }

    public abstract void store(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception;

    public String getVariableName()
    {
        return variableName;
    }

}
