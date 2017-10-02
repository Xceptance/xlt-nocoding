package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public abstract class AbstractSubrequest
{
    protected PropertyManager propertyManager;

    public abstract void execute(XltWebClient webClient) throws Throwable;

    public void setPropertyManager(final PropertyManager propertyManager)
    {
        this.propertyManager = propertyManager;
    }

    public PropertyManager getPropertyManager()
    {
        return propertyManager;
    }

}
