package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.Context;

public class TextValidator extends AbstractValidator
{

    private String validationExpression;

    private String group;

    private final AbstractValidator secondValidation;

    public TextValidator(final String validationExpression)
    {
        this(validationExpression, null, null);
    }

    public TextValidator(final String validationExpression, final String group)
    {
        this(validationExpression, group, null);
    }

    public TextValidator(final String validationExpression, final String group, final AbstractValidator secondValidation)
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
        // Assert both strings are equal
        Assert.assertEquals(validationExpression + " was not " + expressionToValidate, validationExpression, expressionToValidate);

        if (secondValidation != null)
        {
            secondValidation.setExpressionToValidate(getExpressionToValidate());
            secondValidation.execute(context);
        }
    }

    @Override
    protected void resolveValues(final Context context)
    {
        validationExpression = context.resolveString(validationExpression);
        group = context.resolveString(group);
    }

}
