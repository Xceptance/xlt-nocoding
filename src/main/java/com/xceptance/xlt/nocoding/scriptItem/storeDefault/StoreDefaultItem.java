package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Stores a default item that is a simple name-value pair.
 * 
 * @author ckeiner
 */
public class StoreDefaultItem extends StoreDefault
{

    public StoreDefaultItem(final String variableName, final String value)
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
            // Store the item in ConfigItems
            context.storeConfigItem(variableName, value);
            XltLogger.runTimeLogger.debug("Added " + variableName + "=" + value + " to default storage");
        }
        else
        {
            // Delete the speicifed ConfigItem
            context.deleteConfigItem(variableName);
            XltLogger.runTimeLogger.debug("Removed " + variableName + " from default storage");
        }
    }

}
