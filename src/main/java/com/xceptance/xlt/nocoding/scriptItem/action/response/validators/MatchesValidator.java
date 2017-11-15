package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.Context;

public class MatchesValidator extends AbstractValidationMode
{

    private String validationExpression;

    private String group;

    private final AbstractValidationMode secondValidation;

    public MatchesValidator(final String validationExpression)
    {
        this(validationExpression, null, null);
    }

    public MatchesValidator(final String validationExpression, final String group)
    {
        this(validationExpression, group, null);
    }

    public MatchesValidator(final String validationExpression, final String group, final AbstractValidationMode secondValidation)
    {
        this.validationExpression = validationExpression;
        this.group = group;
        this.secondValidation = secondValidation;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        // Resolve values
        resolveValues(context);
        // Either take the group-th item or the first item, depending on whether or not group is specified (and can be turned
        // in an integer)
        int group;
        try
        {
            group = Integer.parseInt(this.group);
        }
        catch (final Exception e)
        {
            group = 0;
        }
        // Assert that the group is lower equal than the size of the result
        Assert.assertTrue("Group is bigger than the size of the selection", group <= getExpressionToValidate().size());
        // Get the expression we want
        final String expressionToValidate = getExpressionToValidate().get(group);
        // Assert that the expression is not null
        Assert.assertNotNull(expressionToValidate);
        // Build a matcher from the fields
        final Matcher matcher = Pattern.compile(validationExpression).matcher(expressionToValidate);
        // Assert we found a match
        Assert.assertTrue(validationExpression + " did not match " + expressionToValidate, matcher.find());

        if (secondValidation != null)
        {
            // Prepare the second validation
            secondValidation.setExpressionToValidate(getExpressionToValidate());
            // Execute it
            secondValidation.execute(context);
        }
    }

    @Override
    protected void resolveValues(final Context context)
    {
        validationExpression = context.resolveString(validationExpression);
        group = context.resolveString(group);
    }

    public String getValidationExpression()
    {
        return validationExpression;
    }

    public String getGroup()
    {
        return group;
    }

    public AbstractValidationMode getSecondValidation()
    {
        return secondValidation;
    }

}
