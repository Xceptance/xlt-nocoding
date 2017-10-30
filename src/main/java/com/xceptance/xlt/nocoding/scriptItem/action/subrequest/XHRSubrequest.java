package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.util.Context;

public class XHRSubrequest extends AbstractSubrequest
{
    private final String name;

    // TODO talk about whether or not this should become a List<AbstractActionItem>
    private final Request request;

    private final Response response;

    private final AbstractSubrequest subrequest;

    private WebRequest webRequest;

    public XHRSubrequest(final String name, final Request request, final Response response, final AbstractSubrequest subrequest)
    {
        this.name = name;
        this.request = request;
        this.response = response;
        this.subrequest = subrequest;
    }

    public XHRSubrequest(final String name, final Request request, final Response response)
    {
        this(name, request, response, null);
    }

    public XHRSubrequest(final String name, final Request request)
    {
        this(name, request, null, null);
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        final Context localContext = new Context(context);
        // Set xhr to be true
        getRequest().setXhr("true");
        getRequest().execute(localContext);

        if (getResponse() != null)
        {
            getResponse().execute(localContext);
        }
        if (getSubrequest() != null)
        {
            getSubrequest().execute(localContext);
        }
    }

    public String getName()
    {
        return name;
    }

    public void setWebRequest(final WebRequest webRequest)
    {
        this.webRequest = webRequest;
    }

    public Request getRequest()
    {
        return request;
    }

    public Response getResponse()
    {
        return response;
    }

    public AbstractSubrequest getSubrequest()
    {
        return subrequest;
    }

}
