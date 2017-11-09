package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
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
            setName(context.getConfigItemByKey(Constants.NAME));
        }
    }

    public void setName(final String name)
    {
        this.name = name;
    }

}
