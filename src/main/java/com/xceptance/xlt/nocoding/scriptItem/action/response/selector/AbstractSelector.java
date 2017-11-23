package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Selects an element from the {@link WebResponse} in the {@link Context}. The selection happens in
 * {@link AbstractSelector#execute(Context)}. To get the result, you need to use {@link AbstractSelector#getResult()}.
 * However, you cannot reuse an instance twice.
 * 
 * @author ckeiner
 */
public abstract class AbstractSelector
{

    /**
     * The expression to use for the selection
     */
    protected String selectionExpression;

    /**
     * The result of the selection
     */
    protected final List<String> result;

    /**
     * Creates an instance of {@link AbstractSelector}, sets {@link #selectionExpression} and creates an {@link ArrayList}
     * for {@link #result}.
     * 
     * @param selectionExpression
     */
    public AbstractSelector(final String selectionExpression)
    {
        this.selectionExpression = selectionExpression;
        result = new ArrayList<String>(1);
    }

    /**
     * Executes the selector. Normally, it should look into the {@link WebResponse} located in {@link Context}. Then sets
     * the selected expression via {@link #addResult(String)}.
     * 
     * @param context
     */
    public abstract void execute(Context context);

    /**
     * Adds a string to the result list.
     * 
     * @param result
     *            The string to be added to the result.
     */
    public void addResult(final String result)
    {
        getResult().add(result);
    }

    /**
     * Resolves the {@link #selectionExpression}.
     * 
     * @param context
     */
    protected void resolveValues(final Context context)
    {
        selectionExpression = context.resolveString(selectionExpression);
    }

    public List<String> getResult()
    {
        return result;
    }

    public String getSelectionExpression()
    {
        return selectionExpression;
    }

    public void setSelectionExpression(final String selectionExpression)
    {
        this.selectionExpression = selectionExpression;
    }

}
