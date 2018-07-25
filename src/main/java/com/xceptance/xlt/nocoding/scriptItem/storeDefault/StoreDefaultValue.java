package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage.DefaultKeyValueStorage;

/**
 * Stores a default item that is a simple name-value pair.
 * 
 * @author ckeiner
 */
public class StoreDefaultValue extends AbstractStoreDefaultItem
{

    /**
     * Creates an instance of {@link StoreDefaultValue} that sets {@link #getVariableName()} and {@link #getValue()}
     * 
     * @param variableName
     *            The name of the default item
     * @param value
     *            The default value of the default item
     */
    public StoreDefaultValue(final String variableName, final String value)
    {
        super(variableName, value);
    }

    /**
     * If {@link #getValue()} is {@link Constants#DELETE}, the default item with the name {@link #getVariableName()} is
     * deleted. Else, it stores a default item.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        super.resolveValues(context);
        // Get the appropriate storage
        final DefaultKeyValueStorage storage = context.getDefaultItems();
        // If the value is not Constants.DELETE
        if (!value.equals(Constants.DELETE))
        {
            // Store the item in ConfigItems
            storage.store(variableName, value);
            XltLogger.runTimeLogger.debug("Added \"" + variableName + "\" with the value \"" + value + "\" to default storage");
        }
        else
        {
            // Delete the specified ConfigItem
            storage.remove(variableName);
            XltLogger.runTimeLogger.debug("Removed \"" + variableName + "\" from default storage");
        }
    }

}
