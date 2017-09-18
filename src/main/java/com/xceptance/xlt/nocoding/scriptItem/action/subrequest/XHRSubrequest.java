package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class XHRSubrequest extends AbstractSubrequest
{
    private final String name;

    private final Request request;

    private final Response response;

    public XHRSubrequest(final String name, final Request request, final Response response)
    {
        this.name = name;
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute(final PropertyManager propertyManager) throws Throwable
    {
        // set xhr to true
        request.setXhr(true);
        // build a lightweightaction since xhr is only lightweight
        final LightWeigthAction action = new LightWeigthAction(request, response, null);
        // execute the action
        action.execute(propertyManager);
    }

    @Override
    public WebRequest getWebRequest() throws MalformedURLException
    {
        return this.request.buildWebRequest();
    }
}