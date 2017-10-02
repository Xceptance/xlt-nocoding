package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;

/**
 * This is the abstract class each Action ScriptItem has, so if the ScriptItem has the form of "- Action : ...", this
 * ScriptItem is conjured.
 */
public abstract class Action implements ScriptItem
{
    /**
     * The request that is defined in this action. Musn't be null
     */
    protected final Request request;

    /**
     * The response that is defined in this action.
     */
    protected final Response response;

    /**
     * All subrequest that are defined in this action.
     */
    protected final List<AbstractSubrequest> subrequests;

    /**
     * Creates an action with the specified request, response and subrequests
     * 
     * @param request
     * @param response
     * @param subrequests
     */
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
