package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

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
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            context.storeDefaultCookie(new NameValuePair(variableName, value));
            XltLogger.runTimeLogger.debug("Added " + variableName.toLowerCase() + "=" + value + " to default cookies");
        }
        else
        {
            // TODO remove cookies?
            // If the variableName is Constants.HEADERS, then we delete all default headers
            if (variableName.equals(Constants.COOKIES))
            {
                context.deleteDefaultCookie();
                XltLogger.runTimeLogger.debug("Removed all default cookies");
            }
            // Else we simply delete the specified header
            else
            {
                context.deleteDefaultCookie(variableName);
                XltLogger.runTimeLogger.debug("Removed " + variableName + " from default cookies");
            }
        }
    }

}
