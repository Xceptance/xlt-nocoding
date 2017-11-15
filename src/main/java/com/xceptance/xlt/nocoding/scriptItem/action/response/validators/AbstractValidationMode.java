package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * The abstract class for every response item that validates the {@link WebResponse}.
 * 
 * @author ckeiner
 */
public abstract class AbstractValidationMode implements AbstractResponseItem
{

    private List<String> expressionToValidate;

    public abstract void execute(Context context) throws Exception;

    public List<String> getExpressionToValidate()
    {
        return expressionToValidate;
    }

    public void setExpressionToValidate(final List<String> expressionToValidate)
    {
        this.expressionToValidate = expressionToValidate;
    }

    protected abstract void resolveValues(final Context context);

}
