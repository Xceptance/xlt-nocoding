package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.duplicate.DuplicateStorage;

public class StoreDefaultCookie extends StoreDefault
{
    public StoreDefaultCookie(final String variableName, final String value)
    {
        super(variableName, value);
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        // Resolve values
        super.resolveValues(context);
        // Get the appropriate storage
        final DuplicateStorage storage = context.getDefaultParameters();
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            storage.store(variableName, value);
            XltLogger.runTimeLogger.debug("Added " + variableName.toLowerCase() + "=" + value + " to default cookies");
        }
        else
        {
            // TODO remove cookies?
            // If the variableName is Constants.HEADERS, then we delete all default headers
            if (variableName.equals(Constants.COOKIES))
            {
                storage.clear();
                XltLogger.runTimeLogger.debug("Removed all default cookies");
            }
            // Else we simply delete the specified header
            else
            {
                storage.remove(variableName);
                XltLogger.runTimeLogger.debug("Removed " + variableName + " from default cookies");
            }
        }
    }

}
