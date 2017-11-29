package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * The abstract class for every subrequest.
 * 
 * @author ckeiner
 */
public abstract class AbstractSubrequest extends AbstractActionItem
{

    /**
     * Executes the subrequest.
     * 
     * @param context
     *            The {@link Context} for this subrequest
     */
    @Override
    public abstract void execute(Context context) throws Throwable;

}
