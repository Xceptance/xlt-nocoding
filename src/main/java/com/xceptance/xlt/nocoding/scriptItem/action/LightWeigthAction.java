package com.xceptance.xlt.nocoding.scriptItem.action;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.WebAction.WebAction;

public class LightWeigthAction extends Action
{
    private LightWeightPage lightWeightPage;

    public LightWeigthAction(final Request request, final Response response, final List<AbstractSubrequest> subrequests)
    {
        super(request, response, subrequests);
    }

    @Override
    public void execute(final PropertyManager propertyManager) throws Throwable
    {
        final List<WebRequest> requestsOfSubrequest = buildWebRequestFromSubrequests(this.subrequests);
        final WebAction action = new WebAction(this.getRequest().getName(), this.getRequest().buildWebRequest(), requestsOfSubrequest,
                                               propertyManager.getWebClient(), (final WebAction x) -> doExecute(x));

        // Execute the WebRequest in xlt
        action.run();
        setLightWeightPage(new LightWeightPage(action.getWebResponse(), action.getTimerName()));

        // Validate response if it is specified
        if (getResponse() != null)
        {
            this.getResponse().setPropertyManager(propertyManager);
            getResponse().execute(getLightWeightPage());
        }
        // do standard validations
        else
        {
            new Response().execute(getLightWeightPage());
        }

        if (getSubrequests() != null)
        {
            for (final AbstractSubrequest abstractSubrequest : subrequests)
            {
                abstractSubrequest.setPropertyManager(propertyManager);
                abstractSubrequest.execute(propertyManager);
            }
        }

    }

    private List<WebRequest> buildWebRequestFromSubrequests(final List<AbstractSubrequest> subrequests) throws MalformedURLException
    {
        final List<WebRequest> webRequests = new ArrayList<WebRequest>(subrequests.size());
        for (final AbstractSubrequest subrequest : subrequests)
        {
            webRequests.add(subrequest.getWebRequest());
        }
        return webRequests;
    }

    public LightWeightPage getLightWeightPage()
    {
        return lightWeightPage;
    }

    public void setLightWeightPage(final LightWeightPage lightWeightPage)
    {
        this.lightWeightPage = lightWeightPage;
    }

    public void doExecute(final WebAction action) throws Exception
    {
        // TODO Result Browser?
        // TODO Unterscheidung Subrequest als Main Request vs normale Request
        if (action.getWebRequest().isXHR())
        {
            action.setWebResponse(action.getWebClient().loadWebResponse(action.getWebRequest()));
        }
        else
        {
            action.setWebResponse(action.getWebClient().loadWebResponse(action.getWebRequest()));
        }

        for (final WebRequest subrequest : action.getSubrequests())
        {
            if (subrequest.isXHR())
            {
                action.getSubrequestResponses().add(action.getWebClient().loadWebResponse(subrequest));
            }
            else
            {
                action.setWebResponse(action.getWebClient().loadWebResponse(subrequest));
            }
        }

    }
}
