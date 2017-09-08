package com.xceptance.xlt.nocoding.scriptItem;

import com.xceptance.xlt.nocoding.util.PropertyManager;

public interface NoCodingScriptItem
{
    public static final PropertyManager propertyManager = new PropertyManager();

    public void executeAction() throws Throwable;
}
