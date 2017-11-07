package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;

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
