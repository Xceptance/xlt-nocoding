package com.xceptance.xlt.nocoding.util;

import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.common.lang.ReflectionUtils;
import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * The WebAction extends the AbstractWebAction of the XLT framework. Therefore, this is the interface to XLT. A
 * WebAction fires a main (web) request, and afterwards an arbitrary amount of Request, Response and AbstractSubrequest.
 * The execution of the main request, etc. can be configured by passing a function to the WebAction.
 * 
 * @author ckeiner
 */
public class WebAction extends AbstractWebAction
{
    /**
     * The context in the current WebAction
     */
    private final Context<?> context;

    /**
     * The main request in this action
     */
    private WebClient webClient;

    /**
     * The list of action items
     */
    private final List<AbstractActionItem> actionItems;

    /**
     * @param timerName
     *            The name of the action
     * @param context
     *            The current {@link Context}
     * @param request
     *            The main request
     * @param actionItems
     */
    public WebAction(final String timerName, final Context<?> context, final List<AbstractActionItem> actionItems)
    {
        super(timerName);
        this.context = context;
        this.webClient = context.getWebClient();
        this.actionItems = actionItems;
    }

    /**
     * Execute the {@link WebAction}.
     * 
     * @param action
     *            The {@link WebAction} that executes this method.
     * @throws Exception
     */
    @Override
    protected void execute() throws Exception
    {
        // clear cache with the private method in WebClient, so we can fire a request twice in a run
        final Map<String, WebResponse> pageLocalCache = ReflectionUtils.readInstanceField(getWebClient(), "pageLocalCache");
        pageLocalCache.clear();
        // function.accept(this);

        // Extract the context
        final Context<?> context = this.getContext();
        // Extract the action items
        final List<AbstractActionItem> actionItems = this.getActionItems();

        // Check if the first actionItem is a Request
        if (!(actionItems.iterator().next() instanceof Request))
        {
            throw new Exception("First item of action \"" + this.getTimerName() + "\" is not a Request!");
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

    @Override
    protected void postValidate() throws Exception
    {
    }

    @Override
    public void preValidate() throws Exception
    {
    }

    public WebClient getWebClient()
    {
        WebClient webClient;
        if (this.webClient != null)
        {
            webClient = this.webClient;
        }
        else
        {
            webClient = super.getWebClient();
        }
        return webClient;
    }

    public void setWebClient(final WebClient webClient)
    {
        this.webClient = webClient;
    }

    public Context<?> getContext()
    {
        return context;
    }

    public List<AbstractActionItem> getActionItems()
    {
        return actionItems;
    }

}
