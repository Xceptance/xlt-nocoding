package com.xceptance.xlt.nocoding.scriptItem;

import com.xceptance.xlt.nocoding.util.Context;

/**
 * This interface defines all script item. A script item is either an "Action", "Store", or default definitions.
 */
public interface ScriptItem
{

    /**
     * The method, which executes the ScriptItem
     * 
     * @param context
     *            The current {@link Context}
     * @throws Throwable
     */
    public void execute(Context context) throws Throwable;
}
