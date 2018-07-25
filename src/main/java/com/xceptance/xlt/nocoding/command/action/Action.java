package com.xceptance.xlt.nocoding.command.action;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.InvalidArgumentException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.ActionItemUtil;
import com.xceptance.xlt.nocoding.util.WebAction;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.resolver.VariableResolver;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;

/**
 * <code>Action</code> is the data model for the "Action" type of the execution.<br>
 * Therefore, it consists of a {@link #name} and list of {@link AbstractActionSubItem}s.
 * 
 * @author ckeiner
 */
public class Action implements Command
{
    /**
     * The name of the action
     */
    protected String name;

    /**
     * The list of actionItems
     */
    protected final List<AbstractActionSubItem> actionItems;

    /**
     * Creates an instance of {@link Action} that sets {@link #actionItems} to an ArrayList of size 1.
     */
    public Action()
    {
        actionItems = new ArrayList<AbstractActionSubItem>(1);
    }

    /**
     * Creates an instance of {@link Action} that sets {@link #name} and {@link #actionItems}.
     * 
     * @param name
     *            The name of the action
     * @param actionItems
     *            A list of {@link AbstractActionSubItem}s
     */
    public Action(final String name, final List<AbstractActionSubItem> actionItems)
    {
        this.name = name;
        this.actionItems = actionItems;
    }

    public List<AbstractActionSubItem> getActionItems()
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
     * response and in the correct order. Finally, it adds the default static subrequests.
     * 
     * @param context
     *            The {@link Context} with the {@link DataStorage}.
     */
    protected void fillDefaultData(final Context<?> context)
    {
        if (name == null || name.isEmpty())
        {
            setName(ActionItemUtil.getDefaultName(context, "Action"));
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

    /**
     * Tries to resolve all variables of non-null attributes
     * 
     * @param context
     *            The {@link Context} with the {@link VariableResolver}
     * @throws InvalidArgumentException
     */
    protected void resolveValues(final Context<?> context)
    {
        if (getName() != null)
        {
            setName(context.resolveString(getName()));
        }
    }

    /**
     * Executes the {@link Action} by building a {@link WebAction} with the {@link #actionItems}. The <code>WebAction</code>
     * then executes the <code>actionItems</code>. In the end, the loaded page gets appended to the result browser.
     * 
     * @throws Throwable
     *             if a Throwable occurs during the execution or when the page is appended to the result browser
     */
    @Override
    public void execute(final Context<?> context) throws Throwable
    {
        // Fill default data
        fillDefaultData(context);
        // Resolve values
        resolveValues(context);

        // Create the WebAction with the data of this action
        final WebAction action = new WebAction(name, context, getActionItems());

        // Try to execute it
        try
        {
            // Execute the requests, responses and subrequests via xlt api
            action.run();
        }
        catch (final Exception e)
        {
            XltLogger.runTimeLogger.error("Execution Step failed : " + getName());
            e.printStackTrace();
            throw new Exception("Execution Step failed : " + getName(), e);
        }
        // And always append the page to the result browser
        finally
        {
            // Append the page to the result browser
            if (context.getWebResponse() != null)
            {
                context.appendToResultBrowser();
            }
        }
    }

}
