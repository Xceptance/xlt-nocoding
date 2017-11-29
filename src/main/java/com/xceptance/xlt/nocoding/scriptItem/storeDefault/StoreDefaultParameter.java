package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Stores a default parameter.
 * 
 * @author ckeiner
 */
public class StoreDefaultParameter extends StoreDefault
{
    /**
     * Creates an instance of {@link StoreDefaultParameter} that sets {@link #getVariableName()} and {@link #getValue()}
     * 
     * @param variableName
     *            The name of the parameter
     * @param value
     *            The default value of the parameter
     */
    public StoreDefaultParameter(final String variableName, final String value)
    {
        super(variableName, value);
    }

    /**
     * If {@link #getValue()} is {@link Constants#DELETE}, the list of default parameters is deleted with
     * {@link Context#deleteDefaultParameter()}. Else it stores a default parameter with
     * {@link Context#storeDefaultParameter(String, String)}.
     */
    @Override
    public void execute(final Context context) throws Throwable
    {
        // Resolve values
        super.resolveValues(context);
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            // Store the default parameter
            context.storeDefaultParameter(variableName, value);
            XltLogger.runTimeLogger.debug("Added " + variableName + "=" + value + " to default parameter storage");
        }
        else
        {
            // If the variableName is Constants.PARAMETERS, then we delete all default parameters
            if (variableName.equals(Constants.PARAMETERS))
            {
                context.deleteDefaultParameter();
                XltLogger.runTimeLogger.debug("Removed all default parameters");
            }
            // else we simply delete the specified parameters
            else
            {
                context.deleteDefaultParameter(variableName);
                XltLogger.runTimeLogger.debug("Removed " + variableName + " from default parameter storage");
            }
        }
    }

}
