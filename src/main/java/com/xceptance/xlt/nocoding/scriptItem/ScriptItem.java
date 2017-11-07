package com.xceptance.xlt.nocoding.scriptItem;

import com.xceptance.xlt.nocoding.util.Context;

/**
 * This interface defines all script item. A script item is either an "Action", "Store", or default definitions.
 */
public interface ScriptItem
{

    /**
     * The single interface method, which executes the ScriptItem
     * 
     * @param context
     * @throws Throwable
     */
    public void execute(Context context) throws Throwable;
}
