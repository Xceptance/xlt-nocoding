package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.InvalidArgumentException;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

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
     * Resolves name
     * 
     * @param context
     *            The {@link Context} with the {@link VariableResolver} and {@link DataStorage}
     */
    protected void resolveName(final Context context)
    {
        // Resolve name
        final String resolvedValue = context.resolveString(getName());
        // Set new name
        setName(resolvedValue);
    }

    /**
     * Fills in the default data and verifies there is only one request, response and in the correct order.
     * 
     * @param context
     */
    protected void fillDefaultData(final Context context)
    {
        if (getName() == null || getName().isEmpty())
        {
            setName(context.getConfigItemByKey(Constants.NAME));
            // Verify that we now have a name for the action
            if (getName() == null || getName().isEmpty())
            {
                throw new InvalidArgumentException("Name cannot be empty or null. Please add a default name or specify one.");
            }
        }

        // Check if the order of the items is allowed
        boolean hasRequest = false;
        boolean hasResponse = false;
        boolean hasSubrequest = false;
        for (final AbstractActionItem abstractActionItem : actionItems)
        {
            if (abstractActionItem instanceof Request)
            {
                if (hasResponse || hasSubrequest || hasRequest)
                {
                    throw new IllegalArgumentException("Request defined after Response and/or Subrequest!");
                }
                hasRequest = true;
            }
            else if (abstractActionItem instanceof Response)
            {
                if (hasSubrequest || hasResponse)
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

        // Add default static requests
        if (context.getDefaultStatic() != null && !context.getDefaultStatic().isEmpty())
        {
            actionItems.add(new StaticSubrequest(context.getDefaultStatic()));
        }
    }

}
