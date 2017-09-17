package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.engine.XltWebClient;
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
        final WebAction action;
        action = new WebAction(this.getRequest().getName(), this.getRequest().buildWebRequest(), propertyManager.getWebClient(),
                                           (final WebAction x) -> doExecute(x));

        // Execute the WebRequest in xlt
        action.run();
        setLightWeightPage(action.getLightWeightPage());

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
        if (action.getWebRequest().isXHR())
        {
            action.setLightWeightPage(((XltWebClient) action.getWebClient()).getLightWeightPage(action.getWebRequest()));
        }
        else
        {
            action.setLightWeightPage(((XltWebClient) action.getWebClient()).getLightWeightPage(action.getWebRequest()));
        }

    }
}
