package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

/**
 * This is the abstract class each Action ScriptItem has, so if the ScriptItem has the form of "- Action : ...", this
 * ScriptItem is conjured.
 */
public abstract class Action implements ScriptItem
{
    /**
     * The list of actionItems. An actionItem can be a Request, Response or AbstractSubrequest
     */
    protected final List<AbstractActionItem> actionItems;

    /**
     * The mandatory request
     */
    protected final Request request;

    /**
     * Creates an action with the specified request, response and subrequests
     * 
     * @param request
     * @param response
     * @param subrequests
     */
    public Action(final Request request, final List<AbstractActionItem> actionItems)
    {
        this.request = request;
        this.actionItems = actionItems;
    }

    public List<AbstractActionItem> getActionItems()
    {
        return actionItems;
    }

    public Request getRequest()
    {
        return request;
    }

}
