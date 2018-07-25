package com.xceptance.xlt.nocoding.scriptItem.action;

import java.io.Serializable;

import com.xceptance.xlt.nocoding.scriptItem.action.request.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * The wrapper class for every action item, i.e. {@link Request}, {@link Response}
 * 
 * @author ckeiner
 */
public abstract class AbstractActionSubItem implements Serializable
{
    /**
     * Executes the action item
     * 
     * @param context
     *            The {@link Context} for the {@link AbstractActionSubItem}
     * @throws Exception
     *             Any exceptions, that happen during the execution
     */
    public abstract void execute(final Context<?> context) throws Exception;
}
