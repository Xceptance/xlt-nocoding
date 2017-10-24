package com.xceptance.xlt.nocoding.scriptItem;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * This is the class for simple assignments, so if a script item has the form of "- variable_1: value_1" this ScriptItem
 * is conjured.
 */
public class StoreDefault implements ScriptItem
{
    private final String variableName;

    private final String value;

    public StoreDefault(final String variableName, final String value)
    {
        this.variableName = variableName;
        this.value = value;
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
            context.removeConfigItem(variableName);
            XltLogger.runTimeLogger.debug("Removed " + variableName + " from default storage");
        }
    }
}
