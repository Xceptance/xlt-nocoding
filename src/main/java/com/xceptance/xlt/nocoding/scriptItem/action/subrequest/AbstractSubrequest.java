package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.Context;

public abstract class AbstractSubrequest
{
    protected Context context;

    public abstract void execute(XltWebClient webClient) throws Throwable;

    public void setContext(final Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }

}
