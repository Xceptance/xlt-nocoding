package com.xceptance.xlt.nocoding.scriptItem.action.response.store;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.RegexpSelector;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Takes an {@link AbstractSelector} and stores the result via {@link Context#storeVariable(String, String)}.
 * 
 * @author ckeiner
 */
public class ResponseStore extends AbstractResponseStore
{

    /**
     * Creates an instance of {@link ResponseStore}, that stores the result from {@link AbstractSelector} under the name
     * {@link #getVariableName()}.
     * 
     * @param variableName
     *            The name of the variable
     * @param selector
     *            The selector to use for the value
     */
    public ResponseStore(final String variableName, final AbstractSelector selector)
    {
        super(variableName, selector);
    }

    /**
     * Resolves values, sets group of the {@link RegexpSelector} if {@link #group} is specified, then stores the result via
     * {@link Context#storeVariable(String, String)}.
     * 
     * @param context
     *            The {@link Context} to use
     */
    @Override
    public void execute(final Context context) throws Exception
    {
        // Execute the selector
        selector.execute(context);
        // Store the solution
        context.getVariables().store(getVariableName(), selector.getResult().get(0));
        XltLogger.runTimeLogger.info("Added Variable: " + variableName + " : " + selector.getResult().get(0));
    }

}
