package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.WebAction;

public class DomAction extends Action
{
    private HtmlPage htmlPage;

    @SuppressWarnings("unused")
    private final long waitingTime = 30000;

    public DomAction(final String name, final List<AbstractActionItem> actionItems)
    {
        super(name, actionItems);
    }

    public DomAction(final String name, final Request request)
    {
        this(name, new ArrayList<AbstractActionItem>(1));
        actionItems.add(request);
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        final WebAction action = new WebAction(name, context, getActionItems(), (final WebAction webAction) -> {
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

        action.run();
    }

    public HtmlPage getHtmlPage()
    {
        return htmlPage;
    }

    public void setHtmlPage(final HtmlPage htmlPage)
    {
        this.htmlPage = htmlPage;
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

        if (actionItems != null && !actionItems.isEmpty())
        {
            for (final AbstractActionItem actionItem : actionItems)
            {
                actionItem.execute(context);
            }
        }
    }

}
