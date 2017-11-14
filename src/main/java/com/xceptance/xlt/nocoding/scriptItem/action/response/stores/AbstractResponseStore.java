package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;

/**
 * The abstract class for every response item that interacts with the storage.
 * 
 * @author ckeiner
 */
public abstract class AbstractResponseStore implements AbstractResponseItem
{
    protected final String variableName;

    protected final AbstractSelector selector;

    public AbstractResponseStore(final String variableName, final AbstractSelector selector)
    {
        this.variableName = variableName;
        this.selector = selector;
    }

    public String getVariableName()
    {
        return variableName;
    }

    public AbstractSelector getSelector()
    {
        return selector;
    }

}
