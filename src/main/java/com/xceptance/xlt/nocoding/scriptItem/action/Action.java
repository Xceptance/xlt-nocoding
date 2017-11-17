package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * This is the abstract class each Action ScriptItem has, so if the ScriptItem has the form of "- Action : ...", this
 * ScriptItem is conjured.
 */
public abstract class Action implements ScriptItem
{
    /**
     * The name of the action
     */
    protected String name;

    /**
     * The list of actionItems. An actionItem can be a Request, Response or AbstractSubrequest
     */
    protected final List<AbstractActionItem> actionItems;

    /**
     * Creates an action with an actionItems list of size 1, since we intend to add a default request.
     */
    public Action()
    {
        actionItems = new ArrayList<AbstractActionItem>(1);
    }

    /**
     * Creates an action with the specified request, response and subrequests
     * 
     * @param request
     * @param response
     * @param subrequests
     */
    public Action(final String name, final List<AbstractActionItem> actionItems)
    {
        this.name = name;
        this.actionItems = actionItems;
    }

    public List<AbstractActionItem> getActionItems()
    {
        return actionItems;
    }

    public String getName()
    {
        return name;
    }

    protected void resolveName(final Context context)
    {
        // Resolve name
        final String resolvedValue = context.resolveString(getName());
        setName(resolvedValue);
    }

    protected void fillDefaultData(final Context context)
    {
        if (getName() == null || getName().isEmpty())
        {
            // TODO Null check
            setName(context.getConfigItemByKey(Constants.NAME));
        }

        // Check if the order of the items is allowed
        boolean hasRequest = false;
        boolean hasResponse = false;
        boolean hasSubrequest = false;
        for (final AbstractActionItem abstractActionItem : actionItems)
        {
            if (abstractActionItem instanceof Request)
            {
                if (hasResponse || hasSubrequest)
                {
                    throw new IllegalArgumentException("Request defined after Response and/or Subrequest!");
                }
                hasRequest = true;
            }
            else if (abstractActionItem instanceof Response)
            {
                if (hasSubrequest)
                {
                    throw new IllegalArgumentException("Response defined after Subrequest!");
                }
                hasResponse = true;
            }
            else if (abstractActionItem instanceof AbstractSubrequest)
            {
                hasSubrequest = true;
            }

        }
        if (!hasRequest)
        {
            final String url = context.getConfigItemByKey(Constants.URL);
            if (url == null)
            {
                throw new IllegalStateException("No default url specified");
            }
            actionItems.add(0, new Request(url));
            hasRequest = true;
        }
        if (!hasResponse)
        {
            int index = 0;
            if (hasRequest)
            {
                index++;
            }
            actionItems.add(index, new Response());
        }
        if (!hasSubrequest)
        {
            // Add default static requests
            if (context.getDefaultStatic() != null && !context.getDefaultStatic().isEmpty())
            {
                actionItems.add(new StaticSubrequest(context.getDefaultStatic()));
            }
        }
    }

    public void setName(final String name)
    {
        this.name = name;
    }

}
