package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.List;

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
    /**
     * The name of the subrequest
     */
    private final String name;

    /**
     * The request, response and subrequests of this subrequest
     */
    private final List<AbstractActionItem> actionItems;

    /**
     * Creates an instance of {@link XhrSubrequest} that sets the {@link #name} and {@link #actionItems}
     * 
     * @param name
     *            The name of the subrequest
     * @param actionItems
     *            The request, response and subrequests of this subrequest
     */
    public XhrSubrequest(final String name, final List<AbstractActionItem> actionItems)
    {
        this.name = name;
        this.actionItems = actionItems;
    }

    /**
     * Executes the subrequest by first creating a local {@link Context} out of the method parameter. Then it asserts the
     * order of the {@link #actionItems}. Then, sets the {@link Request} in the {@link #actionItems} up. Finally, executes
     * the {@link #actionItems}.
     */
    @Override
    public void execute(final Context context) throws Throwable
    {
        // Create a new local context, so we do not overwrite the old context
        final Context localContext = new Context(context);

        // Assert that the order of Request, Response, Subrequest is correct
        assertOrder();
        // TODO If the first action item is not a request, throw an Exception
        if (actionItems.iterator().next() instanceof Request)
        {
            // Get the request
            final Request request = ((Request) actionItems.iterator().next());
            // Set Xhr to true
            request.setXhr("true");
            // Set XhrSubrequest specific headers
            request.getHeaders().put("X-Requested-With", "XMLHttpRequest");
            request.getHeaders().put("Referer", context.getWebResponse().getWebRequest().getUrl().toString());
        }

        // Try and catch to add the name of the XhrSubrequest to the Exception
        try
        {
            // Execute every actionItem
            for (final AbstractActionItem actionItem : actionItems)
            {
                actionItem.execute(localContext);
            }
        }
        catch (final Exception e)
        {
            throw new Exception("XhrSubrequest \"" + name + "\" failed because " + e.getMessage(), e);
        }
    }

    public String getName()
    {
        return name;
    }

    /**
     * Asserts that the order of {@link Request}, {@link Response}, and {@link AbstractSubrequest} is correct
     */
    public void assertOrder()
    {
        boolean hasResponse = false;
        boolean hasSubrequest = false;
        for (final AbstractActionItem actionItem : actionItems)
        {
            // If a Response or AbstractSubrequest is before Request, throw an Exception
            if (actionItem instanceof Request && (hasResponse || hasSubrequest))
            {
                throw new IllegalArgumentException("Request cannot be defined after a response or subrequest.");
            }
            // If it is a Response, set hasResponse to true
            else if (actionItem instanceof Response)
            {
                hasResponse = true;
                // If an AbstractSubrequest is before Response,throw an error
                if (hasSubrequest)
                {
                    throw new IllegalArgumentException("Response mustn't be defined after subrequests.");
                }
            }
            // If an AbstractSubrequest is found, set hasSubrequest to true
            else if (actionItem instanceof AbstractSubrequest && !hasSubrequest)
            {
                hasSubrequest = true;
            }
        }
    }

}
