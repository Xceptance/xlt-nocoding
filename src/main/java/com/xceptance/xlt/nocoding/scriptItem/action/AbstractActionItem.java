package com.xceptance.xlt.nocoding.scriptItem.action;

import com.xceptance.xlt.nocoding.util.Context;

/**
 * The wrapper class for every action item, i.e. request, response.
 * 
 * @author ckeiner
 */
public abstract class AbstractActionItem
{
    public abstract void execute(final Context context) throws Throwable;
}
