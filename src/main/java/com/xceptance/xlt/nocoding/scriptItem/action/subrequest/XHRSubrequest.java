package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class XHRSubrequest extends AbstractSubrequest
{
    private final String name;

    private final Request request;

    private final Response response;

    private WebRequest webRequest;

    public XHRSubrequest(final String name, final Request request, final Response response)
    {
        this.name = name;
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute(final PropertyManager propertyManager) throws Throwable
    {
        // TODO
        webRequest = this.request.buildWebRequest(propertyManager);
    }

    @Override
    public WebRequest getWebRequest() throws MalformedURLException
    {
        return webRequest;
    }
}
