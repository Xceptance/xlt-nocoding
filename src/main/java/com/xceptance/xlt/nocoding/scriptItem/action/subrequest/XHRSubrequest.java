package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.util.Context;

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
    public void execute(final Context context) throws Throwable
    {
        // Check if we already built the webRequest
        if (buildWebRequest(context) != null)
        {
            // load the response
            final WebResponse webResponse = context.getWebClient().loadWebResponse(webRequest);
            // validate response
            response.execute(context, webResponse);
        }
    }

    private WebRequest buildWebRequest(final Context context) throws MalformedURLException
    {
        // Build WebRequest if not specified
        if (webRequest == null)
        {
            getRequest().execute(context);
            webRequest = getRequest().getWebRequest();
        }
        return webRequest;
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

}
