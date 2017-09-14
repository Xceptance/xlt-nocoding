package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;

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
    public void execute() throws Throwable
    {
        // set xhr to true
        request.setXhr(true);
        // build a lightweightaction since xhr is only lightweight
        final LightWeigthAction action = new LightWeigthAction(request, response, null);
        // execute the action
        action.execute(this.propertyManager);
    }
}
