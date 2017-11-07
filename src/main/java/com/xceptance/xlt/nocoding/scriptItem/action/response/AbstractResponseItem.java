package com.xceptance.xlt.nocoding.scriptItem.action.response;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * The interface for every response item. A response item is either an item that stores something or an item that
 * validates the {@link WebResponse} of a http request.
 * 
 * @author ckeiner
 */
public interface AbstractResponseItem
{
    public abstract void execute(final Context context) throws Exception;
}
