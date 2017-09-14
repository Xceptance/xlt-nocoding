package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import com.xceptance.xlt.nocoding.util.PropertyManager;

public abstract class AbstractSubrequest
{
    protected PropertyManager propertyManager;

    public abstract void execute() throws Exception;

    public PropertyManager getPropertyManager()
    {
        return propertyManager;
    }

    public void setPropertyManager(final PropertyManager propertyManager)
    {
        this.propertyManager = propertyManager;
    }

}
