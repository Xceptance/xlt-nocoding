package com.xceptance.xlt.nocoding.scriptItem;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * This is the class for the "Store" command, so if a script item has the form of "- Store: - variable_1 : value_1" this
 * ScriptItem is conjured.
 */
public class StoreItem implements ScriptItem
{
    private String variableName;

    private String value;

    public StoreItem(final String variableName, final String value)
    {
        this.variableName = variableName;
        this.value = value;
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        // Store the variable
        context.storeVariable(variableName, value);
        XltLogger.runTimeLogger.info("Added Variable: " + variableName + " : " + value);
    }

    public String getVariableName()
    {
        return variableName;
    }

    public void setVariableName(final String variableName)
    {
        this.variableName = variableName;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(final String value)
    {
        this.value = value;
    }

}
