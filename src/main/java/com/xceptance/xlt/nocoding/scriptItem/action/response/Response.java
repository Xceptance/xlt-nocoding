package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.List;

import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.engine.LightWeightPageImpl;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

public class Response extends AbstractActionItem
{

    private String httpcode;

    private final List<AbstractResponseItem> responseItems;

    public Response()
    {
        this(null, null);
    }

    public Response(final String httpcode, final List<AbstractResponseItem> responseItems)
    {
        this.httpcode = httpcode;
        this.responseItems = responseItems;
    }

    public String getHttpcode()
    {
        return httpcode;
    }

    public void setHttpcode(final String httpcode)
    {
        this.httpcode = httpcode;
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        // Fill in default data if neccessary
        fillDefaultData(context);
        // Resolve variables
        resolveValues(context);
        // Validate httpcode
        final LightWeightPage page = new LightWeightPageImpl(context.getWebResponse(), context.getWebClient().getTimerName(),
                                                             context.getWebClient());
        if (page != null && httpcode != null)
        {
            new HttpResponseCodeValidator(Integer.parseInt(getHttpcode())).validate(page);
        }

        // Execute every AbstractResponseItem
        for (final AbstractResponseItem abstractResponseItem : responseItems)
        {
            abstractResponseItem.execute(context);
        }
    }

    private void fillDefaultData(final Context context)
    {
        if (getHttpcode() == null)
        {
            setHttpcode(context.getDataStorage().getConfigItemByKey(Constants.HTTPCODE));
        }

    }

    private void resolveValues(final Context context)
    {
        setHttpcode(context.resolveString(httpcode));

    }

    public List<AbstractResponseItem> getResponseItems()
    {
        return responseItems;
    }

}
