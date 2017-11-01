package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.util.Context;

public class XHRSubrequest extends AbstractSubrequest
{
    private final String name;

    private final List<AbstractActionItem> actionItems;

    @SuppressWarnings("unused")
    private WebRequest webRequest;

    public XHRSubrequest(final String name, final List<AbstractActionItem> actionItems)
    {
        this.name = name;
        this.actionItems = actionItems;
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        final Context localContext = new Context(context);

        // TODO Does the first ActionItem have to be a Request?
        for (final AbstractActionItem actionItem : actionItems)
        {
            // If this is a request
            if (actionItem instanceof Request)
            {
                // Set Xhr to true
                ((Request) actionItem).setXhr("true");
            }
            actionItem.execute(localContext);
        }
    }

    public String getName()
    {
        return name;
    }

    public void setWebRequest(final WebRequest webRequest)
    {
        this.webRequest = webRequest;
    }

}
