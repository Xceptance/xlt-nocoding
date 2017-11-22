package com.xceptance.xlt.nocoding.scriptItem.action.response;

import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.RegexpSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.AbstractValidationMode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.ExistsValidator;
import com.xceptance.xlt.nocoding.util.Context;

public class Validator extends AbstractResponseItem
{

    private final String validationName;

    private final AbstractSelector selector;

    private AbstractValidationMode mode;

    private final String group;

    public Validator(final String validationName, final AbstractSelector selector, final AbstractValidationMode mode)
    {
        this(validationName, selector, mode, null);
    }

    public Validator(final String validationName, final AbstractSelector selector, final AbstractValidationMode mode, final String group)
    {
        this.validationName = validationName;
        this.selector = selector;
        this.mode = mode;
        this.group = group;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        // Set group if specified
        if (group != null && selector instanceof RegexpSelector)
        {
            ((RegexpSelector) selector).setGroup(group);
        }
        // If group is specified but the class is not a RegexpSelector, throw an error
        else if (group != null)
        {
            throw new IllegalArgumentException("Group specified but selector is " + selector.getClass().getName()
                                               + " and not a RegexpSelector");
        }
        selector.execute(context);

        // If we don't have a mode, then we simply want to confirm the existance of a solution
        if (mode == null)
        {
            mode = new ExistsValidator();
        }
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

    public String getValidationName()
    {
        return validationName;
    }

    public AbstractSelector getSelector()
    {
        return selector;
    }

    public AbstractValidationMode getMode()
    {
        return mode;
    }

    public String getGroup()
    {
        return group;
    }

}
