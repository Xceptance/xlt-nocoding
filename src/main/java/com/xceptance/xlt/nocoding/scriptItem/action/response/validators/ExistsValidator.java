package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.Context;

public class ExistsValidator extends AbstractValidator
{

    @Override
    public void execute(final Context context) throws Exception
    {
        Assert.assertFalse(getExpressionToValidate().isEmpty());
    }

    @Override
    protected void resolveValues(final Context context)
    {
        // We do not need to resolve anything here
    }

}
