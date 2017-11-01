package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;

public abstract class AbstractResponseStore implements AbstractResponseItem
{
    protected final String variableName;

    public AbstractResponseStore(final String variableName)
    {
        this.variableName = variableName;
    }

    public String getVariableName()
    {
        return variableName;
    }

}
