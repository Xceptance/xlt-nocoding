package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.WebAction.LightWeightPageAction;

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
        final LightWeightPageAction action;

        // In the first action, we do not have a webclient, therefore we build it
        if (propertyManager.getWebClient() == null)
        {
            action = new LightWeightPageAction(null, this.getRequest().getName(), this.getRequest().buildWebRequest());
            propertyManager.setWebClient(action.getWebClient());
        }
        // in every other case, we already know the webclient and reuse it
        else
        {
            action = new LightWeightPageAction(null, this.getRequest().getName(), this.getRequest().buildWebRequest(),
                                               propertyManager.getWebClient());
        }
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
                abstractSubrequest.execute();
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

}
