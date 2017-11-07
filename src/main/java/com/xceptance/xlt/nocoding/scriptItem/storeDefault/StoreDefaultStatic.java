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

    public StoreDefaultStatic(final String variableName, final String value)
    {
        super(variableName, value);
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            // Store the url
            context.storeDefaultStatic(value);
            XltLogger.runTimeLogger.debug("Added " + variableName + "=" + value + " to default static request storage");
        }
        else
        {
            // If the variableName is Constants.STATIC, then we delete all default static request urls
            if (variableName.equals(Constants.STATIC))
            {
                context.deleteDefaultStatic();
            }
            // else we simply delete the specified parameters
            else
            {
                context.deleteDefaultStatic(variableName);
            }
            XltLogger.runTimeLogger.debug("Removed " + variableName + " from default static request storage");
        }
    }

}
