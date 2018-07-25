package com.xceptance.xlt.nocoding.util;

import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.common.lang.ReflectionUtils;
import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.request.Request;
import com.xceptance.xlt.nocoding.command.action.response.Response;
import com.xceptance.xlt.nocoding.command.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * The WebAction extends the AbstractWebAction of the XLT framework. Therefore, this is the interface to XLT. A
 * WebAction fires a main (web) request, and afterwards an arbitrary amount of (XHR Sub)Request, {@link Response} and
 * {@link AbstractSubrequest}.
 * 
 * @author ckeiner
 */
public class WebAction extends AbstractWebAction
{
    /**
     * The context of the current WebAction
     */
    private final Context<?> context;

    /**
     * The WebClient for this action
     */
    private WebClient webClient;

    /**
     * The list of action items
     */
    private final List<AbstractActionSubItem> actionItems;

    /**
     * Creates a new instance of WebAction.
     * 
     * @param timerName
     *            The name of the WebAction
     * @param context
     *            The current {@link Context}
     * @param actionItems
     *            The list of {@link AbstractActionSubItem}s that are to be executed
     */
    public WebAction(final String timerName, final Context<?> context, final List<AbstractActionSubItem> actionItems)
    {
        super(timerName);
        this.context = context;
        this.webClient = context.getWebClient();
        this.actionItems = actionItems;
    }

    /**
     * Execute the {@link WebAction} by executing {@link #actionItems}.
     * 
     * @throws Exception
     *             Any Exception that happens during the execution
     */
    @Override
    protected void execute() throws Exception
    {
        // clear cache with the private method in WebClient, so we can fire a request to the same url twice in a run
        final Map<String, WebResponse> pageLocalCache = ReflectionUtils.readInstanceField(getWebClient(), "pageLocalCache");
        pageLocalCache.clear();

        // Extract the action items
        final List<AbstractActionSubItem> actionItems = this.getActionItems();

        // Check if the first actionItem is a Request
        if (!(actionItems.iterator().next() instanceof Request))
        {
            throw new Exception("First item of action \"" + this.getTimerName() + "\" is not a Request!");
        }

        // If there are actionItems
        if (actionItems != null && !actionItems.isEmpty())
        {
            // Execute every actionItem, i.e. main request and validations.
            for (final AbstractActionSubItem actionItem : actionItems)
            {
                actionItem.execute(getContext());
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

    /**
     * Gets the WebClient if it is set, else it gets the WebClient via {@link AbstractWebAction#getWebClient()}.
     */
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

    public List<AbstractActionSubItem> getActionItems()
    {
        return actionItems;
    }

}
