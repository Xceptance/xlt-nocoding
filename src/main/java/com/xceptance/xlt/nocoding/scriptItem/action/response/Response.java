package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.validators.ContentLengthValidator;
import com.xceptance.xlt.api.validators.HtmlEndTagValidator;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidator;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class Response
{
    public static int DEFAULT_HTTPCODE = 200;

    private final int httpcode;

    private PropertyManager propertyManager;

    private final List<AbstractResponseStore> responseStore;

    private final List<AbstractValidator> validation;

    public Response(final int httpcode, final List<AbstractResponseStore> responseStore, final List<AbstractValidator> validation)
    {
        this.httpcode = httpcode;
        this.responseStore = responseStore;
        this.validation = validation;
    }

    public Response()
    {
        httpcode = DEFAULT_HTTPCODE;
        responseStore = null;
        validation = null;
    }

    public PropertyManager getPropertyManager()
    {
        return propertyManager;
    }

    public void setPropertyManager(final PropertyManager propertyManager)
    {
        this.propertyManager = propertyManager;
    }

    public void execute(final HtmlPage page) throws Exception
    {
        this.validate(page);
        this.store(page);
    }

    public void execute(final LightWeightPage page) throws Exception
    {
        this.validate(page);
        this.store(page);
    }

    public void validate(final HtmlPage page) throws Exception
    {
        // Check for complete HTML.
        HtmlEndTagValidator.getInstance().validate(page);
        if (validation != null)
        {
            for (final AbstractValidator abstractValidator : validation)
            {
                abstractValidator.validate(page);
            }
        }
    }

    public void validate(final LightWeightPage page) throws Exception
    {
        new HttpResponseCodeValidator(httpcode).validate(page);
        // Check the content length, compare delivered content length to the
        // content length that was announced in the HTTP response header.
        // TODO Content Length needn't be specified
        ContentLengthValidator.getInstance().validate(page);

        if (validation != null)
        {
            for (final AbstractValidator abstractValidator : validation)
            {
                abstractValidator.validate(page);
            }
        }
    }

    public void store(final HtmlPage page) throws Exception
    {
        if (responseStore != null)
        {
            for (final AbstractResponseStore abstractResponseStore : responseStore)
            {
                abstractResponseStore.setGlobalStorage(this.propertyManager.getGlobalStorage());
                abstractResponseStore.store(page);
            }
        }
    }

    public void store(final LightWeightPage page) throws Exception
    {
        if (responseStore != null)
        {
            for (final AbstractResponseStore abstractResponseStore : responseStore)
            {
                abstractResponseStore.setGlobalStorage(this.propertyManager.getGlobalStorage());
                abstractResponseStore.store(page);
            }
        }
    }

}
