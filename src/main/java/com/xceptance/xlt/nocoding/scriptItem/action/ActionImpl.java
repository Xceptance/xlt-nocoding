package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.WebAction;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * An implementation of {@link Action}.
 * 
 * @author ckeiner
 */
public class ActionImpl extends Action
{

    /**
     * Creates an instance of {@link ActionImpl} that sets {@link #actionItems} to an {@link ArrayList} of size 1.
     */
    public ActionImpl()
    {
        super();
    }

    /**
     * Creates an instance of {@link ActionImpl} that sets {@link #name} and {@link #actionItems}.
     * 
     * @param name
     *            The name of the action
     * @param actionItems
     *            A {@link List}<{@link AbstractActionItem}>
     */
    public ActionImpl(final String name, final List<AbstractActionItem> actionItems)
    {
        super(name, actionItems);
    }

    /**
     * Executes the {@link ActionImpl} by building a {@link WebAction}, running it and then validating the answer. In the
     * end, the page gets appended to the result browser.
     */
    @Override
    public void execute(final Context context) throws Throwable
    {
        // Fill default data
        fillDefaultData(context);

        // Define the WebAction with the doExecute-Method
        final WebAction action = new WebAction(name, context, getActionItems(), (final WebAction webAction) -> {
            try
            {
                doExecute(webAction);
            }
            catch (final Throwable e)
            {
                final String errorMessage = "Execution Step \"" + getName() + "\" failed: " + e.getMessage();
                XltLogger.runTimeLogger.error(errorMessage);
                e.printStackTrace();
                throw new Exception(errorMessage, e);
            }
        });

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
            throw new Exception(e);
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

    /**
     * This method uses an action as parameter and defines how to execute a {@link WebAction}. This is used for the
     * constructor in {@link WebAction}, so we can define the behavior of {@link WebAction} here.
     * 
     * @param action
     *            The {@link WebAction} that executes this method.
     * @throws Exception
     */
    public void doExecute(final WebAction action) throws Throwable
    {
        // Extract the context
        final Context context = action.getContext();
        // Extract the action items
        final List<AbstractActionItem> actionItems = action.getActionItems();

        // Check if the first actionItem is a Request
        if (!(actionItems.iterator().next() instanceof Request))
        {
            throw new Exception("First item of action \"" + action.getTimerName() + "\" is not a Request!");
        }

        // If there are actionItems
        if (actionItems != null && !actionItems.isEmpty())
        {
            // Execute every actionItem, i.e. main request and validations.
            for (final AbstractActionItem actionItem : actionItems)
            {
                actionItem.execute(context);
            }
        }
    }
}
