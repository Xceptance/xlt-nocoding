package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.Context;

public class CountValidator extends AbstractValidationMode
{
    private String count;

    private final AbstractValidationMode secondValidation;

    public CountValidator(final String count)
    {
        this(count, null);
    }

    public CountValidator(final String count, final AbstractValidationMode secondValidation)
    {
        this.count = count;
        this.secondValidation = secondValidation;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        // Resolve values
        resolveValues(context);
        Integer count;
        try
        {
            count = Integer.parseInt(this.count);
        }
        catch (final Exception e)
        {
            count = 0;
        }
        // Assert that the amount of results is the same as the specified one
        if (count != null)
        {
            Assert.assertTrue("Expected " + this.count + " matches but found " + count.toString() + "matches",
                              count.equals(getExpressionToValidate().size()));
        }
        else
        {
            throw new IllegalArgumentException("Illegal expression: \"" + this.count + "\" cannot be turned into an integer.");
        }
    }

    @Override
    protected void resolveValues(final Context context)
    {
        count = context.resolveString(count);
    }

}
