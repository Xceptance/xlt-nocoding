package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;

public abstract class AbstractValidator implements AbstractResponseItem
{
    protected final String validationName;

    public AbstractValidator(final String validationName)
    {
        this.validationName = validationName;
    }

    public String getValidationName()
    {
        return validationName;
    }

}
