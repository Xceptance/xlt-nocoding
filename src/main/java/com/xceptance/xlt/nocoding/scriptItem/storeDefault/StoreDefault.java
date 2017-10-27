package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

/**
 * This is the class for simple assignments, so if a script item has the form of "- variable_1: value_1" this ScriptItem
 * is conjured.
 */
public abstract class StoreDefault implements ScriptItem
{
    protected final String variableName;

    protected final String value;

    public StoreDefault(final String variableName, final String value)
    {
        this.variableName = variableName;
        this.value = value;
    }

    public String getVariableName()
    {
        return variableName;
    }

    public String getValue()
    {
        return value;
    }

}
