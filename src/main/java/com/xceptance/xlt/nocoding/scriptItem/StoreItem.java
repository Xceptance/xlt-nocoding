package com.xceptance.xlt.nocoding.scriptItem;

import com.xceptance.xlt.nocoding.util.PropertyManager;

/**
 * This is the class for the "Store" command, so if a script item has the form of "- Store: - variable_1 : value_1" this
 * ScriptItem is conjured.
 */
public class StoreItem implements ScriptItem
{
    private final String variableName;

    private final String value;

    public StoreItem(final String variableName, final String value)
    {
        this.variableName = variableName;
        this.value = value;
    }

    @Override
    public void execute(final PropertyManager propertyManager) throws Throwable
    {
        propertyManager.getDataStorage().storeVariable(variableName, value);
    }

}
