package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Stores a default static request. This class does not use the variableName with the single exception of "Static:
 * Delete".
 * 
 * @author ckeiner
 */
public class StoreDefaultStatic extends StoreDefault
{

    /**
     * Creates an instance of {@link StoreDefaultStatic} that sets {@link #getVariableName()} to {@link Constants#STATIC}
     * and {@link #getValue()}
     * 
     * @param value
     *            The URL of the default static request
     */
    public StoreDefaultStatic(final String value)
    {
        // variableName isn't used, so we set it to "Static" to remain some sort of meaning
        super(Constants.STATIC, value);
    }

    /**
     * If {@link #getValue()} is {@link Constants#DELETE}, the list of default static requests is deleted with
     * {@link Context#deleteDefaultStatic()}. Else it stores a default static requests with
     * {@link Context#storeDefaultStatic(String)}.
     */
    @Override
    public void execute(final Context context) throws Throwable
    {
        // Resolve values
        super.resolveValues(context);
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            // Store the URL
            context.storeDefaultStatic(value);
            XltLogger.runTimeLogger.debug("Added " + variableName + "=" + value + " to default static request storage");
        }
        else
        {
            // Delete all default statics
            context.deleteDefaultStatic();
            XltLogger.runTimeLogger.debug("Removed all default static requests");
        }
    }

}
