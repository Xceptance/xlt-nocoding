package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Creates a XhrSubrequest that has a name and can consists of a request, response and another subrequest.
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

        // If the first action item is not a request, throw an Exception
        if (!(actionItems.iterator().next() instanceof Request))
        {
            throw new Exception();
        }
        // If it is a request, set xhr to true
        else
        {
            ((Request) actionItems.iterator().next()).setXhr("true");
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

}
