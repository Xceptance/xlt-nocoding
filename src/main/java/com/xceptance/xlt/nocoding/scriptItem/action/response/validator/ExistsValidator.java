package com.xceptance.xlt.nocoding.scriptItem.action.response.validator;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Validates that {@link #getExpressionToValidate()} is neither null nor empty.
 * 
 * @author ckeiner
 */
public class ExistsValidator extends AbstractValidator
{

    /**
     * Validates that {@link #getExpressionToValidate()} is neither null nor empty.
     */
    @Override
    public void execute(final Context<?> context)
    {
        resolveValues(context);
        Assert.assertNotNull("Expression is null", getExpressionToValidate());
        Assert.assertFalse("Result list is empty", getExpressionToValidate().isEmpty());
    }

    /**
     * Does not resolve anything but must be overridden.
     */
    @Override
    protected void resolveValues(final Context<?> context)
    {
        // We do not need to resolve anything here
    }

}
