package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.ActionItemUtil;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

/**
 * The abstract class for each Action. An Action consist of a {@link #name} and
 * {@link List}<{@link AbstractActionItem}>.
 * 
 * @author ckeiner
 */
public abstract class Action implements ScriptItem
{
    /**
     * The name of the action
     */
    protected String name;

    /**
     * The list of actionItems
     */
    protected final List<AbstractActionItem> actionItems;

    /**
     * Creates an instance of {@link Action} that sets {@link #actionItems} to an {@link ArrayList} of size 1.
     */
    public Action()
    {
        actionItems = new ArrayList<AbstractActionItem>(1);
    }

    /**
     * Creates an instance of {@link Action} that sets {@link #name} and {@link #actionItems}.
     * 
     * @param name
     *            The name of the action
     * @param actionItems
     *            A {@link List}<{@link AbstractActionItem}>
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

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Fills in the default data for name, request, response if it isn't specified and verifies there is only one request,
     * response and in the correct order. Finally, it adds the default static subrequests even if static subrequests are
     * already defined.
     * 
     * @param context
     *            The {@link Context} with the {@link DataStorage}.
     */
    protected void fillDefaultData(final Context context)
    {
        if (name == null || name.isEmpty())
        {
            name = context.getDefaultItems().get(Constants.NAME);
        }
        ActionItemUtil.assertOrder(actionItems);
        ActionItemUtil.fillDefaultData(actionItems, context);

        // Add default static requests
        if (context.getDefaultStatics() != null && !context.getDefaultStatics().getItems().isEmpty())
        {
            actionItems.add(new StaticSubrequest(context.getDefaultStatics().getItems()));
            XltLogger.runTimeLogger.debug("Added default static subrequests to Action " + name);
        }
    }

}
