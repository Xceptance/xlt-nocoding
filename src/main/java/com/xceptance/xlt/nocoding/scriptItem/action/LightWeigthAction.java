package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.engine.LightWeightPageImpl;
import com.xceptance.xlt.engine.SessionImpl;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.webAction.WebAction;

/**
 * The class that describes an action in lightweight mode
 */
public class LightWeigthAction extends Action
{
    /**
     * The light weight page that is built by this action
     */
    private LightWeightPage lightWeightPage;

    public LightWeigthAction(final Request request, final List<AbstractActionItem> actionItems)
    {
        super(request, actionItems);
    }

    /**
     * Executes the light weight action by building a WebAction, running it and then validating the answer. In the end, the
     * page gets appended to the result browser.
     */
    @Override
    public void execute(final Context context) throws Throwable
    {
        final WebAction action = new WebAction(getRequest().getName(), context, getRequest(), getActionItems(),
                                               (final WebAction webAction) -> {
                                                   try
                                                   {
                                                       doExecute(webAction);
                                                   }
                                                   catch (final Throwable e)
                                                   {
                                                       XltLogger.runTimeLogger.error("Execution Step failed");
                                                       e.printStackTrace();
                                                       throw new Exception(e);
                                                   }
                                               });

        try
        {
            // Execute the requests, responses and subrequests via xlt api
            action.run();
            setLightWeightPage(new LightWeightPage(action.getContext().getWebResponse(), action.getTimerName()));
        }
        finally
        {
            // Append the page to the result browser
            if (action.getContext().getWebResponse() != null)
            {
                ((SessionImpl) Session.getCurrent()).getRequestHistory()
                                                    .add(new LightWeightPageImpl(action.getContext().getWebResponse(),
                                                                                 action.getTimerName(),
                                                                                 action.getContext().getWebClient()));
                ;
            }
        }
    }

    /**
     * This method uses an action as parameter and defines how to execute a WebAction. This is used in a lambda method in
     * the execute-Method.
     * 
     * @param action
     * @throws Exception
     */
    public void doExecute(final WebAction action) throws Throwable
    {
        final Context context = action.getContext();
        final List<AbstractActionItem> actionItems = action.getActionItems();
        action.getRequest().execute(context);

        if (actionItems != null && !actionItems.isEmpty())
        {
            for (final AbstractActionItem actionItem : actionItems)
            {
                actionItem.execute(context);
            }
        }
    }

    public LightWeightPage getLightWeightPage()
    {
        return lightWeightPage;
    }

    public void setLightWeightPage(final LightWeightPage lightWeightPage)
    {
        this.lightWeightPage = lightWeightPage;
    }
}
