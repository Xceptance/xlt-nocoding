package com.xceptance.xlt.nocoding.scriptItem.action.response;

import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidationMode;
import com.xceptance.xlt.nocoding.util.Context;

public class Validator implements AbstractResponseItem
{

    private final String validationName;

    private final AbstractSelector selector;

    private final AbstractValidationMode mode;

    public Validator(final String validationName, final AbstractSelector selector, final AbstractValidationMode mode)
    {
        this.validationName = validationName;
        this.selector = selector;
        this.mode = mode;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        selector.execute(context);
        // Set the result of the execution as expression to validate in validator
        mode.setExpressionToValidate(selector.getResult());
        // validate the solution of the selector
        try
        {
            mode.execute(context);
        }
        catch (final Exception e)
        {
            throw new Exception(validationName + " could not validate: " + e.getMessage(), e);
        }
    }

}
