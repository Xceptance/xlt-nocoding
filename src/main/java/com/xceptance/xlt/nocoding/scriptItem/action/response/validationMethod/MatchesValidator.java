package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Validates that the first result in {@link #getExpressionToValidate()} matches the {@link Pattern} provided by
 * {@link #validationExpression}.
 * 
 * @author ckeiner
 */
public class MatchesValidator extends AbstractValidationMethod
{

    /**
     * The {@link Pattern} to match {@link #getExpressionToValidate()} against as {@link String}
     */
    private String validationExpression;

    /**
     * Creates an instance of {@link MatchesValidator} that sets {@link #validationExpression}
     * 
     * @param validationExpression
     *            The {@link Pattern} to match {@link #getExpressionToValidate()} against as {@link String}
     */
    public MatchesValidator(final String validationExpression)
    {
        this.validationExpression = validationExpression;
    }

    /**
     * Resolves values, verifies {@link #getExpressionToValidate()} is neither null nor empty, and matches the first result
     * of {@link #getExpressionToValidate()} against the {@link Pattern} provided by {@link #validationExpression}. Finally,
     * it verifies there is at least one result.
     */
    @Override
    public void execute(final Context context)
    {
        // Resolve values
        resolveValues(context);
        // Assert we have a result list and its has elements in it
        Assert.assertNotNull("Result list is null", getExpressionToValidate());
        Assert.assertFalse("Result list is empty", getExpressionToValidate().isEmpty());
        // Get the expression we want
        final String expressionToValidate = getExpressionToValidate().get(0);
        // Assert that the expression is not null
        Assert.assertNotNull(expressionToValidate);
        // Build a matcher from the fields
        final Matcher matcher = Pattern.compile(validationExpression).matcher(expressionToValidate);
        // Verify a match was found
        Assert.assertTrue(validationExpression + " did not match " + expressionToValidate, matcher.find());
    }

    /**
     * Resolves {@link #validationExpression}
     */
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
