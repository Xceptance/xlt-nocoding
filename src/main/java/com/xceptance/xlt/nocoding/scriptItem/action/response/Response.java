package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * The expected response to a request. A response has the expected httpcode, and a list of response items, which
 * validate or store something of the {@link WebResponse} defined in the {@link Context}.
 * 
 * @author ckeiner
 */
public class Response extends AbstractActionItem
{

    private final List<AbstractResponseItem> responseItems;

    public Response()
    {
        this(new ArrayList<AbstractResponseItem>(1));
        responseItems.add(new HttpcodeValidator(null));
    }

    public Response(final List<AbstractResponseItem> responseItems)
    {
        this.responseItems = responseItems;
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        fillDefaultData(context);
        // Execute every AbstractResponseItem
        for (final AbstractResponseItem abstractResponseItem : responseItems)
        {
            abstractResponseItem.execute(context);
        }
    }

    public List<AbstractResponseItem> getResponseItems()
    {
        return responseItems;
    }

    public void fillDefaultData(final Context context)
    {
        boolean hasHttpcodeValidator = false;
        for (final AbstractResponseItem responseItem : responseItems)
        {
            if (responseItem instanceof HttpcodeValidator)
            {
                hasHttpcodeValidator = true;
            }
        }
        if (!hasHttpcodeValidator)
        {
            responseItems.add(0, new HttpcodeValidator(null));
        }
    }

}
