package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;

/**
 * The abstrac class for every default store item. A default store item is an item that is used as default value for
 * undefined (or in certain cases defined) fields like the httpcode in {@link Response}.
 * 
 * @author ckeiner
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
