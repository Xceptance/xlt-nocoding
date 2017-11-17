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
    public StoreDefaultParameter(final String variableName, final String value)
    {
        super(variableName, value);
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
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
            }
            // else we simply delete the specified parameters
            else
            {
                context.deleteDefaultParameter(variableName);
            }
            XltLogger.runTimeLogger.debug("Removed " + variableName + " from default parameter storage");
        }
    }

}
