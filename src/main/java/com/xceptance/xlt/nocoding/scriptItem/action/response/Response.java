package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.engine.LightWeightPageImpl;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

public class Response extends AbstractActionItem
{
    public static String DEFAULT_HTTPCODE = "200";

    private String httpcode;

    private final List<AbstractResponseStore> responseStore;

    private final List<AbstractValidator> validation;

    public Response(final List<AbstractResponseStore> responseStore, final List<AbstractValidator> validation)
    {
        this(null, responseStore, validation);
    }

    public Response(final String httpcode, final List<AbstractResponseStore> responseStore, final List<AbstractValidator> validation)
    {
        this.httpcode = httpcode;
        this.responseStore = responseStore;
        this.validation = validation;
    }

    public Response()
    {
        this(null, null, null);
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
        // fill everything with data
        fillDefaultData(context);
        // then resolve variables
        resolveValues(context);
        // build the webRequest
        validate(context, context.getWebResponse());
        store(context, context.getWebResponse());
    }

    public void execute(final Context context, final WebResponse webResponse) throws Exception
    {
        // fill everything with data
        fillDefaultData(context);
        // then resolve variables
        resolveValues(context);
        // build the webRequest
        validate(context, webResponse);
        store(context, webResponse);
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

    public void validate(final Context context, final WebResponse webResponse) throws Exception
    {
        // TODO Get timer name
        final LightWeightPage page = new LightWeightPageImpl(webResponse, context.getWebClient().getTimerName(), context.getWebClient());
        if (page != null && httpcode != null)
        {
            new HttpResponseCodeValidator(Integer.parseInt(getHttpcode())).validate(page);
        }
        // Check the content length, compare delivered content length to the
        // content length that was announced in the HTTP response header.
        // TODO Content Length needn't be specified, check for it then validate
        // ContentLengthValidator.getInstance().validate(page);

        if (validation != null)
        {
            for (final AbstractValidator abstractValidator : validation)
            {
                abstractValidator.validate(context, webResponse);
            }
        }
    }

    public void store(final Context context, final WebResponse webResponse) throws Exception
    {
        if (responseStore != null)
        {
            for (final AbstractResponseStore abstractResponseStore : responseStore)
            {
                abstractResponseStore.store(context, webResponse);
            }
        }
    }

}
