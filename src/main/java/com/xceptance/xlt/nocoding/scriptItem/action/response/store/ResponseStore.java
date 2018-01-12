package com.xceptance.xlt.nocoding.scriptItem.action.response.store;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Takes an {@link AbstractExtractor} and stores the result via {@link Context#storeVariable(String, String)}.
 * 
 * @author ckeiner
 */
public class ResponseStore extends AbstractResponseStore
{

    /**
     * Creates an instance of {@link ResponseStore}, that stores the result from {@link AbstractExtractor} under the name
     * {@link #getVariableName()}.
     * 
     * @param variableName
     *            The name of the variable
     * @param extractor
     *            The selector to use for the value
     */
    public ResponseStore(final String variableName, final AbstractExtractor extractor)
    {
        super(variableName, extractor);
    }

    /**
     * Resolves values, sets group of the {@link RegexpExtractor} if {@link #group} is specified, then stores the result via
     * {@link Context#storeVariable(String, String)}.
     * 
     * @param context
     *            The {@link Context} to use
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Execute the selector
        extractor.execute(context);
        // Store the solution
        context.getVariables().store(getVariableName(), extractor.getResult().get(0));
        XltLogger.runTimeLogger.info("Added Variable: " + variableName + " : " + extractor.getResult().get(0));
    }

}
