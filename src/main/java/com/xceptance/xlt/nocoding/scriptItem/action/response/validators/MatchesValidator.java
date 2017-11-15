package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.Context;

public class MatchesValidator extends AbstractValidationMode
{

    private String validationExpression;

    public MatchesValidator(final String validationExpression)
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
        // Build a matcher from the fields
        final Matcher matcher = Pattern.compile(validationExpression).matcher(expressionToValidate);
        // Assert we found a match
        Assert.assertTrue(validationExpression + " did not match " + expressionToValidate, matcher.find());
    }

    @Override
    protected void resolveValues(final Context context)
    {
        validationExpression = context.resolveString(validationExpression);
    }

    public String getValidationExpression()
    {
        return validationExpression;
    }

}
