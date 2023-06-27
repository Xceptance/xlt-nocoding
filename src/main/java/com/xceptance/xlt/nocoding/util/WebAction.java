/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.util;

import java.util.List;
import java.util.Map;

import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;

import com.xceptance.common.lang.ReflectionUtils;
import com.xceptance.xlt.api.actions.AbstractAction;
import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.engine.XltWebClient;
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
public class WebAction extends AbstractAction
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
        super(context.getPreviousWebAction(), timerName);
        this.context = context;
        webClient = context.getWebClient();
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
        context.setPreviousWebAction(this);
        ((XltWebClient) getWebClient()).setTimerName(getTimerName());
        // clear cache with the private method in WebClient, so we can fire a request to the same url twice in a run
        final Map<String, WebResponse> pageLocalCache = ReflectionUtils.readInstanceField(getWebClient(), "pageLocalCache");
        pageLocalCache.clear();

        // Extract the action items
        final List<AbstractActionSubItem> actionItems = getActionItems();

        // Check if the first actionItem is a Request
        if (!(actionItems.iterator().next() instanceof Request))
        {
            throw new Exception("First item of action \"" + getTimerName() + "\" is not a Request!");
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
