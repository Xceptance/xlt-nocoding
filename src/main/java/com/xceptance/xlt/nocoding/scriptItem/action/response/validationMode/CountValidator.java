package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.Context;

/**
 * Validates that {@link #getExpressionToValidate()} has a size of the {@link Integer} value of {@link #count}.
 * 
 * @author ckeiner
 */
public class CountValidator extends AbstractValidationMode
{
    /**
     * The expected size of {@link #getExpressionToValidate()} as {@link String}
     */
    private String count;

    /**
     * Creates an instance of {@link CountValidator}, that sets {@link #count}.
     * 
     * @param count
     *            The expected size of {@link #getExpressionToValidate()} as {@link String}
     */
    public CountValidator(final String count)
    {
        this.count = count;
    }

    /**
     * Resolves values, parses {@link #count} to an {@link Integer} and finally asserts that
     * {@link #getExpressionToValidate()} has a size of the {@link Integer} value of {@link #count}.
     */
    @Override
    public void execute(final Context context)
    {
        // Resolve values
        resolveValues(context);
        // Transform count to an Integer
        final Integer count = Integer.parseInt(this.count);
        // Verify the result list/expressionToValidate is not null
        if (getExpressionToValidate() == null)
        {
            throw new IllegalStateException("Result list is null");
        }
        // Assert that the amount of results is the same as count
        Assert.assertTrue("Expected " + this.count + " matches but found " + count.toString() + " matches",
                          count.equals(getExpressionToValidate().size()));
    }

    /**
     * Resolves {@link #count}.
     */
    @Override
    protected void resolveValues(final Context context)
    {
        count = context.resolveString(count);
    }

}
