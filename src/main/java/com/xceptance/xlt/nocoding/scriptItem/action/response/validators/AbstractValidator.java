package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public abstract class AbstractValidator
{
    protected final String validationName;

    public AbstractValidator(final String validationName)
    {
        this.validationName = validationName;
    }

    public abstract void validate(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception;

}
