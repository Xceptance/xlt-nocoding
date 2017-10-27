package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

public class StoreDefaultParameter extends StoreDefault
{
    public StoreDefaultParameter(final String variableName, final String value)
    {
        super(variableName, value);
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        if (!value.equals("delete"))
        {
            context.storeDefaultParameter(variableName, value);
            XltLogger.runTimeLogger.debug("Added " + variableName + "=" + value + " to default parameter storage");
        }
        else
        {
            if (variableName.equals(Constants.PARAMETERS))
            {
                context.deleteDefaultParameter();
            }
            else
            {
                context.deleteDefaultParameter(variableName);
            }
            XltLogger.runTimeLogger.debug("Removed " + variableName + " from default parameter storage");
        }
    }

}
