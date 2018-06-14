package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage.UniqueStorage;

/**
 * Stores a default cookie.
 * 
 * @author ckeiner
 */
public class StoreDefaultCookie extends StoreDefault
{
    /**
     * Creates an instance of {@link StoreDefaultHeader} that sets {@link #getVariableName()} and {@link #getValue()}
     * 
     * @param variableName
     *            The name of the cookie
     * @param value
     *            The corresponding default value of the cookie
     */
    public StoreDefaultCookie(final String variableName, final String value)
    {
        super(variableName, value);
    }

    /**
     * If {@link #getValue()} is {@link Constants#DELETE}, the list of default cookies is deleted. Else, it stores a default
     * cookie.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        super.resolveValues(context);
        // Get the appropriate storage
        final UniqueStorage storage = context.getDefaultCookies();
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            storage.store(variableName, value);
            XltLogger.runTimeLogger.debug("Added \"" + variableName.toLowerCase() + "\" with the value \"" + value
                                          + "\" to default cookies");
        }
        else
        {
            // If the variableName is Constants.COOKIES, then we delete all default cookies
            if (variableName.equals(Constants.COOKIES))
            {
                storage.clear();
                XltLogger.runTimeLogger.debug("Removed all default cookies");
            }
            // Else we simply delete the specified header
            else
            {
                storage.remove(variableName);
                XltLogger.runTimeLogger.debug("Removed \"" + variableName + "\" from default cookies");
            }
        }
    }

}
