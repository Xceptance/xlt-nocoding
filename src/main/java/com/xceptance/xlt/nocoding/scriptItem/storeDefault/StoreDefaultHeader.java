package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import java.util.Optional;

import com.gargoylesoftware.htmlunit.WebClient;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage.UniqueSingleStorage;

/**
 * Stores a default header and sets it at the {@link WebClient}.
 * 
 * @author ckeiner
 */
public class StoreDefaultHeader extends AbstractStoreDefaultItem
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
        final UniqueSingleStorage storage = context.getDefaultHeaders();
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            // Store the header
            addHeader(variableName, value, context);
        }
        else
        {
            // If the variableName is Constants.HEADERS, then we delete all default headers
            if (variableName.equals(Constants.HEADERS))
            {
                // Remove all default headers from the storage
                storage.getItems().forEach((name) -> {
                    context.getWebClient().removeRequestHeader(name);
                });
                // Remove all default headers from the storage
                storage.clear();
                XltLogger.runTimeLogger.debug("Removed all default headers from storage and WebClient");
            }
            // Else we simply delete the specified header
            else
            {
                // Remove default header from the webclient
                context.getWebClient().removeRequestHeader(variableName);
                // Clear the default header from the storage
                storage.remove(variableName);
                XltLogger.runTimeLogger.debug("Removed \"" + variableName + "\" from default header storage and WebClient");
            }
        }
    }

    private void addHeader(final String variableName, final String value, final Context<?> context)
    {
        final UniqueSingleStorage storage = context.getDefaultHeaders();
        if (storage.getItems().contains(variableName))
        {
            final Optional<String> optional = storage.getItems().stream().filter((item) -> item.equalsIgnoreCase(variableName)).findFirst();
            if (optional.isPresent())
            {
                final String actualKey = optional.get();
                storage.getItems().remove(actualKey);
                context.getWebClient().removeRequestHeader(actualKey);
            }
        }
        storage.store(variableName);
        context.getWebClient().addRequestHeader(variableName, value);
        XltLogger.runTimeLogger.debug("Added \"" + variableName + "\" with the value \"" + value
                                      + "\" to default header storage and WebClient");
    }

}
