package com.xceptance.xlt.nocoding.scriptItem.action.response.store;

import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;

/**
 * The abstract class for every response item that interacts with the storage.
 * 
 * @author ckeiner
 */
public abstract class AbstractResponseStore extends AbstractResponseItem
{
    /**
     * The name of the variable
     */
    protected final String variableName;

    /**
     * The selector for the value of the variable
     */
    protected final AbstractSelector selector;

    /**
     * Sets {@link #variableName} and {@link #selector}.
     * 
     * @param variableName
     *            The name of the variable
     * @param selector
     *            The selector to use for the value
     */
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
