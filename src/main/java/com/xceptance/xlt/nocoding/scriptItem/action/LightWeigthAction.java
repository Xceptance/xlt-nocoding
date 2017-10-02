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

/**
 * The class that describes an action in lightweight mode
 */
public class LightWeigthAction extends Action
{
    /**
     * The light weight page that is built by this action
     */
    private LightWeightPage lightWeightPage;

    public LightWeigthAction(final Request request, final Response response, final List<AbstractSubrequest> subrequests)
    {
        super(request, response, subrequests);
    }

    /**
     * Executes the light weight action by building a WebAction, running it and then validating the answer. In the end, the
     * page gets appended to the result browser.
     */
    @Override
    public void execute(final PropertyManager propertyManager) throws Throwable
    {
        // Build a list of WebRequest from the subrequests
        final List<WebRequest> requestsOfSubrequest = buildWebRequestFromSubrequests(getSubrequests(), propertyManager);

        final WebAction action = new WebAction(getRequest().getName(), getRequest().buildWebRequest(propertyManager), requestsOfSubrequest,
                                               propertyManager.getWebClient(), (final WebAction webAction) -> doExecute(webAction));

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

    /**
     * Builds a list of WebRequests from a list of AbstractSubrequests
     * 
     * @param subrequests
     *            The subrequests to be turned into WebRequests
     * @return The list of WebRequests
     * @throws MalformedURLException
     */
    private List<WebRequest> buildWebRequestFromSubrequests(final List<AbstractSubrequest> subrequests,
                                                            final PropertyManager propertyManager)
        throws MalformedURLException
    {
        List<WebRequest> webRequests = null;
        if (subrequests != null)
        {
            webRequests = new ArrayList<WebRequest>(subrequests.size());
            for (final AbstractSubrequest subrequest : subrequests)
            {
                webRequests.add(subrequest.getWebRequest(propertyManager));
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

    /**
     * This method uses an action as parameter and defines how to execute a WebAction. This is used in a lambda method in
     * the execute-Method.
     * 
     * @param action
     * @throws Exception
     */
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
                // Static subrequest?
                else
                {
                    action.setWebResponse(action.getWebClient().loadWebResponse(subrequest));
                }
            }
        }

    }
}
