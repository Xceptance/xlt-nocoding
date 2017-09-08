package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;

public class Response
{
    private final int httpcode;

    private final Store storage;

    private final List<AbstractValidator> validation;

    public Response(final int httpcode, final Store storage, final List<AbstractValidator> validation)
    {
        this.httpcode = httpcode;
        this.storage = storage;
        this.validation = validation;
    }

    public void validate(final HtmlPage page) throws Exception
    {

        for (final AbstractValidator abstractValidator : validation)
        {
            abstractValidator.validate(page);
        }
    }

    public void validate(final LightWeightPage page) throws Exception
    {

        for (final AbstractValidator abstractValidator : validation)
        {
            abstractValidator.validate(page);
        }
    }

}
