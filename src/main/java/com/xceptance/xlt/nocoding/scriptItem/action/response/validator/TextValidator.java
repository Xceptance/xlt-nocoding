package com.xceptance.xlt.nocoding.scriptItem.action.response.validator;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Validates that the first result in {@link #getExpressionToValidate()} equals {@link #validationExpression}.
 * 
 * @author ckeiner
 */
public class TextValidator extends AbstractValidator
{

    /**
     * The expected value of the first result in {@link #getExpressionToValidate()}
     */
    private String validationExpression;

    /**
     * Creates a {@link TextValidator} that sets {@link #validationExpression}.
     * 
     * @param validationExpression
     *            The expected value of the first result in {@link #getExpressionToValidate()}
     */
    public TextValidator(final String validationExpression)
    {
        this.validationExpression = validationExpression;
    }

    /**
     * Resolves values, verifies {@link #getExpressionToValidate()} is neither null nor empty, and verifies, that the first
     * result in {@link #getExpressionToValidate()} equals {@link #validationExpression}
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        resolveValues(context);
        // Assert we have results
        Assert.assertNotNull("Result list is null", getExpressionToValidate());
        Assert.assertFalse("Result list is empty", getExpressionToValidate().isEmpty());
        // Get the expression we want
        final String expressionToValidate = getExpressionToValidate().get(0);
        // Assert that the expression is not null
        Assert.assertNotNull(expressionToValidate);
        // Assert both strings are equal
        Assert.assertEquals("Expected : " + validationExpression + " but was " + expressionToValidate,
                            validationExpression,
                            expressionToValidate);
    }

    /**
     * Resolves {@link #validationExpression}
     */
    @Override
    protected void resolveValues(final Context<?> context)
    {
        validationExpression = context.resolveString(validationExpression);
    }

    public String getValidationExpression()
    {
        return validationExpression;
    }

}
