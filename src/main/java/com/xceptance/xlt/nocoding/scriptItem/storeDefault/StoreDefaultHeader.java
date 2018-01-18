package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage.UniqueStorage;

/**
 * Stores a default header.
 * 
 * @author ckeiner
 */
public class StoreDefaultHeader extends StoreDefault
{

    /**
     * Creates an instance of {@link StoreDefaultHeader} that sets {@link #getVariableName()} and {@link #getValue()}
     * 
     * @param variableName
     *            The name of the header
     * @param value
     *            The corresponding default value of the header
     */
    public StoreDefaultHeader(final String variableName, final String value)
    {
        super(variableName, value);
    }

    /**
     * If {@link #getValue()} is {@link Constants#DELETE}, the list of default headers is deleted. Else, it stores a default
     * header.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        super.resolveValues(context);
        // Get the appropriate storage
        final UniqueStorage storage = context.getDefaultHeaders();
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            // TODO Lowercase default headers
            // Store the header as lowercase
            storage.store(variableName.toLowerCase(), value);
            XltLogger.runTimeLogger.debug("Added \"" + variableName.toLowerCase() + "\" with the value \"" + value
                                          + "\" to default header storage");
        }
        else
        {
            // If the variableName is Constants.HEADERS, then we delete all default headers
            if (variableName.equals(Constants.HEADERS))
            {
                storage.clear();
                XltLogger.runTimeLogger.debug("Removed all default headers");
            }
            // Else we simply delete the specified header
            else
            {
                storage.remove(variableName);
                XltLogger.runTimeLogger.debug("Removed \"" + variableName + "\" from default header storage");
            }
        }
    }

}
