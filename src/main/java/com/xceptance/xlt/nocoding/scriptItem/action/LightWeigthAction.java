package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.engine.SessionImpl;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;

public class LightWeigthAction extends Action
{
    private LightWeightPage lightWeightPage;

    public LightWeigthAction(final AbstractWebAction previousAction, final String timerName, final Request request, final Response response,
        final List<AbstractSubrequest> subrequests)
    {
        super(previousAction, timerName, request, response, subrequests);
    }

    @Override
    public void preValidate() throws Exception
    {
    }

    @Override
    protected void execute() throws Exception
    {
        // build the webrequest out of the data in request
        final WebRequest webRequest = getRequest().buildWebRequest();
        // fire the lightweight request
        setLightWeightPage(((XltWebClient) getWebClient()).getLightWeightPage(webRequest));

        // Validate response if it is specified
        if (getResponse() != null)
        {
            getResponse().execute(getLightWeightPage());
        }
        else // do standard validations
        {
            new Response().execute(getLightWeightPage());
        }

        // TODO zu spät, finally führt auch aus wenn failt
        // fällt aber evtl weg wenn abstractwebaction weg ist
        dumpPage(getLightWeightPage());

    }

    @Override
    protected void postValidate() throws Exception
    {
    }

    private void dumpPage(final LightWeightPage lightWeightPage)
    {
        if (lightWeightPage != null)
        {
            ((SessionImpl) Session.getCurrent()).getRequestHistory().add(lightWeightPage);
        }

    }

    public LightWeightPage getLightWeightPage()
    {
        return lightWeightPage;
    }

    public void setLightWeightPage(final LightWeightPage lightWeightPage)
    {
        this.lightWeightPage = lightWeightPage;
    }

}
