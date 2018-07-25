package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * The abstract class for every subrequest.
 * 
 * @author ckeiner
 */
public abstract class AbstractSubrequest extends AbstractActionSubItem
{

    /**
     * Executes the subrequest.
     * 
     * @param context
     *            The {@link Context} for this subrequest
     */
    @Override
    public abstract void execute(Context<?> context) throws Exception;

    /**
     * Fills default data of an item.
     * 
     * @param context
     *            The {@link Context} for this subrequest
     */
    public abstract void fillDefaultData(Context<?> context);

}
