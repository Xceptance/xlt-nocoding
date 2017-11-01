package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Stores a default static request. Ignores variableName
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
        if (!value.equals("delete"))
        {
            context.storeDefaultStatic(value);
            XltLogger.runTimeLogger.debug("Added " + variableName + "=" + value + " to default static request storage");
        }
        else
        {
            if (variableName.equals(Constants.STATIC))
            {
                context.deleteDefaultStatic();
            }
            else
            {
                context.deleteDefaultStatic(variableName);
            }
            XltLogger.runTimeLogger.debug("Removed " + variableName + " from default static request storage");
        }
    }

}
