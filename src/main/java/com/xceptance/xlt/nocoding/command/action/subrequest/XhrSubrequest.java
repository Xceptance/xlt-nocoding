package com.xceptance.xlt.nocoding.command.action.subrequest;

import java.util.List;

import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.request.Request;
import com.xceptance.xlt.nocoding.command.action.response.Response;
import com.xceptance.xlt.nocoding.util.ActionItemUtil;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Creates a {@link XhrSubrequest} that has a name and consists of a {@link Request}, and optionally a {@link Response}
 * and maybe multiple {@link AbstractSubrequest}.
 * 
 * @author ckeiner
 */
public class XhrSubrequest extends AbstractSubrequest
{
    /**
     * The name of the subrequest
     */
    private String name;

    /**
     * The request, response and subrequests of this subrequest
     */
    private final List<AbstractActionSubItem> actionItems;

    /**
     * Creates an instance of {@link XhrSubrequest} that sets the {@link #name} and {@link #actionItems}
     * 
     * @param name
     *            The name of the subrequest
     * @param actionItems
     *            A list of {@link AbstractActionSubItem}s
     */
    public XhrSubrequest(final String name, final List<AbstractActionSubItem> actionItems)
    {
        this.name = name;
        this.actionItems = actionItems;
    }

    /**
     * Executes the subrequest by first creating a local {@link Context} out of the method parameter. Then, it asserts the
     * order of the {@link #actionItems}. Then, sets the {@link Request} in the {@link #actionItems} up. Finally, executes
     * the {@link #actionItems}.
     */
    @Override
    public void execute(final Context<?> context) throws Exception
    {
        // Create a new local context, so we do not overwrite the old context
        final Context<?> localContext = context.buildNewContext();

        // Assert that the order of Request, Response, Subrequest is correct
        ActionItemUtil.assertOrder(actionItems);
        fillDefaultData(localContext);

        // Get the request
        final Request request = ((Request) actionItems.iterator().next());
        // Set Xhr to true
        request.setXhr("true");
        // Set XhrSubrequest specific headers
        request.getHeaders().put("X-Requested-With", "XMLHttpRequest");
        request.getHeaders().put("Referer", context.getWebResponse().getWebRequest().getUrl().toString());

        // Try and catch to add the name of the XhrSubrequest to the Exception
        try
        {
            // Execute every actionItem
            for (final AbstractActionSubItem actionItem : actionItems)
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
     * Sets name and request to the default name and request if they aren't not specified
     */
    @Override
    public void fillDefaultData(final Context<?> context)
    {
        if (name == null || name.isEmpty())
        {
            name = ActionItemUtil.getDefaultName(context, "XhrSubrequest");
        }
        ActionItemUtil.fillDefaultData(actionItems, context);
    }

    public List<AbstractActionSubItem> getActionItems()
    {
        return actionItems;
    }

}
