package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

public class StoreDefaultStoreItem extends StoreDefault
{

    public StoreDefaultStoreItem(final String variableName, final String value)
    {
        super(variableName, value);
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        if (!value.equals("delete"))
        {
            context.storeDefaultStoreItem(variableName, value);
            XltLogger.runTimeLogger.debug("Added " + variableName + "=" + value + " to default storeItem storage");
        }
        else
        {
            if (variableName.equals(Constants.STORE))
            {
                context.deleteDefaultStoreItem();
            }
            else
            {
                context.deleteDefaultStoreItem(variableName);
            }
            XltLogger.runTimeLogger.debug("Removed " + variableName + " from default storeItem storage");
        }
    }

}
