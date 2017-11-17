package com.xceptance.xlt.nocoding.scriptItem.action.response;

import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.engine.LightWeightPageImpl;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

public class HttpcodeValidator extends AbstractResponseItem
{
    private String httpcode;

    public HttpcodeValidator(final String httpcode)
    {
        this.httpcode = httpcode;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        resolveValues(context);
        fillDefaultData(context);
        // Validate httpcode
        final LightWeightPage page = new LightWeightPageImpl(context.getWebResponse(), context.getWebClient().getTimerName(),
                                                             context.getWebClient());
        if (page != null && httpcode != null)
        {
            new HttpResponseCodeValidator(Integer.parseInt(this.httpcode)).validate(page);
        }
    }

    @Override
    protected void fillDefaultData(final Context context)
    {
        if (httpcode == null)
        {
            httpcode = context.getConfigItemByKey(Constants.HTTPCODE);
        }
    }

    private void resolveValues(final Context context)
    {
        httpcode = context.resolveString(httpcode);
    }

    public String getHttpcode()
    {
        return httpcode;
    }

}
