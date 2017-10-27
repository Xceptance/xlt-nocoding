package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Context;

public class StoreDefaultItem extends StoreDefault
{

    public StoreDefaultItem(final String variableName, final String value)
    {
        super(variableName, value);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        if (!value.equals("delete"))
        {
            context.storeConfigItem(variableName, value);
            XltLogger.runTimeLogger.debug("Added " + variableName + "=" + value + " to default storage");
        }
        else
        {
            context.deleteConfigItem(variableName);
            XltLogger.runTimeLogger.debug("Removed " + variableName + " from default storage");
        }
    }

}
