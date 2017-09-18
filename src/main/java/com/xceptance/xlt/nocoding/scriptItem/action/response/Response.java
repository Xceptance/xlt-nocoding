package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidator;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class Response
{
    public static int DEFAULT_HTTPCODE = 200;

    private final Integer httpcode;

    private final List<AbstractResponseStore> responseStore;

    private final List<AbstractValidator> validation;

    public Response(final List<AbstractResponseStore> responseStore, final List<AbstractValidator> validation)
    {
        this(null, responseStore, validation);
    }

    public Response(final Integer httpcode, final List<AbstractResponseStore> responseStore, final List<AbstractValidator> validation)
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

    public void execute(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        this.validate(propertyManager, webResponse);
        this.store(propertyManager, webResponse);
    }

    public void validate(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        // TODO Rebuild here
        // new HttpResponseCodeValidator(httpcode).validate(page);
        //// Check the content length, compare delivered content length to the
        //// content length that was announced in the HTTP response header.
        //// TODO Content Length needn't be specified, check for it then validate
        // ContentLengthValidator.getInstance().validate(page);

        if (validation != null)
        {
            for (final AbstractValidator abstractValidator : validation)
            {
                abstractValidator.validate(propertyManager, webResponse);
            }
        }
    }

    public void store(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        if (responseStore != null)
        {
            for (final AbstractResponseStore abstractResponseStore : responseStore)
            {
                abstractResponseStore.store(propertyManager, webResponse);
            }
        }
    }

}
