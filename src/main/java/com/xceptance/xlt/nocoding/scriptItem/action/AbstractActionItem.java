package com.xceptance.xlt.nocoding.scriptItem.action;

import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * The wrapper class for every action item, i.e. {@link Request}, {@link Response}
 * 
 * @author ckeiner
 */
public abstract class AbstractActionItem
{
    /**
     * Executes the action item
     * 
     * @param context
     *            The {@link Context} for the {@link AbstractActionItem}
     * @throws Throwable
     *             Any error that happens during the execution
     */
    public abstract void execute(final Context<?> context) throws Throwable;
}
