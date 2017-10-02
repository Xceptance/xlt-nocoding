package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public abstract class AbstractSubrequest
{

    public abstract void execute(PropertyManager propertyManager) throws Throwable;

    public abstract WebRequest getWebRequest(PropertyManager propertyManager) throws MalformedURLException;

}
