package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.util.Context;

public class ResponseStore extends AbstractResponseStore
{

    public ResponseStore(final String variableName, final AbstractSelector selector)
    {
        super(variableName, selector);
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        // Execute the selector
        selector.execute(context);
        // Store the solution
        context.storeVariable(getVariableName(), selector.getResult().get(0));
        XltLogger.runTimeLogger.info("Added Variable: " + variableName + " : " + selector.getResult().get(0));
    }

}
