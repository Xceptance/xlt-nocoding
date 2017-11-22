package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.Context;

public class ExistsValidator extends AbstractValidationMode
{

    @Override
    public void execute(final Context context) throws Exception
    {
        Assert.assertNotNull("Expression is null", getExpressionToValidate());
        Assert.assertFalse("Result list is empty", getExpressionToValidate().isEmpty());
    }

    @Override
    protected void resolveValues(final Context context)
    {
        // We do not need to resolve anything here
    }

}
