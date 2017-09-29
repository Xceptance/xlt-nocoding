package com.xceptance.xlt.nocoding.scriptItem;

import com.xceptance.xlt.nocoding.util.PropertyManager;

/**
 * This interface defines all script item. Currently "Action", "Store", and simple assignments are supported
 */
public interface ScriptItem
{

    /**
     * The single interface method, which executes the ScriptItem
     * 
     * @param propertyManager
     * @throws Throwable
     */
    public void execute(PropertyManager propertyManager) throws Throwable;
}
