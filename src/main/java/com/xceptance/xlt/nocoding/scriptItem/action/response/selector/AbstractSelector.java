package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Selects an element from the {@link WebResponse} in the {@link Context}. The selection happens in
 * {@link AbstractSelector#execute(Context)}. To get the result, you need to use {@link AbstractSelector#getResult()}.
 * 
 * @author ckeiner
 */
public abstract class AbstractSelector
{

    protected String selectionExpression;

    protected String result;

    public AbstractSelector(final String selectionExpression)
    {
        this.selectionExpression = selectionExpression;
    }

    public abstract void execute(Context context);

    public String getSelectionExpression()
    {
        return selectionExpression;
    }

    public void setSelectionExpression(final String selectionExpression)
    {
        this.selectionExpression = selectionExpression;
    }

    protected void resolveValues(final Context context)
    {
        selectionExpression = context.resolveString(selectionExpression);
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(final String result)
    {
        this.result = result;
    }

}
