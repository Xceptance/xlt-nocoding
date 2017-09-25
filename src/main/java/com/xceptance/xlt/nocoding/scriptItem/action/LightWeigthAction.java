package com.xceptance.xlt.nocoding.scriptItem.action;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.engine.LightWeightPageImpl;
import com.xceptance.xlt.engine.SessionImpl;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.webAction.WebAction;

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
        // First we initialize some variables
        final List<WebRequest> requestsOfSubrequest = buildWebRequestFromSubrequests(this.subrequests);

        // TODO find a better place for this
        this.request.fillData(propertyManager);

        final WebAction action = new WebAction(this.getRequest().getName(), this.getRequest().buildWebRequest(), requestsOfSubrequest,
                                               propertyManager.getWebClient(), (final WebAction x) -> doExecute(x));

        // TODO Talk about this
        try
        {
            // Execute the WebRequest in xlt
            action.run();
            setLightWeightPage(new LightWeightPage(action.getWebResponse(), action.getTimerName()));

            // Validate response if it is specified
            if (getResponse() != null)
            {
                getResponse().execute(propertyManager, action.getWebResponse());
            }
            // do standard validations
            else
            {
                new Response().execute(propertyManager, action.getWebResponse());
            }

            // if (getSubrequests() != null || !getSubrequests().isEmpty())
            // {
            // for (final AbstractSubrequest abstractSubrequest : subrequests)
            // {
            // abstractSubrequest.execute(propertyManager);
            // }
            // }
        }
        finally
        {
            // Append the page to the result browser
            if (action.getWebResponse() != null)
            {
                ((SessionImpl) Session.getCurrent()).getRequestHistory()
                                                    .add(new LightWeightPageImpl(action.getWebResponse(), action.getTimerName(),
                                                                                 (XltWebClient) action.getWebClient()));
                ;
            }
        }
    }

    private List<WebRequest> buildWebRequestFromSubrequests(final List<AbstractSubrequest> subrequests) throws MalformedURLException
    {
        List<WebRequest> webRequests = null;
        if (subrequests != null)
        {
            webRequests = new ArrayList<WebRequest>(subrequests.size());
            for (final AbstractSubrequest subrequest : subrequests)
            {
                webRequests.add(subrequest.getWebRequest());
            }
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
        // TODO Unterscheidung Subrequest als Main Request vs normale Request
        if (action.getWebRequest().isXHR())
        {
            action.setWebResponse(action.getWebClient().loadWebResponse(action.getWebRequest()));
        }
        else
        {
            final WebResponse response = action.getWebClient().loadWebResponse(action.getWebRequest());
            action.setWebResponse(response);
            final LightWeightPage page = new LightWeightPage(action.getWebResponse(), action.getTimerName());
            // TODO Result Browser?
            // TODO fix this
            // if (page != null)
            // {
            // ((SessionImpl) Session.getCurrent()).getRequestHistory().add(page);
            // }
        }

        if (action.getSubrequests() != null && !action.getSubrequests().isEmpty())
        {
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
}
