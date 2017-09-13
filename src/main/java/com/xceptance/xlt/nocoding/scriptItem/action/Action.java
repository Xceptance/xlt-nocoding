package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;

public abstract class Action implements ScriptItem
{
    protected final Request request;

    protected final Response response;

    protected final List<AbstractSubrequest> subrequests;

    public Action(final Request request, final Response response, final List<AbstractSubrequest> subrequests)
    {
        this.request = request;
        this.response = response;
        this.subrequests = subrequests;
    }

    public Request getRequest()
    {
        return request;
    }

    public Response getResponse()
    {
        return response;
    }

    public List<AbstractSubrequest> getSubrequests()
    {
        return subrequests;
    }

}
