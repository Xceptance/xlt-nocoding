package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;

/**
 * This is the abstract class for every response item that interacts with the storage.
 * 
 * @author ckeiner
 */
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
