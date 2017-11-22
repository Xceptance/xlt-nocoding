package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.Context;

public class TextValidator extends AbstractValidationMode
{

    private String validationExpression;

    public TextValidator(final String validationExpression)
    {
        this.validationExpression = validationExpression;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        // Resolve values
        resolveValues(context);
        // Get the expression we want
        final String expressionToValidate = getExpressionToValidate().get(0);
        // Assert that the expression is not null
        Assert.assertNotNull(expressionToValidate);
        // Assert both strings are equal
        Assert.assertEquals("Expected : " + validationExpression + " but was " + expressionToValidate,
                            validationExpression,
                            expressionToValidate);
    }

    @Override
    protected void resolveValues(final Context context)
    {
        validationExpression = context.resolveString(validationExpression);
    }

}