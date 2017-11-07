package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;

/**
 * The abstract class for every response item that validates the {@link WebResponse}.
 * 
 * @author ckeiner
 */
public abstract class AbstractValidator implements AbstractResponseItem
{
    protected final String validationName;

    protected final String validationMode;

    public AbstractValidator(final String validationName, final String validationMode)
    {
        this.validationName = validationName;
        this.validationMode = validationMode;
    }

    public String getValidationName()
    {
        return validationName;
    }

    public String getValidationMode()
    {
        return validationMode;
    }

}
