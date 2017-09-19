package com.xceptance.xlt.nocoding.scriptItem.StoreItem;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.PropertyManager;

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
        // TODO Auto-generated method stub
        propertyManager.getGlobalStorage().storeVariable(variableName, value);
    }

}
