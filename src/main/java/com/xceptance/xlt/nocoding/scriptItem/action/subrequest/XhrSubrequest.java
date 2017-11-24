package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Creates a XhrSubrequest that has a name and can consists of a request, response and multiple subrequest.
 * 
 * @author ckeiner
 */
public class XhrSubrequest extends AbstractSubrequest
{
    private final String name;

    private final List<AbstractActionItem> actionItems;

    @SuppressWarnings("unused")
    private WebRequest webRequest;

    public XhrSubrequest(final String name, final List<AbstractActionItem> actionItems)
    {
        this.name = name;
        this.actionItems = actionItems;
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        final Context localContext = new Context(context);

        assertOrder();
        // If the first action item is not a request, throw an Exception
        if (!(actionItems.iterator().next() instanceof Request))
        {
            final Request request = ((Request) actionItems.iterator().next());
            request.setXhr("true");
            request.getHeaders().put("X-Requested-With", "XMLHttpRequest");
            request.getHeaders().put("Referer", context.getWebResponse().getWebRequest().getUrl().toString());
        }

        for (final AbstractActionItem actionItem : actionItems)
        {
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

    /**
     * Asserts that the order of Request, Response, Subrequest is correct
     */
    public void assertOrder()
    {
        boolean hasResponse = false;
        boolean hasSubrequest = false;
        for (final AbstractActionItem actionItem : actionItems)
        {
            if (actionItem instanceof Request && (hasResponse || hasSubrequest))
            {
                throw new IllegalArgumentException("Request cannot be defined after a response or subrequest.");
            }
            else if (actionItem instanceof Response)
            {
                hasResponse = true;
                if (hasSubrequest)
                {
                    throw new IllegalArgumentException("Response mustn't be defined after subrequests.");
                }
            }
            else if (actionItem instanceof AbstractSubrequest && !hasSubrequest)
            {
                hasSubrequest = true;
            }
        }
    }

}
