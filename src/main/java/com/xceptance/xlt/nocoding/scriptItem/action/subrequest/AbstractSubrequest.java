package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public abstract class AbstractSubrequest
{
    protected PropertyManager propertyManager;

    public abstract void execute(PropertyManager propertyManager) throws Throwable;

    public PropertyManager getPropertyManager()
    {
        return propertyManager;
    }

    public void setPropertyManager(final PropertyManager propertyManager)
    {
        this.propertyManager = propertyManager;
    }

    public abstract WebRequest getWebRequest() throws MalformedURLException;

}
