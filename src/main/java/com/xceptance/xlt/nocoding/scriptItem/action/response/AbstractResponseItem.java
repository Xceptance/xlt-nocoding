package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.io.Serializable;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

/**
 * The interface for every response item. A response item is either an item that stores something or an item that
 * validates the {@link WebResponse} of a HTTP request.
 * 
 * @author ckeiner
 */
public abstract class AbstractResponseItem implements Serializable
{
    /**
     * Executes the item by either storing or validating something in the {@link WebResponse}
     * 
     * @param context
     *            The {@link Context} with the last {@link WebResponse} and {@link DataStorage}
     */
    public abstract void execute(final Context<?> context);
}
