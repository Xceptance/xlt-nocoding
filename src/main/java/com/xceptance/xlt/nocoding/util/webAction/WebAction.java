package com.xceptance.xlt.nocoding.util.webAction;

import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.common.lang.ReflectionUtils;
import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.ThrowingConsumer;

public class WebAction extends AbstractWebAction
{
    /**
     * The context in the current WebAction
     */
    private final Context context;

    /**
     * The main request in this action
     */
    private final Request request;

    /**
     * The main request in this action
     */
    private WebClient webClient;

    /**
     * The list of action items
     */
    private final List<AbstractActionItem> actionItems;

    private ThrowingConsumer<WebAction> function;

    public WebAction(final String timerName, final Context context, final Request request, final List<AbstractActionItem> actionItems,
        final ThrowingConsumer<WebAction> function)
    {
        super(timerName);
        this.context = context;
        this.webClient = context.getWebClient();
        this.request = request;
        this.actionItems = actionItems;
        this.function = function;
    }

    @Override
    protected void execute() throws Exception
    {
        // clear cache with the private method in WebClient, so we can fire a request twice in a run
        final Map<String, WebResponse> pageLocalCache = ReflectionUtils.readInstanceField(getWebClient(), "pageLocalCache");
        pageLocalCache.clear();
        function.accept(this);
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

    public ThrowingConsumer<WebAction> getFunction()
    {
        return function;
    }

    public void setFunction(final ThrowingConsumer<WebAction> function)
    {
        this.function = function;
    }

    public Context getContext()
    {
        return context;
    }

    public List<AbstractActionItem> getActionItems()
    {
        return actionItems;
    }

    public Request getRequest()
    {
        return request;
    }

}
